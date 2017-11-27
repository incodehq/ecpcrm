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

package org.incode.eurocommercial.ecpcrm.fixture.scenarios.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.sudo.SudoService;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.numerator.NumeratorRepository;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmFixtureServiceRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmRegularRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.users.EcpCrmAdminUser;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.dom.authentication.card.AuthenticationDeviceCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.authentication.card.AuthenticationDeviceTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.card.CardCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.card.CardTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.center.CenterCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.center.CenterTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.child.ChildCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.child.ChildTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.childcare.ChildCareCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.childcare.ChildCareTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.hostess.MaryHostessFixtureScript;
import org.incode.eurocommercial.ecpcrm.fixture.dom.numerator.NumeratorTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.request.CardRequestCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.request.CardRequestTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.user.UserCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.user.UserTearDown;

import lombok.Getter;

public class DemoFixture extends FixtureScript {

    public DemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    public final int NUM_CENTERS         = 5;
    public final int NUM_CARDS           = 50;
    public final int NUM_USERS           = 30;
    public final int NUM_CARD_REQUESTS   = 5;
    public final int NUM_CHILDREN        = 20;
    public final int NUM_CHILDCARES      = 40;
    public final int NUM_OPEN_CHILDCARES = 5;

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

    @Inject
    SudoService sudoService;

    @Override
    protected void execute(final ExecutionContext ec) {
        sudoService.sudo(EcpCrmAdminUser.USER_NAME, Arrays.asList(EcpCrmRegularRoleAndPermissions.ROLE_NAME, EcpCrmFixtureServiceRoleAndPermissions.ROLE_NAME),
                new Runnable() {
                    @Override public void run() {
                        String cardNumber;

                        /* First, tear everything down hierarchically */
                        ec.executeChild(DemoFixture.this, new CardRequestTearDown());
                        ec.executeChild(DemoFixture.this, new CardTearDown());
                        ec.executeChild(DemoFixture.this, new ChildCareTearDown());
                        ec.executeChild(DemoFixture.this, new ChildTearDown());
                        ec.executeChild(DemoFixture.this, new UserTearDown());
                        ec.executeChild(DemoFixture.this, new AuthenticationDeviceTearDown());
                        ec.executeChild(DemoFixture.this, new CenterTearDown());
                        ec.executeChild(DemoFixture.this, new NumeratorTearDown());

                        for(int i = 0; i < NUM_CENTERS; i++) {
                            ec.setParameter("id", i + 1);
                            ec.executeChild(DemoFixture.this, new CenterCreate());
                        }

                        /* Add all created centers to the centers List */
                        getCenters().addAll(
                                ec.getResults().stream()
                                        .map(FixtureResult::getObject)
                                        .filter(c -> c instanceof Center)
                                        .map(c -> (Center) c)
                                        .collect(Collectors.toList()));

                        for(Center center : getCenters()) {
                            ec.executeChild(DemoFixture.this, new AuthenticationDeviceCreate().center(center));
                        }

                        for(int i = 0; i < NUM_CARDS; i++) {
                            Center center = getCenters().get(new Random().nextInt(getCenters().size()));

                            cardNumber = center.nextValidCardNumber();

                            ec.setParameter("number", cardNumber);
                            ec.executeChild(DemoFixture.this, new CardCreate().center(center));
                        }

                        for(int i = 0; i < NUM_USERS - NUM_CARD_REQUESTS; i++) {
                            ec.executeChild(DemoFixture.this, new UserCreate());
                        }
                        for(int i = NUM_USERS - NUM_CARD_REQUESTS; i < NUM_USERS; i++) {
                            ec.setParameter("cardNumber", "");
                            ec.executeChild(DemoFixture.this, new UserCreate());
                        }

                        /* Add all created users to the users List */
                        getUsers().addAll(
                                ec.getResults().stream()
                                        .map(FixtureResult::getObject)
                                        .filter(u -> u instanceof User)
                                        .map(u -> (User) u)
                                        .collect(Collectors.toList()));

                        for(int i = 0; i < NUM_CARD_REQUESTS; i++) {
                            /* Only users without cards can request a card */
                            List<User> availableUsers = getUsers().stream()
                                    .filter(u -> u.getCards().isEmpty())
                                    .collect(Collectors.toList());

                            User requestingUser;

                            if(availableUsers.size() > 0) {
                                requestingUser = availableUsers.get(new Random().nextInt(availableUsers.size()));
                            }
                            /* Unless no such users are available */
                            else {
                                requestingUser = getUsers().get(new Random().nextInt(getUsers().size()));
                            }

                            ec.executeChild(DemoFixture.this, new CardRequestCreate().user(requestingUser));
                        }

                        for(int i = 0; i < NUM_CHILDREN; i++) {
                            ec.executeChild(DemoFixture.this, new ChildCreate());
                        }

                        for(int i = 0; i < NUM_CHILDCARES - NUM_OPEN_CHILDCARES; i++) {
                            ec.executeChild(DemoFixture.this, new ChildCareCreate());
                        }
                        for(int i = NUM_CHILDCARES - NUM_OPEN_CHILDCARES; i < NUM_CHILDCARES; i++) {
                            ec.setParameter("checkIn", clockService.nowAsLocalDateTime().minusMinutes(new Random().nextInt(ChildCareCreate.MAX_DURATION)));
                            ec.executeChild(DemoFixture.this, new ChildCareCreate());
                        }

                        final MaryHostessFixtureScript maryScript = new MaryHostessFixtureScript();
                        ec.executeChild(DemoFixture.this, maryScript);

                        Center center = getCenters().get(new Random().nextInt(getCenters().size()));
                        maryScript.getApplicationUser().setAtPath(center.getAtPath());

                        /* Add all created Domain Objects to their respective Lists */
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

                        getChildren().addAll(results.stream()
                                .filter(c -> c instanceof Child)
                                .map(c -> (Child) c)
                                .collect(Collectors.toList()));

                        getChildCares().addAll(results.stream()
                                .filter(c -> c instanceof ChildCare)
                                .map(c -> (ChildCare) c)
                                .collect(Collectors.toList()));
                    }
                });
    }

    @Inject NumeratorRepository numeratorRepository;
    @Inject CardRepository cardRepository;
    @Inject ClockService clockService;
}
