package org.incode.eurocommercial.ecpcrm.restapi;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulMediaType;
import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
import org.apache.isis.viewer.restfulobjects.rendering.service.conneg.PrettyPrinting;
import org.apache.isis.viewer.restfulobjects.server.resources.ResourceAbstract;

import org.incode.eurocommercial.ecpcrm.dom.quick.QuickObject;
import org.incode.eurocommercial.ecpcrm.dom.quick.QuickObjectRepository;

@Path("/crm/api/6.0")
public class EcpCrmResource extends ResourceAbstract  {

    @Override protected void init(
            final RepresentationType representationType,
            final Where where,
            final RepresentationService.Intent intent) {
        super.init(representationType, where, intent);
        this.getServicesInjector().injectServicesInto(this);
    }


    @POST
    @Path("/card-check-unbound")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardCheckUnbound(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"domainType\": \"foo\", \"instanceId\": \"bar\" }"))
                .build();
    }


    private QuickObjectViewModel asVm(final QuickObject x) {
        return QuickObjectViewModel.create(x.getName(), x.getInteger(), asPattern(x.getLocalDate()));
    }

    private static String asPattern(final LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }

    @Inject
    QuickObjectRepository quickObjectRepository;
}
