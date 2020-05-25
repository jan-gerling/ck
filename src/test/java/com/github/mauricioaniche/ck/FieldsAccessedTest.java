package com.github.mauricioaniche.ck;

import com.google.common.collect.Sets;
import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FieldsAccessedTest extends BaseTest {

    @BeforeAll
    public void setUp() {
        report = run(fixturesDir() + "/fieldusage");
    }

    @Test
    public void fieldsAccessedBasic() {
        CKClassResult a = report.get("fieldusage.FieldUsage");

        //Test basic fields access
        CKMethodResult m1 = a.getMethod("m1/0").get();
        Assertions.assertEquals( Sets.newHashSet("a", "b"), m1.getFieldsAccessed());

        //Test field name overloading
        CKMethodResult m2 = a.getMethod("m2/0").get();
        Assertions.assertEquals(Sets.newHashSet("a"), m2.getFieldsAccessed());

        //Test field reference with this
        CKMethodResult m3 = a.getMethod("m3/0").get();
        Assertions.assertEquals(Sets.newHashSet("a"), m3.getFieldsAccessed());
    }

    @Test
    public void fieldsThatAreDeclaredAfter() {
        CKClassResult a = report.get("fieldusage.FieldUsage");
        CKMethodResult m4 = a.getMethod("m4/0").get();
        Assertions.assertEquals(Sets.newHashSet("xx"), m4.getFieldsAccessed());

    }
    @Test
    public void lambdaFunction(){
        CKClassResult a = report.get("fieldusage.FieldUsage");
        CKMethodResult m5 = a.getMethod("m5/0").get();
        Assertions.assertEquals(Sets.newHashSet("b"), m5.getFieldsAccessed());
    }

    @Test
    public void ifBlock() {
        CKClassResult a = report.get("fieldusage.FieldUsage");
        CKMethodResult m6 = a.getMethod("m6/0").get();
        Assertions.assertEquals(Sets.newHashSet("a", "b", "c"), m6.getFieldsAccessed());
    }

    @Test
    public void returnStatement() {
        CKClassResult a = report.get("fieldusage.FieldUsage");
        CKMethodResult m7 = a.getMethod("m7/0").get();
        Assertions.assertEquals(Sets.newHashSet("a", "b"), m7.getFieldsAccessed());
    }

    @Test
    public void nonLocalTest() {
        CKClassResult a = report.get("fieldusage.FieldUsage");
        CKMethodResult m8 = a.getMethod("m8/0").get();
        Assertions.assertEquals(Sets.newHashSet("b"), m8.getFieldsAccessed());
    }

    @Test
    public void realWorldConstructorOnly() {
        Map<String, CKClassResult> report1 = run(fixturesDir() + "/real-world");
        CKClassResult a = report1.get("org.apache.storm.planner.TaskBundle");
        CKMethodResult constructor = a.getMethod("TaskBundle/2[org.apache.storm.planner.IBolt,int]").get();
        Assert.assertEquals(2, constructor.getFieldsAccessed().size());
        Assert.assertEquals(Sets.newHashSet("task", "componentId"), constructor.getFieldsAccessed());
    }

    @Test
    public void realWorldComplex() {
        Map<String, CKClassResult> report1 = run(fixturesDir() + "/real-world");
        CKClassResult a = report1.get("org.apache.storm.scheduler.resource.User");
        CKMethodResult m1 = a.getMethod("markTopoUnsuccess/3[org.apache.storm.scheduler.resource.TopologyDetails,org.apache.storm.scheduler.resource.Cluster,java.lang.String]").get();
        Assert.assertEquals(1, m1.getFieldsAccessed().size());
        Assert.assertEquals(Sets.newHashSet("unsuccess"), m1.getFieldsAccessed());
    }
}