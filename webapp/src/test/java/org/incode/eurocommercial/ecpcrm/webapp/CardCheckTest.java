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
package org.incode.eurocommercial.ecpcrm.webapp;

import java.util.Random;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class CardCheckTest extends EcpCrmTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardGameRepository cardGameRepository;


    private DemoFixture fs;
    private Card card;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        assertThat(card).isNotNull();
    }

    private String endpoint = "card-check";

    private String sendRequest(String cardNumber) throws Exception {
        String request = "{\"card\": \"" + cardNumber +"\", "
                       + "\"origin\": \"borne\"}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String cardNumber = "";

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
            .node("status").isEqualTo(302);
    }

    @Test
    @Ignore
    /* When the device type is not app and the card number contains 3922 */
    // TODO: Hard to test, need to find a nonexisting valid cardnumber that starts with 3922
    public void when_card_does_not_exist_and_is_outdated_we_expect_319_error() throws Exception {
        // given
        String cardNumber = "";

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(319);
    }

    @Test
    /* When the card number does not match the required pattern */
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        // given
        String cardNumber = "1";

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(312);
    }

    @Test
    /* When the card status is "tochange" */
    public void when_card_exists_but_is_outdated_we_expect_319_error() throws Exception {
        // given
        card.setStatus(CardStatus.TOCHANGE);
        String cardNumber = card.getNumber();

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
            .node("status").isEqualTo(319);
    }

    @Test
    /* When the card status is not "enabled" */
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = card.getNumber();

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(317);
    }

    @Test
    @Ignore
    // TODO: Empty users aren't created for cards. So not sure if this applies.
    public void when_card_does_not_exist_and_a_new_user_cant_be_created_for_it_we_expect_313_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(312);
    }

    @Test
    @Ignore
    // TODO: Have to think of a reason why a new card would be created but can't be linked to an - also new - user.
    // TODO: Besides, see TODO at 312
    public void when_card_does_not_exist_and_a_new_user_is_created_for_it_but_they_cant_be_linked_we_expect_314_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(314);
    }

    @Test
    @Ignore
    // TODO: Can't even find status field in user table, which is what is checked in the source code
    public void when_card_exists_but_card_user_is_invalid_we_expect_304_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(304);
    }

    /* These two give regular responses */

    @Test
    public void when_card_exists_but_cant_play_game_we_expect_sad_response() throws Exception {
        // given
        card.play();
        String cardNumber = card.getNumber();

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(200);
        assertThatJson(response)
                .node("game").isEqualTo(false);
    }

    @Test
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
        // given
        while(!card.canPlay()) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        String cardNumber = card.getNumber();

        // when
        String response = sendRequest(cardNumber);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(200);
        assertThatJson(response)
                .node("game").isEqualTo(true);
    }

}