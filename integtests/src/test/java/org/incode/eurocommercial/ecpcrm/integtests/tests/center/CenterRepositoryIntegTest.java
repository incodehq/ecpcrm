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

import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo.DemoFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class CenterRepositoryIntegTest extends EcpCrmIntegTest {
    @Inject FixtureScripts fixtureScripts;

    @Inject CenterRepository centerRepository;

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

    public static class ListAll extends CenterRepositoryIntegTest {
        @Test
        public void all_centers_should_be_listed() {
            // when
            List<Center> allCenters = centerRepository.listAll();

            // then
            assertThat(new TreeSet<>(allCenters)).isEqualTo(new TreeSet<>(fs.getCenters()));
        }
    }

    public static class FindByExactName extends CenterRepositoryIntegTest {
        @Test
        public void when_center_doesnt_exist_no_center_should_be_returned() {
            // given
            String centerName = "This is not the name of an actual Center";

            // when
            Center foundCenter = centerRepository.findByExactName(centerName);

            // then
            assertThat(foundCenter).isNull();
        }

        @Test
        public void when_center_exists_it_should_be_returned() {
            // when
            Center foundCenter = centerRepository.findByExactName(center.getName());

            // then
            assertThat(foundCenter).isEqualTo(center);
        }
    }

    public static class FindByNameContains extends CenterRepositoryIntegTest {
        @Test
        public void when_center_doesnt_exist_no_center_should_be_returned() {
            // given
            String centerName = "This is not the name of an actual Center";

            // when
            List<Center> foundCenters = centerRepository.findByNameContains(centerName);

            // then
            assertThat(foundCenters.size()).isZero();
        }

        @Test
        public void when_full_name_is_entered_it_should_be_the_only_result() {
            // when
            List<Center> foundCenters = centerRepository.findByNameContains(center.getName());

            // then
            assertThat(foundCenters.size()).isEqualTo(1);
            assertThat(foundCenters.get(0)).isEqualTo(center);
        }

        @Test
        public void when_partial_matching_name_is_entered_multiple_results_should_be_returned() {
            // when
            List<Center> foundCenters = centerRepository.findByNameContains("");

            // then
            assertThat(foundCenters.size()).isGreaterThan(1);
        }
    }

    public static class FindByReference extends CenterRepositoryIntegTest {
        @Test
        public void when_center_doesnt_exist_no_center_should_be_returned() {
            // given
            String centerReference = "This is not the name of an actual Center";

            // when
            Center foundCenter = centerRepository.findByReference(centerReference);

            // then
            assertThat(foundCenter).isNull();
        }

        @Test
        public void when_center_exists_it_should_be_returned() {
            // when
            Center foundCenter = centerRepository.findByReference(center.getReference());

            // then
            assertThat(foundCenter).isEqualTo(center);
        }
    }
}
