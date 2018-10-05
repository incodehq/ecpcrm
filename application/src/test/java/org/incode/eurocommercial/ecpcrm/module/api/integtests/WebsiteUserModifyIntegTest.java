///**
// * Copyright 2015-2016 Eurocommercial Properties NV
// * <p>
// * Licensed under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied.  See the License for the
// * specific language governing permissions and limitations
// * under the License.
// */
//package org.incode.eurocommercial.ecpcrm.module.api.integtests;
//
//import java.lang.reflect.Method;
//import java.util.List;
//import java.util.Random;
//
//import javax.inject.Inject;
//
//import org.joda.time.LocalDate;
//import org.junit.Before;
//import org.junit.Test;
//
//import org.apache.isis.applib.fixturescripts.FixtureScripts;
//
//import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
//import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
//import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
//import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
//import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
//import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
//import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
//import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
//import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
//import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
//import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class WebsiteUserModifyIntegTest extends ApiModuleIntegTestAbstract {
//    @Inject FixtureScripts fixtureScripts;
//
//    @Inject CardRepository cardRepository;
//    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;
//    @Inject UserRepository userRepository;
//
//    @Inject ApiService apiService;
//
//    private ApiIntegTestFixture fs;
//    private Card card;
//    private Center center;
//    private User user;
//    private AuthenticationDevice device;
//    private List<AuthenticationDevice> deviceList;
//    private List<User> userList;
//    private List<Card> cardList;
//
//    @Before
//    public void setUp() throws Exception {
//        // given
//        fs = new ApiIntegTestFixture();
//        fixtureScripts.runFixtureScript(fs, null);
//
//        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
//        cardList = cardRepository.findByCenter(center);
//        userList = userRepository.findByCenter(center);
//        deviceList = authenticationDeviceRepository.findByCenter(center);
//        card = cardList.get(new Random().nextInt(cardList.size()));
//        user = userList.get(new Random().nextInt(userList.size()));
//        device = deviceList.get(new Random().nextInt(deviceList.size()));
//
//        assertThat(center).isNotNull();
//        assertThat(user).isNotNull();
//        assertThat(device).isNotNull();
//        assertThat(card).isNotNull();
//    }
//
//    @Test
//    public void when_device_is_invalid_we_expect_301_error() throws Exception {
//        // given
//        String deviceName = device.getName();
//        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
//        String checkCode = apiService.computeCheckCode(user.getEmail());
//        String cardNumber = card.getNumber();
//        String email = user.getEmail();
//        Title title = user.getTitle();
//        String firstName = user.getFirstName();
//        String lastName = user.getLastName();
//        LocalDate birthdate = user.getBirthDate();
//        String children = "";
//        String nbChildren = "" + user.getChildren().size();
//        Boolean hasCar = user.getHasCar();
//        String address = user.getAddress();
//        String zipcode = user.getZipcode();
//        String city = user.getCity();
//        String phoneNumber = user.getPhoneNumber();
//        Boolean promotionalEmails = user.isPromotionalEmails();
//
//        // when
//        Result result = apiService.websiteUserModify(
//                deviceName, deviceSecret, checkCode, cardNumber, email, title, firstName, lastName,
//                birthdate, children, nbChildren, hasCar, address, zipcode, city, phoneNumber,
//                promotionalEmails);
//
//        // then
//        assertThat(result.getStatus()).isEqualTo(301);
//    }
//
//    @Test
//    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
//        // given
//        String deviceName = device.getName();
//        String deviceSecret = device.getSecret();
//        String checkCode = apiService.computeCheckCode(user.getEmail());
//        String cardNumber = card.getNumber();
//        String email = user.getEmail();
//        Title title = user.getTitle();
//        String firstName = user.getFirstName();
//        String lastName = user.getLastName();
//        LocalDate birthdate = user.getBirthDate();
//        String children = "";
//        String nbChildren = "" + user.getChildren().size();
//        Boolean hasCar = user.getHasCar();
//        String address = user.getAddress();
//        String zipcode = user.getZipcode();
//        String city = user.getCity();
//        String phoneNumber = user.getPhoneNumber();
//        Boolean promotionalEmails = user.isPromotionalEmails();
//
//        /* Testing every required argument individually */
//        Object[] args = {
//                deviceName, deviceSecret, checkCode, cardNumber, email, title, firstName, lastName, birthdate, children,
//                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails
//        };
//        int[] mandatory = {2, 4, 5};
//        for(int i : mandatory) {
//            Object[] a = args.clone();
//            a[i] = null;
//
//            // when
//            Method m = ApiService.class.getMethod(
//                    "websiteUserModify",
//                    String.class, String.class, String.class, String.class, String.class, Title.class,
//                    String.class, String.class, LocalDate.class, String.class, String.class, Boolean.class,
//                    String.class, String.class, String.class, String.class, Boolean.class);
//            Result result = (Result) m.invoke(apiService, a);
//
//            // then
//            assertThat(result.getStatus()).isEqualTo(302);
//        }
//    }
//
//    @Test
//    public void when_user_does_not_exist_we_expect_304_error() throws Exception {
//        // given
//        String deviceName = device.getName();
//        String deviceSecret = device.getSecret();
//        String checkCode = apiService.computeCheckCode(user.getEmail());
//        String cardNumber = card.getNumber();
//        String email = user.getFirstName() + user.getLastName() + "8732583265@gmail.com";
//        Title title = user.getTitle();
//        String firstName = user.getFirstName();
//        String lastName = user.getLastName();
//        LocalDate birthdate = user.getBirthDate();
//        String children = "";
//        String nbChildren = "" + user.getChildren().size();
//        Boolean hasCar = user.getHasCar();
//        String address = user.getAddress();
//        String zipcode = user.getZipcode();
//        String city = user.getCity();
//        String phoneNumber = user.getPhoneNumber();
//        Boolean promotionalEmails = user.isPromotionalEmails();
//
//        // when
//        Result result = apiService.websiteUserModify(
//                deviceName, deviceSecret, checkCode, cardNumber, email, title, firstName, lastName,
//                birthdate, children, nbChildren, hasCar, address, zipcode, city, phoneNumber,
//                promotionalEmails);
//
//        // then
//        assertThat(result.getStatus()).isEqualTo(304);
//    }
//
//    @Test
//    public void when_user_exists_we_expect_it_to_get_updated() throws Exception {
//        // given
//        String deviceName = device.getName();
//        String deviceSecret = device.getSecret();
//        String checkCode = apiService.computeCheckCode(user.getEmail());
//        String cardNumber = card.getNumber();
//        String email = user.getEmail();
//        Title title = user.getTitle();
//        String firstName = user.getFirstName();
//        String lastName = user.getLastName();
//        LocalDate birthdate = user.getBirthDate();
//        String children = "";
//        String nbChildren = "" + user.getChildren().size();
//        Boolean hasCar = user.getHasCar();
//        String address = "New Address";
//        String zipcode = "New Zipcode";
//        String city = "New City";
//        String phoneNumber = "New Phone Number";
//        Boolean promotionalEmails = user.isPromotionalEmails();
//
//        // when
//        Result result = apiService.websiteUserModify(
//                deviceName, deviceSecret, checkCode, cardNumber, email, title, firstName, lastName,
//                birthdate, children, nbChildren, hasCar, address, zipcode, city, phoneNumber,
//                promotionalEmails);
//
//        // then
//        assertThat(result.getStatus()).isEqualTo(200);
//        assertThat(user.getAddress()).isEqualTo(address);
//        assertThat(user.getZipcode()).isEqualTo(zipcode);
//        assertThat(user.getCity()).isEqualTo(city);
//        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
//    }
//}
