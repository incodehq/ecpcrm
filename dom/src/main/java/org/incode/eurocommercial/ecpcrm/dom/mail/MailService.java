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
package org.incode.eurocommercial.ecpcrm.dom.mail;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.ecwid.maleorang.MailchimpClient;
import com.ecwid.maleorang.MailchimpException;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.method.v3_0.lists.members.EditMemberMethod;
import com.ecwid.maleorang.method.v3_0.lists.members.MemberInfo;
import com.google.common.base.Strings;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.config.ConfigurationService;

import org.incode.eurocommercial.ecpcrm.dom.user.User;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class MailService {
    private MailchimpClient client;

    @PostConstruct
    public void init() {
        String apiKey = configurationService.getProperty("mailchimp.apiKey");
        if (!Strings.isNullOrEmpty(apiKey)) {
            client = new MailchimpClient(apiKey);
        }
    }

    @PreDestroy
    public void tearDown() {
        try {
            client.close();
        } catch (IOException ignored) {
        }
    }

    public MemberInfo subscribeUser(User user) {
        if (client == null) {
            return null;
        }

        String listId = user.getCenter().getMailchimpListId();
        EditMemberMethod.CreateOrUpdate method = new EditMemberMethod.CreateOrUpdate(listId, user.getEmail());

        method.status = "subscribed";
        method.merge_fields = new MailchimpObject();
        method.merge_fields.mapping.put("FNAME", user.getFirstName());
        method.merge_fields.mapping.put("LNAME", user.getLastName());

        try {
            return client.execute(method);
        } catch (IOException | MailchimpException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MemberInfo unsubscribeUser(User user) {
        if (client == null) {
            return null;
        }

        String listId = user.getCenter().getMailchimpListId();
        EditMemberMethod.CreateOrUpdate method = new EditMemberMethod.CreateOrUpdate(listId, user.getEmail());

        method.status = "unsubscribed";
        method.merge_fields = new MailchimpObject();
        method.merge_fields.mapping.put("FNAME", user.getFirstName());
        method.merge_fields.mapping.put("LNAME", user.getLastName());

        try {
            return client.execute(method);
        } catch (IOException | MailchimpException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Inject ConfigurationService configurationService;
}
