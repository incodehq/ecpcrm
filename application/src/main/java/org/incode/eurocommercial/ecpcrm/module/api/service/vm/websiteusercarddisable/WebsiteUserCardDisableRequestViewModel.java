package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteusercarddisable;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractRequestViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.Card;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardRepository;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.card.CardStatus;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

import javax.inject.Inject;

@NoArgsConstructor(force = true)
@AllArgsConstructor
public class WebsiteUserCardDisableRequestViewModel extends AbstractRequestViewModel {
    @Getter
    @SerializedName("check_code")
    private final String checkCode;

    @Getter
    private final String email;

    @Override
    public Result isValid(AuthenticationDevice device, User user){
        if (Strings.isNullOrEmpty(getCheckCode()) || Strings.isNullOrEmpty(getEmail())) {
            return Result.error(Result.STATUS_INVALID_PARAMETER, "Invalid parameter");
        }

        user = userRepository.findByExactEmailAndCenter(getEmail(), device.getCenter());
        if(user == null){
            return Result.error(Result.STATUS_INVALID_USER, "Invalid user");
        }

        if (!getCheckCode().equals(ApiService.computeCheckCode(getEmail()))) {
            return Result.error(Result.STATUS_INCORRECT_CHECK_CODE, "Incorrect check code");
        }

        for (Card card : user.getCards()){
            card.setStatus(CardStatus.DISABLED);
        }

        return Result.ok();
    }

    @Inject
    UserRepository userRepository;

    @Inject
    CardRepository cardRepository;
}
