package org.incode.eurocommercial.ecpcrm.app.services.apptenancy;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.isisaddons.module.security.dom.user.ApplicationUser;

public class ApplicationTenancyEvaluatorForEcpCrmTest {


    ApplicationTenancyEvaluatorForEcpCrm evaluator;

    HasAtPath domainObject;
    ApplicationUser applicationUser;
    String objectAtPath;
    String userAtPath;

    @Before
    public void setUp() throws Exception {
        evaluator = new ApplicationTenancyEvaluatorForEcpCrm();
        domainObject = new HasAtPath() {
            @Override public String getAtPath() {
                return objectAtPath;
            }
        };
        applicationUser = new ApplicationUser() {
            @Override public String getAtPath() {
                return userAtPath;
            }
        };
    }

    @After
    public void tearDown() throws Exception {

    }

    public static class HidesTest extends ApplicationTenancyEvaluatorForEcpCrmTest {

        @Test
        public void when_object_higher_up_than_user() throws Exception {

            // given
            objectAtPath = "/FRA";
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.hides(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();
        }

        @Test
        public void when_object_lower_down_than_user() throws Exception {

            // given
            objectAtPath = "/FRA/078";
            userAtPath = "/FRA";

            // when
            final String result = evaluator.hides(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();
        }

        @Test
        public void when_object_is_sibling_to_user() throws Exception {

            // given
            objectAtPath = "/FRA/045";
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.hides(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNotNull();

        }

        @Test
        public void when_obj_and_user_have_same_atPath() throws Exception {

            // given
            objectAtPath = "/FRA/078";
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.hides(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();

        }


        @Test
        public void when_obj_has_no_atPath() throws Exception {

            // given
            objectAtPath = null;
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.hides(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();

        }


        @Test
        public void when_user_has_no_atPath() throws Exception {

            // given
            objectAtPath = "/FRA";
            userAtPath = null;

            // when
            final String result = evaluator.hides(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNotNull();

        }


    }

    public static class DisablesTest extends ApplicationTenancyEvaluatorForEcpCrmTest {
        @Test
        public void when_object_higher_up_than_user() throws Exception {

            // given
            objectAtPath = "/FRA";
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.disables(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNotNull();
        }

        @Test
        public void when_object_lower_down_than_user() throws Exception {

            // given
            objectAtPath = "/FRA/078";
            userAtPath = "/FRA";

            // when
            final String result = evaluator.disables(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();
        }

        @Test
        public void when_object_is_sibling_to_user() throws Exception {

            // given
            objectAtPath = "/FRA/045";
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.disables(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNotNull();

        }

        @Test
        public void when_obj_and_user_have_same_atPath() throws Exception {

            // given
            objectAtPath = "/FRA/078";
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.disables(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();

        }


        @Test
        public void when_obj_has_no_atPath() throws Exception {

            // given
            objectAtPath = null;
            userAtPath = "/FRA/078";

            // when
            final String result = evaluator.disables(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNull();

        }


        @Test
        public void when_user_has_no_atPath() throws Exception {

            // given
            objectAtPath = "/FRA";
            userAtPath = null;

            // when
            final String result = evaluator.disables(domainObject, applicationUser);

            // then
            Assertions.assertThat(result).isNotNull();

        }
    }



}