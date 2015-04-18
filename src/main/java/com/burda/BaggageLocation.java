package com.burda;


import java.util.*;

/**
 * Represents a Node in a graph
 */
final class BaggageLocation {

    private final LocationId id;
    private final List<Path> adjacentPaths;

    BaggageLocation(LocationId id, List<Path> adj) {
        this.id = id;
        this.adjacentPaths = Collections.unmodifiableList(adj);
    }

    LocationId getId() {
        return id;
    }

    List<Path> getAdjacentPaths() {
        return adjacentPaths;
    }

}