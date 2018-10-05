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
package org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardgame;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
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
public class CardGameRequestViewModel extends AbstractRequestViewModel {

    @Getter
    @SerializedName("card")
    private final String cardNumber;


    private final String win;

    public Boolean getWin() {
        return asBoolean(win);
    }

    @Getter
    private final String desc;


    public Result isValid(AuthenticationDevice device, User user){
        if (Strings.isNullOrEmpty(getCardNumber())) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid Parameter");
        }

        Center center = device.getCenter();
        Card card = cardRepository.findByExactNumber(getCardNumber());

        if (card == null || card.getOwner() == null || card.getStatus() != CardStatus.ENABLED || card.getCenter() != center) {
            return Result.error(Result.STATUS_INVALID_CARD, "Invalid card");
        }

        if (!card.getOwner().isEnabled()) {
            return Result.error(Result.STATUS_INVALID_USER, "Invalid user");
        }

        if (!card.canPlay()) {
            return Result.error(Result.STATUS_CARD_ALREADY_PLAYED, "Card has already played");
        }

        return Result.ok();
    }


    @Inject
    CardRepository cardRepository;


}
