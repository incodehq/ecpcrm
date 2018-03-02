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
package org.incode.eurocommercial.ecpcrm.integtests.tests.child;

import java.util.SortedSet;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.xactn.TransactionService;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCareRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;
    @Inject TransactionService transactionService;
    @Inject ChildCareRepository childCareRepository;

    IntegTestFixture fs;
    Child child;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        child = fs.getChildren().get(0);
    }

    public static class CheckIn extends ChildIntegTest {
        @Test
        public void when_checked_in_should_be_hidden() {
            // given
            child.checkIn();

            // then
            assertThat(child.hideCheckIn()).isTrue();
        }

        @Test
        public void when_not_checked_in_should_be_visible() {
            // given
            child.checkOut();

            // then
            assertThat(child.hideCheckIn()).isFalse();
        }

        @Test
        public void when_already_checked_in_nothing_should_change() {
            // given
            child.checkIn();
            SortedSet<ChildCare> childCares = Sets.newTreeSet();
            childCares.addAll(child.getChildCares());
            ChildCare activeChildCare = childCareRepository.findActiveChildCareByChild(child);
            assertThat(activeChildCare.getCheckOut()).isNull();

            // when
            child.checkIn();
            transactionService.nextTransaction();

            // then
            assertThat(child.getChildCares()).isEqualTo(childCares);
            assertThat(childCareRepository.findActiveChildCareByChild(child)).isEqualTo(activeChildCare);
            assertThat(activeChildCare.getCheckOut()).isNull();
        }

        @Test
        public void when_not_checked_in_should_check_in() {
            // given
            child.checkOut();
            SortedSet<ChildCare> childCares = Sets.newTreeSet();
            childCares.addAll(child.getChildCares());
            ChildCare activeChildCare = childCareRepository.findActiveChildCareByChild(child);
            assertThat(activeChildCare).isNull();

            // when
            child.checkIn();

            // then
            assertThat(child.getChildCares()).isNotEqualTo(childCares);
            assertThat(child.getChildCares().size()).isEqualTo(childCares.size() + 1);
            assertThat(childCareRepository.findActiveChildCareByChild(child)).isNotNull();
        }
    }

    public static class CheckOut extends ChildIntegTest {
        @Test
        public void when_checked_in_should_be_visible() {
            // given
            child.checkIn();

            // then
            assertThat(child.hideCheckOut()).isFalse();
        }

        @Test
        public void when_not_checked_in_should_be_hidden() {
            // given
            child.checkOut();

            // then
            assertThat(child.hideCheckOut()).isTrue();
        }

        @Test
        public void when_not_checked_in_nothing_should_change() {
            // given
            child.checkOut();
            SortedSet<ChildCare> childCares = Sets.newTreeSet();
            childCares.addAll(child.getChildCares());
            ChildCare activeChildCare = childCareRepository.findActiveChildCareByChild(child);
            assertThat(activeChildCare).isNull();

            // when
            child.checkOut();

            // then
            assertThat(child.getChildCares().size()).isEqualTo(childCares.size());
            assertThat(childCareRepository.findActiveChildCareByChild(child)).isNull();
        }

        @Test
        public void when_checked_in_should_check_out() {
            // given
            child.checkIn();
            SortedSet<ChildCare> childCares = Sets.newTreeSet();
            childCares.addAll(child.getChildCares());
            ChildCare activeChildCare = childCareRepository.findActiveChildCareByChild(child);
            assertThat(activeChildCare).isNotNull();

            // when
            child.checkOut();

            // then
            assertThat(child.getChildCares().size()).isEqualTo(childCares.size());
            assertThat(childCareRepository.findActiveChildCareByChild(child)).isNull();
            assertThat(activeChildCare.getCheckOut()).isNotNull();
        }
    }
}