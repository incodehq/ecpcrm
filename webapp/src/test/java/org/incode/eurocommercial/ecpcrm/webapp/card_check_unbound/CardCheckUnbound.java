package org.incode.eurocommercial.ecpcrm.webapp.card_check_unbound;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.reporters.macosx.P4MergeReporter;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

//@UseReporter(MyBeyondCompare3Reporter.class)
@UseReporter(P4MergeReporter.class)
public class CardCheckUnbound {

//    static {
//        System.setProperty("isis.appManifest", "...");
//    }

    @ClassRule
    public static JettyServerRule server = new JettyServerRule(new EmbeddedJetty());
    private GenericUrl url = new GenericUrl(server.getUrl() + "restful/crm/api/6.0/card-check-unbound");

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
        @Override public void initialize(final HttpRequest httpRequest) throws IOException {
            httpRequest.setHeaders(new HttpHeaders() {
                {
                    setBasicAuthentication("ecpcrm-admin","pass");
                }
            });
        }
    });

    @Before
    public void setUp() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_exists_but_is_not_the_same_center_as_device_we_expect_317_error() throws Exception {
    }

    @Test
    public void when_card_exists_but_is_already_bound_to_user_we_expect_308_error() throws Exception {
        final URL resource = Resources.getResource(CardCheckUnbound.class, "CardCheckUnbound.when_card_exists_but_is_already_bound_to_user_we_expect_308_error.json");
        final String json = Resources.toString(resource, Charsets.UTF_8);

        HttpContent content = ByteArrayContent.fromString("application/json", json);
        HttpRequest request = requestFactory.buildPostRequest(url, content);
        HttpResponse response = request.execute();
        final String responseString = CharStreams.toString(new InputStreamReader(response.getContent()));

        Approvals.verifyJson(responseString);
    }

    @Test
    @Ignore
    public void when_card_exists_and_is_unbound_we_expect_happy_response_existing_card() throws Exception {
    }

    @Test
    @Ignore
    public void when_card_does_not_exist_but_has_valid_number_we_expect_happy_response_non_existing_card() throws Exception {
    }

}