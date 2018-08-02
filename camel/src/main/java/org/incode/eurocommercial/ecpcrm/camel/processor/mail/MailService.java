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
package org.incode.eurocommercial.ecpcrm.camel.processor.mail;

import java.io.IOException;

import com.ecwid.maleorang.MailchimpClient;
import com.ecwid.maleorang.MailchimpException;
import com.ecwid.maleorang.MailchimpObject;
import com.ecwid.maleorang.method.v3_0.lists.members.EditMemberMethod;
import com.ecwid.maleorang.method.v3_0.lists.members.MemberInfo;
import com.google.common.base.Strings;

import org.incode.eurocommercial.ecpcrm.canonical.center.v1.CenterDto;
import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;

import lombok.Setter;

public class MailService {

    @Setter
    private String apiKey;

    private MailchimpClient client;

    public void init() {
        if (!Strings.isNullOrEmpty(apiKey)) {
            client = new MailchimpClient(apiKey);
        }
    }

    public void tearDown() {
        try {
            client.close();
        } catch (IOException ignored) {
        }
    }


    private MemberInfo createOrUpdate(String email, String firstName, String lastName, String listId, String status) {
        if (client == null) {
            return null;
        }

        EditMemberMethod.CreateOrUpdate method = new EditMemberMethod.CreateOrUpdate(listId, email);

        method.status = status;
        method.merge_fields = new MailchimpObject();
        method.merge_fields.mapping.put("FNAME", firstName);
        method.merge_fields.mapping.put("LNAME", lastName);

        try {
            return client.execute(method);
        } catch (IOException | MailchimpException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MemberInfo createOrUpdate(UserDto user, CenterDto center, String status) {
        return createOrUpdate(user.getEmail(), user.getFirstName(), user.getLastName(), center.getMailchimpListId(), status);
    }

    public MemberInfo subscribeUser(String email, String firstName, String lastName, String listId) {
        return createOrUpdate(email, firstName, lastName, listId, "subscribed");
    }

    public MemberInfo unsubscribeUser(String email, String firstName, String lastName, String listId) {
        return createOrUpdate(email, firstName, lastName, listId, "unsubscribed");
    }

    public MemberInfo subscribeUser(UserDto user, CenterDto center) {
        return createOrUpdate(user, center, "subscribed");
    }

    public MemberInfo unsubscribeUser(UserDto user, CenterDto center) {
        return createOrUpdate(user, center, "unsubscribed");
    }
}
