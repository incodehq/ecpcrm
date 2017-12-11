package org.incode.eurocommercial.ecpcrm.restapi;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
import org.apache.isis.viewer.restfulobjects.applib.RestfulMediaType;
import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
import org.apache.isis.viewer.restfulobjects.rendering.service.conneg.PrettyPrinting;
import org.apache.isis.viewer.restfulobjects.server.resources.ResourceAbstract;

import org.incode.eurocommercial.ecpcrm.app.services.api.ApiService;
import org.incode.eurocommercial.ecpcrm.app.services.api.vm.cardcheck.CardCheckRequestViewModel;
import org.incode.eurocommercial.ecpcrm.app.services.api.vm.cardgame.CardGameRequestViewModel;
import org.incode.eurocommercial.ecpcrm.app.services.api.vm.cardrequest.CardRequestRequestViewModel;

@Path("/crm/api/6.0")
public class EcpCrmResource extends ResourceAbstract  {

    private Gson gson = new Gson();

    @Override
    protected void init(
            final RepresentationType representationType,
            final Where where,
            final RepresentationService.Intent intent) {
        super.init(representationType, where, intent);
        this.getServicesInjector().injectServicesInto(this);
    }

    @POST
    @Path("/card-check")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardCheck(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        CardCheckRequestViewModel requestViewModel = gson.fromJson(request, CardCheckRequestViewModel.class);

        return apiService.cardCheck(
                deviceName,
                deviceSecret,
                requestViewModel.getCardNumber(),
                requestViewModel.getOrigin()
        ).asResponse();
    }

    @POST
    @Path("/card-game")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardGame(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        CardGameRequestViewModel requestViewModel = gson.fromJson(request, CardGameRequestViewModel.class);

        return apiService.cardGame(
                deviceName,
                deviceSecret,
                requestViewModel.getCardNumber(),
                requestViewModel.getWin(),
                requestViewModel.getDesc()
        ).asResponse();
    }

    @POST
    @Path("/card-request")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response cardRequest(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        init(RepresentationType.DOMAIN_OBJECT, Where.OBJECT_FORMS, RepresentationService.Intent.ALREADY_PERSISTENT);

        CardRequestRequestViewModel requestViewModel = gson.fromJson(request, CardRequestRequestViewModel.class);

        return apiService.cardRequest(
                deviceName,
                deviceSecret,
                requestViewModel.getOrigin(),
                requestViewModel.getHostess(),
                requestViewModel.getTitle(),
                requestViewModel.getFirstName(),
                requestViewModel.getLastName(),
                requestViewModel.getEmail(),
                requestViewModel.getBirthdate(),
                requestViewModel.getChildren(),
                requestViewModel.getNbChildren(),
                requestViewModel.getHasCar(),
                requestViewModel.getAddress(),
                requestViewModel.getZipcode(),
                requestViewModel.getCity(),
                requestViewModel.getPhoneNumber(),
                requestViewModel.isPromotionalemails(),
                requestViewModel.getCheckItem(),
                requestViewModel.isLost()
        ).asResponse();
    }

    @POST
    @Path("/website-user-create")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteUserCreate(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        return null;
    }

    @POST
    @Path("/website-user-modify")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteUserModify(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        return null;
    }

    @POST
    @Path("/website-user-detail")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.WILDCARD })
    @Produces({
            MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR,
            MediaType.APPLICATION_XML, RestfulMediaType.APPLICATION_XML_OBJECT, RestfulMediaType.APPLICATION_XML_ERROR
    })
    @PrettyPrinting
    public Response websiteUserDetail(
            @FormParam("device") String deviceName,
            @FormParam("key") String deviceSecret,
            @FormParam("request") String request
    ) {
        return null;
    }

    @Inject ApiService apiService;
}
