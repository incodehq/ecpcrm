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

package org.incode.eurocommercial.ecpcrm.module.api.fixture;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.sudo.SudoService;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsIntegTestFixture;

public class ApiIntegTestFixture extends FixtureScript {
    public ApiIntegTestFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Inject SudoService sudoService;
    @Inject CenterRepository centerRepository;

    LoyaltyCardsIntegTestFixture loyaltyCardsIntegTestFixture;

    public List<Center> getCenters() {
        return loyaltyCardsIntegTestFixture.getCenters();
    }

    public List<Card> getCards() {
        return loyaltyCardsIntegTestFixture.getCards();
    }

    @Override
    protected void execute(final ExecutionContext ec) {
        loyaltyCardsIntegTestFixture = new LoyaltyCardsIntegTestFixture();
        ec.executeChild(ApiIntegTestFixture.this, loyaltyCardsIntegTestFixture);
        ec.executeChild(ApiIntegTestFixture.this, new AuthenticationDeviceFixture());

    }

}
