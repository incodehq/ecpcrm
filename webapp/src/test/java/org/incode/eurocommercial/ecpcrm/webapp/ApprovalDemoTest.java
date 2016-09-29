package org.incode.eurocommercial.ecpcrm.webapp;

import org.approvaltests.Approvals;
import org.approvaltests.reporters.UseReporter;
import org.approvaltests.reporters.macosx.P4MergeReporter;
import org.junit.Test;

//@UseReporter(MyBeyondCompare3Reporter.class)
@UseReporter(P4MergeReporter.class)
public class ApprovalDemoTest {


    @Test
    public void testBasicFormatting() throws Exception
    {
        String json = "{\"infos\":{\"address\":\"my address\",\"phone\":\"my phone\"},\"insurance\":{\"forks\":[14,53,123],\"prices\":[5,8,\"3%\"]}}";
        Approvals.verifyJson(json);
    }


}
