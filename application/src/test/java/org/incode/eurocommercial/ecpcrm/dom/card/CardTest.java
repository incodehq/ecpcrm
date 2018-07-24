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
package org.incode.eurocommercial.ecpcrm.dom.card;

import org.junit.Before;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {
    Card card;

    @Before
    public void setUp() throws Exception {
        card = new Card();
    }

    public static class HidingActions extends CardTest {
        @Test
        public void when_card_status_is_enabled_it_cant_be_enabled() throws Exception {
            // when
            card.setStatus(CardStatus.ENABLED);

            // then
            assertThat(card.hideEnable()).isTrue();
        }

        @Test
        public void when_card_status_is_disabled_it_cant_be_disabled() throws Exception {
            // when
            card.setStatus(CardStatus.DISABLED);

            // then
            assertThat(card.hideUnenable()).isTrue();
        }
    }
}
