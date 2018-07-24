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
package org.incode.eurocommercial.ecpcrm.app.seed;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.dom.seed.roles.AuditModuleRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.CommandModuleRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.DevUtilsModuleRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmFixtureServiceRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmRegularRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.EcpCrmVetoSelectedMembersPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.HostessRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.PublishingModuleRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.SessionLoggerModuleRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.SettingsModuleRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.roles.TranslationServicePoMenuRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.dom.seed.users.EcpCrmAdminUser;

@DomainService(
        nature = NatureOfService.DOMAIN
)
@DomainServiceLayout(
        menuOrder = "1100" // not visible, but determines the order initialized (must come after security module's seed service)
)
public class EcpCrmRolesAndPermissionsSeedService {

    //region > init
    @Programmatic
    @PostConstruct
    public void init() {
        fixtureScripts.runFixtureScript(new SeedFixtureScript(), null);
    }
    //endregion

    //region  >  (injected)
    @Inject
    FixtureScripts fixtureScripts;
    //endregion

    public static class SeedFixtureScript extends FixtureScript {

        @Override
        protected void execute(final ExecutionContext executionContext) {
            executionContext.executeChild(this, new EcpCrmRegularRoleAndPermissions());
            executionContext.executeChild(this, new HostessRoleAndPermissions());
            executionContext.executeChild(this, new EcpCrmVetoSelectedMembersPermissions());
            executionContext.executeChild(this, new EcpCrmFixtureServiceRoleAndPermissions());

            executionContext.executeChild(this, new AuditModuleRoleAndPermissions());
            executionContext.executeChild(this, new CommandModuleRoleAndPermissions());
            executionContext.executeChild(this, new DevUtilsModuleRoleAndPermissions());
            executionContext.executeChild(this, new PublishingModuleRoleAndPermissions());
            executionContext.executeChild(this, new SessionLoggerModuleRoleAndPermissions());
            executionContext.executeChild(this, new SettingsModuleRoleAndPermissions());

            executionContext.executeChild(this, new TranslationServicePoMenuRoleAndPermissions());

            executionContext.executeChild(this, new EcpCrmAdminUser());
        }

    }

}
