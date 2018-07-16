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
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.numerator.Numerator;
import org.incode.eurocommercial.ecpcrm.dom.numerator.NumeratorRepository;

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
    public List<User> findByCenter(
            final Center center
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        User.class,
                        "findByCenter",
                        "center", center));
    }

    @Programmatic
    public User findByExactEmailAndCenter(
            final String email,
            final Center center) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        User.class,
                        "findByExactEmailAndCenter",
                        "email", email,
                        "center", center));
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
    public List<User> findByFirstAndLastName(
            final String firstName,
            final String lastName
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        User.class,
                        "findByFirstAndLastName",
                        "firstName", firstName,
                        "lastName", lastName));
    }

    @Programmatic
    public User findByReference(
            final String reference
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        User.class,
                        "findByCode",
                        "reference", reference));
    }


    @Programmatic
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
            @Parameter(optionality = Optionality.OPTIONAL) final String cardNumber,
            final boolean promotionalEmails,
            final Boolean hasCar,
            String reference
    ) {
        Numerator userNumerator = numeratorRepository.findOrCreate("userNumerator", "%d", 0);

        final User user = repositoryService.instantiate(User.class);

        user.setEnabled(enabled);
        user.setTitle(title);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAddress(address);
        user.setZipcode(zipcode);
        user.setCity(city);
        user.setPhoneNumber(phoneNumber);
        user.setCenter(center);
        user.setHasCar(hasCar);

        if (promotionalEmails) {
            user.subscribeToPromotionalEmails();
        } else {
            user.unsubscribeFromPromotionalEmails();
        }

        if(reference == null) {
            reference = userNumerator.nextIncrementStr();
        }
        user.setReference(reference);

        /* Update numerator with new user reference */
        long ref = Long.parseLong(reference);
        if(ref > userNumerator.getLastIncrement()) {
            userNumerator.setLastIncrement(ref);
        }

        repositoryService.persist(user);

        if(!Strings.isNullOrEmpty(cardNumber)) {
            user.newCard(cardNumber);
        }

        return user;
    }

    @Programmatic
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
            @Parameter(optionality = Optionality.OPTIONAL) final String cardNumber,
            final boolean promotionalEmails,
            final Boolean hasCar,
            String reference
    ) {
        if(findByExactEmailAndCenter(email, center) != null) {
            return TranslatableString.tr("User with email {email} already exists", "email", email);
        }
        return validateFindOrCreate(enabled, title, firstName, lastName, email, address, zipcode, city, phoneNumber, center, cardNumber, promotionalEmails, hasCar, reference);
    }

    @Programmatic
    public User findOrCreate(
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
            @Parameter(optionality = Optionality.OPTIONAL) final String cardNumber,
            final boolean promotionalEmails,
            final Boolean hasCar,
            final String reference
    ) {
        User user = findByExactEmailAndCenter(email, center);
        if(user == null) {
            user = findByReference(reference);
        }
        if(user == null) {
            user = newUser(
                    enabled,
                    title,
                    firstName,
                    lastName,
                    email,
                    address,
                    zipcode,
                    city,
                    phoneNumber,
                    center,
                    cardNumber,
                    promotionalEmails,
                    hasCar,
                    reference);
        }
        return user;
    }

    @Programmatic
    public TranslatableString validateFindOrCreate(
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
            @Parameter(optionality = Optionality.OPTIONAL) final String cardNumber,
            final boolean promotionalEmails,
            final Boolean hasCar,
            final String reference
    ) {
        if(Strings.isNullOrEmpty(cardNumber)) {
            return null;
        }
        if(!cardRepository.cardNumberIsValid(cardNumber, center.getCode())) {
            return TranslatableString.tr("Card number {cardNumber} is invalid", "cardNumber", cardNumber);
        }
        if(!cardRepository.cardExists(cardNumber)) {
            return  TranslatableString.tr("Card with number {cardNumber} doesn't exist", "cardNumber", cardNumber);
        }
        return null;
    }

    @Programmatic
    public void delete(final User user) {
        repositoryService.remove(user);
    }

    @Inject RepositoryService repositoryService;
    @Inject CardRepository cardRepository;
    @Inject NumeratorRepository numeratorRepository;
}
