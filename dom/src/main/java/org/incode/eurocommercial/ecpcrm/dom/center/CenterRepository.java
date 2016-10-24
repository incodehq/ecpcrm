package org.incode.eurocommercial.ecpcrm.dom.center;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Center.class)
public class CenterRepository {


    @Programmatic
    public Center newCenter(final String reference, final String name){
        Center center = repositoryService.instantiate(Center.class);
        center.setReference(reference);
        center.setName(name);
        repositoryService.persist(center);
        return center;
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
    public Center findByRreference(
            final String reference
    ) {
        return repositoryService.uniqueMatch(
                new QueryDefault<>(
                        Center.class,
                        "findByReference",
                        "reference", reference));
    }

    @Inject
    private RepositoryService repositoryService;

}
