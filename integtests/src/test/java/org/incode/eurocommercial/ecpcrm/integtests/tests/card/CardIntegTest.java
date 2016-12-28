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
package org.incode.eurocommercial.ecpcrm.integtests.tests.card;

import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGame;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject CardGameRepository cardGameRepository;


    DemoFixture fs;
    Card card;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(ThreadLocalRandom.current().nextInt(0, fs.getCards().size()));
        assertThat(card).isNotNull();
    }

    public static class Play extends CardIntegTest {
        @Test
        public void when_card_has_not_played_it_can_play() throws Exception {
            // when
            assertThat(cardGameRepository.findByCardAndDate(card, clockService.now())).isNull();

            // then
            assertThat(card.canPlay()).isTrue();
        }

        @Test
        public void when_card_has_played_it_cant_play() throws Exception {
            // when
            card.play();

            // then
            assertThat(card.canPlay()).isFalse();
        }

        @Test
        public void when_card_can_play_and_plays_a_card_game_is_created() throws Exception {
            // given
            assertThat(card.canPlay()).isTrue();

            // when
            CardGame cardGame = card.play();

            // then
            assertThat(card.canPlay()).isFalse();
            assertThat(cardGame).isNotNull();
            assertThat(card.getCardGames().last()).isEqualTo(cardGame);
            assertThat(cardGameRepository.findByCardAndDate(card, clockService.now())).isEqualTo(cardGame);
        }

        @Test
        public void when_card_cant_play_and_plays_no_card_game_is_created() throws Exception {
            // given
            CardGame cardGame = card.play();
            int sizeOnCard = card.getCardGames().size();
            int sizeOnRepository = cardGameRepository.listAll().size();

            assertThat(card.canPlay()).isFalse();

            // when
            cardGame = card.play();

            // then
            assertThat(cardGame).isNull();
            assertThat(sizeOnCard).isEqualTo(card.getCardGames().size());
            assertThat(sizeOnRepository).isEqualTo(cardGameRepository.listAll().size());
        }
    }
}
