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
package org.incode.eurocommercial.ecpcrm.module.api.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck.CardCheckRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardgame.CardGameRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardrequest.CardRequestRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websitecardrequest.WebsiteCardRequestRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercarddisable.WebsiteUserCardDisableRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate.WebsiteUserCreateRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercreate.WebsiteUserCreateResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail.WebsiteUserDetailRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail.WebsiteUserDetailResponseViewModel;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusermodify.WebsiteUserModifyRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.request.CardRequestType;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.menu.UserMenu;

import javax.inject.Inject;

@DomainService(
        nature = NatureOfService.DOMAIN,
        menuOrder = "100"
)
public class ApiService {

    public static String computeCheckCode(String email) {
        String secret = "320498FJEZR458FNBLA895HFLR48G";
        String toBeHashed = email + secret;
        System.out.println(DigestUtils.md5Hex(toBeHashed));
        return DigestUtils.md5Hex(toBeHashed);
    }

    public Result cardCheck(AuthenticationDevice device, CardCheckRequestViewModel requestViewModel) {
        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(301, "Invalid device");
        }

        final Result validationResult = requestViewModel.isValid(device, null);
        return validationResult;
    }

    public Result cardGame(AuthenticationDevice device, CardGameRequestViewModel requestViewModel) {
        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        final Result validationResult = requestViewModel.isValid(device, null);
        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }

        Card card = cardRepository.findByExactNumber(requestViewModel.getCardNumber());
        card.play(requestViewModel.getWin());
        return Result.ok();
    }

    public Result cardRequest(AuthenticationDevice device, CardRequestRequestViewModel requestViewModel) {
        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        final Result validationResult = requestViewModel.isValid(device, null);
        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }

        User user = userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter());

        if (user == null) {
            user = wrapperFactory.wrap(userMenu).newUser(
                    true,
                    requestViewModel.getTitle(),
                    requestViewModel.getFirstName(),
                    requestViewModel.getLastName(),
                    requestViewModel.getEmail(),
                    requestViewModel.getBirthdate(),
                    requestViewModel.getAddress(),
                    requestViewModel.getZipcode(),
                    requestViewModel.getCity(),
                    requestViewModel.getPhoneNumber(),
                    device.getCenter(),
                    null,
                    requestViewModel.getPromotionalEmails(),
                    requestViewModel.getHasCar()
            );
        }

        cardRequestRepository.findOrCreate(user, CardRequestType.PICK_UP_IN_CENTER);

        return Result.ok();
    }

    public Result websiteCardRequest(AuthenticationDevice device, WebsiteCardRequestRequestViewModel requestViewModel){
        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        final Result validationResult = requestViewModel.isValid(device, userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter()));
        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }

        User user = userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter());

        cardRequestRepository.findOrCreate(user, CardRequestType.PICK_UP_IN_CENTER);

        return Result.ok();
    }

    public Result websiteUserCreate(AuthenticationDevice device, WebsiteUserCreateRequestViewModel requestViewModel) {
        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        final Result validationResult = requestViewModel.isValid(device, userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter()));
        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }

        User user = userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter());

        //TODO: handle Isis validation on email here
        if (user == null) {
            user = wrapperFactory.wrap(userMenu).newUser(
                    true,
                    requestViewModel.getTitle(),
                    requestViewModel.getFirstName(),
                    requestViewModel.getLastName(),
                    requestViewModel.getEmail(),
                    requestViewModel.getBirthdate(),
                    requestViewModel.getAddress(),
                    requestViewModel.getZipcode(),
                    requestViewModel.getCity(),
                    requestViewModel.getPhoneNumber(),
                    device.getCenter(),
                    null,
                    requestViewModel.getPromotionalEmails(),
                    requestViewModel.hasCar()
            );
        }

        return Result.ok(new WebsiteUserCreateResponseViewModel(user));
    }

    public Result websiteUserModify(AuthenticationDevice device, WebsiteUserModifyRequestViewModel requestViewModel)
    {
        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        User user = userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter());

        final Result validationResult = requestViewModel.isValid(device, user);
        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }

        return Result.ok();
    }

    public Result websiteUserDetail(AuthenticationDevice device,  WebsiteUserDetailRequestViewModel requestViewModel) {

        if (requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if (device == null || !device.isActive()) {
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        User user = userRepository.findByExactEmailAndCenter(requestViewModel.getEmail(), device.getCenter());

        final Result validationResult = requestViewModel.isValid(device, user);
        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }

        return Result.ok(new WebsiteUserDetailResponseViewModel(user));
    }

    public Result websiteUserCardDisable(AuthenticationDevice device, WebsiteUserCardDisableRequestViewModel requestViewModel){

        if(requestViewModel == null) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Failed to parse parameters");
        }

        if(device == null || !device.isActive()){
            return Result.error(Result.STATUS_INVALID_DEVICE, "Invalid device");
        }

        final Result validationResult = requestViewModel.isValid(device, null);

        if (validationResult.getStatus() != Result.STATUS_OK) {
            // validation failed
            return validationResult;
        }
        return validationResult;
    }

    @Inject
    private CardRepository cardRepository;
    @Inject
    private CardRequestRepository cardRequestRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private AuthenticationDeviceRepository authenticationDeviceRepository;
    @Inject
    private WrapperFactory wrapperFactory;
    @Inject
    private UserMenu userMenu;
}