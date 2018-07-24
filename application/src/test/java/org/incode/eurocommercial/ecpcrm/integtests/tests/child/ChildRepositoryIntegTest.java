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

import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.child.ChildRepository;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.scenarios.test.IntegTestFixture;
import org.incode.eurocommercial.ecpcrm.integtests.tests.EcpCrmIntegTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ChildRepositoryIntegTest extends EcpCrmIntegTest {
    @Inject private FixtureScripts fixtureScripts;
    @Inject ChildRepository childRepository;

    IntegTestFixture fs;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new IntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);
    }

    public static class ListAll extends ChildRepositoryIntegTest {
        @Test
        public void all_children_should_be_listed() {
            // when
            List<Child> childList = childRepository.listAll();

            // then
            assertThat(new TreeSet<>(childList)).isEqualTo(new TreeSet<>(fs.getChildren()));
        }
    }


    public static class FindByParent extends ChildRepositoryIntegTest {
        @Test
        public void when_user_has_no_children_no_result_should_be_returned() {
            // given
            User user = fs.getUsers().stream()
                    .filter(u -> u.getChildren().isEmpty())
                    .findAny()
                    .get();

            // when
            List<Child> noChildren = childRepository.findByParent(user);

            // then
            assertThat(noChildren).isEmpty();
        }

        @Test
        public void when_user_has_children_they_should_be_returned() {
            // given
            User user = fs.getChildren().get(0).getParent();

            // when
            List<Child> children = childRepository.findByParent(user);

            // then
            assertThat(children).isNotEmpty();
            assertThat(new TreeSet<>(children)).isEqualTo(new TreeSet<>(user.getChildren()));
        }
    }

    public static class FindByParentAndName extends ChildRepositoryIntegTest {
        @Test
        public void when_user_has_no_children_no_result_should_be_returned() {
            // given
            User user = fs.getUsers().stream()
                    .filter(u -> u.getChildren().isEmpty())
                    .findAny()
                    .get();
            String childName = "Bob";

            // when
            Child foundChild = childRepository.findByParentAndName(user, childName);

            // then
            assertThat(foundChild).isNull();
        }

        @Test
        public void when_user_has_children_but_not_this_one_no_result_should_be_returned() {
            // given
            User user = fs.getChildren().get(0).getParent();
            String childName = "Not an actual child name";

            // when
            Child foundChild = childRepository.findByParentAndName(user, childName);

            // then
            assertThat(foundChild).isNull();
        }

        @Test
        public void when_user_has_specified_child_it_should_be_returned() {
            // given
            Child child = fs.getChildren().get(0);
            User user = child.getParent();

            // when
            Child foundChild = childRepository.findByParentAndName(user, child.getName());

            // then
            assertThat(foundChild).isEqualTo(child);
        }
    }

}
