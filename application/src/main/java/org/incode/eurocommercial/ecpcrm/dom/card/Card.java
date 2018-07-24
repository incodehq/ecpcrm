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
package org.incode.eurocommercial.ecpcrm.dom.card;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;

import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGame;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@Queries({
        @Query(
                name = "findByExactNumber", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE number == :number "),
        @Query(
                name = "findByNumberContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE number.indexOf(:number) >= 0 "),
        @Query(
                name = "findByStatus", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE status == :status "),
        @Query(
                name = "findByOwner", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE owner == :owner "),
        @Query(
                name = "findByCenter", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE center == :center "),
        @Query(
                name = "findByStatusAndOwner", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.card.Card "
                        + "WHERE status == :status "
                        + "&& owner == :owner")
})
@DomainObject(
        editing = Editing.DISABLED,
        bounded = true
)
public class Card implements Comparable<Card>, HasAtPath {

    @Override
    public int compareTo(final Card other) {
        return ObjectContracts.compare(this, other, "number");
    }

    public String title() {
        return getNumber();
    }

    @Column(allowsNull = "false")
    @Unique
    @Property
    @Getter @Setter
    private String number;

    @Column(allowsNull = "true")
    @Property
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Getter @Setter
    private User owner;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private CardStatus status;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private Center center;

    @Column(allowsNull = "false")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime createdAt;

    @Column(allowsNull = "true")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime givenToUserAt;

    @Column(allowsNull = "true")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime sentToUserAt;

    @Persistent(mappedBy = "card", dependentElement = "true")
    @Collection
    @CollectionLayout(hidden = Where.EVERYWHERE)
    @Getter @Setter
    private SortedSet<CardGame> cardGames = new TreeSet<>();


    @Action
    @ActionLayout(named = "Disable")
    /* Disable is an Apache Isis keyword, hence unenable */
    public Card unenable() {
        this.setStatus(CardStatus.DISABLED);
        return this;
    }
    public boolean hideUnenable() {
        return this.getStatus() == CardStatus.DISABLED;
    }

    @Action
    public Card tagAsLost() {
        this.setStatus(CardStatus.LOST);
        return this;
    }
    public boolean hideTagAsLost() {
        return this.getStatus() == CardStatus.LOST;
    }

    @Action
    public Card enable() {
        this.setStatus(CardStatus.ENABLED);
        return this;
    }
    public boolean hideEnable() {
        return this.getStatus() == CardStatus.ENABLED;
    }

    @Programmatic
    public boolean canPlay() {
        return cardGameRepository.findByCardAndDate(this, clockService.now()) == null && (this.getStatus() == CardStatus.ENABLED || this.getStatus() == CardStatus.TOCHANGE);
    }

    @Programmatic
    public CardGame play() {
        if(!canPlay()) {
            return null;
        }
        return cardGameRepository.newCardGame(this, clockService.now(), new Random().nextBoolean());
    }

    @Programmatic
    public CardGame play(Boolean outcome) {
        if(outcome == null) {
            return play();
        }
        if(!canPlay()) {
            return null;
        }
        return cardGameRepository.newCardGame(this, clockService.now(), outcome);
    }

    public boolean hideGivenToUserAt() {
        return getGivenToUserAt() == null;
    }

    public boolean hideSentToUserAt() {
        return getSentToUserAt() == null;
    }

    @Override public String getAtPath() {
        return getCenter().getAtPath();
    }

    @Inject CardGameRepository cardGameRepository;
    @Inject ClockService clockService;


}
