package org.incode.eurocommercial.ecpcrm.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulMediaType;
import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
import org.apache.isis.viewer.restfulobjects.rendering.service.conneg.PrettyPrinting;
import org.apache.isis.viewer.restfulobjects.server.resources.ResourceAbstract;

@Path("/hello")
public class HelloWorldResource extends ResourceAbstract  {

    @Override protected void init(
            final RepresentationType representationType,
            final Where where,
            final RepresentationService.Intent intent) {
        super.init(representationType, where, intent);
        this.getServicesInjector().injectServicesInto(this);
    }


    @GET
    @Path("/world/{domainType}/{instanceId}")
    @Consumes({ MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response object(@PathParam("domainType") String domainType, @PathParam("instanceId") final String instanceId) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"domainType\": \"%s\", \"instanceId\": \"%s\" }", domainType, instanceId))
                .build();
    }

//    @GET
//    @Path("/all")
//    @Consumes({ MediaType.WILDCARD })
//    @Produces({
//            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
//            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
//    })
//    @PrettyPrinting
//    public Response object() {
//        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);
//
//        this.getServicesInjector().injectServicesInto(this);
//
//        final List<QuickObjectViewModel> quickObjects = quickObjectRepository.listAll().stream()
//                .map(x -> asVm(x))
//                .collect(Collectors.toList());
//
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//        String json = gson.toJson(quickObjects);
//
//        return Response
//                .ok()
//                .type(MediaType.APPLICATION_JSON_TYPE)
//                .entity(json)
//                .build();
//    }
//
//    private QuickObjectViewModel asVm(final QuickObject x) {
//        return QuickObjectViewModel.create(x.getName(), x.getInteger(), asPattern(x.getLocalDate()));
//    }
//
//    private static String asPattern(final LocalDate localDate) {
//        return localDate == null ? null : localDate.toString();
//    }
//
//    @Inject
//    QuickObjectRepository quickObjectRepository;
}
