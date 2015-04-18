package com.burda;

/**
 * Represents a path between two locations
 */
class Path {
    private final LocationId start;
    private final LocationId end;
    private final int distance;

    Path(LocationId s, LocationId e, int dist) {
        this.start = s;
        this.end = e;
        this.distance = dist;
    }

    LocationId getStartingLocation() {
        return start;
    }

    LocationId getDestination() {
        return end;
    }

    Integer getDistance() {
        return distance;
    }
}
