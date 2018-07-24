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
package org.incode.eurocommercial.ecpcrm.dom.child;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.Gender;
import org.incode.eurocommercial.ecpcrm.dom.user.User;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Child.class
)
public class ChildRepository {

    @Programmatic
    public List<Child> listAll() {
        return repositoryService.allInstances(Child.class);
    }

    @Programmatic
    public List<Child> findByParent(
            final User parent
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Child.class,
                        "findByParent",
                        "parent", parent));
    }

    @Programmatic
    public Child findByParentAndName(
            final User parent,
            final String name
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Child.class,
                        "findByParentAndName",
                        "parent", parent,
                        "name", name));
    }

    @Programmatic
    private Child newChild(
            final String name,
            final Gender gender,
            final LocalDate birthdate,
            final User parent
    ) {
        Child child = repositoryService.instantiate(Child.class);

        child.setName(name);
        child.setGender(gender);
        child.setBirthdate(birthdate);
        child.setParent(parent);
        child.setNotes("");

        parent.getChildren().add(child);
        repositoryService.persist(child);
        return child;
    }

    @Programmatic
    public Child findOrCreate(
            final String name,
            final Gender gender,
            final LocalDate birthdate,
            final User parent
    ) {
        Child child = findByParentAndName(parent, name);
        child = child != null ? child : newChild(name, gender, birthdate, parent);
        return child;
    }

    @Inject RepositoryService repositoryService;
}
