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
package org.incode.eurocommercial.ecpcrm.webapp;

import java.util.Random;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class CardGameTest extends EcpCrmTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardGameRepository cardGameRepository;


    private DemoFixture fs;
    private Card card;
    private Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
    }

    private String endpoint = "card-game";

    private String sendRequest(String cardNumber, String win, String desc) throws Exception {
        String request = "{\"card\": \"" + cardNumber + "\", \"win\": \"" + win + "\", \"desc\": \"" + desc + "\"}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        // given
        String cardNumber = "";
        String win = "";
        String desc = "";

        // when
        String response = sendRequest(cardNumber, win, desc);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(302);
    }

    @Test
    public void when_card_does_not_exist_we_expect_303_error() throws Exception {
        // given
        String cardNumber = center.nextValidCardNumber();
        String win = "";
        String desc = "";

        // when
        String response = sendRequest(cardNumber, win, desc);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(303);
    }

    @Test
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        // given
        card.setStatus(CardStatus.DISABLED);
        String cardNumber = card.getNumber();
        String win = "";
        String desc = "";

        // when
        String response = sendRequest(cardNumber, win, desc);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Awaiting response from biggerband
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_303_error() throws Exception {
    }

    @Test
    public void when_card_exists_but_has_no_user_we_expect_303_error() throws Exception {
        // given
        while(card.getOwner() != null) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        String cardNumber = card.getNumber();
        String win = "";
        String desc = "";

        // when
        String response = sendRequest(cardNumber, win, desc);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Awaiting response from biggerband
    public void when_card_exists_but_card_user_is_invalid_we_expect_304_error() throws Exception {

    }

    @Test
    public void when_card_exists_but_has_already_played_we_expect_315_error() throws Exception {
        // given
        card.play();
        String cardNumber = card.getNumber();
        String win = "";
        String desc = "";

        // when
        String response = sendRequest(cardNumber, win, desc);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(315);
    }

    @Test
    public void when_card_exists_and_can_play_game_we_expect_happy_response() throws Exception {
        while(!card.canPlay()) {
            card = fs.getCards().get(new Random().nextInt(fs.getCards().size()));
        }
        String cardNumber = card.getNumber();
        String win = "";
        String desc = "";

        // when
        String response = sendRequest(cardNumber, win, desc);

        // then
        assertThatJson(response)
                .node("status").isEqualTo(200);
    }

}