package org.incode.eurocommercial.ecpcrm.app.services.api.vm.websiteuserdetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.incode.eurocommercial.ecpcrm.dom.user.User;

import lombok.Data;
import static org.incode.eurocommercial.ecpcrm.app.services.api.ApiService.asString;

@Data(staticConstructor = "create")
public class WebsiteUserDetailResponseViewModel {
    private final String id;
    private final String title;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String address;
    private final String zipcode;
    private final String city;
    private final String full_address;
    private final String phoneNumber;
    private final String birthDate;
    private final String center;
    private final String enabled;
    private final String optin;
    private final String car;
    private final List<String> boutiques;
    private final String haschildren;
    private final String nb_children;
    private final List<ChildViewModel> children;
    private final List<CardViewModel> cards;
    private final List<ChildCareViewModel> child_cares;

    public static WebsiteUserDetailResponseViewModel fromUser(final User user) {
        List<ChildViewModel> userChildren = user.getChildren().stream()
                .map(ChildViewModel::fromChild)
                .collect(Collectors.toList());
        List<CardViewModel> userCards = user.getCards().stream()
                .map(CardViewModel::fromCard)
                .collect(Collectors.toList());
        List<ChildCareViewModel> userChildCares = user.getChildren().stream()
                .map(ChildCareViewModel::fromChild)
                .collect(Collectors.toList());
        List<String> boutiques = new ArrayList<>();

        return WebsiteUserDetailResponseViewModel.create(
                user.getReference(),
                user.getTitle().toString().toLowerCase(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress(),
                user.getZipcode(),
                user.getCity(),
                user.getAddress() + " - " + user.getZipcode() + " - " + user.getCity(),
                user.getPhoneNumber(),
                asString(user.getBirthDate()),
                user.getCenter().title(),
                asString(user.isEnabled()),
                asString(user.isPromotionalEmails()),
                asString(user.getHasCar()),
                boutiques,
                asString(userChildren.size() != 0),
                asString(userChildren.size()),
                userChildren,
                userCards,
                userChildCares
        );
    }
}
