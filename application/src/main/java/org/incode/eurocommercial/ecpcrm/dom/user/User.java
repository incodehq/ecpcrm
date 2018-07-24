/**
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.eurocommercial.ecpcrm.dom.user;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.mail.MailService;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@Queries({
        @Query(
                name = "findByExactEmailAndCenter", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE email == :email "
                        + "&& center == :center"),
        @Query(
                name = "findByCenter", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE center == :center"),
        @Query(
                name = "findByEmailContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE email.indexOf(:email) >= 0 "),
        @Query(
                name = "findByNameContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE firstName.indexOf(:name) >= 0 "
                        + "|| lastName.indexOf(:name) >= 0"),
        @Query(
                name = "findByFirstAndLastName", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE firstName.indexOf(:firstName) >= 0 "
                        + "&& lastName.indexOf(:lastName) >= 0"),
        @Query(
                name = "findByCode", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE reference == :reference")
})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        paged = 1000
)
public class User implements Comparable<User>, HasAtPath {

    @Override
    public int compareTo(final User other) {
        return ObjectContracts.compare(this, other, "email");
    }

    public String title() {
        return getFirstName() + " " + getLastName();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String reference;

    @Column(allowsNull = "false")
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private Title title;

    @Column(allowsNull="false", length = 40)
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String firstName;

    @Column(allowsNull="false", length = 40)
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String lastName;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String email;

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String address;

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String zipcode;

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String city;

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private String phoneNumber;

    /* This is in Biggerband's domain model, but not implemented */
    @Column(allowsNull = "true")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDate birthDate;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private Center center;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private boolean enabled;

    @Column(allowsNull = "false")
    @Getter @Setter
    private boolean promotionalEmails;

    @Action
    public User subscribeToPromotionalEmails() {
        promotionalEmails = true;
        mailService.subscribeUser(this);
        return this;
    }

    @Action
    public User unsubscribeFromPromotionalEmails() {
        promotionalEmails = false;
        mailService.unsubscribeUser(this);
        return this;
    }

    public boolean hideSubscribeToPromotionalEmails() {
        return promotionalEmails;
    }

    public boolean hideUnsubscribeFromPromotionalEmails() {
        return !promotionalEmails;
    }

    // region > children

    @Persistent(mappedBy = "parent", dependentElement = "true")
    @Collection
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<Child> children = new TreeSet<>();

    @Action
    public User newChild(String name, Gender gender, LocalDate birthdate) {
        childRepository.findOrCreate(name, gender, birthdate, this);
        return this;
    }

    // endregion > children

    // region > cards

    @Persistent(mappedBy = "owner", dependentElement = "false")
    @Collection
    @CollectionLayout(render = RenderType.EAGERLY)
    @Getter @Setter
    private SortedSet<Card> cards = new TreeSet<>();

    @Action
    @ActionLayout(named = "Give Card")
    public User newCard(String cardNumber) {
        Card card = cardRepository.findOrCreate(cardNumber, CardStatus.ENABLED, getCenter());
        if(card != null) {
            /* Nothing should change if this is already the owner */
            if(card.getOwner() != this) {
                /* Can't steal cards from previous owner */
                if(card.getOwner() != null) {
                    return this;
                }

                /* Disable the previous card of the user, unless it is already tagged as lost */
                if(!getCards().isEmpty()) {
                    Card previousCard = getCards().last();
                    if(previousCard.getStatus() != CardStatus.LOST) {
                        previousCard.unenable();
                    }
                }

                card.enable();
                card.setOwner(this);
                card.setGivenToUserAt(clockService.nowAsLocalDateTime());
                getCards().add(card);
            }
        }
        return this;
    }

    public TranslatableString validateNewCard(String cardNumber) {
        if(cardNumber == null) {
            return TranslatableString.tr("No number entered");
        }
        if(!cardRepository.cardNumberIsValid(cardNumber, center.getCode())) {
            return TranslatableString.tr("Card number {cardNumber} is invalid", "cardNumber", cardNumber);
        }
        Card card = cardRepository.findByExactNumber(cardNumber);
        if(card != null && card.getOwner() != null) {
            return TranslatableString.tr("Card with number {cardNumber}  already has an owner: {owner}", "cardNumber", card.getNumber(), "owner", card.getOwner());
        }

        return null;
    }

    // endregion > cards

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    private Boolean hasCar;

    @Override public String getAtPath() {
        return getCenter().getAtPath();
    }

    @Inject private ClockService clockService;
    @Inject private CardRepository cardRepository;
    @Inject private ChildRepository childRepository;
    @Inject private MailService mailService;

}
