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

package org.incode.eurocommercial.ecpcrm.module.application.fixture.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.sudo.SudoService;

import org.incode.eurocommercial.ecpcrm.module.api.fixture.AuthenticationDeviceFixture;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.security.roles.EcpCrmFixtureServiceRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.security.roles.EcpCrmRegularRoleAndPermissions;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.security.users.EcpCrmAdminUser;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.security.users.MaryHostessUser;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsDemoFixture;

public class EcpCrmApplicationDemoFixture extends FixtureScript {
    public EcpCrmApplicationDemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Inject SudoService sudoService;
    @Inject CenterRepository centerRepository;

    @Override
    protected void execute(final ExecutionContext ec) {
        sudoService.sudo(EcpCrmAdminUser.USER_NAME, Arrays.asList(EcpCrmRegularRoleAndPermissions.ROLE_NAME, EcpCrmFixtureServiceRoleAndPermissions.ROLE_NAME),
                new Runnable() {
                    @Override public void run() {
                        ec.executeChild(EcpCrmApplicationDemoFixture.this, new LoyaltyCardsDemoFixture());
                        ec.executeChild(EcpCrmApplicationDemoFixture.this, new AuthenticationDeviceFixture());

                        final MaryHostessUser maryScript = new MaryHostessUser();
                        ec.executeChild(EcpCrmApplicationDemoFixture.this, maryScript);

                        final List<Center> centerList = centerRepository.listAll();
                        Center center = centerList.get(new Random().nextInt(centerList.size()));
                        maryScript.getApplicationUser().setAtPath(center.getAtPath());
                    }
                });
    }

}
