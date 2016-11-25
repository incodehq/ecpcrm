/**
 *  Copyright 2015-2016 Eurocommercial Properties NV
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
package org.incode.eurocommercial.ecpcrm.dom.user;

import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = User.class
)
public class UserRepository {

    @Programmatic
    public List<User> listAll() {
        return repositoryService.allInstances(User.class);
    }

    @Programmatic
    public User findByExactEmail(
            final String email
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        User.class,
                        "findByExactEmail",
                        "email", email));
    }

    @Programmatic
    public List<User> findByEmailContains(
            final String email
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        User.class,
                        "findByEmailContains",
                        "email", email));
    }

    @Programmatic
    public List<User> findByNameContains(
            final String name
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        User.class,
                        "findByNameContains",
                        "name", name));
    }


    @Programmatic
    public User newUser(
            final boolean enabled,
            final Title title,
            final String firstName,
            final String lastName,
            final String email,
            final Center center,
            final Card card,
            final boolean promotionalEmails
    ) {
        final User user = repositoryService.instantiate(User.class);
        user.setEnabled(enabled);
        user.setTitle(title);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setCenter(center);
        user.setCard(card);
        user.setPromotionalEmails(promotionalEmails);
        repositoryService.persist(user);
        return user;
    }

    @Programmatic
    public User findOrCreate(
            final boolean enabled,
            final Title title,
            final String firstName,
            final String lastName,
            final String email,
            final Center center,
            @Parameter(optionality = Optionality.OPTIONAL) final String cardNumber,
            final boolean promotionalEmails
    ) {
        User user = findByExactEmail(email);
        Card card = null;
        if(!Strings.isNullOrEmpty(cardNumber))
            card = cardRepository.findOrCreate(cardNumber, CardStatus.ENABLED, null, center);
        if(user == null) {
            user = newUser(
                    enabled,
                    title,
                    firstName,
                    lastName,
                    email,
                    center,
                    card,
                    promotionalEmails);
        }
        return user;
    }

    @Programmatic
    public void delete(final User user) {
        repositoryService.remove(user);
    }

    @Inject
    RepositoryService repositoryService;

    @Inject
    CardRepository cardRepository;

}
