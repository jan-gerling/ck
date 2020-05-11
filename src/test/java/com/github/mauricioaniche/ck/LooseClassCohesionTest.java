package com.github.mauricioaniche.ck;

import junit.framework.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class LooseClassCohesionTest extends BaseTest {
    private static Map<String, CKClassResult> report;

    @BeforeAll
    public static void setUp() {
        report = run(fixturesDir() + "/ClassCohesion");
    }

    @Test
    public void highCohesion() {
        CKClassResult ckClass = report.get("ClassCohesion.HighCohesion");
        Assert.assertEquals(1f, ckClass.getLooseClassCohesion());
    }

    @Test
    public void noCohesion() {
        CKClassResult ckClass = report.get("ClassCohesion.NoCohesion");
        Assert.assertEquals(0f, ckClass.getLooseClassCohesion());
    }

    @Test
    public void mediumCohesion1() {
        CKClassResult ckClass = report.get("ClassCohesion.MediumCohesion");
        Assert.assertEquals(2f / 6f, ckClass.getLooseClassCohesion());
    }

    @Test
    public void mediumCohesion2() {
        CKClassResult ckClass = report.get("ClassCohesion.Simple1");
        Assert.assertEquals(2f / 10f, ckClass.getLooseClassCohesion());
    }

    @Test
    public void mediumCohesion3() {
        CKClassResult ckClass = report.get("ClassCohesion.Simple2");
        Assert.assertEquals(6f / 10f, ckClass.getLooseClassCohesion());
    }
}