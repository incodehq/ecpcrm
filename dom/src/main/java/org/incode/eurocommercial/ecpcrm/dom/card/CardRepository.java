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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;
import org.incode.eurocommercial.ecpcrm.dom.numerator.NumeratorRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;

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
    public List<Card> findByOwner(
            final User owner
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Card.class,
                        "findByOwner",
                        "owner", owner));
    }

    @Programmatic
    public List<Card> findByStatusAndOwner(
            final CardStatus status,
            final User owner
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Card.class,
                        "findByStatusAndOwner",
                        "status", status,
                        "owner", owner));
    }

    @Programmatic
    public List<Card> allEnabledCardsWithoutOwner() {
        return findByStatusAndOwner(CardStatus.ENABLED, null);
    }

    @Programmatic
    public Card newCard(
            String number,
            final CardStatus status,
            final Center center
    ) {
        Numerator numerator = center.getNumerator();

        /* If no number was specified, get one from the numerator */
        if(Strings.isNullOrEmpty(number)) {
            number = center.nextValidCardNumber();
        }

        if(!cardNumberIsValid(number, center.getCode())) {
            return null;
        }

        final Card card = repositoryService.instantiate(Card.class);

        card.setNumber(number);
        card.setStatus(status);
        card.setCenter(center);
        card.setCreatedAt(clockService.nowAsLocalDateTime());

        /* Update numerator with new number */
        long num = Long.parseLong(number);
        if(num > numerator.getLastIncrement()) {
            numerator.setLastIncrement(num);
        }

        repositoryService.persist(card);
        return card;
    }

    @Programmatic
    public Card newFakeCard(final CardStatus status, final Center center) {
        String number = nextCardNumber(center.getFakeNumerator());

        final Card card = repositoryService.instantiate(Card.class);

        card.setNumber(number);
        card.setStatus(status);
        card.setCenter(center);
        card.setCreatedAt(clockService.nowAsLocalDateTime());

        repositoryService.persist(card);

        center.getFakeNumerator().setLastIncrement(Long.parseLong(number));
        return card;
    }

    @Programmatic
    public List<Card> newBatch(
            final String startNumber,
            final int batchSize,
            final CardStatus status,
            final Center center
    ) {
        String centerCode = center.getCode();
        BigInteger start = new BigInteger(startNumber);
        List<Card> results = new ArrayList<>();

        /* Adds only valid numbers to results */
        for(BigInteger i = start;  results.size() < batchSize; i = i.add(BigInteger.ONE)) {
            String cardNumber = i.toString();
            if(cardNumberIsValid(cardNumber, centerCode)) {
                results.add(findOrCreate(cardNumber, status, center));
            }
        }
        return results;
    }

    @Programmatic
    public Card findOrCreate(
            final String number,
            final CardStatus status,
            final Center center
    ) {
        Card card = findByExactNumber(number);
        card = card != null ? card : newCard(number, status, center);
        return card;
    }

    @Programmatic
    public TranslatableString validateFindOrCreate(final String number, final CardStatus status, final Center center) {
        return Strings.isNullOrEmpty(number) || cardNumberIsValid(number, center.getCode()) ? null : TranslatableString.tr("Card number is invalid");
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
        return cardNumberIsValid(cardNumber, null);
    }

    @Programmatic
    public boolean cardNumberIsValid(String cardNumber, String centerCode) {
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

        /* Number starts with any valid center code */
        if(centerRepository.findByCode(cardNumber.substring(1, 4)) == null) {
            return false;
        }

        /* Number starts with specific center code if specified */
        if(centerCode != null && !cardNumber.substring(1, 4).equals(centerCode)) {
            return false;
        }

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

    @Programmatic
    public String nextCardNumber(final Numerator numerator) {
        long largestCardNumberSoFar = numerator.getLastIncrement();

        //split off last digit i.e. remove checksum; then increment
        long withoutChecksum, temp;
        withoutChecksum = temp = (largestCardNumberSoFar / 10) + 1;

        //Calculate the weighted sum of the first 12 digits of the card-number
        int sum = 0;
        //reversed order (%10 -> /10 method produces array of digits in reversed order)
        int[] multipliers = {3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1};

        for(int i = 0; i < 12; ++i) {
            sum += multipliers[i] * (int)(temp % 10);
            temp /= 10;
        }

        int checksum = (10 - sum % 10) % 10;

        //attach checksum
        return withoutChecksum + "" + checksum;
    }

    @Inject RepositoryService repositoryService;
    @Inject ClockService clockService;
    @Inject NumeratorRepository numeratorRepository;
    @Inject CenterRepository centerRepository;

}
