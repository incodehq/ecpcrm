package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

import lombok.Data;

@Data(staticConstructor = "create")
public class WebsiteUserDetailResponseViewModel {
    private final String id;
    private final String name;
    private final String email;
    private final String title;
    private final String first_name;
    private final String last_name;
    private final String birthdate;
    private final String optin;
    private final String car;
    private final List<String> boutiques;
    private final String address;
    private final String zipcode;
    private final String city;
    private final String full_address;
    private final String phone;
    private final String haschildren;
    private final String nb_children;
    private final List<ChildViewModel> children;
    private final List<CardViewModel> cards;
    private final String request;

    public static WebsiteUserDetailResponseViewModel fromUser(final User user) {
        List<ChildViewModel> userChildren = user.getChildren().stream()
                .map(ChildViewModel::fromChild)
                .collect(Collectors.toList());
        List<CardViewModel> userCards = user.getCards().stream()
                .map(CardViewModel::fromCard)
                .collect(Collectors.toList());
        List<String> boutiques = new ArrayList<>();
        String requestStatus = user.getOpenCardRequest() != null ? "new" : null;

        return WebsiteUserDetailResponseViewModel.create(
                user.getReference(),
                user.getEmail(),
                user.getEmail(),
                user.getTitle().toString().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                ApiService.asString(user.getBirthDate()),
                ApiService.asString(user.isPromotionalEmails()),
                ApiService.asString(user.getHasCar()),
                boutiques,
                user.getAddress(),
                user.getZipcode(),
                user.getCity(),
                user.getAddress() + " - " + user.getZipcode() + " - " + user.getCity(),
                user.getPhoneNumber(),
                ApiService.asString(userChildren.size() != 0),
                ApiService.asString(userChildren.size()),
                userChildren,
                userCards,
                requestStatus
        );
    }
}
