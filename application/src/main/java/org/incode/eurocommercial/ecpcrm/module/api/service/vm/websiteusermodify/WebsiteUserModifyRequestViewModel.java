/*
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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusermodify;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.joda.time.LocalDate;

import javax.inject.Inject;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class WebsiteUserModifyRequestViewModel extends AbstractRequestViewModel {
    @Getter
    @SerializedName("check_code")
    private final String checkCode;

    @Getter
    @SerializedName("card")
    private final String cardNumber;

    @Getter
    private final String email;

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

    private final String birthdate;
    public LocalDate getBirthdate(){
        return asLocalDate(birthdate);
    }

    @Getter
    private final String children;

    @SerializedName("nb_children")
    @Getter
    private final String nbChildren;

    @SerializedName("car")
    private final String hasCar;
    public Boolean getHasCar(){
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
    public Boolean getPromotionalEmails(){
        return asBoolean(promotionalEmails);
    }

    @Override
    public Result isValid(AuthenticationDevice device, User user) {
        if (Strings.isNullOrEmpty(getCheckCode()) || Strings.isNullOrEmpty(getEmail()) || getTitle() == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter");
        }

        Center center = device.getCenter();

        if (user == null) {
            return Result.error(Result.STATUS_INVALID_USER, "Invalid user");
        }

        user.setTitle(getTitle());

        if (!Strings.isNullOrEmpty(getFirstName())) {
            user.setFirstName(getFirstName());
        }

        if (!Strings.isNullOrEmpty(getLastName())) {
            user.setLastName(getLastName());
        }

        if (getBirthdate() != null) {
            user.setBirthDate(getBirthdate());
        }

        if (hasCar != null) {
            user.setHasCar(getHasCar());
        }

        if (!Strings.isNullOrEmpty(getAddress())) {
            user.setAddress(getAddress());
        }

        if (!Strings.isNullOrEmpty(getZipcode())) {
            user.setZipcode(getZipcode());
        }

        if (!Strings.isNullOrEmpty(getCity())) {
            user.setCity(getCity());
        }

        if (!Strings.isNullOrEmpty(getPhoneNumber())) {
            user.setPhoneNumber(getPhoneNumber());
        }

        if (promotionalEmails != null && getPromotionalEmails() != user.isPromotionalEmails()) {
            if (getPromotionalEmails()) {
                wrapperFactory.wrap(user).subscribeToPromotionalEmails();
            } else {
                wrapperFactory.wrap(user).unsubscribeFromPromotionalEmails();
            }
        }

        return Result.ok();

    }

    @Inject
    private WrapperFactory wrapperFactory;
}
