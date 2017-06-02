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

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.eurocommercial.ecpcrm.dom.Title;
import org.incode.eurocommercial.ecpcrm.dom.center.Center;

@DomainService(
        nature = NatureOfService.DOMAIN,
        menuOrder = "100"
)
public class ApiService {
    public Result cardCheck(String cardNumber, String origin) {
        return Result.ok();
    }

    public Result cardGame(String cardNumber, String win, String desc) {
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

    public Result userCreate(
            Title title,
            String firstName,
            String lastName,
            String email,
            String address,
            String zipcode,
            String city,
            Center center,
            String cardNumber,
            boolean promotionalEmails
    ) {
        return Result.ok();
    }

    public Result userUpdate(
            Title title,
            String firstName,
            String lastName,
            String email,
            String address,
            String zipcode,
            String city,
            Center center,
            String cardNumber,
            boolean promotionalEmails
    ) {
        return Result.ok();
    }

    public Result userDetail(String reference) {
        return Result.ok();
    }

}