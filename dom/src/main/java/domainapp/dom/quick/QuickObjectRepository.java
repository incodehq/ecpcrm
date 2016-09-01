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
package domainapp.dom.quick;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = QuickObject.class
)
public class QuickObjectRepository {

    //region > listAll (programmatic)

    @Programmatic
    public List<QuickObject> listAll() {
        return container.allInstances(QuickObject.class);
    }
    //endregion

    //region > findByName (programmatic)

    @Programmatic
    public QuickObject findByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.uniqueMatch(
                new QueryDefault<>(
                        QuickObject.class,
                        "findByName",
                        "name", name));
    }
    //endregion

    //region > findByNameContains (programmatic)

    @Programmatic
    public List<QuickObject> findByNameContains(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        QuickObject.class,
                        "findByNameContains",
                        "name", name));
    }
    //endregion

    //region > create (programmatic)

    @Programmatic
    public QuickObject create(final String name) {
        final QuickObject obj = container.newTransientInstance(QuickObject.class);
        obj.setName(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > findOrCreate (programmatic)

    @Programmatic
    public QuickObject findOrCreate(
            @ParameterLayout(named="Name")
            final String name
    ) {
        QuickObject quickObject = findByName(name);
        if(quickObject == null) {
            quickObject = create(name);
        }
        return quickObject;
    }
    //endregion


    //region > injected services

    @javax.inject.Inject 
    DomainObjectContainer container;

    //endregion
}
