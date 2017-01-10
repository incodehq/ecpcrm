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

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject CardRepository cardRepository;

    @Inject ChildRepository childRepository;

    DemoFixture fs;
    User user;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(ThreadLocalRandom.current().nextInt(0, fs.getUsers().size()));
        assertThat(user).isNotNull();
    }

    public static class GiveCard extends UserIntegTest {

        @Test
        public void when_card_does_not_exist_it_isnt_assigned() {
            // given
            Card card = cardRepository.findByOwner(user);
            if(card != null) {
                card.setOwner(null);
            }
            String cardNumber = "Not a CardNumber";

            // when
            user.giveCard(cardNumber);

            // then
            assertThat(cardRepository.findByOwner(user)).isNull();
        }

        @Test
        public void when_card_exists_it_is_assigned() {
            // given
            Card card = cardRepository.findByOwner(user);
            if(card != null) {
                card.setOwner(null);
            }
            card = fs.getCards().get(ThreadLocalRandom.current().nextInt(0, fs.getCards().size()));

            // when
            user.giveCard(card.getNumber());

            // then
            assertThat(cardRepository.findByOwner(user)).isEqualTo(card);
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

            // when
            user.newChild(childName);

            // then
            assertThat(user.getChildren().size()).isEqualTo(1);
            assertThat(user.getChildren().first().getName()).isEqualTo(childName);
        }

        @Test
        public void when_user_has_some_children_but_not_this_one_it_is_created() {
            // given
            User user = fs.getChildren().get(new Random().nextInt(fs.getChildren().size())).getParent();
            int numberOfChildren = user.getChildren().size();
            String childName = user.getChildren().first().getName();
            childName = childName.substring(2) + childName;

            // when
            user.newChild(childName);

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
            String childName = user.getChildren().first().getName();

            // when
            user.newChild(childName);

            // then
            assertThat(user.getChildren().size()).isEqualTo(numberOfChildren);
        }
    }
}
