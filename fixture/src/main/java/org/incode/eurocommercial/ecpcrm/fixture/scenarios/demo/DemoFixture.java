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
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.child.Child;
import org.incode.eurocommercial.ecpcrm.dom.childcare.ChildCare;
import org.incode.eurocommercial.ecpcrm.dom.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmFixtureServiceRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmRegularRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.users.EcpCrmAdminUser;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.dom.authentication.AuthenticationDeviceCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.authentication.AuthenticationDeviceTearDown;
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

    public final int NUM_CENTERS                    = 2;
    public final int CARDS_PER_CENTER               = 7;
    public final int USERS_WITH_CARDS_PER_CENTER    = 4;
    public final int USERS_WITHOUT_CARDS_PER_CENTER = 4;
    public final int CARD_REQUESTS_PER_CENTER       = 2;
    public final int CHILDREN_PER_USER              = 1;
    public final int CLOSED_CHILDCARES_PER_CHILD    = 2;
    public final int OPEN_CHILDCARES_PER_CENTER     = 2;

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
                        /* First, tear everything down hierarchically */
                        ec.executeChild(DemoFixture.this, new CardRequestTearDown());
                        ec.executeChild(DemoFixture.this, new CardTearDown());
                        ec.executeChild(DemoFixture.this, new ChildCareTearDown());
                        ec.executeChild(DemoFixture.this, new ChildTearDown());
                        ec.executeChild(DemoFixture.this, new UserTearDown());
                        ec.executeChild(DemoFixture.this, new AuthenticationDeviceTearDown());
                        ec.executeChild(DemoFixture.this, new CenterTearDown());
                        ec.executeChild(DemoFixture.this, new NumeratorTearDown());

                        for (int i = 0; i < NUM_CENTERS; i++) {
                            CenterCreate centerCreateFixture = new CenterCreate()
                                    .id("" + i + 1);
                            ec.executeChild(DemoFixture.this, centerCreateFixture);

                            Center center = centerCreateFixture.center();
                            AuthenticationDeviceCreate authenticationDeviceCreateFixture = new AuthenticationDeviceCreate()
                                    .center(center);
                            ec.executeChild(DemoFixture.this, authenticationDeviceCreateFixture);

                            for (int j = 0; j < CARDS_PER_CENTER; j++) {
                                String cardNumber = center.nextValidCardNumber();
                                CardCreate cardCreateFixture = new CardCreate()
                                        .center(center)
                                        .number(cardNumber);
                                ec.executeChild(DemoFixture.this, cardCreateFixture);
                            }
                            for (int j = 0; j < USERS_WITHOUT_CARDS_PER_CENTER + USERS_WITH_CARDS_PER_CENTER; j++) {
                                UserCreate userCreateFixture = new UserCreate()
                                        .center(center);
                                if (j < USERS_WITHOUT_CARDS_PER_CENTER) {
                                    userCreateFixture.cardNumber("");
                                }
                                ec.executeChild(DemoFixture.this, userCreateFixture);

                                User user = userCreateFixture.user();

                                /* Add card requests and children to all odd users */
                                if (j % 2 == 1) {
                                    if (j < CARD_REQUESTS_PER_CENTER * 2) {
                                        CardRequestCreate cardRequestCreateFixture = new CardRequestCreate()
                                                .user(user);
                                        ec.executeChild(DemoFixture.this, cardRequestCreateFixture);
                                    }

                                    for (int k = 0; k < CHILDREN_PER_USER; k++) {
                                        ChildCreate childCreateFixture = new ChildCreate()
                                                .parent(user);
                                        ec.executeChild(DemoFixture.this, childCreateFixture);

                                        Child child = childCreateFixture.child();

                                        for (int h = 0; h < CLOSED_CHILDCARES_PER_CHILD; h++) {
                                            ChildCareCreate childCareCreateFixture = new ChildCareCreate()
                                                    .child(child);
                                            ec.executeChild(DemoFixture.this, childCareCreateFixture);
                                        }
                                    }

                                    if (j < OPEN_CHILDCARES_PER_CENTER) {
                                        Child child = user.getChildren().first();
                                        ChildCareCreate childCareCreateFixture = new ChildCareCreate()
                                                .child(child)
                                                .checkIn(clockService.nowAsLocalDateTime().minusMinutes(new Random().nextInt(ChildCareCreate.MAX_DURATION)));
                                        ec.executeChild(DemoFixture.this, childCareCreateFixture);
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

                        final MaryHostessFixtureScript maryScript = new MaryHostessFixtureScript();
                        ec.executeChild(DemoFixture.this, maryScript);

                        Center center = getCenters().get(new Random().nextInt(getCenters().size()));
                        maryScript.getApplicationUser().setAtPath(center.getAtPath());
                    }
                });
    }

    @Inject ClockService clockService;
}
