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
package org.incode.eurocommercial.ecpcrm.integtests.tests.user;

import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;
    @Inject UserRepository userRepository;
    @Inject CenterRepository centerRepository;

    IntegTestFixture fs;
    User user;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(0);
        assertThat(user).isNotNull();
    }

    public static class ListAll extends UserRepositoryIntegTest {
        @Test
        public void all_users_should_be_listed() {
            // when
            List<User> allUsers = userRepository.listAll();

            // then
            assertThat(new TreeSet<>(allUsers)).isEqualTo(new TreeSet<>(fs.getUsers()));
        }
    }

    public static class FindByExactEmailAndCenter extends UserRepositoryIntegTest {
        @Test
        public void when_user_does_not_exist_no_user_should_be_returned() {
            // given
            String userEmail = "This is not the email of an actual User";
            Center center = user.getCenter();

            // when
            User foundUser = userRepository.findByExactEmailAndCenter(userEmail, center);

            // then
            assertThat(foundUser).isNull();
        }

        @Test
        public void when_user_exists_it_should_be_returned() {
            // when
            User foundUser = userRepository.findByExactEmailAndCenter(user.getEmail(), user.getCenter());

            // then
            assertThat(foundUser).isEqualTo(user);
        }

        @Test
        public void when_user_exists_but_not_for_given_center_no_user_should_be_returned() {
            // given
            int centerCode = 10;
            while (centerRepository.findByCode("0" + centerCode) != null) {
                centerCode++;
            }
            Center center = centerRepository.findOrCreate("0" + centerCode, "New Center", "" + centerCode, null);

            // when
            User foundUser = userRepository.findByExactEmailAndCenter(user.getEmail(), center);

            //then
            assertThat(foundUser).isNull();
        }
    }

    public static class FindByCenter extends UserRepositoryIntegTest {
        @Test
        public void when_center_has_no_users_no_users_should_be_returned() {
            // given
            int centerCode = 10;
            while (centerRepository.findByCode("0" + centerCode) != null) {
                centerCode++;
            }
            Center center = centerRepository.findOrCreate("0" + centerCode, "New Center", "" + centerCode, null);

            // when
            List<User> foundUsers = userRepository.findByCenter(center);

            // then
            assertThat(foundUsers).isEmpty();
        }

        @Test
        public void all_returned_users_should_belong_to_center() {
            // given
            Center center = user.getCenter();

            // when
            List<User> foundUsers = userRepository.findByCenter(center);

            // then
            for (User foundUser : foundUsers) {
                assertThat(foundUser.getCenter()).isEqualTo(center);
            }
        }

        @Test
        public void all_center_users_should_be_returned() {
            // given
            List<User> unfoundUsers = userRepository.listAll();
            Center center = user.getCenter();

            // when
            List<User> foundUsers = userRepository.findByCenter(center);
            unfoundUsers.removeAll(foundUsers);

            // then
            for(User unfoundUser : unfoundUsers) {
                assertThat(unfoundUser.getCenter()).isNotEqualTo(center);
            }
        }
    }

    public static class FindByEmaiLContains extends UserRepositoryIntegTest {
        @Test
        public void when_user_does_not_exist_no_user_should_be_returned() {
            // given
            String userEmail = "This is not the email of an actual User";

            // when
            List<User> foundUsers = userRepository.findByEmailContains(userEmail);

            // then
            assertThat(foundUsers.size()).isZero();
        }

        @Test
        public void when_full_email_is_entered_it_should_be_the_only_result() {
            // when
            List<User> foundUsers = userRepository.findByEmailContains(user.getEmail());

            // then
            assertThat(foundUsers.size()).isEqualTo(1);
            assertThat(foundUsers.get(0)).isEqualTo(user);
        }

        @Test
        public void when_partial_matching_email_is_entered_multiple_results_should_be_returned() {
            // when
            List<User> foundUsers = userRepository.findByEmailContains("@");

            // then
            assertThat(foundUsers.size()).isGreaterThan(1);
        }
    }

    public static class FindByNameContains extends UserRepositoryIntegTest {
        @Test
        public void when_user_does_not_exist_no_user_should_be_returned() {
            // given
            String userName = "This is not the name of an actual User";

            // when
            List<User> foundUsers = userRepository.findByNameContains(userName);

            // then
            assertThat(foundUsers.size()).isZero();
        }

        @Test
        public void when_full_first_or_last_name_is_entered_at_least_one_result_should_be_returned() {
            // when
            List<User> foundUsersByFirst = userRepository.findByNameContains(user.getFirstName());
            List<User> foundUsersByLast = userRepository.findByNameContains(user.getLastName());

            // then
            assertThat(foundUsersByFirst.size()).isPositive();
            assertThat(foundUsersByLast.size()).isPositive();
        }

        @Test
        public void when_partial_matching_name_is_entered_multiple_results_should_be_returned() {
            // when
            List<User> foundUsers = userRepository.findByNameContains(user.getFirstName().substring(0,2));

            // then
            assertThat(foundUsers.size()).isPositive();
        }
    }

    public static class FindByFirstAndLastName extends UserRepositoryIntegTest {
        @Test
        public void when_user_does_not_exist_no_user_should_be_returned() {
            // given
            String userName = "This is not the name of an actual User";

            // when
            List<User> foundUsers = userRepository.findByFirstAndLastName(userName, userName);

            // then
            assertThat(foundUsers.size()).isZero();
        }

        @Test
        public void when_full_first_or_last_name_is_entered_at_least_one_result_should_be_returned() {
            // when
            List<User> foundUsersByFirst = userRepository.findByFirstAndLastName(user.getFirstName(), "");
            List<User> foundUsersByLast = userRepository.findByFirstAndLastName("", user.getLastName());

            // then
            assertThat(foundUsersByFirst.size()).isPositive();
            assertThat(foundUsersByLast.size()).isPositive();
        }

        @Test
        public void when_full_first_and_last_name_is_entered_at_least_one_result_should_be_returned() {
            // when
            List<User> foundUsers = userRepository.findByFirstAndLastName(user.getFirstName(), user.getLastName());

            // then
            assertThat(foundUsers.size()).isPositive();
        }

        @Test
        public void when_partial_first_and_last_name_is_entered_at_least_one_result_should_be_returned() {
            // when
            List<User> foundUsers = userRepository.findByFirstAndLastName(
                    user.getFirstName().substring(0, 2), user.getLastName().substring(0, 2));

            // then
            assertThat(foundUsers.size()).isPositive();
        }
    }

    public static class FindByReference extends UserRepositoryIntegTest {
        @Test
        public void when_user_does_not_exist_no_user_should_be_returned() {
            // given
            String userReference = "This is not the reference of an actual User";

            // when
            User foundUser = userRepository.findByReference(userReference);

            // then
            assertThat(foundUser).isNull();
        }

        @Test
        public void when_user_exists_it_should_be_returned() {
            // when
            User foundUser = userRepository.findByReference(user.getReference());

            // then
            assertThat(foundUser).isEqualTo(user);
        }
    }
}
