/*
 *  Copyright 2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
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
package org.incode.eurocommercial.ecpcrm.module.application.fixture.security.roles;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.CardMenu;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.UserMenu;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionRule;
import org.isisaddons.module.security.seed.scripts.AbstractRoleAndPermissionsFixtureScript;

import org.incode.eurocommercial.ecpcrm.module.api.menu.AdminMenu;
import org.incode.eurocommercial.ecpcrm.module.application.EcpCrmApplicationModule;
import org.incode.eurocommercial.ecpcrm.module.application.service.homepage.HomePageViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.EcpCrmLoyaltyCardsModule;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.CenterMenu;

public class HostessRoleAndPermissions extends AbstractRoleAndPermissionsFixtureScript {

    public static final String ROLE_NAME = "hostess-role";

    public HostessRoleAndPermissions() {
        super(ROLE_NAME, "Can manage ecpcrm dom, except centers");
    }

    @Override
    protected void execute(final ExecutionContext executionContext) {
        newPackagePermissions(
                ApplicationPermissionRule.ALLOW,
                ApplicationPermissionMode.CHANGING,
                EcpCrmLoyaltyCardsModule.class.getPackage().getName(),
                EcpCrmApplicationModule.class.getPackage().getName()
        );
        newPackagePermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                Center.class.getPackage().getName()
        );
        newClassPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                CenterMenu.class
        );
        newMemberPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                User.class,
                "center"
        );
        newMemberPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                Card.class,
                "center"
        );

        newPackagePermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                AdminMenu.class.getPackage().getName()
        );
        newMemberPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                CardMenu.class,
                "downloadAllCards"
        );
        newMemberPermissions(
                ApplicationPermissionRule.VETO,
                ApplicationPermissionMode.VIEWING,
                UserMenu.class,
                "downloadAllUsers"
        );
    }
}
