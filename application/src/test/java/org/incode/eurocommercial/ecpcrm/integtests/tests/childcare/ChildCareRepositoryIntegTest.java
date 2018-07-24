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

import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCareRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildCareRepositoryIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject ClockService clockService;
    @Inject ChildCareRepository childCareRepository;
    @Inject ChildRepository childRepository;

    IntegTestFixture fs;
    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        center = fs.getCenters().get(0);
    }

    public static class ListAll extends ChildCareRepositoryIntegTest {
        @Test
        public void all_children_should_be_listed() {
            // when
            List<ChildCare> childCares = childCareRepository.listAll();

            // then
            assertThat(new TreeSet<>(childCares)).isEqualTo(new TreeSet<>(fs.getChildCares()));
        }
    }

    public static class FindByChild extends ChildCareRepositoryIntegTest {
        @Test
        public void when_child_has_no_child_cares_no_result_should_be_returned() {
            // given
            User user = fs.getUsers().get(0);
            Child child = childRepository.findOrCreate("Test Child", Gender.MALE, clockService.now().minusYears(8), user);

            // when
            List<ChildCare> childCares = childCareRepository.findByChild(child);

            // then
            assertThat(childCares).isEmpty();
        }

        @Test
        public void when_child_has_child_cares_they_should_be_returned() {
            // given
            Child child = fs.getChildren().get(0);
            childCareRepository.newChildCare(child);

            // when
            List<ChildCare> childCares = childCareRepository.findByChild(child);

            // then
            assertThat(childCares).isNotEmpty();
        }
    }

    public static class FindByDateRange extends ChildCareRepositoryIntegTest {
        @Test
        public void when_no_child_cares_in_range_no_results_should_be_returned() {
            // given
            LocalDateTime start = clockService.nowAsLocalDateTime().plusYears(50);
            LocalDateTime end = start.plusYears(1);

            // when
            List<ChildCare> childCares = childCareRepository.findByDateRange(center, start, end);

            // then
            assertThat(childCares).isEmpty();
        }

        @Test
        public void when_child_cares_are_in_range_they_should_be_returned() {
            // given
            LocalDateTime start = clockService.nowAsLocalDateTime().minusYears(50);
            LocalDateTime end = clockService.nowAsLocalDateTime().plusYears(50);

            // when
            List<ChildCare> childCares = childCareRepository.findByDateRange(center, start, end);

            // then
            assertThat(childCares).isNotEmpty();
        }
    }

    public static class FindByChildAndDateRange extends ChildCareRepositoryIntegTest {
        @Test
        public void when_no_child_cares_in_range_no_results_should_be_returned() {
            // given
            Child child = fs.getChildren().get(0);
            LocalDateTime start = clockService.nowAsLocalDateTime().plusYears(50);
            LocalDateTime end = start.plusYears(1);

            // when
            List<ChildCare> childCares = childCareRepository.findByChildAndDateRange(child, start, end);

            // then
            assertThat(childCares).isEmpty();
        }

        @Test
        public void when_child_cares_are_in_range_they_should_be_returned() {
            // given
            Child child = fs.getChildren().get(0);
            childCareRepository.newChildCare(child);

            LocalDateTime start = clockService.nowAsLocalDateTime().minusYears(50);
            LocalDateTime end = clockService.nowAsLocalDateTime().plusYears(50);

            // when
            List<ChildCare> childCares = childCareRepository.findByChildAndDateRange(child, start, end);

            // then
            assertThat(childCares).isNotEmpty();
        }
    }

    public static class FindActiveChildCareByChild extends ChildCareRepositoryIntegTest {
        @Test
        public void when_child_has_no_child_cares_no_result_is_returned() {
            // given
            User user = fs.getUsers().get(0);
            Child child = childRepository.findOrCreate("Test Child", Gender.MALE, clockService.now().minusYears(8), user);

            // when
            ChildCare childCare = childCareRepository.findActiveChildCareByChild(child);

            // then
            assertThat(childCare).isNull();
        }

        @Test
        public void when_child_has_no_active_child_care_no_result_is_returned() {
            // given
            User user = fs.getUsers().get(0);
            Child child = childRepository.findOrCreate("Test Child", Gender.MALE, clockService.now().minusYears(8), user);
            ChildCare childCare = childCareRepository.newChildCare(child);
            childCare.doCheckOut();

            // when
            ChildCare activeChildCare = childCareRepository.findActiveChildCareByChild(child);

            // then
            assertThat(activeChildCare).isNull();
        }

        @Test
        public void when_child_has_an_active_child_care_it_is_returned() {
            // given
            User user = fs.getUsers().get(0);
            Child child = childRepository.findOrCreate("Test Child", Gender.MALE, clockService.now().minusYears(8), user);
            childCareRepository.newChildCare(child);

            // when
            ChildCare childCare = childCareRepository.findActiveChildCareByChild(child);

            // then
            assertThat(childCare).isNotNull();
        }
    }

    public static class FindActiveChildCares extends ChildCareRepositoryIntegTest {
        @Test
        public void when_no_child_cares_are_active_no_result_is_returned() {
            // given
            for (ChildCare childCare : fs.getChildCares()) {
                if (childCare.getCheckOut() == null)
                    childCare.doCheckOut();
            }

            // when
            List<ChildCare> childCares = childCareRepository.findActiveChildCares(center);

            // then
            assertThat(childCares).isEmpty();
        }

        @Test
        public void when_child_cares_are_active_they_are_returned() {
            // given
            List<ChildCare> activeChildCares = Lists.newArrayList();

            // when
            for (Center center : fs.getCenters()) {
                activeChildCares.addAll(childCareRepository.findActiveChildCares(center));
            }

            // then
            assertThat(activeChildCares).isNotEmpty();
        }
    }
}
