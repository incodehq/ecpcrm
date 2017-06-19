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

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CenterTest {
    Center center;

    @Before
    public void setUp() throws Exception {
        center = new Center();
        center.setReference("043");
        center.setNumerator(new Numerator());
        center.getNumerator().setLastIncrement(2043000000005L);
    }

    public static class NextValidCardNumber extends CenterTest {
        @Test
        public void isFaster() {
            //given
            //when
            long startTime = System.nanoTime();
            center.nextValidCardNumber();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            long startTime2 = System.nanoTime();
            center.nextValidCardNumberOld();
            long endTime2 = System.nanoTime();
            long duration2 = (endTime2 - startTime2);

            //then
            assertThat(duration).isLessThan(duration2 / 1000); // 1000x speedup?
            System.out.println("duration for new implementation: " + duration);
            System.out.println("duration for old implementation: " + duration2);
        }
    }
}
