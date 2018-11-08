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
package org.incode.eurocommercial.ecpcrm.module.api.integtests.resource;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.api.EcpCrmResource;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate.WebsiteUserCreateResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteUserCreateIntegTest extends ApiModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;

    @Inject CardRepository cardRepository;
    @Inject UserRepository userRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    private EcpCrmResource resource;

    private ApiIntegTestFixture fs;
    private Card card;
    private Center center;
    private User user;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;
    private List<User> userList;
    private List<Card> cardList;

    @Before
    public void setUp() throws Exception {
        // given
        resource = new EcpCrmResourceForTesting();
        serviceRegistry.injectServicesInto(resource);

        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        cardList = cardRepository.findByCenter(center);
        userList = userRepository.findByCenter(center);
        deviceList = authenticationDeviceRepository.findByCenter(center);
        card = cardList.get(new Random().nextInt(cardList.size()));
        user = userList.get(new Random().nextInt(userList.size()));
        device = deviceList.get(new Random().nextInt(deviceList.size()));

        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
        assertThat(device).isNotNull();
        assertThat(card).isNotNull();
    }

    @Test
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
        String centerId = center.getId();
        String checkCode = ApiService.computeCheckCode(user.getEmail());
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();

        String requestJson = String.format(
                "{\"center_id\": \"%s\"," +
                        "\"check_code\": \"%s\"," +
                        "\"title\": \"%s\"," +
                        "\"first_name\": \"%s\"," +
                        "\"last_name\": \"%s\"," +
                        "\"email\": \"%s\"," +
                        "\"birthdate\": \"%s\"," +
                        "\"children\": \"%s\"," +
                        "\"nb_children\": \"%s\"," +
                        "\"car\": \"%s\"," +
                        "\"address\": \"%s\"," +
                        "\"zipcode\": \"%s\"," +
                        "\"city\": \"%s\"," +
                        "\"phone\": \"%s\"," +
                        "\"optin\": \"%s\" }" ,
                centerId,
                checkCode,
                title,
                firstName,
                lastName,
                email,
                birthdate,
                children,
                nbChildren,
                hasCar,
                address,
                zipcode,
                city,
                phoneNumber,
                promotionalEmails
        );

        // when
        Response response = resource.websiteUserCreate(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String centerId = center.getId();
        String checkCode = ApiService.computeCheckCode(user.getEmail());
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();

        String requestJson = String.format(
                "{\"center_id\": \"%s\"," +
                        "\"check_code\": \"%s\"," +
                        "\"title\": \"%s\"," +
                        "\"first_name\": \"%s\"," +
                        "\"last_name\": \"%s\"," +
                        "\"email\": \"%s\"," +
                        "\"birthdate\": \"%s\"," +
                        "\"children\": \"%s\"," +
                        "\"nb_children\": \"%s\"," +
                        "\"car\": \"%s\"," +
                        "\"address\": \"%s\"," +
                        "\"zipcode\": \"%s\"," +
                        "\"city\": \"%s\"," +
                        "\"phone\": \"%s\"," +
                        "\"optin\": \"%s\" }" ,
                centerId,
                checkCode,
                title,
                firstName,
                lastName,
                email,
                birthdate,
                children,
                nbChildren,
                hasCar,
                address,
                zipcode,
                city,
                phoneNumber,
                promotionalEmails
        );

        String[] requiredParameters = {"center_id", "check_code", "title", "first_name", "last_name", "email"};

        for (String parameter : requiredParameters) {
            String replaceRegex = String.format("\"%s\": \".*?\",?", parameter);
            String newRequestJson = requestJson.replaceAll(replaceRegex, "");

            // when
            Response response = resource.websiteUserCreate(deviceName, deviceSecret, newRequestJson);

            // then
            Response expectedResponse = Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter").asResponse();
            assertThat(response).isEqualToComparingFieldByField(expectedResponse);
        }
    }

    @Test
    public void when_check_code_is_invalid_we_expect_402_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = ApiService.computeCheckCode(user.getEmail()) + "NOT A CORRECT CHECK CODE";

        String requestJson = String.format(
                "{\"center_id\": \"%s\"," +
                        "\"check_code\": \"%s\"," +
                        "\"title\": \"%s\"," +
                        "\"first_name\": \"%s\"," +
                        "\"last_name\": \"%s\"," +
                        "\"email\": \"%s\"," +
                        "\"birthdate\": \"%s\"," +
                        "\"children\": \"%s\"," +
                        "\"nb_children\": \"%s\"," +
                        "\"car\": \"%s\"," +
                        "\"address\": \"%s\"," +
                        "\"zipcode\": \"%s\"," +
                        "\"city\": \"%s\"," +
                        "\"phone\": \"%s\"," +
                        "\"optin\": \"%s\" }" ,
                centerId,
                checkCode,
                title,
                firstName,
                lastName,
                email,
                birthdate,
                children,
                nbChildren,
                hasCar,
                address,
                zipcode,
                city,
                phoneNumber,
                promotionalEmails
        );

        // when
        Response response = resource.websiteUserCreate(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INCORRECT_CHECK_CODE, "Incorrect check code").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_user_with_email_already_exists_we_expect_305_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = ApiService.computeCheckCode(user.getEmail());

        String requestJson = String.format(
                "{\"center_id\": \"%s\"," +
                        "\"check_code\": \"%s\"," +
                        "\"title\": \"%s\"," +
                        "\"first_name\": \"%s\"," +
                        "\"last_name\": \"%s\"," +
                        "\"email\": \"%s\"," +
                        "\"birthdate\": \"%s\"," +
                        "\"children\": \"%s\"," +
                        "\"nb_children\": \"%s\"," +
                        "\"car\": \"%s\"," +
                        "\"address\": \"%s\"," +
                        "\"zipcode\": \"%s\"," +
                        "\"city\": \"%s\"," +
                        "\"phone\": \"%s\"," +
                        "\"optin\": \"%s\" }" ,
                centerId,
                checkCode,
                title,
                firstName,
                lastName,
                email,
                birthdate,
                children,
                nbChildren,
                hasCar,
                address,
                zipcode,
                city,
                phoneNumber,
                promotionalEmails
        );

        // when
        Response response = resource.websiteUserCreate(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_EMAIL_ALREADY_EXISTS, "Email already exists").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    // TODO: 316 error when a user can't be created, 314 when a fake card can't be created
    // However, these things can not occur when the email address is not already in use

    @Test
    public void when_email_is_unique_and_check_code_is_valid_we_expect_happy_response() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getFirstName() + user.getLastName() + "8732583265@gmail.com";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = ApiService.computeCheckCode(email);

        String requestJson = String.format(
                "{\"center_id\": \"%s\"," +
                        "\"check_code\": \"%s\"," +
                        "\"title\": \"%s\"," +
                        "\"first_name\": \"%s\"," +
                        "\"last_name\": \"%s\"," +
                        "\"email\": \"%s\"," +
                        "\"birthdate\": \"%s\"," +
                        "\"children\": \"%s\"," +
                        "\"nb_children\": \"%s\"," +
                        "\"car\": \"%s\"," +
                        "\"address\": \"%s\"," +
                        "\"zipcode\": \"%s\"," +
                        "\"city\": \"%s\"," +
                        "\"phone\": \"%s\"," +
                        "\"optin\": \"%s\" }",
                centerId,
                checkCode,
                title,
                firstName,
                lastName,
                email,
                birthdate,
                children,
                nbChildren,
                hasCar,
                address,
                zipcode,
                city,
                phoneNumber,
                promotionalEmails
        );

        // then
        Response response = resource.websiteUserCreate(deviceName, deviceSecret, requestJson);
        User userCreated = userRepository.findByExactEmailAndCenter(email, center);

        //when
        Response expectedResponse = Result.ok(new WebsiteUserCreateResponseViewModel(userCreated)).asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }


    //This test showing minimum requirements for userCreateWebsite
    @Test
    public void when_email_is_unique_and_check_code_is_valid_and_no_birthdate_we_expect_happy_response() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getFirstName() + user.getLastName() + "9998732583265@gmail.com";
        // only the above params are needed to create a new users via the website.
        // below are not given via the Json, simulating the users not filling this in via the website.
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = ApiService.computeCheckCode(email);

        String requestJson = String.format(
                "{\"center_id\": \"%s\"," +
                        "\"check_code\": \"%s\"," +
                        "\"title\": \"%s\"," +
                        "\"first_name\": \"%s\"," +
                        "\"last_name\": \"%s\"," +
                        "\"email\": \"%s\"," +
                        "\"optin\": \"%s\" }",
                centerId,
                checkCode,
                title,
                firstName,
                lastName,
                email,
                promotionalEmails
        );

        // then
        Response response = resource.websiteUserCreate(deviceName, deviceSecret, requestJson);
        User userCreated = userRepository.findByExactEmailAndCenter(email, center);

        //when
        Response expectedResponse = Result.ok(new WebsiteUserCreateResponseViewModel(userCreated)).asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }
}
