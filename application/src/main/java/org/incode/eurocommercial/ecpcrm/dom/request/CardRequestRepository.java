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
package org.incode.eurocommercial.ecpcrm.dom.request;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDateTime;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.user.User;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = CardRequest.class
)
public class CardRequestRepository {
    @Programmatic
    public List<CardRequest> listAll() {
        return repositoryService.allInstances(CardRequest.class);
    }

    @Programmatic
    public List<CardRequest> listOpenRequests() {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        CardRequest.class,
                        "findByApproved",
                        "approved", null));
    }

    @Programmatic
    public List<CardRequest> listRecentlyIssuedRequests() {
        LocalDateTime end = clockService.nowAsLocalDateTime();
        LocalDateTime start = end.minusDays(7);
        return repositoryService.allMatches(
                new QueryDefault<>(
                        CardRequest.class,
                        "findByIssueDateRange",
                        "startDate", start,
                        "endDate", end));
    }

    @Programmatic
    public List<CardRequest> listRecentlyHandledRequests() {
        LocalDateTime end = clockService.nowAsLocalDateTime();
        LocalDateTime start = end.minusDays(7);
        return repositoryService.allMatches(
                new QueryDefault<>(
                        CardRequest.class,
                        "findByHandleDateRange",
                        "startDate", start,
                        "endDate", end));
    }

    @Programmatic
    public CardRequest openRequestForUser(
            User user
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        CardRequest.class,
                        "findByApprovedAndUser",
                        "approved", null,
                        "requestingUser", user));
    }

    @Programmatic
    private CardRequest newCardRequest(
            User user,
            CardRequestType type
    ) {
        if(user == null) {
            return null;
        }

        final CardRequest cardRequest = repositoryService.instantiate(CardRequest.class);

        cardRequest.setRequestingUser(user);
        cardRequest.setType(type);
        cardRequest.setIssueDate(clockService.nowAsLocalDateTime());

        repositoryService.persist(cardRequest);
        return cardRequest;
    }

    @Programmatic
    public CardRequest findOrCreate(
            User user,
            CardRequestType type
    ) {
        CardRequest cardRequest = openRequestForUser(user);
        cardRequest = cardRequest != null ? cardRequest : newCardRequest(user, type);
        return cardRequest;
    }

    @Inject RepositoryService repositoryService;
    @Inject ClockService clockService;
}
