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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;

import static org.assertj.core.api.Assertions.assertThat;

public class CardRepositoryTest {
    Numerator numerator;
    CardRepository cardRepository;

    @Before
    public void setUp() throws Exception {
        cardRepository = new CardRepository();
        numerator = new Numerator();
        numerator.setLastIncrement(2017000000000L);
    }

    public static class NextCardNumber extends CardRepositoryTest {
        @Test
        public void it_should_return_exactly_expected_numbers() {
            // given
            int testSize = 10;
            List<String> cardNumbers = new ArrayList<>();

            // when
            while (cardNumbers.size() < testSize) {
                String cardNumber = cardRepository.nextCardNumber(numerator);
                cardNumbers.add(cardNumber);
                numerator.setLastIncrement(Long.parseLong(cardNumber));

            }
            assertThat(cardNumbers).containsExactly(
                    "2017000000013",
                    "2017000000020",
                    "2017000000037",
                    "2017000000044",
                    "2017000000051",
                    "2017000000068",
                    "2017000000075",
                    "2017000000082",
                    "2017000000099",
                    "2017000000105"
            );
        }
    }
}
