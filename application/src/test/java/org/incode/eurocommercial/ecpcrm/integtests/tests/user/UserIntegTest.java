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
package org.incode.eurocommercial.ecpcrm.integtests.tests.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject ClockService clockService;
    @Inject CardRepository cardRepository;
    @Inject ChildRepository childRepository;

    IntegTestFixture fs;
    User user;
    List<Card> availableUnassignedCards;
    List<Card> availableAssignedCards;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(0);

        availableUnassignedCards = cardRepository.listUnassignedCards().stream()
                .filter(c -> c.getCenter() == user.getCenter())
                .collect(Collectors.toList());
        /* Make sure that there is at least one available card */

        availableAssignedCards = fs.getCards().stream()
                .filter(c -> c.getOwner() != null && c.getOwner() != user && c.getCenter() == user.getCenter())
                .collect(Collectors.toList());

        assertThat(user).isNotNull();
    }

    public static class NewCard extends UserIntegTest {

        @Test
        public void when_card_number_is_invalid_it_is_not_assigned() {
            // given
            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            String cardNumber = "Not a CardNumber";

            // when
            user.newCard(cardNumber);

            // then
            assertThat(cardRepository.listAll()).isEqualTo(cardsFromListAll);
            assertThat(cardRepository.findByOwner(user)).isEqualTo(cardsFromQuery);
            assertThat(Lists.newArrayList(user.getCards())).isEqualTo(cardsOnUser);
        }

        @Test
        public void when_card_number_is_valid_but_card_does_not_exist_it_is_created_and_assigned() {
            // given
            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            String cardNumber = user.getCenter().nextValidCardNumber();

            // when
            user.newCard(cardNumber);
            Card createdCard = cardRepository.findByExactNumber(cardNumber);

            // then
            assertThat(createdCard).isNotNull();

            assertThat(cardsFromListAll).doesNotContain(createdCard);
            assertThat(cardsFromQuery).doesNotContain(createdCard);
            assertThat(cardsOnUser).doesNotContain(createdCard);

            assertThat(cardRepository.listAll()).contains(createdCard);
            assertThat(cardRepository.findByOwner(user)).contains(createdCard);
            assertThat(user.getCards()).contains(createdCard);
        }

        @Test
        public void when_card_is_already_assigned_to_user_nothing_changes() {
            // given
            user = fs.getUsers().stream()
                    .filter(u -> !u.getCards().isEmpty())
                    .findAny()
                    .get();

            Card card = user.getCards().first();
            card.unenable();

            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            // when
            user.newCard(card.getNumber());

            // then
            assertThat(cardRepository.listAll()).isEqualTo(cardsFromListAll);
            assertThat(cardRepository.findByOwner(user)).isEqualTo(cardsFromQuery);
            assertThat(Lists.newArrayList(user.getCards())).isEqualTo(cardsOnUser);
            assertThat(card.getStatus()).isEqualTo(CardStatus.DISABLED);
        }

        @Test
        public void when_card_exists_and_is_available_it_is_assigned() {
            // given
            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            Card card = cardRepository.listUnassignedCards().stream()
                    .filter(c -> c.getCenter() == user.getCenter())
                    .findAny()
                    .get();

            // when
            user.newCard(card.getNumber());

            // then
            assertThat(cardsFromListAll).contains(card);
            assertThat(cardsFromQuery).doesNotContain(card);
            assertThat(cardsOnUser).doesNotContain(card);

            assertThat(cardRepository.findByOwner(user)).contains(card);
            assertThat(user.getCards()).contains(card);
        }

        @Test
        public void when_card_was_assigned_to_another_user_nothing_happens() {
            // given
            Card card = fs.getCards().stream()
                    .filter(c -> c.getOwner() != null && c.getOwner() != user && c.getCenter() == user.getCenter())
                    .findAny()
                    .get();

            User firstOwner = card.getOwner();

            // when
            user.newCard(card.getNumber());

            // then
            assertThat(cardRepository.findByOwner(firstOwner)).contains(card);
            assertThat(firstOwner.getCards()).contains(card);
            assertThat(cardRepository.findByOwner(user)).doesNotContain(card);
            assertThat(user.getCards()).doesNotContain(card);
            assertThat(card.getOwner()).isEqualTo(firstOwner);
        }

        @Test
        public void when_card_is_assigned_to_user_its_given_at_is_set() {
            // given
            Card card = cardRepository.listUnassignedCards().stream()
                    .filter(c -> c.getCenter() == user.getCenter())
                    .findAny()
                    .get();


            // when
            user.newCard(card.getNumber());

            // then
            assertThat(card.getGivenToUserAt().toLocalDate()).isEqualTo(clockService.now());
        }

        @Test
        public void when_card_is_assigned_to_user_its_sent_at_is_not_set() {
            // given
            Card card = cardRepository.listUnassignedCards().stream()
                    .filter(c -> c.getCenter() == user.getCenter())
                    .findAny()
                    .get();

            // when
            user.newCard(card.getNumber());

            // then
            assertThat(card.getSentToUserAt()).isNull();
        }
    }

    public static class NewChild extends UserIntegTest {
        @Test
        public void when_user_has_no_children_a_new_child_is_created() {
            // given
            user = fs.getUsers().stream()
                    .filter(u -> u.getChildren().isEmpty())
                    .findAny()
                    .get();

            String childName = "Bob";
            LocalDate date = clockService.now();

            // when
            user.newChild(childName, Gender.MALE, date);

            // then
            assertThat(user.getChildren().size()).isEqualTo(1);
            assertThat(user.getChildren().first().getName()).isEqualTo(childName);
        }

        @Test
        public void when_user_has_some_children_but_not_this_one_it_is_created() {
            // given
            user = fs.getChildren().get(0).getParent();
            int numberOfChildren = user.getChildren().size();
            Child child = user.getChildren().first();
            String childName = child.getName().substring(2) + child.getName();

            // when
            user.newChild(childName, child.getGender(), child.getBirthdate());

            // then
            assertThat(user.getChildren().size()).isEqualTo(numberOfChildren + 1);
            Child newChild = childRepository.findByParentAndName(user, childName);
            assertThat(newChild).isNotNull();
            assertThat(user.getChildren().contains(newChild)).isTrue();
        }

        @Test
        public void when_child_already_exists_for_user_no_new_child_is_created() {
            user = fs.getChildren().get(0).getParent();
            int numberOfChildren = user.getChildren().size();
            Child child = user.getChildren().first();

            // when
            user.newChild(child.getName(), child.getGender(), child.getBirthdate());

            // then
            assertThat(user.getChildren().size()).isEqualTo(numberOfChildren);
        }
    }
}
