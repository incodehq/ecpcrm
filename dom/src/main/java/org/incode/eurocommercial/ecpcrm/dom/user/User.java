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
                name = "findByReference", language = "JDOQL",
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
    @Property
    @Getter @Setter
    private Title title;

    @Column(allowsNull="false", length = 40)
    @Property
    @Getter @Setter
    private String firstName;

    @Column(allowsNull="false", length = 40)
    @Property
    @Getter @Setter
    private String lastName;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private String email;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String address;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String zipcode;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private String city;

    @Column(allowsNull = "true")
    @Property
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
    @Property
    @Getter @Setter
    private boolean promotionalEmails;

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
            /* Remove the card from its previous owner if it has one */
            if(card.getOwner() != null && card.getOwner() != this) {
                card.getOwner().getCards().remove(card);
            }
            card.setOwner(this);
            card.setGivenToUserAt(clockService.nowAsLocalDateTime());
            getCards().add(card);
        }
        return this;
    }

    public String validateNewCard(String cardNumber) {
        if(cardNumber == null) {
            return "No number entered";
        }
        if(!cardRepository.cardNumberIsValid(cardNumber, center.getReference())) {
            return "Card number " + cardNumber + " is invalid";
        }

        return null;
    }

    // endregion > cards

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private Boolean hasCar;

    @Override public String getAtPath() {
        return getCenter().getAtPath();
    }

    @Inject private ClockService clockService;
    @Inject private CardRepository cardRepository;
    @Inject private ChildRepository childRepository;

}
