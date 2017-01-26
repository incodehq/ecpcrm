package org.incode.eurocommercial.ecpcrm.restapi;

import java.util.List;

import lombok.Data;

@Data(staticConstructor = "create")
public class UserViewModel {
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
//    TODO: Fully implement this viewmodel
//    private final String car;
//    private final SortedSet<String> boutiques;
    private final String haschildren;
    private final String nb_children;
    private final List<ChildViewModel> children;
    private final List<CardViewModel> cards;
//    private final SortedSet<ChildCareViewModel> child_cares;
}
