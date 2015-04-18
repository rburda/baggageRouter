package com.burda;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class RouteTest {

    @Test
    public void testDistanceCalculation() throws Exception {
        Route r = new Route(createTestPaths());
        Assert.assertEquals(r.getDistance(), Integer.valueOf(7));
    }

    @Test
    public void testPrint() throws Exception {
        Route r = new Route(createTestPaths());
        Assert.assertEquals(r.print(), "foo bar baz : 7");
    }

    @Test
    public void testPrintWithOnePath() throws Exception {
        Route r = Route.create(new Path(new LocationId("foo"), new LocationId("bar"), 3));
        Assert.assertEquals(r.print(), "foo bar : 3");
    }

    private ArrayList<Path> createTestPaths() {
        Path p1 = new Path(new LocationId("foo"), new LocationId("bar"), 5);
        Path p2 = new Path(new LocationId("bar"), new LocationId("baz"), 2);

        ArrayList<Path> paths = new ArrayList<>();
        paths.add(p1);
        paths.add(p2);

        return paths;
    }
}
