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

import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;

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
                        + "&& requestingUser == :requestingUser "),
        @Query(
                name = "findByIssueDateRange", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.request.CardRequest "
                        + "WHERE issueDate >= :startDate "
                        + "&& issueDate <= :endDate "),
        @Query(
                name = "findByHandleDateRange", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.request.CardRequest "
                        + "WHERE handleDate >= :startDate "
                        + "&& handleDate <= :endDate ")
})
@DomainObject(
        editing = Editing.DISABLED
)
public class CardRequest implements Comparable<CardRequest>, HasAtPath {
    @Override
    public int compareTo(final CardRequest other) {
        return ObjectContracts.compare(this, other, "requestingUser", "issueDate", "handleDate");
    }

    public String title() {
        return requestingUser.getFirstName() + " " + requestingUser.getLastName() + " - " + issueDate.toString();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private CardRequestType type;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    private User requestingUser;

    @Column(allowsNull = "false")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime issueDate;

    @Column(allowsNull = "true")
    @Property
    @Persistent
    @Getter @Setter
    private LocalDateTime handleDate;

    @Column(allowsNull = "true")
    @Property
    @PropertyLayout(hidden = Where.ALL_TABLES)
    @Getter @Setter
    private Card assignedCard;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    private Boolean approved;

    @Action
    public CardRequest approve(String cardNumber) {
        requestingUser.newCard(cardNumber);
        Card card = cardRepository.findByExactNumber(cardNumber);
        /* Check if the creation was successful */
        if(card != null && card.getOwner().equals(requestingUser)) {
            setAssignedCard(card);
            card.setGivenToUserAt(null);
            card.setSentToUserAt(clockService.nowAsLocalDateTime());
            this.setHandleDate(clockService.nowAsLocalDateTime());
            setApproved(true);
        }
        return this;
    }

    public TranslatableString validateApprove(String cardNumber) {
        return requestingUser.validateNewCard(cardNumber);
    }

    @Action
    public CardRequest deny() {
        setApproved(false);
        this.setHandleDate(clockService.nowAsLocalDateTime());
        return this;
    }

    @Action
    public CardRequest reapprove(String cardNumber) {
        return approve(cardNumber);
    }

    public TranslatableString validateReapprove(String cardNumber) {
        return validateApprove(cardNumber);
    }

    public boolean hideApprove() {
        return getApproved() != null;
    }

    public boolean hideDeny() {
        return getApproved() != null;
    }

    public boolean hideReapprove() {
        return getApproved() == null || getApproved();
    }

    public boolean hideAssignedCard() {
        return getAssignedCard() == null;
    }

    public boolean hideApproved() {
        return getApproved() == null;
    }

    public boolean hideHandleDate() {
        return getApproved() == null;
    }

    @Override public String getAtPath() {
        return getRequestingUser().getAtPath();
    }

    @Inject ClockService clockService;
    @Inject CardRepository cardRepository;
}
