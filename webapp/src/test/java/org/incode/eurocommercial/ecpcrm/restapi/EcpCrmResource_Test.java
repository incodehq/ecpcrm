//package org.incode.eurocommercial.ecpcrm.restapi;
//
//import com.google.gson.Gson;
//import org.apache.isis.applib.annotation.Where;
//import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
//import org.apache.isis.viewer.restfulobjects.applib.RepresentationType;
//import org.apache.isis.viewer.restfulobjects.rendering.service.RepresentationService;
//import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDevice;
//import org.incode.eurocommercial.ecpcrm.module.api.dom.authentication.AuthenticationDeviceRepository;
//import org.incode.eurocommercial.ecpcrm.module.api.service.ApiService;
//import org.incode.eurocommercial.ecpcrm.module.api.service.Result;
//import org.incode.eurocommercial.ecpcrm.module.api.service.vm.cardrequest.CardRequestRequestViewModel;
//import org.jmock.Expectations;
//import org.jmock.auto.Mock;
//import org.junit.Rule;
//import org.junit.Test;
//
//import javax.ws.rs.core.Response;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class EcpCrmResource_Test {
//
//    @Rule
//    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);
//
//    @Mock
//    ApiService mockApiService;
//
//    @Mock
//    AuthenticationDeviceRepository mockAuthenticationDeviceRepository;
//
//    @Mock
//    Gson mockGson;
//
//    @Test
//    public void testCardRequest_status_OK() throws Exception {
//
//        final EcpCrmResource ecpCrmResource = new EcpCrmResource() {
//            @Override
//            protected void init(
//                    final RepresentationType representationType,
//                    final Where where,
//                    final RepresentationService.Intent intent) {
//            }
//        };
//
//        ecpCrmResource.apiService = mockApiService;
//
//        final String deviceName = "fooDevice";
//        final String deviceSecret = "barSecret";
//        final String request = "bazJson";
//
//        final AuthenticationDevice device = new AuthenticationDevice();
//        final CardRequestRequestViewModel viewModel = new CardRequestRequestViewModel();
//
//        final Response okResponse = new Result(Result.STATUS_OK, null, viewModel).asResponse();
//
//        //given
//        context.checking(new Expectations() {
//            {
//                oneOf(mockAuthenticationDeviceRepository).findByNameAndSecret(deviceName, deviceSecret);
//                will(returnValue(device));
//                oneOf(mockGson).fromJson(request, CardRequestRequestViewModel.class);
//                will(returnValue(viewModel));
//                oneOf(mockApiService).cardRequest(device, viewModel);
//                will(returnValue(okResponse));
//            }
//        });
//
//        //when
//        Response resp = ecpCrmResource.cardRequest(deviceName, deviceSecret, request);
//
//        assertThat(resp).equals(resp_302);
//
//    }
//
//}
