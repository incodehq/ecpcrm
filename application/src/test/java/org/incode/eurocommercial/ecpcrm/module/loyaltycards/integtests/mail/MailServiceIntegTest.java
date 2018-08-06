/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.mail;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.fixturescripts.FixtureScripts;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.fixture.LoyaltyCardsIntegTestFixture;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.integtests.LoyaltyCardModuleIntegTestAbstract;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.services.MailService;

import static org.assertj.core.api.Assertions.assertThat;

public class MailServiceIntegTest extends LoyaltyCardModuleIntegTestAbstract {
    @Inject private FixtureScripts fixtureScripts;
    @Inject MailService mailService;

    LoyaltyCardsIntegTestFixture fs;
    User user;
    Center center;

    @Before
    public void setUp() throws Exception {
        // given
        fs = new LoyaltyCardsIntegTestFixture();
        fixtureScripts.runFixtureScript(fs, null);

        user = fs.getUsers().get(0);
        center = user.getCenter();

        assertThat(user).isNotNull();
        assertThat(center).isNotNull();
    }

    public static class MailchimpCallback extends MailServiceIntegTest {

        @Test
        public void user_gets_unsubscribed() {
            // given
            user.setPromotionalEmails(true);

            // when
            mailService.mailchimpWebhookCallback(center.getMailchimpListId(), user.getEmail());

            // then
            assertThat(user.isPromotionalEmails()).isFalse();
        }

    }
}
