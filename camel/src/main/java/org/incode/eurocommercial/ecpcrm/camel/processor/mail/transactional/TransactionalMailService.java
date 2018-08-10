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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

import org.incode.eurocommercial.ecpcrm.canonical.center.v1.CenterDto;
import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

import lombok.Setter;


public class TransactionalMailService {

    @Setter
    private String apiKey;

    private MandrillApi mandrillApi;

    public void init() {
        if (!Strings.isNullOrEmpty(apiKey)) {
            mandrillApi = new MandrillApi(apiKey);
        }
    }

    public void tearDown() {
    }

    public void addUserAsRecipientToMessage(MandrillMessage message, UserDto userDto) {
        List<MandrillMessage.Recipient> recipients = message.getTo() == null ? Lists.newArrayList() : message.getTo();

        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(userDto.getEmail());
        recipient.setName(userDto.getFirstName());

        recipients.add(recipient);
        message.setTo(recipients);
    }

    public void addCenterAsSenderToMessage(MandrillMessage message, CenterDto centerDto) {
        message.setFromEmail(centerDto.getContactEmail());
        message.setFromName(centerDto.getName());
    }

    public MandrillMessageStatus[] sendTemplateMail(String templateName, UserDto toUser, CenterDto fromCenter) throws IOException, MandrillApiError {
        MandrillMessage message = new MandrillMessage();

        addUserAsRecipientToMessage(message, toUser);
        message.setPreserveRecipients(true);

        addCenterAsSenderToMessage(message, fromCenter);

        Map<String, String> templateContent = new HashMap<>();

        return mandrillApi.messages().sendTemplate(templateName, templateContent, message, true);
    }
}
