package org.incode.eurocommercial.ecpcrm.module.loyaltycards.canonical.v1;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.eurocommercial.ecpcrm.canonical.user.v1.UserDto;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.Title;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.user.User;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class UserDtoFactory extends DtoFactoryAbstract {
    @Programmatic
    public UserDto newDto(final User user) {
        final UserDto dto = new UserDto();

        dto.setAtPath(user.getAtPath());
        dto.setReference(user.getReference());
        dto.setTitle(toDto(user.getTitle()));
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setZipcode(user.getZipcode());
        dto.setCity(user.getCity());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setBirthDate(asXMLGregorianCalendar(user.getBirthDate()));
        dto.setCenter(mappingHelper.oidDtoFor(user.getCenter()));
        dto.setEnabled(user.isEnabled());
        dto.setHasCar(user.getHasCar());

        user.getChildren().forEach(child -> dto.getChildren().add(mappingHelper.oidDtoFor(child)));
        user.getCards().forEach(card -> dto.getCards().add(mappingHelper.oidDtoFor(card)));

        return dto;
    }

    private org.incode.eurocommercial.ecpcrm.canonical.user.v1.Title toDto(final Title title) {
        return org.incode.eurocommercial.ecpcrm.canonical.user.v1.Title.fromValue(title.name());
    }

}
