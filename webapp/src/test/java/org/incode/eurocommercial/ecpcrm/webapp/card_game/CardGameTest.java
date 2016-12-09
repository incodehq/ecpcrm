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
package org.incode.eurocommercial.ecpcrm.webapp.card_game;

import org.junit.Ignore;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.webapp.ecp_crm_test.EcpCrmTest;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

@Ignore
public class CardGameTest extends EcpCrmTest {

    private String endpoint = "card-game";

    private String sendRequest(String cardNumber, String win, String desc) throws Exception {
        String request = "{\"card\": \"" + cardNumber + "\", \"win\": \"" + win + "\", \"desc\": \"" + desc + "\"}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        String cardNumber = "";
        String win = "";
        String desc = "";
        assertThatJson(sendRequest(cardNumber, win, desc))
                .node("status").isEqualTo(302);
    }

    @Test
    public void when_card_does_not_exist_we_expect_303_error() throws Exception {
        String cardNumber = "1";
        String win = "";
        String desc = "";
        assertThatJson(sendRequest(cardNumber, win, desc))
            .node("status").isEqualTo(303);
    }

    @Test
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        String cardNumber = "3922071988537";
        String win = "";
        String desc = "";
        assertThatJson(sendRequest(cardNumber, win, desc))
            .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_303_error() throws Exception {
    }

    @Test
    public void when_card_exists_but_has_no_user_we_expect_303_error() throws Exception {
        String cardNumber = "1";
        String win = "";
        String desc = "";
        assertThatJson(sendRequest(cardNumber, win, desc))
            .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_card_exists_but_card_user_is_invalid_we_expect_304_error() throws Exception {

    }

    @Test
    @Ignore
    // TODO: Send two requests to ensure that the card has already played
    public void when_card_exists_but_has_already_played_we_expect_315_error() throws Exception {
        String cardNumber = "1";
        String win = "";
        String desc = "";
        sendRequest(cardNumber, win, desc);
        assertThatJson(sendRequest(cardNumber, win, desc))
            .node("status").isEqualTo(315);
    }

    @Test
    @Ignore
    // TODO: Create a test card to test this with, to ensure that this will be true
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
    }

}