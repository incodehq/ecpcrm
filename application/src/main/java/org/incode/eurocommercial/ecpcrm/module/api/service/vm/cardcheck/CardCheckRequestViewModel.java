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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardcheck;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceType;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

import javax.inject.Inject;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CardCheckRequestViewModel  extends AbstractRequestViewModel {
    @Getter
    @SerializedName("card")
    private final String cardNumber;

    @Getter
    private final String origin;

    @Override
    public Result isValid(AuthenticationDevice device, User user) {
        if (Strings.isNullOrEmpty(getCardNumber()) || Strings.isNullOrEmpty(getOrigin())) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter");
        }
        
        Center center = device.getCenter();
        Card card = cardRepository.findByExactNumber(getCardNumber());

        if (card != null) {
            if (card.getStatus() == CardStatus.TOCHANGE) {
                return Result.error(Result.STATUS_OUTDATED_CARD, "Outdated card");
            }
            if (card.getStatus() != CardStatus.ENABLED) {
                return Result.error(Result.STATUS_INVALID_CARD, "Invalid card");
            }
            if (card.getCenter() != center) {
                return Result.error(Result.STATUS_CARD_CENTER_NOT_EQUAL_TO_DEVICE_CENTER, "Card center is not equal to device center");
            }
            if (card.getOwner() == null || !card.getOwner().isEnabled()) {
                return Result.error(Result.STATUS_INVALID_USER, "Invalid user");
            }
        }
        else{
            if (device.getType() != AuthenticationDeviceType.APP && cardNumber.startsWith("3922")) {
                return Result.error(Result.STATUS_OUTDATED_CARD, "Outdated card");
            }
            if (!cardRepository.cardNumberIsValid(cardNumber)) {
                return Result.error(Result.STATUS_INVALID_CARD, "Invalid card number");
            }

            //TODO: In the old code, a new blank user is created for a nonexisting card, why?
            return Result.error(Result.STATUS_UNABLE_TO_BIND_USER, "Unable to bind user to card");
        }

        return Result.ok(new CardCheckResponseViewModel(card));
    }

    @Inject CardRepository cardRepository;
}
