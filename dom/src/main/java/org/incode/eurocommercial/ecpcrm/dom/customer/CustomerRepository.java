/**
 *  Copyright 2015-2016 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.incode.eurocommercial.ecpcrm.dom.customer;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Customer.class
)
public class CustomerRepository {

    @Programmatic
    public List<Customer> listAll() {
        return container.allInstances(Customer.class);
    }

    @Programmatic
    public Customer findByExactName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.uniqueMatch(
                new QueryDefault<>(
                        Customer.class,
                        "findByExactName",
                        "name", name));
    }

    @Programmatic
    public List<Customer> findByNameContains(
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Customer.class,
                        "findByNameContains",
                        "name", name));
    }


    @Programmatic
    public Customer create(final String name) {
        final Customer customer = container.newTransientInstance(Customer.class);
        customer.setName(name);
        container.persistIfNotAlready(customer);
        return customer;
    }

    @Programmatic
    public Customer findOrCreate(
            final String name
    ) {
        Customer customer = findByExactName(name);
        if(customer == null) {
            customer = create(name);
        }
        return customer;
    }

    @Programmatic
    public void delete(final Customer customer) {
        container.removeIfNotAlready(customer);
    }

    @javax.inject.Inject 
    DomainObjectContainer container;

}
