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
package org.incode.eurocommercial.ecpcrm.dom.card;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
public class CardMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Card> listAll() {
        return cardRepository.listAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Card> findByNumber(
            @ParameterLayout(named = "Number") final String number
    ) {
        return cardRepository.findByNumberContains(number);
    }

    public static class CreateDomainEvent extends ActionDomainEvent<CardMenu> {}

    @Action(domainEvent = CreateDomainEvent.class, semantics = SemanticsOf.IDEMPOTENT)
    @MemberOrder(sequence = "3")
    public Card newCard(
            final @ParameterLayout(named = "Number") String number,
            final @ParameterLayout(named = "Status") CardStatus status,
            final @ParameterLayout(named = "Client ID") String clientId,
            final @ParameterLayout(named = "Center") Center center
    ) {
        return cardRepository.findOrCreate(number, status, clientId, center);
    }

    @Inject
    CardRepository cardRepository;

    @Inject
    CenterRepository centerRepository;
}
