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
package domainapp.dom.seed.users;

import java.util.Arrays;

import org.isisaddons.module.security.dom.user.AccountType;
import org.isisaddons.module.security.seed.scripts.AbstractUserAndRolesFixtureScript;
import org.isisaddons.module.security.seed.scripts.IsisModuleSecurityAdminRoleAndPermissions;

import domainapp.dom.seed.roles.AuditModuleRoleAndPermissions;
import domainapp.dom.seed.roles.CommandModuleRoleAndPermissions;
import domainapp.dom.seed.roles.DevUtilsModuleRoleAndPermissions;
import domainapp.dom.seed.roles.DomainAppFixtureServiceRoleAndPermissions;
import domainapp.dom.seed.roles.DomainAppRegularRoleAndPermissions;
import domainapp.dom.seed.roles.PublishingModuleRoleAndPermissions;
import domainapp.dom.seed.roles.SessionLoggerModuleRoleAndPermissions;
import domainapp.dom.seed.roles.SettingsModuleRoleAndPermissions;
import domainapp.dom.seed.roles.TogglzModuleAdminRole;
import domainapp.dom.seed.roles.TranslationServicePoMenuRoleAndPermissions;
import domainapp.dom.seed.tenancies.DomainAppAdminUserTenancy;
import domainapp.dom.seed.tenancies.UsersTenancy;

public class DomainAppAdminUser extends AbstractUserAndRolesFixtureScript {

    public static final String USER_NAME = "domainapp-admin";
    public static final String TENANCY_PATH = UsersTenancy.TENANCY_PATH + USER_NAME;

    private static final String PASSWORD = "pass";

    public DomainAppAdminUser() {
        super(USER_NAME, PASSWORD, null,
                DomainAppAdminUserTenancy.TENANCY_PATH, AccountType.LOCAL,
                Arrays.asList(IsisModuleSecurityAdminRoleAndPermissions.ROLE_NAME,
                              TogglzModuleAdminRole.ROLE_NAME,
                              AuditModuleRoleAndPermissions.ROLE_NAME,
                              CommandModuleRoleAndPermissions.ROLE_NAME,
                              SessionLoggerModuleRoleAndPermissions.ROLE_NAME,
                              SettingsModuleRoleAndPermissions.ROLE_NAME,
                              PublishingModuleRoleAndPermissions.ROLE_NAME,
                              DevUtilsModuleRoleAndPermissions.ROLE_NAME,
                              DomainAppRegularRoleAndPermissions.ROLE_NAME,
                              DomainAppFixtureServiceRoleAndPermissions.ROLE_NAME,
                              TranslationServicePoMenuRoleAndPermissions.ROLE_NAME
                        ));
    }


    @Override
    protected void execute(ExecutionContext executionContext) {
        super.execute(executionContext);
    }

}
