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
package org.incode.eurocommercial.ecpcrm.dom.user;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.center.CenterRepository;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Users",
        menuOrder = "11"
)
public class UserMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<User> findByEmail(
            final String email
    ) {
        return userRepository.findByEmailContains(email);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "3")
    public List<User> findByName(
            final String name
    ) {
        /* Splits name on spaces to search on both first and last name */
        String[] splitName = name.split("\\s+", 2);
        if(splitName.length > 1) {
            return userRepository.findByFirstAndLastName(splitName[0], splitName[1]);
        }
        return userRepository.findByNameContains(name);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "3")
    public User findByReference(
            final String reference
    ) {
        return userRepository.findByReference(reference);
    }

    public static class CreateDomainEvent extends ActionDomainEvent<UserMenu> {}

    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "4")
    public User newUser(
            final boolean enabled,
            final Title title,
            final String firstName,
            final String lastName,
            final String email,
            final String address,
            final String zipcode,
            final String city,
            final String phoneNumber,
            final Center center,
            final @Parameter(optionality = Optionality.OPTIONAL) String cardNumber,
            final boolean promotionalEmails,
            final @Parameter(optionality = Optionality.OPTIONAL) Boolean hasCar
    ) {
        return userRepository.newUser(
                enabled, title, firstName, lastName, email, address, zipcode, city, phoneNumber, center, cardNumber, promotionalEmails, hasCar, null);
    }

    public TranslatableString validateNewUser(
            final boolean enabled,
            final Title title,
            final String firstName,
            final String lastName,
            final String email,
            final String address,
            final String zipcode,
            final String city,
            final String phoneNumber,
            final Center center,
            final String cardNumber,
            final boolean promotionalEmails,
            final Boolean hasCar
    ) {
        return userRepository.validateNewUser(enabled, title, firstName, lastName, email, address, zipcode, city, phoneNumber, center, cardNumber, promotionalEmails, hasCar, null);
    }

    @Inject UserRepository userRepository;
    @Inject CenterRepository centerRepository;
}
