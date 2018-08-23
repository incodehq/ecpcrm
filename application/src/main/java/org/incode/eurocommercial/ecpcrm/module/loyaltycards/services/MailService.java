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
package org.incode.eurocommercial.ecpcrm.module.loyaltycards.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.CenterRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

@DomainService(
        nature = NatureOfService.VIEW_REST_ONLY
)
public class MailService {

    @Action(semantics = SemanticsOf.SAFE)
    public List<User> mailchimpWebhookCallback(String listId, String email) {
        List<Center> centers = centerRepository.findByMailchimpListId(listId);

        List<User> users = centers.stream()
                .map(center -> userRepository.findByExactEmailAndCenter(email, center))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (User user : users) {
            user.setPromotionalEmails(false);
        }

        return users;
    }

    @Inject private UserRepository userRepository;
    @Inject private CenterRepository centerRepository;
}
