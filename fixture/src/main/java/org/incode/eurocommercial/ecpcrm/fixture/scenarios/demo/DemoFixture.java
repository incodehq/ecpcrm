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

package org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.numerator.NumeratorRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.dom.card.CardCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.card.CardTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.center.CenterCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.center.CenterTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.request.CardRequestCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.request.CardRequestTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.user.UserCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.user.UserTearDown;

import lombok.Getter;

public class DemoFixture extends FixtureScript {

    public DemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    public final int NUM_CENTERS = 5;
    public final int NUM_CARDS   = 50;
    public final int NUM_USERS   = 30;
    public final int NUM_CARD_REQUESTS = 5;

    @Getter
    private final List<Center> centers = Lists.newArrayList();

    @Getter
    private final List<Card> cards = Lists.newArrayList();

    @Getter
    private final List<User> users = Lists.newArrayList();

    @Getter
    private final List<CardRequest> cardRequests = Lists.newArrayList();

    @Override
    protected void execute(final ExecutionContext ec) {
        String cardNumber;

        // zap everything
        ec.executeChild(this, new CardRequestTearDown());
        ec.executeChild(this, new CardTearDown());
        ec.executeChild(this, new UserTearDown());
        ec.executeChild(this, new CenterTearDown());

        for(int i = 0; i < NUM_CENTERS; i++) {
            ec.executeChild(this, new CenterCreate());
        }
        getCenters().addAll(
                ec.getResults().stream()
                        .map(FixtureResult::getObject)
                        .filter(c -> c instanceof Center)
                        .map(c -> (Center) c)
                        .collect(Collectors.toList()));

        for(int i = 0; i < NUM_CARDS; i++) {
            Center center = getCenters().get(ThreadLocalRandom.current().nextInt(0, getCenters().size()));
            do {
                cardNumber = center.getNumerator().nextIncrementStr();
            } while(!cardRepository.cardNumberIsValid(cardNumber, center.getReference()));

            ec.setParameter("number", cardNumber);
            ec.executeChild(this, new CardCreate().center(center));
        }

        for(int i = 0; i < NUM_USERS - NUM_CARD_REQUESTS; i++) {
            ec.executeChild(this, new UserCreate());
        }
        for(int i = NUM_USERS - NUM_CARD_REQUESTS; i < NUM_USERS; i++) {
            ec.setParameter("cardNumber", "");
            ec.executeChild(this, new UserCreate());
        }
        getUsers().addAll(
                ec.getResults().stream()
                        .map(FixtureResult::getObject)
                        .filter(u -> u instanceof User)
                        .map(u -> (User) u)
                        .collect(Collectors.toList()));

        for(int i = 0; i < NUM_CARD_REQUESTS; i++) {
            List<User> availableUsers = getUsers().stream().filter(u -> cardRepository.findByOwner(u) == null).collect(Collectors.toList());
            User requestingUser = availableUsers.get(ThreadLocalRandom.current().nextInt(0, availableUsers.size()));
            ec.executeChild(this, new CardRequestCreate().user(requestingUser));
        }

        List<Object> results  = ec.getResults().stream()
                .map(FixtureResult::getObject)
                .collect(Collectors.toList());

        getCards().addAll(results.stream()
                .filter(c -> c instanceof Card)
                .map(c -> (Card) c)
                .collect(Collectors.toList()));

        getCardRequests().addAll(results.stream()
                .filter(c -> c instanceof CardRequest)
                .map(c -> (CardRequest) c)
                .collect(Collectors.toList()));
    }

    @Inject NumeratorRepository numeratorRepository;
    @Inject CardRepository cardRepository;
}
