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
package org.incode.eurocommercial.ecpcrm.integtests.tests.api;

import java.util.Random;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.app.services.api.ApiService;
import org.incode.eurocommercial.ecpcrm.app.services.api.Result;
import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class CardCheckIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;

    @Inject ApiService apiService;


    DemoFixture fs;
    Card card;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        assertThat(card).isNotNull();
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String cardNumber = "";
        String origin = "";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(302);
    }

    @Test
    /* When the device type is not app and the card number contains 3922 */
    public void when_card_does_not_exist_and_is_outdated_we_expect_319_error() throws Exception {
        // given
        String cardNumber = "3922000000000";
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(319);
    }

    @Test
    /* When the card number does not match the required pattern */
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        // given
        String cardNumber = "10";
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(312);
    }

    @Test
    /* When the card status is "tochange" */
    public void when_card_exists_but_is_outdated_we_expect_319_error() throws Exception {
        // given
        card.setStatus(CardStatus.TOCHANGE);
        String cardNumber = card.getNumber();
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(319);
    }

    @Test
    /* When the card status is not "enabled" */
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = card.getNumber();
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Empty users aren't created for cards. This can't happen in our new system.
    public void when_card_does_not_exist_or_is_unbound_and_a_new_user_cant_be_created_for_it_we_expect_313_error() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Empty users aren't created for cards. This can't happen in our new system.
    public void when_card_does_not_exist_or_is_unbound_and_a_new_user_is_created_for_it_but_they_cant_be_linked_we_expect_314_error() throws Exception {
    }

    @Test
    public void when_card_exists_but_card_user_is_invalid_we_expect_304_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        card.getOwner().setEnabled(false);
        String cardNumber = card.getNumber();
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(304);
    }

    /* These two give regular responses */

    @Test
    public void when_card_exists_but_cant_play_game_we_expect_sad_response() throws Exception {
        // given
        card.play();
        String cardNumber = card.getNumber();
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        // TODO: Convert response to viewmodel and check the following is false:
        // assertThatJson(response)
                // .node("game").isEqualTo(false);
    }

    @Test
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
        // given
        while(!card.canPlay()) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        String cardNumber = card.getNumber();
        String origin = "borne";

        // when
        Result result = apiService.cardCheck(cardNumber, origin);

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        // TODO: Convert response to viewmodel and check the following is true:
        // assertThatJson(response)
        // .node("game").isEqualTo(true);
    }

}
