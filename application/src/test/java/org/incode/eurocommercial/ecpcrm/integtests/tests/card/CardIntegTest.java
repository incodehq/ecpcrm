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

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGame;
import org.incode.eurocommercial.ecpcrm.dom.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CardIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject ClockService clockService;
    @Inject CardGameRepository cardGameRepository;

    IntegTestFixture fs;
    Card card;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(0);
        assertThat(card).isNotNull();
    }

    public static class Play extends CardIntegTest {
        @Test
        public void when_card_has_not_played_and_is_enabled_it_can_play() throws Exception {
            // given
            card = fs.getCards().stream()
                    .filter(c -> cardGameRepository.findByCardAndDate(c, clockService.now()) == null)
                    .findAny()
                    .get();
            card.setStatus(CardStatus.ENABLED);

            // when
            boolean canPlay = card.canPlay();

            // then
            assertThat(canPlay).isTrue();
        }

        @Test
        public void when_card_has_played_it_can_not_play() throws Exception {
            // given
            card.setStatus(CardStatus.ENABLED);
            card.play();

            // when
            boolean canPlay = card.canPlay();

            // then
            assertThat(canPlay).isFalse();
        }

        @Test
        public void when_card_is_disabled_or_lost_it_can_not_play() throws Exception {
            // given
            card = fs.getCards().stream()
                    .filter(c -> cardGameRepository.findByCardAndDate(c, clockService.now()) == null)
                    .findAny()
                    .get();

            // when
            card.setStatus(CardStatus.DISABLED);
            boolean canPlayWhenDisabled = card.canPlay();

            card.setStatus(CardStatus.LOST);
            boolean canPlayWhenLost = card.canPlay();

            // then
            assertThat(canPlayWhenDisabled).isFalse();
            assertThat(canPlayWhenLost).isFalse();
        }

        @Test
        public void when_card_can_play_and_plays_a_card_game_is_created() throws Exception {
            // given
            card = fs.getCards().stream()
                    .filter(Card::canPlay)
                    .findAny()
                    .get();

            // when
            CardGame cardGame = card.play();

            // then
            assertThat(card.canPlay()).isFalse();
            assertThat(cardGame).isNotNull();
            assertThat(card.getCardGames().last()).isEqualTo(cardGame);
            assertThat(cardGameRepository.findByCardAndDate(card, clockService.now())).isEqualTo(cardGame);
        }

        @Test
        public void when_card_can_not_play_and_plays_no_card_game_is_created() throws Exception {
            // given
            card.play();
            int sizeOnCard = card.getCardGames().size();
            int sizeOnRepository = cardGameRepository.listAll().size();

            assertThat(card.canPlay()).isFalse();

            // when
            CardGame cardGame = card.play();

            // then
            assertThat(cardGame).isNull();
            assertThat(sizeOnCard).isEqualTo(card.getCardGames().size());
            assertThat(sizeOnRepository).isEqualTo(cardGameRepository.listAll().size());
        }
    }
}
