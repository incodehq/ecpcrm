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

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.eurocommercial.ecpcrm.dom.CardStatus;
import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;

@DomainService(
        nature = NatureOfService.DOMAIN,
        menuOrder = "100"
)
public class ApiService {
    public Result cardCheck(
            Center center,
            String cardNumber,
            String origin
    ) {
        if(Strings.isNullOrEmpty(cardNumber) || Strings.isNullOrEmpty(origin)) {
            return Result.error(302, "Invalid parameter");
        }

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
            //TODO: Check device type
            if("device type" != "app" && cardNumber.startsWith("3933")) {
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
            String cardNumber,
            String win,
            String desc
    ) {
        if(Strings.isNullOrEmpty(cardNumber)) {
            return Result.error(302, "Invalid Parameter");
        }

        Card card = cardRepository.findByExactNumber(cardNumber);
        if(card == null || card.getOwner() == null || card.getStatus() != CardStatus.ENABLED) {
            return Result.error(303, "Invalid card");
        }

        if(!card.getOwner().isEnabled()) {
            return Result.error(304, "Invalid user");
        }

        if(!card.canPlay()) {
            return Result.error(315, "Card has already played");
        }

        card.play();

        return Result.ok();
    }

    public Result cardRequest(
            Center center,
            String origin,
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            String children,
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
            Center center,
            String checkCode,
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            Boolean hasChildren,
            String nb_children,
            Boolean hasCar,
            String address,
            String zipcode,
            String city,
            String phoneNumber,
            boolean promotionalEmails
    ) {
        return Result.ok();
    }

    public Result websiteUserModify(
            Center center,
            String checkCode,
            String cardNumber,
            Title title,
            String firstName,
            String lastName,
            String email,
            LocalDate birthdate,
            Boolean hasChildren,
            String nb_children,
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
            Center center,
            String email,
            String checkCode
    ) {
        return Result.ok();
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

}