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
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.FixtureResult;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.fixture.dom.card.CardCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.card.CardTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.center.CenterCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.center.CenterTearDown;
import org.incode.eurocommercial.ecpcrm.fixture.dom.user.UserCreate;
import org.incode.eurocommercial.ecpcrm.fixture.dom.user.UserTearDown;

import lombok.Getter;

public class DemoFixture extends FixtureScript {

    public DemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    public final int NUM_CENTERS = 5;
    public final int NUM_CARDS   = 100;
    public final int NUM_USERS   = 75;

    @Getter
    private final List<Center> centers = Lists.newArrayList();

    @Getter
    private final List<Card> cards = Lists.newArrayList();

    @Getter
    private final List<User> users = Lists.newArrayList();

    private FakeDataService faker;


    @Override
    protected void execute(final ExecutionContext ec) {

        // zap everything
        ec.executeChild(this, new UserTearDown());
        ec.executeChild(this, new CardTearDown());
        ec.executeChild(this, new CenterTearDown());

        for(int i = 0; i < NUM_CENTERS; i++) {
            ec.executeChild(this, new CenterCreate());
        }

        for(int i = 0; i < NUM_CARDS; i++) {
            ec.executeChild(this, new CardCreate());
        }

        for(int i = 0; i < NUM_USERS; i++) {
            ec.executeChild(this, new UserCreate());
        }

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
    }
}
