/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.dom.request;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@Queries({
        @Query(
                name = "findByApproved", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.request.CardRequest "
                        + "WHERE approved == :approved "),
        @Query(
                name = "findByApprovedAndUser", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.request.CardRequest "
                        + "WHERE approved == :approved "
                        + "&& requestingUser == :requestingUser ")
})
@DomainObject(
        editing = Editing.DISABLED
)
public class CardRequest implements Comparable<CardRequest>{
    @Override
    public int compareTo(final CardRequest other) {
        return ObjectContracts.compare(this, other, "requestingUser", "date");
    }

    public String title() {
        return requestingUser.getFirstName() + " " + requestingUser.getLastName() + " - " + date.toString();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private User requestingUser;

    @Column(allowsNull = "false")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDate date;

    @Column(allowsNull = "true")
    @Property
    @PropertyLayout(hidden = Where.ALL_TABLES)
    @Getter @Setter
    private Card assignedCard;

    @Column(allowsNull = "true")
    @Property
    @PropertyLayout(hidden = Where.ALL_TABLES)
    @Getter @Setter
    private Boolean approved;

    @Action
    public CardRequest approve(String cardNumber) {
        requestingUser.giveCard(cardNumber);
        Card card = cardRepository.findByOwner(requestingUser);
        if(card != null && card.getNumber().equals(cardNumber)) {
            setAssignedCard(card);
            setApproved(true);
        }
        return this;
    }

    public String validateApprove(String cardNumber) {
        return requestingUser.validateGiveCard(cardNumber);
    }

    @Action
    public CardRequest deny() {
        setApproved(false);
        return this;
    }

    public boolean hideApprove() {
        return getApproved() != null;
    }

    public boolean hideDeny() {
        return getApproved() != null;
    }

    public boolean hideAssignedCard() {
        return assignedCard == null;
    }

    public boolean hideApproved() {
        return approved == null;
    }

    @Inject CardRepository cardRepository;
}

