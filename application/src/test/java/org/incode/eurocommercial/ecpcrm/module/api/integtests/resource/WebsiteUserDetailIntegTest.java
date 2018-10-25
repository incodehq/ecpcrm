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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.incode.eurocommercial.ecpcrm.module.api.EcpCrmResource;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail.WebsiteUserDetailResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class WebsiteUserDetailIntegTest extends ApiModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;

    @Inject UserRepository userRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    @Inject ApiService apiService;

    private EcpCrmResource resource;


    private ApiIntegTestFixture fs;
    private Center center;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;
    private User user;
    private List<User> userList;

    @Before
    public void setUp() throws Exception {
        // given
        resource = new EcpCrmResourceForTesting();
        serviceRegistry.injectServicesInto(resource);

        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));

        deviceList = authenticationDeviceRepository.findByCenter(center);
        device = deviceList.get(new Random().nextInt(deviceList.size()));

        userList = userRepository.findByCenter(center);
        user = userList.get(new Random().nextInt(userList.size()));

        assertThat(center).isNotNull();
        assertThat(device).isNotNull();
        assertThat(user).isNotNull();
    }

    @Test
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
        String email = user.getEmail();
        String checkCode = apiService.computeCheckCode(user.getEmail());

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        // when
        Response response = resource.websiteUserDetail(deviceName, deviceSecret, requestJson);

        //then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

//    @Test
//    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
//        // given
//        String deviceName = device.getName();
//        String deviceSecret = device.getSecret();
//        String email = user.getEmail();
//        String checkCode = apiService.computeCheckCode(user.getEmail());
//
//        /* Testing every required argument individually */
//        Object[] args = {deviceName, deviceSecret, email, checkCode};
//        int[] mandatory = {2, 3};
//        for(int i : mandatory) {
//            Object[] a = args.clone();
//            a[i] = null;
//
//            // when
//            Method m = ApiService.class.getMethod(
//                    "websiteUserDetail", String.class, String.class, String.class, String.class);
//            Result result = (Result) m.invoke(apiService, a);
//
//            // then
//            assertThat(result.getStatus()).isEqualTo(302);
//        }
//    }

    @Test
    public void when_user_does_not_exist_we_expect_304_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = "THIS IS DEFINITELY NOT AN EXISTING EMAIL";
        String checkCode = apiService.computeCheckCode(email);

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        // when
        Response response = resource.websiteUserDetail(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_USER, "Invalid user").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_check_code_is_incorrect_we_expect_402_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = user.getEmail();
        String checkCode = "INCORRECT CHECK CODE";

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        // when
        Response response = resource.websiteUserDetail(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INCORRECT_CHECK_CODE, "Incorrect check code").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_user_exists_and_check_code_is_correct_we_expect_happy_case() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String email = user.getEmail();
        String checkCode = apiService.computeCheckCode(email);

        String requestJson = String.format("{\"email\": \"%s\", \"check_code\": \"%s\"}", email, checkCode);

        // when
        Response response = resource.websiteUserDetail(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.ok(new WebsiteUserDetailResponseViewModel(user)).asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }
}
