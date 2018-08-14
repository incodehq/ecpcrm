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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websitecardrequest;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDate;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class WebsiteCardRequestRequestViewModel {
    @Getter
    private final String origin;

    @Getter
    @SerializedName("center_id")
    private final String centerId;

    @Getter
    private final Title title;

    @Getter
    @SerializedName("first_name")
    private final String firstName;

    @Getter
    @SerializedName("last_name")
    private final String lastName;

    @Getter
    private final String email;

    @Getter
    private final String passwword;

    @Getter
    private final LocalDate birthdate;

    @Getter
    private final String children;

    @SerializedName("nb_children")
    @Getter
    private final String nbChildren;

    @SerializedName("car")
    @Getter
    private final Boolean hasCar;

    @Getter
    private final String address;

    @Getter
    private final String zipcode;

    @Getter
    private final String city;

    @Getter
    @SerializedName("phone")
    private final String phoneNumber;

    @Getter
    @SerializedName("optin")
    private final Boolean promotionalEmails;

    @Getter
    @SerializedName("check_code")
    private final String checkCode;

    @Getter
    private final String boutiques;
}
