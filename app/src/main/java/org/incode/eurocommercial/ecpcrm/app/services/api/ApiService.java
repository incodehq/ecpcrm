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
package org.incode.eurocommercial.ecpcrm.app.services.api;

import javax.inject.Inject;

import com.google.common.base.Strings;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.eurocommercial.ecpcrm.app.services.api.vm.cardcheck.CardCheckResponseViewModel;
import org.incode.eurocommercial.ecpcrm.app.services.api.vm.websiteusercreate.WebsiteUserCreateResponseViewModel;
import org.incode.eurocommercial.ecpcrm.app.services.api.vm.websiteuserdetail.WebsiteUserDetailResponseViewModel;
import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

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

        if(device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if(Strings.isNullOrEmpty(cardNumber) || Strings.isNullOrEmpty(origin)) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();

        Card card = cardRepository.findByExactNumber(cardNumber);

        if(card != null) {
            if(card.getStatus() == CardStatus.TOCHANGE) {
                return Result.error(319, "Outdated card");
            }
            if(card.getStatus() != CardStatus.ENABLED) {
                return Result.error(303, "Invalid card");
            }
            if(card.getCenter() != center) {
                return Result.error(317, "Card center is not equal to device center");
            }
            if(card.getOwner() == null || !card.getOwner().isEnabled()) {
                return Result.error(304, "Invalid user");
            }
        } else {
            if(device.getType() != AuthenticationDeviceType.APP && cardNumber.startsWith("3933")) {
                return Result.error(319, "Outdated card");
            }
            if(!cardRepository.cardNumberIsValid(cardNumber)) {
                return Result.error(312, "Invalid card number");
            }

            //TODO: In the old code, a new blank user is created for a nonexisting card, why?
            return Result.error(314, "Unable to bind user to card");
        }

        return Result.ok(CardCheckResponseViewModel.fromCard(card));
    }

    public Result cardGame(
            String deviceName,
            String deviceSecret,
            String cardNumber,
            Boolean win,
            String desc
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if(device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if(Strings.isNullOrEmpty(cardNumber)) {
            return Result.error(302, "Invalid Parameter");
        }

        Center center = device.getCenter();
        Card card = cardRepository.findByExactNumber(cardNumber);

        if(card == null || card.getOwner() == null || card.getStatus() != CardStatus.ENABLED || card.getCenter() != center) {
            return Result.error(303, "Invalid card");
        }

        if(!card.getOwner().isEnabled()) {
            return Result.error(304, "Invalid user");
        }

        if(!card.canPlay()) {
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
            boolean promotionalEmails,
            String checkItem,
            boolean lost
    ) {
        return Result.ok();
    }

    public Result websiteUserCreate(
            String deviceName,
            String deviceSecret,
            String checkCode,
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            Boolean hasChildren,
            String nbChildren,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            boolean promotionalEmails
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if(device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if(Strings.isNullOrEmpty(checkCode) || title == null || Strings.isNullOrEmpty(firstName) ||
           Strings.isNullOrEmpty(lastName) || Strings.isNullOrEmpty(email)) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();

        if(userRepository.findByExactEmailAndCenter(email, center) != null) {
            return Result.error(304, "Invalid user");
        }

        if(!checkCode.equals(this.computeCheckCode(email))) {
            return Result.error(402, "Incorrect check code");
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
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            Boolean hasChildren,
            String nbChildren,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            boolean promotionalEmails
    ) {
        return Result.ok();
    }

    public Result websiteUserDetail(
            String deviceName,
            String deviceSecret,
            String email,
            String checkCode
    ) {
        AuthenticationDevice device = authenticationDeviceRepository.findByNameAndSecret(deviceName, deviceSecret);

        if(device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        if(Strings.isNullOrEmpty(email) || Strings.isNullOrEmpty(checkCode)) {
            return Result.error(302, "Invalid parameter");
        }

        Center center = device.getCenter();

        User user = userRepository.findByExactEmailAndCenter(email, center);

        if(user == null) {
            return Result.error(304, "Invalid user");
        }

        if(!checkCode.equals(this.computeCheckCode(email))) {
            return Result.error(402, "Incorrect check code");
        }

        return Result.ok(WebsiteUserDetailResponseViewModel.fromUser(user));
    }

    public static boolean asBoolean(final int i) {
        return i > 0;
    }
    public static boolean asBoolean(final String s) {
        if(s.toUpperCase().equals("TRUE")) {
            return true;
        }
        return false;
    }
    public static String asString(final int i) {
        return "" + i;
    }
    public static String asString(final boolean bool) {
        return bool ? "true" : "false";
    }
    public static String asString(final LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }

    @Inject CardRepository cardRepository;
    @Inject UserRepository userRepository;
    @Inject AuthenticationDeviceRepository authenticationDeviceRepository;
}