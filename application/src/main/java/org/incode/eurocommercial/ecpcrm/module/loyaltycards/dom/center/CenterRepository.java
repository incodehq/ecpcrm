package org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.center;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;

import org.incode.eurocommercial.ecpcrm.module.loyaltycards.dom.numerator.NumeratorRepository;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Center.class
)
public class CenterRepository {

    @Programmatic
    public List<Center> listAll() {
        return repositoryService.allInstances(Center.class);
    }

    @Programmatic
    public Center findByExactName(
            final String name
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Center.class,
                        "findByExactName",
                        "name", name));
    }

    @Programmatic
    public List<Center> findByNameContains(
            final String name
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Center.class,
                        "findByNameContains",
                        "name", name));
    }

    @Programmatic
    public List<Center> findByMailchimpListId(
            final String mailchimpListId
    ) {
        QCenter center = QCenter.candidate();
        return isisJdoSupport.executeQuery(Center.class, center.mailchimpListId.eq(mailchimpListId));
    }

    @Programmatic
    public Center findByCode(
            final String code
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Center.class,
                        "findByCode",
                        "code", code));
    }

    @Programmatic
    private Center newCenter(final String code, final String name, final String id, final String mailchimpListId, final String contactEmail) {
        Center center = repositoryService.instantiate(Center.class);
        center.setCode(code);
        center.setId(id);
        center.setName(name);
        center.setNumerator(numeratorRepository.findOrCreate(
                name, "%d", Long.parseLong("2" + code + "000000000")));
        center.setAtPath("/FRA/" + code);
        center.setMailchimpListId(mailchimpListId);
        center.setContactEmail(contactEmail);
        repositoryService.persist(center);
        return center;
    }

    @Programmatic
    public Center findOrCreate(final String code, final String name, final String id, final String mailchimpListId, final String contactEmail) {
        Center center = findByCode(code);
        center = center != null ? center : findByExactName(name);
        center = center != null ? center : newCenter(code, name, id, mailchimpListId, contactEmail);
        return center;
    }

    @Inject private RepositoryService repositoryService;
    @Inject private NumeratorRepository numeratorRepository;
    @Inject private IsisJdoSupport isisJdoSupport;
}
