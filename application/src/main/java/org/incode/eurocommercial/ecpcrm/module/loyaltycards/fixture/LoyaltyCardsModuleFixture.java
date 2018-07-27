/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with DemoFixture.this work for additional information
 *  regarding copyright ownership.  The ASF licenses DemoFixture.this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use DemoFixture.this file except in compliance
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

package org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.child.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.card.CardCreate;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.card.request.CardRequestCreate;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.center.CenterCreate;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.child.ChildCreate;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.child.childcare.ChildCareCreate;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.user.UserCreate;

import lombok.Getter;

public class LoyaltyCardsModuleFixture extends FixtureScript {
    public LoyaltyCardsModuleFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    public int NUM_CENTERS                    = 2;
    public int CARDS_PER_CENTER               = 5;
    public int USERS_WITH_CARDS_PER_CENTER    = 3;
    public int USERS_WITHOUT_CARDS_PER_CENTER = 3;
    public int CARD_REQUESTS_PER_CENTER       = 2;
    public int CHILDREN_PER_USER              = 1;
    public int CLOSED_CHILDCARES_PER_CHILD    = 2;
    public int OPEN_CHILDCARES_PER_CENTER     = 2;

    @Getter
    private final List<Center> centers = Lists.newArrayList();

    @Getter
    private final List<Card> cards = Lists.newArrayList();

    @Getter
    private final List<User> users = Lists.newArrayList();

    @Getter
    private final List<CardRequest> cardRequests = Lists.newArrayList();

    @Getter
    private final List<Child> children = Lists.newArrayList();

    @Getter
    private final List<ChildCare> childCares = Lists.newArrayList();
    
    @Override
    protected void execute(final ExecutionContext ec) {
        for (int i = 0; i < NUM_CENTERS; i++) {
            CenterCreate centerCreateFixture = new CenterCreate()
                    .id("" + i + 1);
            ec.executeChild(LoyaltyCardsModuleFixture.this, centerCreateFixture);

            Center center = centerCreateFixture.center();

            for (int j = 0; j < CARDS_PER_CENTER; j++) {
                String cardNumber = center.nextValidCardNumber();
                CardCreate cardCreateFixture = new CardCreate()
                        .center(center)
                        .number(cardNumber);
                ec.executeChild(LoyaltyCardsModuleFixture.this, cardCreateFixture);
            }
            for (int j = 0; j < USERS_WITHOUT_CARDS_PER_CENTER + USERS_WITH_CARDS_PER_CENTER; j++) {
                UserCreate userCreateFixture = new UserCreate()
                        .center(center);
                if (j < USERS_WITHOUT_CARDS_PER_CENTER) {
                    userCreateFixture.cardNumber("");
                }
                ec.executeChild(LoyaltyCardsModuleFixture.this, userCreateFixture);

                User user = userCreateFixture.user();

                /* Add card requests and children to all odd users */
                if (j % 2 == 1) {
                    if (j < CARD_REQUESTS_PER_CENTER * 2) {
                        CardRequestCreate cardRequestCreateFixture = new CardRequestCreate()
                                .user(user);
                        ec.executeChild(LoyaltyCardsModuleFixture.this, cardRequestCreateFixture);
                    }

                    for (int k = 0; k < CHILDREN_PER_USER; k++) {
                        ChildCreate childCreateFixture = new ChildCreate()
                                .parent(user);
                        ec.executeChild(LoyaltyCardsModuleFixture.this, childCreateFixture);

                        Child child = childCreateFixture.child();

                        for (int h = 0; h < CLOSED_CHILDCARES_PER_CHILD; h++) {
                            ChildCareCreate childCareCreateFixture = new ChildCareCreate()
                                    .child(child);
                            ec.executeChild(LoyaltyCardsModuleFixture.this, childCareCreateFixture);
                        }
                    }

                    if (j < OPEN_CHILDCARES_PER_CENTER) {
                        Child child = user.getChildren().first();
                        ChildCareCreate childCareCreateFixture = new ChildCareCreate()
                                .child(child)
                                .checkIn(clockService.nowAsLocalDateTime().minusMinutes(new Random().nextInt(ChildCareCreate.MAX_DURATION)));
                        ec.executeChild(LoyaltyCardsModuleFixture.this, childCareCreateFixture);
                    }
                }
            }
        }

        /* Add all created Domain Objects to their respective Lists */
        List<Object> results  = ec.getResults().stream()
                .map(FixtureResult::getObject)
                .collect(Collectors.toList());

        getCenters().addAll(results.stream()
                .filter(c -> c instanceof Center)
                .map(c -> (Center) c)
                .collect(Collectors.toList()));

        getUsers().addAll(results.stream()
                .filter(u -> u instanceof User)
                .map(u -> (User) u)
                .collect(Collectors.toList()));

        getCards().addAll(results.stream()
                .filter(c -> c instanceof Card)
                .map(c -> (Card) c)
                .collect(Collectors.toList()));

        getCardRequests().addAll(results.stream()
                .filter(c -> c instanceof CardRequest)
                .map(c -> (CardRequest) c)
                .collect(Collectors.toList()));

        getChildren().addAll(results.stream()
                .filter(c -> c instanceof Child)
                .map(c -> (Child) c)
                .collect(Collectors.toList()));

        getChildCares().addAll(results.stream()
                .filter(c -> c instanceof ChildCare)
                .map(c -> (ChildCare) c)
                .collect(Collectors.toList()));
    }

    @Inject ClockService clockService;
}
