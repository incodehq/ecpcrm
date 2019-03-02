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
package org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.card.game;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.game.CardGame;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.game.CardGameRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.LoyaltyCardModuleIntegTestAbstract;

import static org.assertj.core.api.Assertions.assertThat;

public class CardGameRepositoryIntegTest extends LoyaltyCardModuleIntegTestAbstract {
    @Inject private FixtureScripts fixtureScripts;
    @Inject CardGameRepository cardGameRepository;

    LoyaltyCardsIntegTestFixture fs;
    Center center;
    Card card;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new LoyaltyCardsIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        card = fs.getCards().get(0);
        center = card.getCenter();

        assertThat(card).isNotNull();
        assertThat(center).isNotNull();
    }

    public static class FindByDateRange extends CardGameRepositoryIntegTest {
        public final LocalDate DATE_1 = new LocalDate(2019, 1, 1);
        public final LocalDate DATE_2 = new LocalDate(2019, 1, 20);
        public final LocalDate DATE_3 = new LocalDate(2019, 1, 25);
        public final LocalDate DATE_4 = new LocalDate(2018, 6, 6);
        public final LocalDate DATE_5 = new LocalDate(2015, 3, 20);

        public CardGame game1;
        public CardGame game2;
        public CardGame game3;
        public CardGame game4;
        public CardGame game5;

        @Before
        public void setUpCardGames() {
            // given
            game1 = cardGameRepository.newCardGame(card, DATE_1, false);
            game2 = cardGameRepository.newCardGame(card, DATE_2, false);
            game3 = cardGameRepository.newCardGame(card, DATE_3, false);
            game4 = cardGameRepository.newCardGame(card, DATE_4, false);
            game5 = cardGameRepository.newCardGame(card, DATE_5, false);
        }

        @Test
        public void should_find_card_games_for_periods() {
            // when
            List<CardGame> dailyCardGames = cardGameRepository.findByDateRange(DATE_1, DATE_1);
            List<CardGame> weeklyCardGames = cardGameRepository.findByDateRange(DATE_2, DATE_2.plusWeeks(1));
            List<CardGame> monthlyCardGames = cardGameRepository.findByDateRange(DATE_1, DATE_1.plusMonths(1));
            List<CardGame> yearlyCardGames = cardGameRepository.findByDateRange(DATE_1.minusYears(1), DATE_1);

            // then
            assertThat(dailyCardGames).containsExactly(game1);
            assertThat(weeklyCardGames).containsExactly(game2, game3);
            assertThat(monthlyCardGames).containsExactly(game1, game2, game3);
            assertThat(yearlyCardGames).containsExactly(game1, game4);
        }
    }
}
