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
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.app.services.api.ApiService;
import org.incode.eurocommercial.ecpcrm.app.services.api.Result;
import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGame;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardGameIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardRepository cardRepository;
    @Inject CardGameRepository cardGameRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;

    @Inject ApiService apiService;

    private IntegTestFixture fs;
    private Card card;
    private Center center;
    private AuthenticationDevice device;
    private List<AuthenticationDevice> deviceList;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));

        center = card.getCenter();

        deviceList = authenticationDeviceRepository.findByCenter(center);
        device = deviceList.get(new Random().nextInt(deviceList.size()));

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
        assertThat(device).isNotNull();
    }

    @Test
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret() + "NOT A REAL SECRET";
        String cardNumber = card.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(301);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = "";
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret,cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(302);
    }

    @Test
    public void when_card_does_not_exist_we_expect_303_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = center.nextValidCardNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(303);
    }

    @Test
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = card.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(303);
    }

    @Test
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_303_error() throws Exception {
        // given
        fs.getCenters().remove(center);
        Center otherCenter = fs.getCenters().get(0);
        Card otherCard = cardRepository.findByCenter(otherCenter).get(0);

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = otherCard.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(303);
    }

    @Test
    public void when_card_exists_but_has_no_user_we_expect_303_error() throws Exception {
        // given
        Card cardWithoutOwner = cardRepository.listUnassignedCards().get(0);
        Center center = cardWithoutOwner.getCenter();
        AuthenticationDevice device = authenticationDeviceRepository.findByCenter(center).get(0);

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = cardWithoutOwner.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(303);
    }

    @Test
    public void when_card_exists_but_card_user_is_disabled_we_expect_304_error() throws Exception {
        Card cardWithOwner = cardRepository.listAll().stream()
                .filter(card -> card.getOwner() != null)
                .collect(Collectors.toList())
                .get(0);
        cardWithOwner.getOwner().setEnabled(false);
        Center center = cardWithOwner.getCenter();
        AuthenticationDevice device = authenticationDeviceRepository.findByCenter(center).get(0);

        // given
        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = cardWithOwner.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(304);
    }

    @Test
    public void when_card_exists_but_has_already_played_we_expect_315_error() throws Exception {
        // given
        Card cardWithOwner = cardRepository.listAll().stream()
                .filter(card -> card.getOwner() != null)
                .collect(Collectors.toList())
                .get(0);
        cardWithOwner.play();
        Center center = cardWithOwner.getCenter();
        AuthenticationDevice device = authenticationDeviceRepository.findByCenter(center).get(0);

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = cardWithOwner.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);

        // then
        assertThat(result.getStatus()).isEqualTo(315);
    }

    @Test
    public void when_card_exists_and_can_play_game_we_expect_happy_response_win() throws Exception {
        Card cardWithOwnerWhichCanPlay = cardRepository.listAll().stream()
                .filter(card -> card.getOwner() != null && card.canPlay())
                .collect(Collectors.toList())
                .get(0);
        Center center = cardWithOwnerWhichCanPlay.getCenter();
        AuthenticationDevice device = authenticationDeviceRepository.findByCenter(center).get(0);

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = cardWithOwnerWhichCanPlay.getNumber();
        boolean win = true;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);
        CardGame createdCardGame = cardGameRepository.findByCardAndDate(cardWithOwnerWhichCanPlay, clockService.now());

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(createdCardGame).isNotNull();
        assertThat(createdCardGame.isOutcome()).isTrue();
        assertThat(cardWithOwnerWhichCanPlay.canPlay()).isFalse();
    }

    @Test
    public void when_card_exists_and_can_play_game_we_expect_happy_response_lose() throws Exception {
        Card cardWithOwnerWhichCanPlay = cardRepository.listAll().stream()
                .filter(card -> card.getOwner() != null && card.canPlay())
                .collect(Collectors.toList())
                .get(0);
        Center center = cardWithOwnerWhichCanPlay.getCenter();
        AuthenticationDevice device = authenticationDeviceRepository.findByCenter(center).get(0);

        String deviceName = device.getName();
        String deviceSecret = device.getSecret();
        String cardNumber = cardWithOwnerWhichCanPlay.getNumber();
        boolean win = false;
        String desc = "";

        // when
        Result result = apiService.cardGame(deviceName, deviceSecret, cardNumber, win, desc);
        CardGame createdCardGame = cardGameRepository.findByCardAndDate(cardWithOwnerWhichCanPlay, clockService.now());

        // then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(createdCardGame).isNotNull();
        assertThat(createdCardGame.isOutcome()).isFalse();
        assertThat(cardWithOwnerWhichCanPlay.canPlay()).isFalse();
    }
}
