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
                    .type("application/json")
                    .entity(String.format("{ \"domainType\": \"%s\", \"instanceId\": \"%s\" }", domainType, instanceId))
                    .build();
        }

}
