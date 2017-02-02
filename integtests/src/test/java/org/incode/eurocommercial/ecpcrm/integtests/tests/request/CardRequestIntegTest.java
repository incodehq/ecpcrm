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
package org.incode.eurocommercial.ecpcrm.integtests.tests.request;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRequestIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject CardRepository cardRepository;

    DemoFixture fs;
    CardRequest cardRequest;
    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        cardRequest = fs.getCardRequests().get(
                new Random().nextInt(fs.getCardRequests().size()));
        assertThat(cardRequest).isNotNull();
    }

    public static class Approve extends CardRequestIntegTest {
        @Test
        public void when_card_is_invalid_card_requests_stays_the_same() {
            // given
            String cardNumber = "10";

            // when
            cardRequest.approve(cardNumber);

            // then
            assertThat(cardRequest.getApproved()).isNull();
            assertThat(cardRequest.getAssignedCard()).isNull();
        }

        @Test
        public void when_card_is_valid_card_request_is_approved_and_card_is_assigned() {
            // given
            List<Card> availableCards = cardRepository.findByCenter(cardRequest.getRequestingUser().getCenter());
            Card card = availableCards.get(new Random().nextInt(availableCards.size()));

            // when
            cardRequest.approve(card.getNumber());

            // then
            assertThat(cardRequest.getApproved()).isTrue();
            assertThat(cardRequest.getAssignedCard()).isEqualTo(card);
        }
    }

    public static class Deny extends CardRequestIntegTest {
        @Test
        public void card_request_is_denied_and_no_card_is_assigned() {
            // when
            cardRequest.deny();

            // then
            assertThat(cardRequest.getApproved()).isFalse();
            assertThat(cardRequest.getAssignedCard()).isNull();
        }
    }

    public static class Reapprove extends CardRequestIntegTest {
        @Test
        public void when_card_is_invalid_card_requests_stays_the_same() {
            // given
            cardRequest.deny();
            String cardNumber = "10";

            // when
            cardRequest.reapprove(cardNumber);

            // then
            assertThat(cardRequest.getApproved()).isFalse();
            assertThat(cardRequest.getAssignedCard()).isNull();
        }

        @Test
        public void when_card_is_valid_card_request_is_approved_and_card_is_assigned() {
            // given
            cardRequest.deny();
            List<Card> availableCards = cardRepository.findByCenter(cardRequest.getRequestingUser().getCenter());
            Card card = availableCards.get(new Random().nextInt(availableCards.size()));

            // when
            cardRequest.reapprove(card.getNumber());

            // then
            assertThat(cardRequest.getApproved()).isTrue();
            assertThat(cardRequest.getAssignedCard()).isEqualTo(card);
        }
    }

}
