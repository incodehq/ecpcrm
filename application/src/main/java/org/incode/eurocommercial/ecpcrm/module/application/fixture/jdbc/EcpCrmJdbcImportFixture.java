package org.incode.eurocommercial.ecpcrm.module.application.fixture.jdbc;

import org.apache.isis.applib.fixturescripts.DiscoverableFixtureScript;

import org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel.CardImport;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel.CardRequestImport;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel.CenterImport;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel.ChildImport;
import org.incode.eurocommercial.ecpcrm.module.application.fixture.viewmodel.UserImport;

public class EcpCrmJdbcImportFixture extends DiscoverableFixtureScript {

    @Override
    protected void execute(final ExecutionContext executionContext) {

                final JdbcFixture jdbcFixture = new JdbcFixture(
                        "les-atlantes-import",
                        "ecpcrm",
                        "ecpcrm",
                        getClasses()
                        );
                executionContext.executeChild(this, jdbcFixture);
    }

    public static Class[] getClasses() {
        return new Class[] {
                CenterImport.class,
                CardImport.class,
                UserImport.class,
                ChildImport.class,
                CardRequestImport.class
        };
    }
}
