package org.incode.eurocommercial.ecpcrm.webapp;

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
import org.junit.ClassRule;
import org.junit.Test;

//@UseReporter(MyBeyondCompare3Reporter.class)
@UseReporter(P4MergeReporter.class)
public class CardCheckUnbound {

//    static {
//        System.setProperty("isis.appManifest", "...");
//    }

    @ClassRule
    public static JettyServerRule server = new JettyServerRule(new EmbeddedJetty());

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    @Test
    public void sadCase() throws Exception
    {
        final URL resource = Resources.getResource(CardCheckUnbound.class, "CardCheckUnbound.sadCase.json");
        final String json = Resources.toString(resource, Charsets.UTF_8);

        final String serverUrl = server.getUrl() + "restful/crm/api/6.0/card-check-unbound";

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override public void initialize(final HttpRequest httpRequest) throws IOException {
                httpRequest.setHeaders(new HttpHeaders() {
                    {
                        setBasicAuthentication("ecpcrm-admin","pass");
                    }
                });
            }
        });

        GenericUrl url = new GenericUrl(serverUrl);
        HttpContent content = ByteArrayContent.fromString("application/json", json);
        HttpRequest request = requestFactory.buildPostRequest(url, content);
        HttpResponse response = request.execute();
        final String responseString = CharStreams.toString(new InputStreamReader(response.getContent()));

        Approvals.verifyJson(responseString);
    }

}