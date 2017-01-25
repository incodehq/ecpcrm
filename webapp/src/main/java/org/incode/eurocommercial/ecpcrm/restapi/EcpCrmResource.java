package org.incode.eurocommercial.ecpcrm.restapi;

import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulMediaType;
import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
import org.apache.isis.viewer.restfulobjects.rendering.service.conneg.PrettyPrinting;
import org.apache.isis.viewer.restfulobjects.server.resources.ResourceAbstract;

import org.incode.eurocommercial.ecpcrm.dom.user.User;
import org.incode.eurocommercial.ecpcrm.dom.user.UserRepository;

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
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/card-check")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardCheck(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/card-game")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardGame(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/card-request")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardRequest(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/card-request-detail")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardRequestDetail(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/card-request-edit")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardRequestEdit(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/card-requests")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardRequests(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/user-create")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response userCreate(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);


        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @POST
    @Path("/user-update")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response userUpdate(InputStream body) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(String.format("{ \"status\": 200, \"message\": \"test\"}"))
                .build();
    }

    @GET
    @Path("/user-detail")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR
    })
    @PrettyPrinting
    public Response userDetail(@HeaderParam("id") String reference) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);
        Gson gson = new Gson();

        if(reference == null) {
            return Response
                    .ok()
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new JsonBuilder()
                            .add("status", 302)
                            .add("message", "Invalid parameter")
                            .toJsonString())
                    .build();
        }

        User user = userRepository.findByReference(reference);

        if(user == null) {
            return Response
                    .ok()
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new JsonBuilder()
                            .add("status", 304)
                            .add("message", "Invalid user")
                            .toJsonString())
                    .build();
        }

        JsonObject userJson = new JsonParser().parse(gson.toJson(asVm(user))).getAsJsonObject();

        return Response
                .ok()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new JsonBuilder()
                        .add("status", 200)
                        .add("response", userJson)
                        .toJsonString())
                .build();
    }

    private UserViewModel asVm(final User x) {
        return UserViewModel.create(
                x.getReference(),
                x.getTitle().toString().toLowerCase(),
                x.getFirstName(),
                x.getLastName(),
                x.getEmail(),
                x.getAddress(),
                x.getZipcode(),
                x.getCity(),
                x.getAddress() + " - " + x.getZipcode() + " - " + x.getCity(),
                x.getPhoneNumber(),
                asString(x.getBirthDate()),
                x.getCenter().title(),
                asString(x.isEnabled()),
                asString(x.isPromotionalEmails())
        );
    }

    private static String asString(final boolean bool) {
        return bool ? "true" : "false";
    }

    private static String asString(final LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }


    public class JsonBuilder {
        private final JsonObject json = new JsonObject();

        public String toJsonString() {
            return json.toString();
        }

        public JsonObject toJsonObject() {
            return json;
        }

        public JsonBuilder add(String key, String value) {
            json.addProperty(key, value);
            return this;
        }

        public JsonBuilder add(String key, Number value) {
            json.addProperty(key, value);
            return this;
        }

        public JsonBuilder add(String key, Boolean value) {
            json.addProperty(key, value);
            return this;
        }

        public JsonBuilder add(String key, JsonBuilder value) {
            json.add(key, value.toJsonObject());
            return this;
        }

        public JsonBuilder add(String key, JsonObject value) {
            json.add(key, value);
            return this;
        }
    }

    @Inject UserRepository userRepository;
}
