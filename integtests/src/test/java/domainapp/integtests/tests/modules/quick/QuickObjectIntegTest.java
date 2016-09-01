/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.integtests.tests.modules.quick;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.core.metamodel.services.jdosupport.Persistable_datanucleusIdLong;
import org.apache.isis.core.metamodel.services.jdosupport.Persistable_datanucleusVersionTimestamp;

import domainapp.dom.quick.QuickObject;
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import static org.assertj.core.api.Assertions.assertThat;

public class QuickObjectIntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;

    DemoFixture fs;
    QuickObject quickObjectPojo;
    QuickObject quickObjectWrapped;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);

        quickObjectPojo = fs.getQuickObjects().get(0);

        assertThat(quickObjectPojo).isNotNull();
        quickObjectWrapped = wrap(quickObjectPojo);
    }

    public static class Name extends QuickObjectIntegTest {

        @Test
        public void accessible() throws Exception {
            // when
            final String name = quickObjectWrapped.getName();
            // then
            assertThat(name).isNotNull();
        }

        @Test
        public void cannotBeUpdatedDirectly() throws Exception {

            // expect
            expectedExceptions.expect(DisabledException.class);

            // when
            quickObjectWrapped.setName("new name");
        }
    }

    public static class UpdateName extends QuickObjectIntegTest {

        @Test
        public void happyCase() throws Exception {

            // when
            quickObjectWrapped.updateName("new name");

            // then
            assertThat(quickObjectWrapped.getName()).isEqualTo("new name");
        }

        @Test
        public void failsValidation() throws Exception {

            // expect
            expectedExceptions.expect(InvalidException.class);
            expectedExceptions.expectMessage("Exclamation mark is not allowed");

            // when
            quickObjectWrapped.updateName("new name!");
        }
    }


    public static class Title extends QuickObjectIntegTest {

        @Inject
        DomainObjectContainer container;

        @Test
        public void interpolatesName() throws Exception {

            // given
            final String name = quickObjectWrapped.getName();

            // when
            final String title = container.titleOf(quickObjectWrapped);

            // then
            assertThat(title).isEqualTo("Object: " + name);
        }
    }

    public static class DataNucleusId extends QuickObjectIntegTest {

        @Test
        public void shouldBePopulated() throws Exception {
            // when
            final Long id = mixin(Persistable_datanucleusIdLong.class, quickObjectPojo).$$();
            // then
            assertThat(id).isGreaterThanOrEqualTo(0);
        }
    }

    public static class DataNucleusVersionTimestamp extends QuickObjectIntegTest {

        @Test
        public void shouldBePopulated() throws Exception {
            // when
            final Timestamp timestamp = mixin(Persistable_datanucleusVersionTimestamp.class, quickObjectPojo).$$();
            // then
            assertThat(timestamp).isNotNull();
        }
    }


}