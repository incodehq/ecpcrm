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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class WebsiteUserCreateRequestViewModel extends AbstractRequestViewModel {

    private final String user_id;
    private final String number;

    @Getter
    @SerializedName("center_id")
    private final String centerId;

    @Getter
    @SerializedName("check_code")
    private final String checkCode;

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
    public LocalDate getBirthdate (){
        return asLocalDate(birthdate);
    }

    @Getter
    private final String children;

    @SerializedName("nb_children")
    @Getter
    private final String nbChildren;

    @SerializedName("car")
    private final String hasCar;

    public boolean hasCar(){
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

    public boolean getPromotionalEmails(){
        return asDeterministicBoolean(promotionalEmails);
    }

    @Override
    public Result isValid(AuthenticationDevice device, User user) {
        if (Strings.isNullOrEmpty(getCenterId()) || Strings.isNullOrEmpty(getCheckCode()) || getTitle() == null ||
                Strings.isNullOrEmpty(getFirstName()) || Strings.isNullOrEmpty(getLastName()) || Strings.isNullOrEmpty(getEmail())
        ) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter");
        }

        if (!getCheckCode().equals(ApiService.computeCheckCode(getEmail()))) {
            return Result.error(Result.STATUS_INCORRECT_CHECK_CODE, "Incorrect check code");
        }

        Center center = device.getCenter();

        if (userRepository.findByExactEmailAndCenter(getEmail(), center) != null) {
            return Result.error(Result.STATUS_EMAIL_ALREADY_EXISTS, "Email already exists");
        }

        return Result.ok();
    }

    @Inject
    UserRepository userRepository;
}
