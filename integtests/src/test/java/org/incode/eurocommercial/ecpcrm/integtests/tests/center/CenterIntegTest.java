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
package org.incode.eurocommercial.ecpcrm.integtests.tests.center;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CenterIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject CardRepository cardRepository;

    IntegTestFixture fs;
    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(0);
        assertThat(center).isNotNull();
    }

    public static class NextValidCardNumber extends CenterIntegTest {
        @Test
        public void it_should_return_only_valid_numbers() {
            // given
            int testSize = 20;
            List<String> cardNumbers = new ArrayList<>();

            // when
            while (cardNumbers.size() < testSize) {
                String cardNumber = center.nextValidCardNumber();
                cardNumbers.add(cardNumber);
                cardRepository.newCard(cardNumber, CardStatus.ENABLED, center);
            }

            // then
            for (String number : cardNumbers) {
                assertThat(cardRepository.cardNumberIsValid(number, center.getCode())).isTrue();
            }
        }

        @Test
        public void it_should_return_all_valid_numbers() {
            // given
            int testSize = 20;
            List<BigInteger> cardNumbers = new ArrayList<>();

            // when
            while (cardNumbers.size() < testSize) {
                String cardNumber = center.nextValidCardNumber();
                cardNumbers.add(new BigInteger(cardNumber));
                cardRepository.newCard(cardNumber, CardStatus.ENABLED, center);
            }

            // then
            BigInteger start = cardNumbers.get(0);
            BigInteger end = cardNumbers.get(cardNumbers.size() - 1);
            BigInteger step = BigInteger.ONE;

            for (BigInteger i = start; i.compareTo(end) < 0; i = i.add(step)) {
                if (!cardNumbers.contains(i)) {
                    assertThat(cardRepository.cardNumberIsValid(i.toString(), center.getCode())).isFalse();
                }
            }
        }
    }

    public static class NextFakeCardNumber extends CenterIntegTest {
        @Test
        public void it_should_return_only_valid_numbers() {
            // given
            int testSize = 20;
            List<String> cardNumbers = new ArrayList<>();

            // when
            // when
            while (cardNumbers.size() < testSize) {
                String cardNumber = center.nextFakeCardNumber();
                cardNumbers.add(cardNumber);
                cardRepository.newFakeCard(CardStatus.ENABLED, center);
            }

            // then
            for (String number : cardNumbers) {
                assertThat(cardRepository.cardNumberIsValid(number, center.getCode())).isTrue();
            }
        }

        @Test
        public void it_should_return_all_valid_numbers() {
            // given
            int testSize = 20;
            List<BigInteger> cardNumbers = new ArrayList<>();

            // when
            while (cardNumbers.size() < testSize) {
                String cardNumber = center.nextFakeCardNumber();
                cardNumbers.add(new BigInteger(cardNumber));
                cardRepository.newFakeCard(CardStatus.ENABLED, center);
            }

            // then
            BigInteger start = cardNumbers.get(0);
            BigInteger end = cardNumbers.get(cardNumbers.size() - 1);
            BigInteger step = BigInteger.ONE;

            for (BigInteger i = start; i.compareTo(end) < 0; i = i.add(step)) {
                if (!cardNumbers.contains(i)) {
                    assertThat(cardRepository.cardNumberIsValid(i.toString(), center.getCode())).isFalse();
                }
            }
        }

        @Test
        public void it_should_return_only_fake_numbers() {
            // given
            int testSize = 20;
            List<String> cardNumbers = new ArrayList<>();

            // when
            while (cardNumbers.size() < testSize) {
                String cardNumber = center.nextFakeCardNumber();
                cardNumbers.add(cardNumber);
                cardRepository.newFakeCard(CardStatus.ENABLED, center);
            }

            // then
            for (String number : cardNumbers) {
                assertThat(number.indexOf("987")).isEqualTo(4);
            }
        }
    }

}
