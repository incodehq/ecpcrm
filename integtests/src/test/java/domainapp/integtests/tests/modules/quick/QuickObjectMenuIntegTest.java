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

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Throwables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import domainapp.dom.quick.QuickObject;
import domainapp.dom.quick.QuickObjectMenu;
import domainapp.fixture.dom.quick.QuickObjectsTearDown;
import domainapp.fixture.scenarios.demo.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import static org.assertj.core.api.Assertions.assertThat;

public class QuickObjectMenuIntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    QuickObjectMenu quickObjectMenu;

    public static class ListAll extends QuickObjectMenuIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            DemoFixture fs = new DemoFixture();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            final List<QuickObject> all = wrap(quickObjectMenu).listAll();

            // then
            assertThat(all).hasSize(fs.getQuickObjects().size());

            QuickObject quickObject = wrap(all.get(0));
            assertThat(quickObject.getName()).isEqualTo(fs.getQuickObjects().get(0).getName());
        }

        @Test
        public void whenNone() throws Exception {

            // given
            FixtureScript fs = new QuickObjectsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            final List<QuickObject> all = wrap(quickObjectMenu).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }

    public static class Create extends QuickObjectMenuIntegTest {

        @Test
        public void happyCase() throws Exception {

            // given
            FixtureScript fs = new QuickObjectsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();

            // when
            wrap(quickObjectMenu).create("Faz");

            // then
            final List<QuickObject> all = wrap(quickObjectMenu).listAll();
            assertThat(all).hasSize(1);
        }

        @Test
        public void whenAlreadyExists() throws Exception {

            // given
            FixtureScript fs = new QuickObjectsTearDown();
            fixtureScripts.runFixtureScript(fs, null);
            nextTransaction();
            wrap(quickObjectMenu).create("Faz");
            nextTransaction();

            // then
            expectedExceptions.expectCause(causalChainContains(SQLIntegrityConstraintViolationException.class));

            // when
            wrap(quickObjectMenu).create("Faz");
            nextTransaction();
        }

        private static Matcher<? extends Throwable> causalChainContains(final Class<?> cls) {
            return new TypeSafeMatcher<Throwable>() {
                @Override
                protected boolean matchesSafely(Throwable item) {
                    final List<Throwable> causalChain = Throwables.getCausalChain(item);
                    for (Throwable throwable : causalChain) {
                        if(cls.isAssignableFrom(throwable.getClass())){
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("exception with causal chain containing " + cls.getSimpleName());
                }
            };
        }
    }

}