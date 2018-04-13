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
package org.incode.eurocommercial.ecpcrm.integtests.tests.translations;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.wrapper.InvalidException;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardMenu;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.impersonation.UserServiceWithImpersonation;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmFixtureServiceRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmRegularRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.users.EcpCrmAdminUser;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserMenu;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

public class GenerateTranslationsIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject CardMenu cardMenu;
    @Inject CardRepository cardRepository;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject UserMenu userMenu;
    @Inject UserRepository userRepository;
    @Inject UserServiceWithImpersonation userServiceWithImpersonation;

    IntegTestFixture fs;
    Center center;
    CardRequest request;
    User user;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(0);
        request = cardRequestRepository.listOpenRequests().stream()
                .filter(cr -> cr.getRequestingUser().getCenter() == center)
                .findFirst()
                .orElse(null);
        user = userRepository.findByCenter(center).get(0);
        userServiceWithImpersonation.setUser(EcpCrmAdminUser.USER_NAME, EcpCrmRegularRoleAndPermissions.ROLE_NAME, EcpCrmFixtureServiceRoleAndPermissions.ROLE_NAME);
    }

    public static class CardMenuValidateNewCard extends GenerateTranslationsIntegTest {
        @Test
        public void when_card_number_is_invalid_we_expect_invalid_exception() {
            // given
            String number = "89469q649836";

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(cardMenu).newCard(number, center);
            }).isInstanceOf(InvalidException.class);
        }
    }

    public static class CardRequestValidateApprove extends GenerateTranslationsIntegTest {
        @Test
        public void when_card_number_is_invalid_we_expect_invalid_exception() {
            // given
            String number = "89469q649836";

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(request).approve(number);
            }).isInstanceOf(InvalidException.class);
        }
    }

    public static class CardRequestValidateReapprove extends GenerateTranslationsIntegTest {
        @Test
        public void when_card_number_is_invalid_we_expect_invalid_exception() {
            // given
            String number = "89469q649836";

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(request).deny();
                wrap(request).reapprove(number);
            }).isInstanceOf(InvalidException.class);
        }
    }

    public static class UserValidateNewCard extends GenerateTranslationsIntegTest {
        @Test
        public void when_card_number_is_empty_we_expect_invalid_exception() {
            // given
            String number = "";

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(user).newCard(number);
            }).isInstanceOf(InvalidException.class);
        }

        @Test
        public void when_card_number_is_invalid_we_expect_invalid_exception() {
            // given
            String number = "89469q649836";

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(user).newCard(number);
            }).isInstanceOf(InvalidException.class);
        }

        @Test
        public void when_card_already_has_owner_we_expect_invalid_exception() {
            // given
            String number = cardRepository.findByCenter(center).stream()
                    .filter(c -> c.getOwner() != null && c.getOwner() != user)
                    .map(Card::getNumber)
                    .findFirst()
                    .orElse("");

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(user).newCard(number);
            }).isInstanceOf(InvalidException.class);
        }
    }

    public static class UserMenuValidateNewUser extends GenerateTranslationsIntegTest {
        @Test
        public void when_email_already_exists_we_expect_invalid_exception() {
            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(userMenu).newUser(
                        user.isEnabled(),
                        user.getTitle(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getAddress(),
                        user.getZipcode(),
                        user.getCity(),
                        user.getPhoneNumber(),
                        center,
                        "",
                        user.isPromotionalEmails(),
                        user.getHasCar()
                );
            }).isInstanceOf(InvalidException.class);
        }

        @Test
        public void when_card_number_is_invalid_we_expect_invalid_exception() {
            // given
            String number = "89469q649836";

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(userMenu).newUser(
                        user.isEnabled(),
                        user.getTitle(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getFirstName() + user.getLastName() + "12345@gmail.com",
                        user.getAddress(),
                        user.getZipcode(),
                        user.getCity(),
                        user.getPhoneNumber(),
                        center,
                        number,
                        user.isPromotionalEmails(),
                        user.getHasCar()
                );
            }).isInstanceOf(InvalidException.class);
        }
        @Test
        public void when_card_number_does_not_exist_we_expect_invalid_exception() {
            // given
            String number = center.nextValidCardNumber();

            // then
            Assertions.assertThatThrownBy(() -> {
                // when
                wrap(userMenu).newUser(
                        user.isEnabled(),
                        user.getTitle(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getFirstName() + user.getLastName() + "12345@gmail.com",
                        user.getAddress(),
                        user.getZipcode(),
                        user.getCity(),
                        user.getPhoneNumber(),
                        center,
                        number,
                        user.isPromotionalEmails(),
                        user.getHasCar()
                );
            }).isInstanceOf(InvalidException.class);
        }
    }
}
