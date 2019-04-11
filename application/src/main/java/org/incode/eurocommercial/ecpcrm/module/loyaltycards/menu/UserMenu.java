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
package org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;
import java.util.List;

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
    @MemberOrder(sequence = "1")
    public List<User> listAllUsers(){
        return userRepository.listAll();
    }

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

    public boolean default0NewUser(){ return true; }

    @Action(domainEvent = CreateDomainEvent.class, publishing = Publishing.ENABLED, semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    @MemberOrder(sequence = "4")
    public User newUser(
            final boolean enabled,
            final Title title,
            final String firstName,
            final String lastName,
            final String email,
            final @Parameter(optionality = Optionality.OPTIONAL) LocalDate birthDate,
            final @Parameter(optionality = Optionality.OPTIONAL) String address,
            final @Parameter(optionality = Optionality.OPTIONAL) String zipcode,
            final @Parameter(optionality = Optionality.OPTIONAL) String city,
            final @Parameter(optionality = Optionality.OPTIONAL) String phoneNumber,
            final Center center,
            final @Parameter(optionality = Optionality.OPTIONAL) String cardNumber,
            final boolean promotionalEmails,
            final @Parameter(optionality = Optionality.OPTIONAL) Boolean hasCar
    ) {
        return userRepository.newUser(
                enabled,
                title,
                firstName,
                lastName,
                email,
                birthDate,
                address,
                zipcode,
                city,
                phoneNumber,
                center,
                cardNumber,
                promotionalEmails,
                hasCar,
                null
        );
    }

    public TranslatableString validateNewUser(
            final boolean enabled,
            final Title title,
            final String firstName,
            final String lastName,
            final String email,
            final LocalDate birthDate,
            final String address,
            final String zipcode,
            final String city,
            final String phoneNumber,
            final Center center,
            final String cardNumber,
            final boolean promotionalEmails,
            final Boolean hasCar
    ) {
        return userRepository.validateNewUser(
                enabled,
                title,
                firstName,
                lastName,
                email,
                birthDate,
                address,
                zipcode,
                city,
                phoneNumber,
                center,
                cardNumber,
                promotionalEmails,
                hasCar,
                null
        );
    }

    @Inject private UserRepository userRepository;
}
