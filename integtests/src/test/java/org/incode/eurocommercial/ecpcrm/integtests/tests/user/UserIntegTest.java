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

import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject CardRepository cardRepository;

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
}
