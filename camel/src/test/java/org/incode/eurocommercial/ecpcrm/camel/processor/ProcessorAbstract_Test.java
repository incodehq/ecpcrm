package org.incode.eurocommercial.ecpcrm.camel.processor;

import org.apache.camel.impl.DefaultMessage;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ProcessorAbstract_Test {

    public static class SomeDto {

        public SomeDto() {
        }

        public SomeDto(final String foo) {
            this.foo = foo;
        }

        private String foo;

        public String getFoo() {
            return foo;
        }

        public void setFoo(final String foo) {
            this.foo = foo;
        }
    }

    @Test
    public void whenNone() throws Exception {

        final DefaultMessage defaultMessage = new DefaultMessage();

        assertThat(ProcessorAbstract.getHeader(defaultMessage, SomeDto.class, "default"), is(nullValue()));
    }

    @Test
    public void whenOne() throws Exception {

        final DefaultMessage defaultMessage = new DefaultMessage();

        final SomeDto otherDto = new SomeDto("xxx");

        ProcessorAbstract.setHeader(defaultMessage, otherDto, "default");
        assertThat(ProcessorAbstract.getHeader(defaultMessage, SomeDto.class, "default"), is(otherDto));
        assertThat(ProcessorAbstract.getHeader(defaultMessage, SomeDto.class, "otherRole"), is(nullValue()));

    }


}