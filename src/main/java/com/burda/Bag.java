package com.burda;

/**
 * Represents baggage that is starting at 'startingLocation' and is desired to arrive at 'destination'
 */
final class Bag {
    private final String id;
    private final LocationId startingLocation;
    private final LocationId destination;

    Bag(String id, LocationId start, LocationId dest) {
        this.id = id;
        this.startingLocation = start;
        this.destination = dest;
    }

    String getId() {
        return id;
    }

    LocationId getStartingLocation() {
        return startingLocation;
    }

    LocationId getDestination() {
        return destination;
    }
}