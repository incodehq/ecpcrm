package org.incode.eurocommercial.ecpcrm.module.api.service.vm.websiteuserdetail;

import lombok.Getter;
import lombok.Setter;
import org.incode.eurocommercial.ecpcrm.module.api.service.vm.AbstractBaseViewModel;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebsiteUserDetailResponseViewModel extends AbstractBaseViewModel {
    @Getter @Setter
    String id;
    @Getter @Setter
    String name;
    @Getter @Setter
    String email;
    @Getter @Setter
    String title;
    @Getter @Setter
    String first_name;
    @Getter @Setter
    String last_name;
    @Getter @Setter
    String birthdate;
    @Getter @Setter
    String optin;
    @Getter @Setter
    String car;
    @Getter @Setter
    List<String> boutiques;
    @Getter @Setter
    String address;
    @Getter @Setter
    String zipcode;
    @Getter @Setter
    String city;
    @Getter @Setter
    String full_address;
    @Getter @Setter
    String phone;
    @Getter @Setter
    String haschildren;
    @Getter @Setter
    String nb_children;
    @Getter @Setter
    List<ChildViewModel> children;
    @Getter @Setter
    List<CardViewModel> cards;
    @Getter @Setter
    String open_card_request;
    @Getter @Setter
    String request;

    public WebsiteUserDetailResponseViewModel(User user){
        setId(user.getReference());
        setEmail(user.getEmail());
        setName(user.getEmail());
        setTitle(user.getTitle().toString().toLowerCase());
        setFirst_name(user.getFirstName());
        setLast_name(user.getLastName());
        setBirthdate(asString(user.getBirthDate()));
        setOptin(asString(user.isPromotionalEmails()));
        setCar(asString(user.getHasCar()));
        setAddress(user.getAddress());
        setZipcode(user.getZipcode());
        setCity(user.getCity());
        setFull_address(user.getAddress() + " - " + user.getZipcode() + " - " + user.getCity());
        setPhone(user.getPhoneNumber());

        setChildren(user.getChildren()
                .stream()
                .map(child -> {ChildViewModel cvm = new ChildViewModel(child);
                    return cvm;})
                .collect(Collectors.toList()));

        setCards(user.getCards()
                .stream()
                .map(card -> {CardViewModel vm = new CardViewModel(card);
                    return vm;})
                .collect(Collectors.toList()));

        setBoutiques(new ArrayList<>());
        setOpen_card_request(user.getOpenCardRequest() == null ? "false" : "true");
    }
}
