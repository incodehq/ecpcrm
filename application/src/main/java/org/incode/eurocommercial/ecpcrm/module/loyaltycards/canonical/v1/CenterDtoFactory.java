package org.incode.eurocommercial.ecpcrm.module.loyaltycards.canonical.v1;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.incode.eurocommercial.ecpcrm.canonical.center.v1.CenterDto;
import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center.Center;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class CenterDtoFactory extends DtoFactoryAbstract {
    @Programmatic
    public CenterDto newDto(final Center center) {
        final CenterDto dto = new CenterDto();

        dto.setAtPath(center.getAtPath());
        dto.setId(center.getId());
        dto.setCode(center.getCode());
        dto.setName(center.getName());
        dto.setMailchimpListId(center.getMailchimpListId());

        return dto;
    }
}
