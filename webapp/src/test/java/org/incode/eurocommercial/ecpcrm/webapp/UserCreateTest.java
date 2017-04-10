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
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class UserCreateTest extends EcpCrmTestAbstract {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardGameRepository cardGameRepository;


    private DemoFixture fs;
    private Card card;
    private Center center;
    private User user;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        user = fs.getUsers().get(new Random().nextInt(fs.getUsers().size()));

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
    }

    private String endpoint = "user-create";

    private String sendRequest(String cardNumber, String firstName, String lastName, String email) throws Exception {
        String address = "Test Lane 1";
        String zipcode = "12345";
        String city = "Testsville";
        String optin = "Test";
        String request =
                "{"
                        + "\"card\": \"" + cardNumber + "\", "
                        + "\"title\": \"" + "mr" + "\", "
                        + "\"first_name\": \"" + firstName + "\", "
                        + "\"last_name\": \"" + lastName + "\", "
                        + "\"address\": \"" + address + "\", "
                        + "\"zipcode\": \"" + zipcode + "\", "
                        + "\"city\": \"" + city + "\", "
                        + "\"email\": \"" + email + "\", "
                        + "\"optin\": \"" + optin + "\""
                        + "}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String cardNumber = "";
        String firstName = "";
        String lastName = "";
        String email = "";

        // when
        String response = sendRequest(cardNumber, firstName, lastName, email);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(302);
    }

    @Test
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        // given
        String cardNumber = "1";
        String firstName = "Test";
        String lastName = "Test";
        String email = "test@test.com";

        // when
        String response = sendRequest(cardNumber, firstName, lastName, email);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(312);
    }

    @Test
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = card.getNumber();
        String firstName = "Test";
        String lastName = "Test";
        String email = "test@test.com";

        // when
        String response = sendRequest(cardNumber, firstName, lastName, email);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Awaiting response from Biggerband
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
    }

    @Test
    public void when_card_exists_but_is_already_bound_to_user_we_expect_308_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        String cardNumber = card.getNumber();
        String firstName = "Test";
        String lastName = "Test";
        String email = "test@test.com";

        // when
        String response = sendRequest(cardNumber, firstName, lastName, email);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(308);
    }

    @Test
    public void when_email_already_exists_we_expect_309_error() throws Exception {
        // given
        String cardNumber = "";
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();

        // when
        String response = sendRequest(cardNumber, firstName, lastName, email);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(309);
    }

    @Test
    @Ignore
    // TODO: Not sure of a reason why this should happen
    public void when_new_user_is_created_but_its_data_cant_be_updated_we_expect_316_error() throws Exception {
    }

    @Test
    @Ignore
    // Redundant test, see 303, 308
    public void when_card_exists_but_cant_be_bound_we_expect_314_error() throws Exception {

    }

    @Test
    @Ignore
    // This won't happen
    public void when_card_does_not_exist_and_cant_be_created_or_bound_we_expect_314_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_can_be_bound_we_expect_happy_response() throws Exception {
        // given
        while(card.getOwner() != null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        String cardNumber = card.getNumber();
        String firstName = user.getFirstName() + "TEST";
        String lastName = user.getLastName() + "TEST";
        String email = firstName + "." + lastName + "@test.com";

        // when
        String response = sendRequest(cardNumber, firstName, lastName, email);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(200);
    }

}