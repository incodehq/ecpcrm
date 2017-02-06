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
import java.util.Random;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CenterIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;
    @Inject CenterRepository centerRepository;
    @Inject CardRepository cardRepository;

    DemoFixture fs;

    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(new Random().nextInt(fs.getCenters().size()));
        assertThat(center).isNotNull();
    }

    public static class NextValidCardNumber extends CenterIntegTest {
        @Test
        public void it_should_return_only_valid_numbers() {
            // given
            int testSize = 100;
            List<String> cardNumbers = new ArrayList<>();

            // when
            while(cardNumbers.size() < testSize) {
                cardNumbers.add(center.nextValidCardNumber());
            }

            // then
            cardNumbers.forEach(c ->
                    assertThat(cardRepository.cardNumberIsValid(c, center.getReference())).isTrue()
            );
        }

        @Test
        public void it_should_return_all_valid_numbers() {
            // given
            int testSize = 100;
            List<String> cardNumbers = new ArrayList<>();

            // when
            while(cardNumbers.size() < testSize) {
                cardNumbers.add(center.nextValidCardNumber());
            }

            // then
            for(int i = 1; i < cardNumbers.size(); i++) {
                BigInteger start = new BigInteger(cardNumbers.get(i - 1)).add(BigInteger.ONE);
                BigInteger end = new BigInteger(cardNumbers.get(i));
                for(BigInteger cardNumber = start; cardNumber.compareTo(end) == -1; cardNumber = cardNumber.add(BigInteger.ONE)) {
                    assertThat(cardRepository.cardNumberIsValid(cardNumber.toString(), center.getReference())).isFalse();
                }
            }
        }
    }

}
