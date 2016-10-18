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
package org.incode.eurocommercial.ecpcrm.webapp.card_request;

import org.junit.Ignore;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.webapp.ecp_crm_test.EcpCrmTest;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

public class CardRequestTest extends EcpCrmTest {

    private String endpoint = "card-request";

    private String sendRequest(String firstName, String lastName, String email, String checkItem, String lost) throws Exception {
        String address = "Test Lane 1";
        String zipcode = "12345";
        String city = "Testsville";
        String optin = "Test";
        String request =
                "{"
                        + "\"origin\": \"" + "borne" + "\", "
                        + "\"hostess\": \"" + "" + "\", "
                        + "\"title\": \"" + "mr" + "\", "
                        + "\"first_name\": \"" + firstName + "\", "
                        + "\"last_name\": \"" + lastName + "\", "
                        + "\"email\": \"" + email + "\", "
                        + "\"birthdate\": \"" + "" + "\", "
                        + "\"children\": \"" + "" + "\", "
                        + "\"nb_children\": \"" + "" + "\", "
                        + "\"car\": \"" + "" + "\", "
                        + "\"address\": \"" + address + "\", "
                        + "\"zipcode\": \"" + zipcode + "\", "
                        + "\"city\": \"" + city + "\", "
                        + "\"phone\": \"" + "" + "\", "
                        + "\"optin\": \"" + optin + "\", "
                        + "\"check_item\": \"" + checkItem + "\", "
                        + "\"lost\": \"" + lost + "\""
              + "}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        String firstName = "";
        String lastName = "";
        String email = "";
        String checkItem = "";
        String lost = "";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(302);
    }

    @Test
    @Ignore
    // TODO: We should create a specific test user for this
    public void when_email_exists_and_user_is_invalid_we_expect_304_error() throws Exception {
        String firstName = "";
        String lastName = "";
        String email = "";
        String checkItem = "";
        String lost = "";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(304);
    }

    @Test
    @Ignore
    // TODO: The email should be existing and the first and last name should be correct for the user
    public void when_email_exists_and_card_not_lost_and_check_is_valid_we_expect_318_error() throws Exception {
        String firstName = "Test";
        String lastName = "Test";
        String email = "TODO: Existing email";
        String checkItem = firstName + " " + lastName;
        String lost = "false";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(318);
    }

    @Test
    @Ignore
    // TODO: The email should be existing
    public void when_email_exists_and_card_not_lost_and_check_is_invalid_we_expect_306_error() throws Exception {
        String firstName = "Test";
        String lastName = "Test";
        String email = "TODO: Existing email";
        String checkItem = "XXX";
        String lost = "false";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(306);
    }

    @Test
    @Ignore
    // TODO: The email should be existing, but the name should not be correct for the user
    public void when_email_exists_and_card_not_lost_and_user_does_not_match_we_expect_305_error() throws Exception {
        String firstName = "Test";
        String lastName = "Test";
        String email = "TODO: Existing email";
        String checkItem = "";
        String lost = "false";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(305);
    }

    @Test
    @Ignore
    // TODO: The email should be existing and the first and last name should be correct for the user
    public void when_email_exists_and_no_check_is_made_we_expect_318_error() throws Exception {
        String firstName = "Test";
        String lastName = "Test";
        String email = "TODO: Existing email";
        String checkItem = "";
        String lost = "false";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(318);
    }

    @Test
    @Ignore
    // TODO: The email should be existing. The cards status should be checked
    public void when_email_exists_and_card_is_already_bound_we_expect_card_to_be_tagged_as_lost() throws Exception {
        String firstName = "Test";
        String lastName = "Test";
        String email = "TODO: Existing email";
        String checkItem = "";
        String lost = "true";
        sendRequest(firstName, lastName, email, checkItem, lost);
        // TODO: Check that card status is lost
    }

    @Test
    @Ignore
    // TODO: We should create a specific test case for this
    public void when_email_exists_but_card_already_has_a_handled_request_we_expect_307_error() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this one
    public void when_user_data_cant_be_updated_we_expect_316_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_user_data_can_be_updated_we_expect_happy_response() throws Exception {
        String firstName = "Test";
        String lastName = "Test";
        String email = "Test";
        String checkItem = "";
        String lost = "false";
        assertThatJson(sendRequest(firstName, lastName, email, checkItem, lost))
            .node("status").isEqualTo(200);
        // TODO: Check that a request has been created
    }
}