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
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRepositoryIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject CardRepository cardRepository;

    IntegTestFixture fs;
    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(0);
        assertThat(center).isNotNull();
    }

    public static class ListAll extends CardRepositoryIntegTest {
        @Test
        public void all_cards_should_be_listed() {
            // when
            List<Card> cardList = cardRepository.listAll();

            // then
            assertThat(new TreeSet<>(cardList)).isEqualTo(new TreeSet<>(fs.getCards()));
        }
    }

    public static class AllEnabledCardsWithoutOwner extends CardRepositoryIntegTest {
        @Before
        public void setUpEnabledCardsWithoutOwner() {
            // given
            int cardsToSetup = 3;
            for (Card c : cardRepository.findByOwner(null)) {
                c.setStatus(CardStatus.ENABLED);
                cardsToSetup--;
                if (cardsToSetup <= 0) {
                    break;
                }
            }
        }

        @Test
        public void all_returned_cards_should_be_enabled_andwithout_owner() {
            // when
            List<Card> allEnabledCardsWithoutOwner = cardRepository.allEnabledCardsWithoutOwner();

            // then
            for (Card c : allEnabledCardsWithoutOwner) {
                assertThat(c.getOwner()).isNull();
                assertThat(c.getStatus()).isEqualTo(CardStatus.ENABLED);
            }
        }

        @Test
        public void all_enabled_cards_without_owners_should_be_returned() {
            // given
            List<Card> allCards = cardRepository.listAll();
            List<Card> expected = allCards.stream()
                    .filter(c -> c.getStatus() == CardStatus.ENABLED)
                    .filter(c -> c.getOwner() == null)
                    .collect(Collectors.toList());

            // when
            List<Card> actual = cardRepository.allEnabledCardsWithoutOwner();

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    public static class ListUnassignedCards extends CardRepositoryIntegTest {
        @Test
        public void all_returned_cards_should_be_unassigned() {
            // when
            List<Card> unassignedCards = cardRepository.listUnassignedCards();

            // then
            for (Card c : unassignedCards) {
                assertThat(c.getOwner()).isNull();
            }
        }

        @Test
        public void all_unassigned_cards_should_be_returned() {
            // given
            List<Card> cardList = fs.getCards();

            // when
            List<Card> unassignedCards = cardRepository.listUnassignedCards();
            cardList.removeAll(unassignedCards);

            // then
            for (Card c : cardList) {
                assertThat(c.getOwner()).isNotNull();
            }
        }
    }

    public static class FindByExactNumber extends CardRepositoryIntegTest {
        @Test
        public void when_card_does_not_exist_no_card_should_be_returned() {
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
            Card card = fs.getCards().get(0);

            // when
            Card foundCard = cardRepository.findByExactNumber(card.getNumber());

            // then
            assertThat(foundCard).isEqualTo(card);
        }
    }

    public static class FindByNumberContains extends CardRepositoryIntegTest {
        @Test
        public void when_card_does_not_exist_no_card_should_be_returned() {
            // given
            String cardNumber = "1000000000000";

            // when
            List<Card> foundCards = cardRepository.findByNumberContains(cardNumber);

            // then
            assertThat(foundCards).isEmpty();
        }

        @Test
        public void when_entire_number_is_entered_single_result_should_be_returned() {
            // given
            Card card = fs.getCards().get(0);

            // when
            List<Card> foundCards = cardRepository.findByNumberContains(card.getNumber());

            // then
            assertThat(foundCards.size()).isEqualTo(1);
            assertThat(foundCards.get(0)).isEqualTo(card);
        }

        @Test
        public void when_number_part_is_entered_all_results_should_be_returned() {
            // given
            String cardNumber = fs.getCards().get(0).getNumber().substring(0, 1);

            // when
            List<Card> foundCards = cardRepository.findByNumberContains(cardNumber);

            // then
            assertThat(foundCards).isEqualTo(fs.getCards());
        }
    }

    public static class FindByCenter extends CardRepositoryIntegTest {
        @Test
        public void all_returned_cards_should_be_corresponding_to_center() {
            // when
            List<Card> centerCards = cardRepository.findByCenter(center);

            // then
            for (Card c : centerCards) {
                assertThat(c.getCenter()).isEqualTo(center);
            }
        }

        @Test
        public void all_cards_corresponding_to_center_should_be_returned() {
            // given
            List<Card> cardList = fs.getCards();

            // when
            List<Card> centerCards = cardRepository.findByCenter(center);
            cardList.removeAll(centerCards);

            // then
            for (Card c : cardList) {
                assertThat(c.getCenter()).isNotEqualTo(center);
            }
        }
    }

    public static class FindByOwner extends CardRepositoryIntegTest {
        @Test
        public void if_user_has_cards_they_should_be_returned() {
            // given
            Card cardWithOwner = fs.getCards().stream()
                    .filter(c -> c.getOwner() != null)
                    .findAny()
                    .get();

            // when
            List<Card> foundCards = cardRepository.findByOwner(cardWithOwner.getOwner());

            // then
            assertThat(foundCards).contains(cardWithOwner);
        }

        @Test
        public void if_user_has_no_cards_nothing_should_be_returned() {
            // given
            User userWithoutCard = new User();

            // when
            List<Card> foundCards = cardRepository.findByOwner(userWithoutCard);

            // then
            assertThat(foundCards).isEmpty();
        }
    }

    public static class NewCard extends CardRepositoryIntegTest {
        @Test
        public void when_card_number_is_invalid_no_card_is_created() {
            // given
            String number = "10";
            List<Card> allCards = cardRepository.listAll();

            // when
            Card card = cardRepository.newCard(number, CardStatus.ENABLED, center);

            // then
            assertThat(card).isNull();
            assertThat(cardRepository.listAll()).isEqualTo(allCards);
        }

        @Test
        public void when_card_number_is_valid_new_card_is_created() {
            // given
            String number = center.nextValidCardNumber();
            List<Card> allCards = cardRepository.listAll();

            // when
            Card card = cardRepository.newCard(number, CardStatus.ENABLED, center);

            // then
            assertThat(card).isNotNull();
            assertThat(cardRepository.listAll().size()).isEqualTo(allCards.size() + 1);
        }

        @Test
        public void when_no_card_number_is_passed_a_new_valid_card_is_created() {
            // given
            String expectedNumber = center.nextValidCardNumber();
            List<Card> allCards = cardRepository.listAll();

            // when
            Card card = cardRepository.newCard(null, CardStatus.ENABLED, center);

            // then
            assertThat(card).isNotNull();
            assertThat(card.getNumber()).isEqualTo(expectedNumber);
            assertThat(cardRepository.listAll().size()).isEqualTo(allCards.size() + 1);
        }
    }

    public static class NewBatch extends CardRepositoryIntegTest {
        @Test
        public void all_cards_in_batch_are_valid() {
            // given
            String startNumber = center.nextValidCardNumber();
            int batchSize = 100;

            // when
            List<Card> createdCards = cardRepository.newBatch(startNumber, batchSize, CardStatus.ENABLED, center);

            // then
            for (Card c : createdCards) {
                assertThat(cardRepository.cardNumberIsValid(c.getNumber(), center.getCode())).isTrue();
            }
        }

        @Test
        public void all_card_numbers_come_after_start_number() {
            // given
            String startNumber = center.nextValidCardNumber();
            int batchSize = 100;

            // when
            List<Card> createdCards = cardRepository.newBatch(startNumber, batchSize, CardStatus.ENABLED, center);

            // then
            BigInteger minNumber = createdCards.stream()
                    .map(c -> new BigInteger(c.getNumber()))
                    .sorted()
                    .collect(Collectors.toList())
                    .get(0);
            assertThat(minNumber).isGreaterThanOrEqualTo(new BigInteger(startNumber));
        }

        @Test
        public void correct_number_of_cards_are_created() {
            // given
            String startNumber = center.nextValidCardNumber();
            int batchSize = 100;

            // when
            List<Card> createdCards = cardRepository.newBatch(startNumber, batchSize, CardStatus.ENABLED, center);

            // then
            assertThat(createdCards.size()).isEqualTo(batchSize);
        }

        @Test
        public void all_cards_are_being_persisted() {
            // given
            String startNumber = center.nextValidCardNumber();
            int batchSize = 100;

            // when
            List<Card> createdCards = cardRepository.newBatch(startNumber, batchSize, CardStatus.ENABLED, center);

            // then
            List<Card> allCards = cardRepository.listAll();
            for (Card c : createdCards) {
                assertThat(allCards).contains(c);
            }
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
            int[] digits = center.getCode().chars()
                    .map(Character::getNumericValue)
                    .toArray();
            int[] multipliers = {3, 1, 3};
            int incorrectChecksum = 2;
            for(int i = 0; i < digits.length; i++) {
                incorrectChecksum += digits[i] * multipliers[i];
            }

            incorrectChecksum = (9 - incorrectChecksum % 10) % 10;
            String cardNumber = "2" + center.getCode() + "00000000" + incorrectChecksum;

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isFalse();
        }

        @Test
        public void when_card_number_satisfies_all_conditions_it_is_valid() {
            // given
            int[] digits = center.getCode().chars()
                    .map(Character::getNumericValue)
                    .toArray();
            int[] multipliers = {3, 1, 3};
            int checksum = 2;
            for(int i = 0; i < digits.length; i++) {
                checksum += digits[i] * multipliers[i];
            }

            checksum = (10 - checksum % 10) % 10;
            String cardNumber = "2" + center.getCode() + "00000000" + checksum;

            // when
            boolean valid = cardRepository.cardNumberIsValid(cardNumber);

            // then
            assertThat(valid).isTrue();
        }
    }
}
