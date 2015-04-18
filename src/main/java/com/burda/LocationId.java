package com.burda;

final class LocationId {

    private static final String ARRIVAL = "ARRIVAL";
    static final LocationId BAGGAGE_CLAIM = new LocationId("BaggageClaim");

    static final boolean isEquivalenToBaggageClaim(String id) {
        return id.equals(ARRIVAL);
    }

    private final String id;

    LocationId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!(o instanceof LocationId)) {
            return false;
        }

        return id.equals(((LocationId)o).id);
    }
}