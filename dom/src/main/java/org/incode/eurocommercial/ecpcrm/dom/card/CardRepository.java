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

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;
import org.incode.eurocommercial.ecpcrm.dom.numerator.NumeratorRepository;

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
    private Card newCard(
            String number,
            final CardStatus status,
            final Center center
    ) {
        Numerator cardNumerator = numeratorRepository.findOrCreateNumerator("cardNumerator", "%d", BigInteger.ZERO);

        final Card card = repositoryService.instantiate(Card.class);

        if(Strings.isNullOrEmpty(number)) {
            while(!cardNumberIsValid(number)) {
                number = cardNumerator.nextIncrementStr();
            }
        }
        card.setNumber(number);

        card.setStatus(status);
        card.setCenter(center);

        BigInteger num = new BigInteger(number);
        if(num.compareTo(cardNumerator.getLastIncrement()) == 1) {
            cardNumerator.setLastIncrement(num);
        }

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
    public String validateFindOrCreate(final String number, final CardStatus status, final Center center) {
        return Strings.isNullOrEmpty(number) || cardNumberIsValid(number) ? null : "Card number is invalid";
    }

    @Programmatic
    public void delete(final Card card) {
        repositoryService.remove(card);
    }

    @Programmatic
    public boolean cardExists(String cardNumber) {
        return findByExactNumber(cardNumber) != null;
    }

    @Programmatic
    public boolean cardNumberIsValid(String cardNumber) {
        if(Strings.isNullOrEmpty(cardNumber)) {
            return false;
        }
        /* Number has 13 digits */
        if(cardNumber.length() != 13) {
            return false;
        }

        /* Number starts with 20 */
        if(!cardNumber.startsWith("20")) {
            return false;
        }

        /* Number starts with center code */
        // TODO: check center

        /* Checksum */
        int[] digits = cardNumber.chars()
                .map(Character::getNumericValue)
                .toArray();
        int[] multipliers = {1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3};

        int sum = 0;

        for(int i = 0; i < digits.length - 1; i++) {
            sum += digits[i] * multipliers[i];
        }

        int key = (10 - sum % 10) % 10;

        if(digits[digits.length - 1] != key) {
            return false;
        }

        return true;
    }

    @Inject RepositoryService repositoryService;
    @Inject NumeratorRepository numeratorRepository;

}
