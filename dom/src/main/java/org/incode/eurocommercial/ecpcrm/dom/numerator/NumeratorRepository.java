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
package org.incode.eurocommercial.ecpcrm.dom.numerator;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Numerator.class
)
public class NumeratorRepository {

    @Programmatic
    public List<Numerator> listAll() {
        return repositoryService.allInstances(Numerator.class);
    }

    @Programmatic
    public Numerator findByName(
            final String name
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Numerator.class,
                        "findByName",
                        "name", name));
    }

    @Programmatic
    private Numerator newNumerator(
            final String numeratorName,
            final String format,
            final long lastIncrement) {
        final Numerator numerator = repositoryService.instantiate(Numerator.class);
        numerator.setName(numeratorName);
        numerator.setFormat(format);
        numerator.setLastIncrement(lastIncrement);
        repositoryService.persist(numerator);
        return numerator;
    }

    @Programmatic
    public Numerator findOrCreate(
            final String numeratorName,
            final String format,
            final long lastIncrement) {
        Numerator numerator = findByName(numeratorName);
        numerator = numerator != null ? numerator : newNumerator(numeratorName, format, lastIncrement);
        return numerator;
    }

    @Inject RepositoryService repositoryService;
}
