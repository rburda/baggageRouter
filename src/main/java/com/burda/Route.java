package com.burda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A Route represents a path from a start to an end location that are not neccessarily adjacent. Its up to an external
 * user of this class to determine if the route is optimal.
 * The start of the route is represented by the start location of the first path in this route.
 * The end of the route is represented by the destination location of the last path in this route.
 */
final class Route {

    /**
     * A comparator that determines sorting order based on the distances of the two routes.
     */
    static final Comparator<Route> ROUTE_DISTANCE_COMPARATOR = new Comparator<Route>() {

        @Override
        public int compare(Route o1, Route o2) {
            return o1.distance - o2.distance;
        }
    };

    private final List<Path> paths;
    private final int distance;
    private final LocationId destination;

    static Route create(Path p) {
        ArrayList<Path> paths = new ArrayList<>();
        paths.add(p);
        return new Route(paths);
    }

    static Route createFromExisting(Route existing, Path extension) {
        ArrayList<Path> newRoutePaths = new ArrayList<>();
        newRoutePaths.addAll(existing.paths);
        newRoutePaths.add(extension);
        return new Route(newRoutePaths);
    }

    Route(List<Path> pathsForRoute) {
        this.paths = Collections.unmodifiableList(pathsForRoute);
        int tempDistance = 0;
        for (Path p: paths) {
            tempDistance +=p.getDistance();
        }
        this.distance = tempDistance;
        this.destination = paths.get(paths.size()-1).getDestination();
    }

    Integer getDistance() {
        return distance;
    }

    LocationId getDestination() {
        return destination;
    }

    String print() {
        StringBuilder builder = new StringBuilder();
        builder.append(paths.get(0).getStartingLocation()).append(" ");
        for (int i=0; i < paths.size(); i++) {
            builder.append(paths.get(i).getDestination()).append(" ");
        }
        builder.append(": ").append(distance);
        return builder.toString();
    }

    public String toString() {
        return print();
    }

    @Override
    public int hashCode() {
        return destination.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Route)) {
            return false;
        }

        Route other = (Route) o;
        return destination.equals(other.destination);
    }
}