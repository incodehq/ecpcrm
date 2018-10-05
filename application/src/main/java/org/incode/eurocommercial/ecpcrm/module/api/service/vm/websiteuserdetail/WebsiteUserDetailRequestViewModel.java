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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

import static org.incode.eurocommercial.ecpcrm.module.api.service.ApiService.computeCheckCode;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class WebsiteUserDetailRequestViewModel extends AbstractRequestViewModel {
    @Getter
    private final String email;

    @Getter
    @SerializedName("check_code")
    private final String checkCode;


    @Override
    public Result isValid(AuthenticationDevice device, User user) {
        if (Strings.isNullOrEmpty(getEmail()) || Strings.isNullOrEmpty(getCheckCode())) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter");
        }

        Center center = device.getCenter();

        if (user == null) {
            return Result.error(Result.STATUS_INVALID_USER, "Invalid user");
        }

        if (!getCheckCode().equals(computeCheckCode(getEmail()))) {
            return Result.error(Result.STATUS_INCORRECT_CHECK_CODE, "Incorrect check code");
        }

        return  Result.ok();
    }
}
