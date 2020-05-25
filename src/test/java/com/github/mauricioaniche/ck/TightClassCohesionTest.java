package com.github.mauricioaniche.ck;

import junit.framework.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TightClassCohesionTest extends BaseTest {
    @BeforeAll
    public void setUp() {
        report = run(fixturesDir() + "/ClassCohesion");
    }

    @Test
    public void highCohesion() {
        CKClassResult ckClass = report.get("ClassCohesion.HighCohesion");
        Assert.assertEquals(1f, ckClass.getTightClassCohesion(),0.0000001);
    }

    @Test
    public void noCohesion() {
        CKClassResult ckClass = report.get("ClassCohesion.NoCohesion");
        System.out.println(ckClass.getVisibleMethods());
        Assert.assertEquals(0f, ckClass.getTightClassCohesion(),0.0000001);
    }

    @Test
    public void mediumCohesion1() {
        CKClassResult ckClass = report.get("ClassCohesion.MediumCohesion");
        Assert.assertEquals(2f / 6f, ckClass.getTightClassCohesion(),0.0000001);
    }

    @Test
    public void mediumCohesion2() {
        CKClassResult ckClass = report.get("ClassCohesion.Simple1");
        Assert.assertEquals(2f / 10f, ckClass.getTightClassCohesion(),0.0000001);
    }

    @Test
    public void mediumCohesion3() {
        CKClassResult ckClass = report.get("ClassCohesion.Simple2");
        Assert.assertEquals(4f / 10f, ckClass.getTightClassCohesion(),0.0000001);
    }

    @Test
    public void hugeClassCohesion(){
        Map<String, CKClassResult> reportHuge = run(fixturesDir() + "/real-world-huge-class");
        CKClassResult ckClass = reportHuge.get("com.satoshilabs.trezor.lib.protobuf.TrezorMessage");
        Assert.assertTrue(ckClass.getTightClassCohesion() < 0.000001f);
    }

    @Test
    public void noVisibleMethods(){
        CKClassResult ckClass = report.get("ClassCohesion.NoVisibleMethods");
        Assert.assertEquals(-1, ckClass.getTightClassCohesion(),0.0000001);
    }

    @Test
    public void realWorldConstructorOnly() {
        Map<String, CKClassResult> report1 = run(fixturesDir() + "/real-world");
        CKClassResult a = report1.get("org.apache.storm.planner.TaskBundle");
        Assertions.assertEquals(-1f, a.getTightClassCohesion(), 0.0000000000001f);
        Assertions.assertEquals(-1f, a.getLooseClassCohesion(), 0.0000000000001f);
    }

    @Test
    public void realWorldComplex() {
        Map<String, CKClassResult> report1 = run(fixturesDir() + "/real-world");
        CKClassResult a = report1.get("org.apache.storm.pmml.runner.jpmml.JpmmlFactory");
        Assertions.assertEquals(0.0f, a.getTightClassCohesion(), 0.0000000000001f);
        Assertions.assertEquals(0.0f, a.getLooseClassCohesion(), 0.0000000000001f);
    }

    //This test was added in order to verify if the TCC and LCC calculation causes any exceptions.
    @Test
    public void realWorldComplex2() {
        Map<String, CKClassResult> report1 = run(fixturesDir() + "/real-world");
        CKClassResult a = report1.get("org.apache.storm.scheduler.resource.User");
        Assertions.assertTrue(0 <= a.getTightClassCohesion() && a.getTightClassCohesion() <= 1);
        Assertions.assertTrue(0 <= a.getLooseClassCohesion() && a.getLooseClassCohesion() <= 1);
    }
}