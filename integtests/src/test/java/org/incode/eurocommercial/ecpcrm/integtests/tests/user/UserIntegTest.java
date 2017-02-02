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
import java.util.Random;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;

    @Inject ChildRepository childRepository;

    DemoFixture fs;
    User user;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(new Random().nextInt(fs.getUsers().size()));
        assertThat(user).isNotNull();
    }

    public static class NewCard extends UserIntegTest {

        @Test
        public void when_card_number_is_invalid_it_isnt_assigned() {
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
        public void when_card_number_is_valid_but_card_doesnt_exist_it_is_created_and_assigned() {
            // given
            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            Numerator centerNumerator = user.getCenter().getNumerator();
            String cardNumber;
            do {
                cardNumber = centerNumerator.nextIncrementStr();
            } while(!cardRepository.cardNumberIsValid(cardNumber, user.getCenter().getReference()));

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
            while(user.getCards().isEmpty()) {
                user = fs.getUsers().get(new Random().nextInt(fs.getUsers().size()));
            }
            Card card = user.getCards().first();

            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            // when
            user.newCard(card.getNumber());

            // then
            assertThat(cardRepository.listAll()).isEqualTo(cardsFromListAll);
            assertThat(cardRepository.findByOwner(user)).isEqualTo(cardsFromQuery);
            assertThat(Lists.newArrayList(user.getCards())).isEqualTo(cardsOnUser);
        }

        @Test
        public void when_card_exists_it_is_assigned() {
            // given
            List<Card> cardsFromListAll = cardRepository.listAll();
            List<Card> cardsFromQuery = cardRepository.findByOwner(user);
            List<Card> cardsOnUser = Lists.newArrayList(user.getCards());

            Card card;
            do {
                card = cardsFromListAll.get(new Random().nextInt(cardsFromListAll.size()));
            } while(user.getCards().contains(card));

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
        public void when_card_was_assigned_to_another_user_it_is_unassigned() {
            // given
            List<Card> cardsFromListAll = cardRepository.listAll();

            Card card;
            do {
                card = cardsFromListAll.get(new Random().nextInt(cardsFromListAll.size()));
            } while(user.getCards().contains(card) || card.getOwner() == null);

            User firstOwner = card.getOwner();

            // when
            user.newCard(card.getNumber());

            // then
            assertThat(cardRepository.findByOwner(firstOwner)).doesNotContain(card);
            assertThat(firstOwner.getCards()).doesNotContain(card);
        }
    }

    public static class NewChild extends UserIntegTest {
        @Test
        public void when_user_has_no_children_a_new_child_is_created() {
            // given
            User user;
            do {
                user = fs.getUsers().get(new Random().nextInt(fs.getUsers().size()));
            } while(!user.getChildren().isEmpty());
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
            User user = fs.getChildren().get(new Random().nextInt(fs.getChildren().size())).getParent();
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
            User user = fs.getChildren().get(new Random().nextInt(fs.getChildren().size())).getParent();
            int numberOfChildren = user.getChildren().size();
            Child child = user.getChildren().first();

            // when
            user.newChild(child.getName(), child.getGender(), child.getBirthdate());

            // then
            assertThat(user.getChildren().size()).isEqualTo(numberOfChildren);
        }
    }
}
