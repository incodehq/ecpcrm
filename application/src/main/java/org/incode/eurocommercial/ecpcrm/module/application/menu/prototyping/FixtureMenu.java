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
package org.incode.eurocommercial.ecpcrm.module.application.menu.prototyping;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.application.fixture.jdbc.EcpCrmJdbcImportFixture;
import org.incode.eurocommercial.ecpcrm.module.application.service.homepage.HomePageService;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsDemoFixture;

/**
 * Enables fixtures to be installed from the application.
 */
@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Prototyping",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY,
        menuOrder = "500.10")
public class FixtureMenu {

    @Action(restrictTo = RestrictTo.PROTOTYPING)
    @ActionLayout(cssClassFa="fa fa-refresh")
    @MemberOrder(sequence = "500.10.2")
    public Object recreateDummyData() {
        fixtureScripts.runFixtureScript(new LoyaltyCardsDemoFixture(), null);
        return homePageService.homePage();
    }

    @Action(restrictTo = RestrictTo.PROTOTYPING)
    @ActionLayout(cssClassFa="fa fa-download")
    @MemberOrder(sequence = "500.10.2")
    public Object runImport(String database, String username, String password) {
        fixtureScripts.runFixtureScript(new EcpCrmJdbcImportFixture(database, username, password), null);
        return homePageService.homePage();
    }

    @Inject private HomePageService homePageService;
    @Inject private FixtureScripts fixtureScripts;
}
