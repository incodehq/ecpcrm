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

import org.joda.time.LocalDate;
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
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class CardRequestIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;

    @Inject CardRequestRepository cardRequestRepository;

    @Inject ApiService apiService;


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

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String origin = "";
        Title title = null;
        String firstName = "";
        String lastName = "";
        String email = "";
        LocalDate birthdate = null;
        String children = "";
        Boolean hasCar = null;
        String address = "";
        String zipcode = "";
        String city = "";
        String phoneNumber = "";
        boolean promotionalEmails = false;
        String checkItem = "";
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(302);
    }

    @Test
    @Ignore
    // TODO: I can't see how this is checked in the old api
    public void when_email_exists_and_user_is_invalid_we_expect_304_error() throws Exception {
    }

    @Test
    /* Valid check means checkItem is equal to firstName + " " + lastName of user */
    public void when_email_exists_and_card_is_not_lost_and_check_is_valid_we_expect_318_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        card.setStatus(CardStatus.ENABLED);
        User user = card.getOwner();

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = firstName + " " + lastName;
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(318);
    }

    @Test
    /* Invalid check means checkItem is not equal to firstName + " " + lastName of user */
    public void when_email_exists_and_card_is_not_lost_and_check_is_invalid_we_expect_306_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        card.setStatus(CardStatus.ENABLED);
        User user = card.getOwner();

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "Incorrect Check Item";
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(306);
    }

    @Test
    /* When checkItem is not present, it checks the first and last name */
    public void when_email_exists_and_card_is_not_lost_and_check_is_not_set_and_user_does_not_match_we_expect_305_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        card.setStatus(CardStatus.ENABLED);
        User user = card.getOwner();

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = "Incorrect first name";
        String lastName = "Incorrect last name";
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "";
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(305);
    }

    @Test
    /* When checkItem is not present, it checks the first and last name */
    public void when_email_exists_and_card_is_not_lost_and_check_is_not_set_and_user_matches_we_expect_318_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        card.setStatus(CardStatus.ENABLED);
        User user = card.getOwner();

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "";
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(318);
    }

    @Test
    public void when_email_exists_and_lost_is_true_we_expect_card_to_be_tagged_as_lost() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        card.setStatus(CardStatus.ENABLED);
        User user = card.getOwner();

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "";
        boolean lost = true;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(card.getStatus()).isEqualTo(CardStatus.LOST);
    }

    @Test
    /* Can't request duplicate cards */
    public void when_email_exists_but_user_already_has_a_handled_request_we_expect_307_error() throws Exception {
        // given
        while(card.getOwner() == null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        User user = card.getOwner();
        cardRequestRepository.findOrCreate(user);

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "";
        boolean lost = false;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(307);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this one
    public void when_user_data_cant_be_updated_we_expect_316_error() throws Exception {
    }

    @Test
    /* If we want to use an existing user without card, we need to set lost = true */
    public void when_email_exists_and_lost_is_true_and_user_has_no_requests_we_expect_happy_response() throws Exception {
        // given
        while(!user.getCards().isEmpty() || cardRequestRepository.openRequestForUser(user) != null) {
            user = fs.getUsers().get(new Random().nextInt(fs.getUsers().size()));
        }

        String origin = "borne";
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();
        String checkItem = "";
        /* If we want to use an existing user without card, we need to set lost = true */
        boolean lost = true;

        // when
        Result result = apiService.cardRequest(
                origin, title, firstName, lastName, email, birthdate, children, hasCar,
                address, zipcode, city, phoneNumber, promotionalEmails, checkItem, lost);

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(cardRequestRepository.openRequestForUser(user)).isNotNull();
    }

}
