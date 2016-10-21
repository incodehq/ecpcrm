package org.incode.eurocommercial.ecpcrm.dom.center;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Center.class)
public class CenterRepository {


    public Center newCenter(final String reference, final String name){
        Center center = factoryService.instantiate(Center.class);
        center.setReference(reference);
        center.setName(name);
        repositoryService.persist(center);
        return center;
    }

    @Inject
    private DomainObjectContainer container;


    @Inject
    private RepositoryService repositoryService;

    @Inject
    private FactoryService factoryService;
}
