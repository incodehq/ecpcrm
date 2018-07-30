package org.incode.eurocommercial.ecpcrm.camel.webapp;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import org.incode.eurocommercial.ecpcrm.camel.processor.ProcessorAbstract;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration({"classpath:org/incode/eurocommercial/ecpcrm/camel/webapp/MyFilterTest-context.xml"})
public class MyFilterTest {

    @Before
    public void setUp() throws Exception {
        //BasicConfigurator.configure();
    }

    @Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock://result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @DirtiesContext
    @Test
    public void matches_FRA() throws Exception {

        Exchange exchange = build("<something/>", "/FRA");

        resultEndpoint.expectedBodiesReceived("<something/>");
        template.send(exchange);

        resultEndpoint.assertIsSatisfied();
    }

    @DirtiesContext
    @Test
    public void does_not_match_ITA() throws Exception {

        Exchange exchange = build("<something/>", "/ITA");

        resultEndpoint.setExpectedMessageCount(0);
        template.send(exchange);

        resultEndpoint.assertIsSatisfied();
    }

    private Exchange build(final String body, final String atPath) {
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(body);

        final Dto dto = new Dto();
        dto.setAtPath(atPath);

        ProcessorAbstract.setHeader(exchange.getIn(), dto, "default");

        return exchange;
    }

}