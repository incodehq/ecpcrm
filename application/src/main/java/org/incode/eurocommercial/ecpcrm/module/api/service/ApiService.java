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
package org.incode.eurocommercial.ecpcrm.module.api.service;

import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck.CardCheckResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate.WebsiteUserCreateResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail.WebsiteUserDetailResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequest;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestType;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

@DomainService(
        nature = NatureOfService.DOMAIN,
        menuOrder = "100"
)
public class ApiService {

    public String computeCheckCode(String email) {
        String secret = "320498FJEZR458FNBLA895HFLR48G";
        String toBeHashed = email + secret;
        return DigestUtils.md5Hex(toBeHashed);
    }

    public Result cardCheck(
            String deviceName,
            String deviceSecret,
            String cardNumber,
            String origin
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(cardNumber) || Strings.isNullOrEmpty(origin)) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();

        Card card = cardRepository.findByExactNumber(cardNumber);

        if (card != null) {
            if (card.getStatus() == CardStatus.TOCHANGE) {
                return Result.error(319, "Outdated card");
            }
            if (card.getStatus() != CardStatus.ENABLED) {
                return Result.error(303, "Invalid card");
            }
            if (card.getCenter() != center) {
                return Result.error(317, "Card center is not equal to device center");
            }
            if (card.getOwner() == null || !card.getOwner().isEnabled()) {
                return Result.error(304, "Invalid user");
            }

            return Result.ok(CardCheckResponseViewModel.fromCard(card));

        } else {
            if (device.getType() != AuthenticationDeviceType.APP && cardNumber.startsWith("3922")) {
                return Result.error(319, "Outdated card");
            }
            if (!cardRepository.cardNumberIsValid(cardNumber)) {
                return Result.error(312, "Invalid card number");
            }

            //TODO: In the old code, a new blank user is created for a nonexisting card, why?
            return Result.error(314, "Unable to bind user to card");
        }
    }

    public Result cardGame(
            String deviceName,
            String deviceSecret,
            String cardNumber,
            Boolean win,
            String desc
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(cardNumber)) {
            return Result.error(302, "Invalid Parameter");
        }

        Center center = device.getCenter();
        Card card = cardRepository.findByExactNumber(cardNumber);

        if (card == null || card.getOwner() == null || card.getStatus() != CardStatus.ENABLED || card.getCenter() != center) {
            return Result.error(303, "Invalid card");
        }

        if (!card.getOwner().isEnabled()) {
            return Result.error(304, "Invalid user");
        }

        if (!card.canPlay()) {
            return Result.error(315, "Card has already played");
        }

        card.play(win);

        return Result.ok();
    }

    public Result cardRequest(
            String deviceName,
            String deviceSecret,
            String origin,
            String hostess,
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            String children,
            String nbChildren,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            Boolean promotionalEmails,
            String checkItem,
            Boolean lost
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(origin) || title == null || Strings.isNullOrEmpty(firstName) ||
            Strings.isNullOrEmpty(lastName) || Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(address) ||
            Strings.isNullOrEmpty(zipcode) || Strings.isNullOrEmpty(city) || promotionalEmails == null
        ) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();
        User user = userRepository.findByExactEmailAndCenter(email, center);

        if (user != null) {
            if (lost) {
                if (!user.getCards().isEmpty()) {
                    user.getCards().first().setStatus(CardStatus.LOST);
                }
                List<CardRequest> existingRequests = cardRequestRepository.findByUser(user);
                if (existingRequests.isEmpty()) {
                    cardRequestRepository.findOrCreate(user, CardRequestType.PICK_UP_IN_CENTER);
                } else {
                    return Result.error(307, "TODO");
                }
            } else {
                if (Strings.isNullOrEmpty(checkItem)) {
                    if (firstName.equals(user.getFirstName()) && lastName.equals(user.getLastName())) {
                        return Result.error(318, "Email exists, valid check, ask if lost");
                    } else {
                        return Result.error(305, "Email already exists");
                    }
                } else {
                    if (checkItem.equals(user.getFirstName() + " " + user.getLastName())) {
                        return Result.error(318, "Email exists, valid check, ask if lost");
                    } else {
                        return Result.error(306, "Email already exists, invalid check");
                    }
                }
            }
        } else {
            user = userRepository.findOrCreate(
                    true,
                    title,
                    firstName,
                    lastName,
                    email,
                    address,
                    zipcode,
                    city,
                    phoneNumber,
                    center,
                    null,
                    promotionalEmails,
                    hasCar,
                    null
            );
            cardRequestRepository.findOrCreate(user, CardRequestType.PICK_UP_IN_CENTER);
        }

        return Result.ok();
    }

    public Result websiteCardRequest(
            String deviceName,
            String deviceSecret,
            String origin,
            String centerId,
            Title title,
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate birthdate,
            String children,
            String nbChildren,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            Boolean promotionalEmails,
            String checkCode,
            String boutiques
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(centerId) || title == null || Strings.isNullOrEmpty(firstName) ||
            Strings.isNullOrEmpty(lastName) || Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(address) ||
            Strings.isNullOrEmpty(zipcode)  || Strings.isNullOrEmpty(city) || Strings.isNullOrEmpty(checkCode)
        ) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();
        User user = userRepository.findByExactEmailAndCenter(email, center);

        if (user == null) {
            return Result.error(304, "Invalid user");
        }

        if (!checkCode.equals(this.computeCheckCode(email))) {
            return Result.error(402, "Incorrect check code");
        }

        cardRequestRepository.findOrCreate(user, CardRequestType.PICK_UP_IN_CENTER);

        return Result.ok();
    }

    public Result websiteUserCreate(
            String deviceName,
            String deviceSecret,
            String centerId,
            String checkCode,
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            String children,
            String nbChildren,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            Boolean promotionalEmails
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(centerId) || Strings.isNullOrEmpty(checkCode) || title == null ||
            Strings.isNullOrEmpty(firstName) || Strings.isNullOrEmpty(lastName) || Strings.isNullOrEmpty(email)
        ) {
            return Result.error(302, "Invalid parameter");
        }

        if (!checkCode.equals(this.computeCheckCode(email))) {
            return Result.error(402, "Incorrect check code");
        }

        Center center = device.getCenter();

        if (userRepository.findByExactEmailAndCenter(email, center) != null) {
            return Result.error(305, "Email already exists");
        }

        User user = userRepository.findOrCreate(
                true, title, firstName, lastName, email, address, zipcode, city,
                phoneNumber, center, null, promotionalEmails, hasCar, null
        );
        user.setBirthDate(birthdate);

        Card card = cardRepository.newFakeCard(CardStatus.ENABLED, center);
        user.newCard(card.getNumber());

        return Result.ok(WebsiteUserCreateResponseViewModel.fromUser(user));
    }

    public Result websiteUserModify(
            String deviceName,
            String deviceSecret,
            String checkCode,
            String cardNumber,
            String email,
            Title title,
            String firstName,
            String lastName,
            LocalDate birthdate,
            String children,
            String nbChildren,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            Boolean promotionalEmails
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(checkCode) || Strings.isNullOrEmpty(email) || title == null) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();
        User user = userRepository.findByExactEmailAndCenter(email, center);

        if (user == null) {
            return Result.error(304, "Invalid user");
        }

        user.setTitle(title);

        if (!Strings.isNullOrEmpty(firstName)) {
            user.setFirstName(firstName);
        }

        if (!Strings.isNullOrEmpty(lastName)) {
            user.setLastName(lastName);
        }

        if (birthdate != null) {
            user.setBirthDate(birthdate);
        }

        if (hasCar != null) {
            user.setHasCar(hasCar);
        }

        if (!Strings.isNullOrEmpty(address)) {
            user.setAddress(address);
        }

        if (!Strings.isNullOrEmpty(zipcode)) {
            user.setZipcode(zipcode);
        }

        if (!Strings.isNullOrEmpty(city)) {
            user.setCity(city);
        }

        if (!Strings.isNullOrEmpty(phoneNumber)) {
            user.setPhoneNumber(phoneNumber);
        }

        if (promotionalEmails != null) {
            if (promotionalEmails) {
                user.subscribeToPromotionalEmails();
            } else {
                user.unsubscribeFromPromotionalEmails();
            }
        }

        return Result.ok();
    }

    public Result websiteUserDetail(
            String deviceName,
            String deviceSecret,
            String email,
            String checkCode
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if (Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(checkCode)) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();

        User user = userRepository.findByExactEmailAndCenter(email, center);

        if (user == null) {
            return Result.error(304, "Invalid user");
        }

        if (!checkCode.equals(this.computeCheckCode(email))) {
            return Result.error(402, "Incorrect check code");
        }

        return Result.ok(WebsiteUserDetailResponseViewModel.fromUser(user));
    }

    public static Boolean asBoolean(final int i) {
        return i > 0;
    }
    public static Boolean asBoolean(final String s) {
        return s == null ? null : s.toUpperCase().equals("TRUE");
    }
    public static Title asTitle(final String title) {
        return title == null ? null : Title.valueOf(title.toUpperCase());
    }
    public static LocalDate asLocalDate(final String localDate) {
        return DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(localDate);
    }


    public static String asString(final int i) {
        return "" + i;
    }
    public static String asString(final boolean bool) {
        return bool ? "true" : "false";
    }
    public static String asString(final LocalDate localDate) {
        return localDate == null ? null : localDate.toString("dd/MM/yyyy");
    }
    public static String asDateString(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toString("dd/MM/yyyy");
    }

    @Inject private CardRepository cardRepository;
    @Inject private CardRequestRepository cardRequestRepository;
    @Inject private UserRepository userRepository;
    @Inject private AuthenticationDeviceRepository authenticationDeviceRepository;
}