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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardrequest;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CardRequestRequestViewModel extends AbstractRequestViewModel {
    @Getter
    private final String origin;

    @Getter
    private final String hostess;

    private final String title;

    public Title getTitle() {
        return asTitle(title);
    }

    @Getter
    @SerializedName("first_name")
    private final String firstName;

    @Getter
    @SerializedName("last_name")
    private final String lastName;

    @Getter
    private final String email;

    private final String birthdate;

    public LocalDate getBirthdate() {
        return asLocalDate(birthdate);
    }

    @Getter
    private final String children;

    @SerializedName("nb_children")
    @Getter
    private final String nbChildren;

    @SerializedName("car")
    private final String hasCar;

    public Boolean getHasCar() {
        return asBoolean(hasCar);
    }

    @Getter
    private final String address;

    @Getter
    private final String zipcode;

    @Getter
    private final String city;

    @Getter
    @SerializedName("phone")
    private final String phoneNumber;

    @SerializedName("optin")
    private final String promotionalEmails;

    public Boolean getPromotionalEmails() {
        return asBoolean(promotionalEmails);
    }

    @Getter
    @SerializedName("check_item")
    private final String checkItem;

    private final String lost;

    public Boolean getLost() {
        return asBoolean(lost);
    }

    @Override
    public Result isValid(AuthenticationDevice device, User user) {

        user = userRepository.findByExactEmailAndCenter(getEmail(), device.getCenter());

        if (Strings.isNullOrEmpty(getOrigin()) || getTitle() == null || Strings.isNullOrEmpty(getFirstName()) ||
                Strings.isNullOrEmpty(getLastName()) || Strings.isNullOrEmpty(getEmail()) || Strings.isNullOrEmpty(getAddress()) ||
                Strings.isNullOrEmpty(getZipcode()) || Strings.isNullOrEmpty(getCity()) || getPromotionalEmails() == null || getLost() == null
        ) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter");
        }

        if (user != null) {
            if (getLost()) {
                if (!user.getCards().isEmpty()) {
                    user.getCards().first().setStatus(CardStatus.LOST);
                }
                if (!cardRequestRepository.findByUser(user).isEmpty()) {
                    return Result.error(Result.STATUS_DUPLICATE_REQUEST_FOR_REPLACEMENT_LOST_CARD, "Duplicate request for replacement of lost card");
                }
            } else {
                if (Strings.isNullOrEmpty(getCheckItem())) {
                    if (getFirstName().equals(user.getFirstName()) && getLastName().equals(user.getLastName())) {
                        return Result.error(Result.STATUS_EMAIL_ALREADY_EXISTS_VALID_CHECK_ASK_IF_LOST, "Email exists, valid check, ask if lost");
                    } else {
                        return Result.error(Result.STATUS_EMAIL_ALREADY_EXISTS, "Email already exists");
                    }
                } else {
                    if (getCheckItem().equals(user.getFirstName() + " " + user.getLastName())) {
                        return Result.error(Result.STATUS_EMAIL_ALREADY_EXISTS_VALID_CHECK_ASK_IF_LOST, "Email exists, valid check, ask if lost");
                    } else {
                        return Result.error(Result.STATUS_EMAIL_ALREADY_EXISTS_INVALID_CHECK, "Email already exists, invalid check");
                    }
                }
            }
        }

        return Result.ok();
    }

    @Inject
    CardRequestRepository cardRequestRepository;

    @Inject
    UserRepository userRepository;
}