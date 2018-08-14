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
package org.incode.eurocommercial.ecpcrm.module.api.integtests;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteCardRequestIntegTest extends ApiModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;

    @Inject CardRepository cardRepository;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject UserRepository userRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    @Inject ApiService apiService;


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
        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));

        cardList = cardRepository.findByCenter(center);
        userList = userRepository.findByCenter(center).stream()
                .filter(u -> cardRequestRepository.openRequestForUser(u) == null)
                .collect(Collectors.toList());

        deviceList = authenticationDeviceRepository.findByCenter(center);

        device = deviceList.get(new Random().nextInt(deviceList.size()));
        card = cardList.get(new Random().nextInt(cardList.size()));
        user = userList.get(new Random().nextInt(userList.size()));

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
        assertThat(device).isNotNull();
    }

    @Test
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
        String origin = "site";
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = "";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = apiService.computeCheckCode(email);
        String boutiques = "";

        // when
        Result result = apiService.websiteCardRequest(
                deviceName, deviceSecret, origin, centerId, title, firstName, lastName, email, password, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkCode, boutiques
        );

        // then
        assertThat(result.getStatus()).isEqualTo(301);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "site";
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = "";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = apiService.computeCheckCode(email);
        String boutiques = "";

        /* Testing every required argument individually */
        Object[] args = {
                deviceName, deviceSecret, origin, centerId, title, firstName, lastName, email, password, birthdate,
                children, nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkCode, boutiques
        };

        int[] mandatory = {3, 4, 5, 6, 7, 13, 14, 15, 18};
        for(int i : mandatory) {
            Object[] a = args.clone();
            a[i] = null;

            // when
            Method m = ApiService.class.getMethod(
                    "websiteCardRequest",
                    String.class, String.class, String.class, String.class, Title.class, String.class, String.class,
                    String.class, String.class, LocalDate.class, String.class, String.class, Boolean.class, String.class,
                    String.class, String.class, String.class, Boolean.class, String.class, String.class);
            Result result = (Result) m.invoke(apiService, a);

            // then
            assertThat(result.getStatus()).isEqualTo(302);
        }
    }

    @Test
    public void when_user_does_not_exist_we_expect_304_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "site";
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getFirstName() + user.getLastName() + "8732583265@gmail.com";
        String password = "";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = apiService.computeCheckCode(email);
        String boutiques = "";

        // when
        Result result = apiService.websiteCardRequest(
                deviceName, deviceSecret, origin, centerId, title, firstName, lastName, email, password, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkCode, boutiques
        );

        // then
        assertThat(result.getStatus()).isEqualTo(304);
    }

    @Test
    public void when_check_code_does_not_match_we_expect_402_error() {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "site";
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = "";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = "INVALID CHECK CODE";
        String boutiques = "";

        // when
        Result result = apiService.websiteCardRequest(
                deviceName, deviceSecret, origin, centerId, title, firstName, lastName, email, password, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkCode, boutiques
        );

        // then
        assertThat(result.getStatus()).isEqualTo(402);
    }


    @Test
    public void when_user_exists_and_check_code_matches_we_expect_card_request_to_be_created() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String origin = "site";
        String centerId = center.getId();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = "";
        LocalDate birthdate = user.getBirthDate();
        String children = "";
        String nbChildren = "";
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        Boolean promotionalEmails = user.isPromotionalEmails();
        String checkCode = apiService.computeCheckCode(email);
        String boutiques = "";

        // when
        Result result = apiService.websiteCardRequest(
                deviceName, deviceSecret, origin, centerId, title, firstName, lastName, email, password, birthdate, children,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails, checkCode, boutiques
        );

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(cardRequestRepository.openRequestForUser(user)).isNotNull();
    }
}
