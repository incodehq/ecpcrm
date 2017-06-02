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

import java.util.List;
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
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class UserCreateIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject UserRepository userRepository;

    @Inject ApiService apiService;


    private DemoFixture fs;
    private Card card;
    private Center center;
    private User user;
    private List<User> userList;
    private List<Card> cardList;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        cardList = cardRepository.findByCenter(center);
        userList = userRepository.findByCenter(center);
        card = cardList.get(new Random().nextInt(cardList.size()));
        user = userList.get(new Random().nextInt(userList.size()));

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        Title title = null;
        String firstName = "";
        String lastName = "";
        String email = "";
        String address = "";
        String zipcode = "";
        String city = "";
        String cardNumber = "";
        boolean promotionalEmails = false;

        // when
        Result result = apiService.userCreate(title, firstName, lastName, email, address, zipcode, city, center, cardNumber, promotionalEmails);

        // then
        assertThat(result.getStatus()).isEqualTo(302);
    }

    @Test
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        // given
        Title title = user.getTitle();
        String firstName = "Testy";
        String lastName = "McTestFace";
        String email = "testymctestface1991@emailio.com";
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String cardNumber = "20";
        boolean promotionalEmails = false;

        // when
        Result result = apiService.userCreate(title, firstName, lastName, email, address, zipcode, city, center, cardNumber, promotionalEmails);

        // then
        assertThat(result.getStatus()).isEqualTo(312);
    }

    @Test
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        card.setStatus(CardStatus.DISABLED);

        Title title = user.getTitle();
        String firstName = "Testy";
        String lastName = "McTestFace";
        String email = "testymctestface1991@emailio.com";
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String cardNumber = card.getNumber();
        boolean promotionalEmails = false;

        // when
        Result result = apiService.userCreate(title, firstName, lastName, email, address, zipcode, city, center, cardNumber, promotionalEmails);

        // then
        assertThat(result.getStatus()).isEqualTo(303);
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
            card = cardList.get(new Random().nextInt(cardList.size()));
        }

        Title title = user.getTitle();
        String firstName = "Testy";
        String lastName = "McTestFace";
        String email = "testymctestface1991@emailio.com";
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String cardNumber = card.getNumber();
        boolean promotionalEmails = false;

        // when
        Result result = apiService.userCreate(title, firstName, lastName, email, address, zipcode, city, center, cardNumber, promotionalEmails);

        // then
        assertThat(result.getStatus()).isEqualTo(308);
    }

    @Test
    public void when_email_already_exists_we_expect_309_error() throws Exception {
        // given
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String cardNumber = card.getNumber();
        boolean promotionalEmails = false;

        // when
        Result result = apiService.userCreate(title, firstName, lastName, email, address, zipcode, city, center, cardNumber, promotionalEmails);

        // then
        assertThat(result.getStatus()).isEqualTo(309);
    }

    @Test
    @Ignore
    // TODO: Not sure of a reason why this should happen
    public void when_new_user_is_created_but_its_data_cant_be_updated_we_expect_316_error() throws Exception {
    }

    @Test
    @Ignore
    // Redundant test, see 303, 308, there's no other reason why a card cannot be bound
    public void when_card_exists_but_cant_be_bound_we_expect_314_error() throws Exception {

    }

    @Test
    @Ignore
    // Nonexisting cards with valid numbers can always be created
    public void when_card_does_not_exist_and_cant_be_created_or_bound_we_expect_314_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_can_be_bound_we_expect_happy_response() throws Exception {
        // given
        while(card.getOwner() != null) {
            card = cardList.get(new Random().nextInt(cardList.size()));
        }

        Title title = user.getTitle();
        String firstName = user.getFirstName() + "TEST";
        String lastName = user.getLastName() + "TEST";
        String email = firstName + "." + lastName + "@test.com";
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String cardNumber = card.getNumber();
        boolean promotionalEmails = false;

        // when
        Result result = apiService.userCreate(title, firstName, lastName, email, address, zipcode, city, center, cardNumber, promotionalEmails);

        // then
        assertThat(result.getStatus()).isEqualTo(200);
    }

}
