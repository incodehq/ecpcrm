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

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.registry.ServiceRegistry2;

import org.incode.eurocommercial.ecpcrm.module.api.EcpCrmResource;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.module.api.fixture.ApiIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck.CardCheckResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;

import static org.assertj.core.api.Assertions.assertThat;

public class CardCheckIntegTest extends ApiModuleIntegTestAbstract {
    @Inject FixtureScripts fixtureScripts;
    @Inject ServiceRegistry2 serviceRegistry;

    @Inject CardRepository cardRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    private EcpCrmResource resource;

    private ApiIntegTestFixture fs;
    private Card card;
    private Center center;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;

    @Before
    public void setUp() throws Exception {
        // given
        resource = new EcpCrmResourceForTesting();
        serviceRegistry.injectServicesInto(resource);

        fs = new ApiIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().stream()
                .filter(c -> c.canPlay() && c.getOwner() != null)
                .findAny()
                .get();

        center = card.getCenter();

        deviceList = authenticationDeviceRepository.findByCenter(center);
        device = deviceList.get(new Random().nextInt(deviceList.size()));
    }

    @Test
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
        String cardNumber = card.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = "";
        String origin = "";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    /* When the device type is not app and the card number contains 3922 */
    public void when_card_does_not_exist_and_is_outdated_we_expect_319_error() throws Exception {
        // given
        device.setType(AuthenticationDeviceType.BORNE);

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = "3922000000000";
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_OUTDATED_CARD, "Outdated card").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    /* When the card number does not match the required pattern */
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = "10";
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_CARD_NUMBER, "Invalid card number").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    /* When the card status is "tochange" */
    public void when_card_exists_but_is_outdated_and_device_type_is_not_app_we_expect_319_error() throws Exception {
        // given
        device = authenticationDeviceRepository.newAuthenticationDevice(
                center, AuthenticationDeviceType.SITE, "New device", "SECRET", true);
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        card.setStatus(CardStatus.TOCHANGE);
        String cardNumber = card.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_OUTDATED_CARD, "Outdated card").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    /* When the card status is not "enabled" */
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = card.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_CARD, "Invalid card").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
        // given
        Center otherCenter = fs.getCenters().stream()
                .filter(c -> !c.equals(center))
                .findAny().get();
        Card otherCard = cardRepository.findByCenter(otherCenter).stream()
                .filter(card -> card.getStatus() != CardStatus.DISABLED)
                .findAny().get();

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = otherCard.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_CARD_CENTER_NOT_EQUAL_TO_DEVICE_CENTER, "Card center is not equal to device center").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

//    @Test
//    @Ignore
//    // Empty users aren't created for cards. This can't happen in our new system.
//    public void when_card_does_not_exist_or_is_unbound_and_a_new_user_cant_be_created_for_it_we_expect_313_error() throws Exception {
//    }
//
//    @Test
//    @Ignore
//    // Empty users aren't created for cards. This can't happen in our new system.
//    public void when_card_does_not_exist_or_is_unbound_and_a_new_user_is_created_for_it_but_they_cant_be_linked_we_expect_314_error() throws Exception {
//    }

    @Test
    public void when_card_exists_but_card_user_is_invalid_we_expect_304_error() throws Exception {
        // given
        card.getOwner().setEnabled(false);
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = card.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.error(Result.STATUS_INVALID_USER, "Invalid user").asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    /* These two give regular responses */

    @Test
    public void when_card_exists_but_cant_play_game_we_expect_sad_response() throws Exception {
        // given
        card.play(false);
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = card.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.ok(new CardCheckResponseViewModel(card)).asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = card.getNumber();
        String origin = "borne";

        String requestJson = String.format("{\"card\": \"%s\", \"origin\": \"%s\"}", cardNumber, origin);

        // when
        Response response = resource.cardCheck(deviceName, deviceSecret, requestJson);

        // then
        Response expectedResponse = Result.ok(new CardCheckResponseViewModel(card)).asResponse();
        assertThat(response).isEqualToComparingFieldByField(expectedResponse);
    }
}
