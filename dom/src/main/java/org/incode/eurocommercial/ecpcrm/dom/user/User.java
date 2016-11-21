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

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.util.ObjectContracts;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.person.Person;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE
)
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Queries({
        @Query(
                name = "findByExactEmail", language = "JDOQL",
                value = "SELECT "
                        + "FROM org.incode.eurocommercial.ecpcrm.dom.user.User "
                        + "WHERE email == :email "),
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
                        + "|| lastName.indexOf(:name) >= 0")
})
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        paged = 1000
)
public class User extends Person implements Comparable<User> {

    @Override
    public int compareTo(final User other) {
        return ObjectContracts.compare(this, other, "email");
    }

    public String title() {
        return super.title();
    }

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    @MemberOrder(sequence = "8")
    private boolean enabled;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    @MemberOrder(sequence = "5")
    private String email;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    @MemberOrder(sequence = "7")
    private Center center;

    @Column(allowsNull = "true")
    @Property
    @Getter @Setter
    @MemberOrder(sequence = "6")
    private Card card;

    @Column(allowsNull = "false")
    @Property
    @Getter @Setter
    @MemberOrder(sequence = "9")
    private Boolean promotionalEmails;

    @Action
    public User giveCard(String cardNumber) {
        card = cardRepository.findOrCreate(cardNumber, CardStatus.ENABLED, null, getCenter());
        setCard(card);
        return this;
    }

    /* This is in Biggerband's domain model, but not implemented */
//    @Column(allowsNull = "true")
//    @Property
//    @Getter @Setter
//    private Boolean hasCar;

    @Inject
    private CardRepository cardRepository;

}