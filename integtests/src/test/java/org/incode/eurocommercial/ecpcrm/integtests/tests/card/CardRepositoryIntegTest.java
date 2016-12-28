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
package org.incode.eurocommercial.ecpcrm.integtests.tests.card;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRepositoryIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;

    DemoFixture fs;

    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(ThreadLocalRandom.current().nextInt(0, fs.getCenters().size()));
        assertThat(center).isNotNull();
    }

    public static class ListAll extends CardRepositoryIntegTest {
        @Test
        public void all_cards_should_be_listed() {
            // when
            List<Card> cardList = cardRepository.listAll();

            // then
            assertThat(cardList.size()).isEqualTo(fs.NUM_CARDS);
            assertThat(cardList).isEqualTo(fs.getCards());
        }
    }

    public static class ListUnassignedCards extends CardRepositoryIntegTest {
        @Test
        public void only_unassigned_cards_should_be_returned() {
            // when
            List<Card> unassignedCards = cardRepository.listUnassignedCards();

            // then
            unassignedCards.forEach(c -> assertThat(c.getOwner()).isNull());
        }

        @Test
        public void all_unassigned_cards_should_be_returned() {
            // given
            List<Card> cardList = fs.getCards();

            // when
            List<Card> unassignedCards = cardRepository.listUnassignedCards();

            // then
            cardList.removeAll(unassignedCards);
            cardList.forEach(c -> assertThat(c.getOwner()).isNotNull());
        }
    }

    public static class FindByExactNumber extends CardRepositoryIntegTest {
        @Test
        public void when_card_doesnt_exist_no_card_should_be_returned() {
            // given
            String cardNumber = "100";

            // when
            Card foundCard = cardRepository.findByExactNumber(cardNumber);

            // then
            assertThat(foundCard).isNull();
        }

        @Test
        public void when_card_exists_it_should_be_returned() {
            // given
            Card randomCard = fs.getCards().get(ThreadLocalRandom.current().nextInt(0, fs.getCards().size()));

            // when
            Card foundCard = cardRepository.findByExactNumber(randomCard.getNumber());

            // then
            assertThat(foundCard).isEqualTo(randomCard);
        }
    }

    public static class FindByNumberContains extends CardRepositoryIntegTest {
        @Test
        public void when_nonexisting_number_part_is_entered_no_result_should_be_returned() {
            // given
            String cardNumber = "100";

            // when
            List<Card> foundCards = cardRepository.findByNumberContains(cardNumber);

            // then
            assertThat(foundCards.size()).isEqualTo(0);
        }

        @Test
        public void when_entire_number_is_entered_single_result_should_be_returned() {
            // given
            Card randomCard = fs.getCards().get(ThreadLocalRandom.current().nextInt(0, fs.getCards().size()));

            // when
            List<Card> foundCards = cardRepository.findByNumberContains(randomCard.getNumber());

            // then
            assertThat(foundCards.size()).isEqualTo(1);
            assertThat(foundCards.get(0)).isEqualTo(randomCard);
        }

        @Test
        public void when_number_part_is_entered_all_results_should_be_returned() {
            // given
            String cardNumber = fs.getCards().get(0).getNumber().substring(0, 2);

            // when
            List<Card> foundCards = cardRepository.findByNumberContains(cardNumber);

            // then
            assertThat(foundCards.size()).isEqualTo(fs.NUM_CARDS);
            assertThat(foundCards).isEqualTo(fs.getCards());
        }
    }

    public static class FindByCenter extends CardRepositoryIntegTest {
        @Test
        public void all_returned_cards_should_be_corresponding_to_center() {
            // when
            List<Card> centerCards = cardRepository.findByCenter(center);

            // then
            centerCards.forEach(c -> assertThat(c.getCenter()).isEqualTo(center));
        }

        @Test
        public void all_cards_corresponding_to_center_should_be_returned() {
            // given
            List<Card> cardList = fs.getCards();

            // when
            List<Card> centerCards = cardRepository.findByCenter(center);

            // then
            cardList.removeAll(centerCards);
            cardList.forEach(c -> assertThat(c.getCenter()).isNotEqualTo(center));
        }
    }

    public static class FindByOwner extends CardRepositoryIntegTest {
        @Test
        public void if_user_has_card_card_should_be_returned() {
            // given
            List<Card> cardsWithOwners = fs.getCards().stream()
                    .filter(c -> c.getOwner() != null)
                    .collect(Collectors.toList());

            Card cardWithOwner = cardsWithOwners.get(ThreadLocalRandom.current().nextInt(0, cardsWithOwners.size()));

            // when
            Card foundCard = cardRepository.findByOwner(cardWithOwner.getOwner());

            // then
            assertThat(foundCard).isEqualTo(cardWithOwner);
        }

        @Test
        public void if_user_has_no_card_no_card_should_be_returned() {
            // given
            User userWithoutCard = new User();

            // when
            Card userCard = cardRepository.findByOwner(userWithoutCard);

            // then
            assertThat(userCard).isNull();
        }
    }

    public static class NewBatch extends CardRepositoryIntegTest {
        // given
        private String startNumber = "2000000000000";
        private String endNumber = "2000000100000";
        private CardStatus status = CardStatus.ENABLED;

        @Test
        public void all_cards_in_batch_are_valid() {
            // when
            List<Card> createdCards = cardRepository.newBatch(startNumber, endNumber, status, center);

            // then
            createdCards.forEach(c -> assertThat(cardRepository.cardNumberIsValid(c.getNumber())));
        }

        @Test
        public void all_card_numbers_are_between_start_and_end() {
            // when
            List<Card> createdCards = cardRepository.newBatch(startNumber, endNumber, status, center);

            // then
            List<BigInteger> numbers = createdCards.stream().map(c -> new BigInteger(c.getNumber())).sorted().collect(Collectors.toList());
            assertThat(numbers.get(0)).isGreaterThanOrEqualTo(new BigInteger(startNumber));
            assertThat(numbers.get(numbers.size() - 1)).isLessThanOrEqualTo(new BigInteger(endNumber));
        }

        @Test
        public void all_cards_are_being_persisted() {
            // given
            int initialCards = cardRepository.listAll().size();

            // when
            int createdCards = cardRepository.newBatch(startNumber, endNumber, status, center).size();

            // then
            assertThat(initialCards + createdCards).isEqualTo(cardRepository.listAll().size());
        }
    }

    public static class CardNumberIsValid extends CardRepositoryIntegTest {
        @Test
        public void when_card_number_doesnt_have_thirteen_digits_it_is_invalid() {
            // given
            String cardNumber = "80085";

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isFalse();
        }

        @Test
        public void when_card_number_doesnt_start_with_20_it_is_invalid() {
            // given
            String cardNumber = "8008500000000";

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isFalse();
        }

        @Test
        @Ignore
        public void when_card_number_doesnt_start_with_center_code_it_is_invalid() {
            // given
            String cardNumber = "2000000000000";

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isFalse();
        }

        @Test
        public void when_card_number_doesnt_satisfy_checksum_it_is_invalid() {
            // given
            String cardNumber = "2000000000009";

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isFalse();
        }

        @Test
        public void when_card_number_satisfies_all_conditions_it_is_valid() {
            // given
            String cardNumber = "2000000000008";

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isTrue();
        }
    }
}
