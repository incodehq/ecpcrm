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
package org.incode.eurocommercial.ecpcrm.integtests.tests.childcare;

import java.util.Random;

import javax.inject.Inject;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCareRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildCareIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject ClockService clockService;

    @Inject ChildCareRepository childCareRepository;

    @Inject ChildRepository childRepository;

    DemoFixture fs;
    Child child;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        child = fs.getChildren().get(new Random().nextInt(fs.getChildren().size()));
    }

    public static class DoCheckOut extends ChildCareIntegTest {
        @Test
        public void if_child_care_is_not_checked_out_it_is_checked_out() {
            // given
            ChildCare childCare = childCareRepository.newChildCare(child);

            // when
            childCare.doCheckOut();

            //then
            assertThat(childCare.getCheckOut()).isGreaterThanOrEqualTo(clockService.nowAsLocalDateTime().minusSeconds(10));
            assertThat(childCare.getCheckOut()).isLessThanOrEqualTo(clockService.nowAsLocalDateTime());
        }

        @Test
        public void if_child_care_is_already_checked_out_it_is_not_checked_out_again() {
            // given
            ChildCare childCare = childCareRepository.newChildCare(child);
            childCare.setCheckIn(clockService.nowAsLocalDateTime().minusDays(1));
            LocalDateTime checkOut = clockService.nowAsLocalDateTime().minusDays(1).plusMinutes(30);
            childCare.setCheckOut(checkOut);

            // when
            childCare.doCheckOut();

            // then
            assertThat(childCare.getCheckOut()).isEqualTo(checkOut);
        }
    }
}
