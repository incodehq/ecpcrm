package org.incode.eurocommercial.ecpcrm.module.api.menu;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.UserRepository;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "application.AdminMenu"
)
@DomainServiceLayout(
        named = "Administration",
        menuOrder = "999",
        menuBar = DomainServiceLayout.MenuBar.SECONDARY
)
public class AdminMenu {

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    public List<User> resyncCenterUsers(final Center center){
        return userRepository.resyncMailchimp(center);
    }

    @Inject private UserRepository userRepository;
}

