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
package org.incode.eurocommercial.ecpcrm.webapp.card_check;

import org.junit.Ignore;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.webapp.ecp_crm_test.EcpCrmTest;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

public class CardCheckTest extends EcpCrmTest {

    private String endpoint = "card-check";

    private String sendRequest(String cardNumber) throws Exception {
        String request = "{\"card\": \"" + cardNumber +"\", "
                       + "\"origin\": \"borne\"}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        String cardNumber = "";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(302);
    }

    @Test
    @Ignore
    /* When the device type is not app and the card number contains 3922 */
    // TODO: Hard to test, need to find a nonexisting valid cardnumber that starts with 3922
    public void when_card_does_not_exist_and_is_outdated_we_expect_319_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(319);
    }

    @Test
    /* When the card number does not match the required pattern */
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(312);
    }

    @Test
    /* When the card status is "tochange" */
    public void when_card_exists_but_is_outdated_we_expect_319_error() throws Exception {
        String cardNumber = "3922071977845";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(319);
    }

    @Test
    /* When the card status is not "enabled" */
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        String cardNumber = "2037000090418";
        assertThatJson(sendRequest(cardNumber))
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
    // TODO: Need to find an unused valid card number, which is not too easy by itself, but also for some reason a user can't be created for it
    public void when_card_does_not_exist_and_a_new_user_cant_be_created_for_it_we_expect_313_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(312);
    }

    @Test
    @Ignore
    // TODO: Have to think of a reason why a new card would be created but can't be linked to an - also new - user
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

    // TODO: These tests depend on whether a game was already played this day for the user, so we have to figure out a test case
    @Test
    @Ignore
    public void when_card_exists_but_cant_play_game_we_expect_sad_response() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
    }


}