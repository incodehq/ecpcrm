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

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Card.class
)
public class CardRepository {

    @Programmatic
    public List<Card> listAll() {
        return repositoryService.allInstances(Card.class);
    }

    @Programmatic
    public List<Card> listUnassignedCards() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Card.class,
                        "findByOwner",
                        "owner", null));
    }

    @Programmatic
    public List<Card> listEnabledCards() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Card.class,
                        "findByStatus",
                        "status", CardStatus.ENABLED));
    }

    @Programmatic
    public Card findByExactNumber(
            final String number
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Card.class,
                        "findByExactNumber",
                        "number", number));
    }

    @Programmatic
    public List<Card> findByNumberContains(
            final String number
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Card.class,
                        "findByNumberContains",
                        "number", number));
    }

    @Programmatic
    public List<Card> findByCenter(
            final Center center
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Card.class,
                        "findByCenter",
                        "center", center));
    }


    @Programmatic
    public Card newCard(
            final String number,
            final CardStatus status,
            final Center center
    ) {
        final Card card = repositoryService.instantiate(Card.class);
        card.setNumber(number);
        card.setStatus(status);
        card.setCenter(center);
        repositoryService.persist(card);
        return card;
    }

    @Programmatic
    public Card findOrCreate(
            final String number,
            final CardStatus status,
            final Center center
    ) {
        Card card = findByExactNumber(number);
        if(card == null) {
            card = newCard(
                    number,
                    status,
                    center
            );
        }
        return card;
    }

    @Programmatic
    public void delete(final Card card) {
        repositoryService.remove(card);
    }

    public String checkCardNumber(String cardNumber) {
        if(cardNumber == null) {
            return null;
        }
        Card card = findByExactNumber(cardNumber);
        if(card == null) {
            return "Card with number " + cardNumber + " doesn't exist";
        }
        return null;
    }

    @Inject
    RepositoryService repositoryService;

}
