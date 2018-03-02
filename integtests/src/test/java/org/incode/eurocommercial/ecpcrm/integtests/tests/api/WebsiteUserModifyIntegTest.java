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

import java.lang.reflect.Method;
import java.util.List;
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
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class WebsiteUserModifyIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject UserRepository userRepository;

    @Inject ApiService apiService;


    private IntegTestFixture fs;
    private Card card;
    private Center center;
    private User user;
    private List<User> userList;
    private List<Card> cardList;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
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
        Center center = this.center;
        String checkCode = apiService.computeCheckCode(user.getEmail());
        String cardNumber = card.getNumber();
        Title title = user.getTitle();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate birthdate = user.getBirthDate();
        Boolean hasChildren = user.getChildren().size() > 1;
        String nbChildren = "" + user.getChildren().size();
        Boolean hasCar = user.getHasCar();
        String address = user.getAddress();
        String zipcode = user.getZipcode();
        String city = user.getCity();
        String phoneNumber = user.getPhoneNumber();
        boolean promotionalEmails = user.isPromotionalEmails();

        /* Testing every required argument individually */
        Object[] args = {
                center, checkCode, cardNumber, title, firstName, lastName, email, birthdate, hasChildren,
                nbChildren, hasCar, address, zipcode, city, phoneNumber, promotionalEmails
        };
        int[] mandatory = {0, 1, 2, 3, 4, 5, 6};
        for(int i : mandatory) {
            Object[] a = args.clone();
            a[i] = null;

            // when
            Method m = ApiService.class.getMethod(
                    "websiteUserModify",
                    Center.class, String.class, String.class, Title.class, String.class, String.class, String.class, LocalDate.class,
                    Boolean.class, String.class, Boolean.class, String.class, String.class, String.class, String.class, boolean.class
            );
            Result result = (Result) m.invoke(apiService, a);

            // then
            assertThat(result.getStatus()).isEqualTo(302);
        }
    }
}
