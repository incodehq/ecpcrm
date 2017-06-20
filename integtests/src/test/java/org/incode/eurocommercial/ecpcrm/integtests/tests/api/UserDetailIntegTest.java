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
import org.incode.eurocommercial.ecpcrm.app.services.api.UserViewModel;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.numerator.NumeratorRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class UserDetailIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;
    @Inject CardRequestRepository cardRequestRepository;
    @Inject UserRepository userRepository;
    @Inject NumeratorRepository numeratorRepository;

    @Inject ApiService apiService;


    private DemoFixture fs;
    private Center center;
    private User user;
    private List<User> userList;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        userList = userRepository.findByCenter(center);
        user = userList.get(new Random().nextInt(userList.size()));

        assertThat(center).isNotNull();
        assertThat(user).isNotNull();
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String reference = null;

        // when
        Result result = apiService.userDetail(reference);

        // then
        assertThat(result.getStatus()).isEqualTo(302);
    }

    @Test
    public void when_user_does_not_exist_we_expect_304_error() throws Exception {
        // given
        String reference = numeratorRepository.findByName("userNumerator").nextIncrementStr();

        // when
        Result result = apiService.userDetail(reference);

        // then
        assertThat(result.getStatus()).isEqualTo(302);
    }

    @Test
    public void when_user_exists_we_expect_it_to_be_returned() throws Exception {
        // given
        String reference = user.getReference();

        // when
        Result result = apiService.userDetail(reference);

        // then
        assertThat(result.getStatus()).isEqualTo(200);

        assertThat(result.getResponse() instanceof UserViewModel).isTrue();
        UserViewModel returnedUser = (UserViewModel) result.getResponse();
        assertThat(returnedUser).isEqualTo(UserViewModel.fromUser(user));
    }

}
