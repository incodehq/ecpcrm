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
package org.incode.eurocommercial.ecpcrm.camel.processor.mail.transactional;

import com.microtripit.mandrillapp.lutung.view.MandrillMessage;

import org.apache.camel.BeanInject;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import org.incode.eurocommercial.ecpcrm.canonical.center.v1.CenterDto;
import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration({"classpath:org/incode/eurocommercial/ecpcrm/camel/routing/camel-config.xml"})
public class TransactionalMailServiceTest {
    @BeanInject TransactionalMailService transactionalMailService;

    @Test
    public void can_add_user_as_recipient_to_message() {
        MandrillMessage message = new MandrillMessage();

        UserDto userDto = new UserDto();
        userDto.setFirstName("Tester");
        userDto.setEmail("test@test.com");

        transactionalMailService.addUserAsRecipientToMessage(message, userDto);

        assertThat(message.getTo().size()).isEqualTo(1);
        assertThat(message.getTo().get(0).getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    public void can_add_center_as_sender_to_message() {
        MandrillMessage message = new MandrillMessage();

        CenterDto centerDto = new CenterDto();
        centerDto.setName("Test");
        centerDto.setContactEmail("test@test.com");

        transactionalMailService.addCenterAsSenderToMessage(message, centerDto);

        assertThat(message.getFromEmail()).isEqualTo(centerDto.getContactEmail());
        assertThat(message.getFromName()).isEqualTo(centerDto.getName());
    }
}
