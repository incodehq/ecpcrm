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
package org.incode.eurocommercial.ecpcrm.dom.childcare;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = ChildCare.class
)
public class ChildCareRepository {
    public List<ChildCare> listAll() {
        return repositoryService.allInstances(ChildCare.class);
    }

    public List<ChildCare> findByChild(Child child) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        ChildCare.class,
                        "findByChild",
                        "child", child));
    }

    public List<ChildCare> findByDateRange(Center center, LocalDateTime start, LocalDateTime end) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        ChildCare.class,
                        "findByDateRange",
                        "center", center,
                        "start", start,
                        "end", end));
    }

    public List<ChildCare> findByChildAndDateRange(Child child, LocalDateTime start, LocalDateTime end) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        ChildCare.class,
                        "findByChildAndDateRange",
                        "child", child,
                        "start", start,
                        "end", end));
    }

    public ChildCare findActiveChildCareByChild(Child child) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        ChildCare.class,
                        "findActiveChildCareByChild",
                        "child", child
                ));
    }

    public List<ChildCare> findActiveChildCares(Center center) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        ChildCare.class,
                        "findActiveChildCares",
                        "center", center
                ));
    }

    public ChildCare newChildCare(Child child, LocalDateTime checkIn) {
        ChildCare childCare = repositoryService.instantiate(ChildCare.class);

        childCare.setChild(child);
        childCare.setCheckIn(checkIn);

        repositoryService.persist(childCare);
        return childCare;
    }

    public ChildCare newChildCare(Child child) {
        LocalDateTime now = clockService.nowAsLocalDateTime();
        return newChildCare(child, now);
    }

    public ChildCare findOrCreate(Child child) {
        ChildCare childCare = findActiveChildCareByChild(child);
        childCare = childCare != null ? childCare : newChildCare(child);

        return childCare;
    }


    @Inject private RepositoryService repositoryService;
    @Inject private ClockService clockService;
}
