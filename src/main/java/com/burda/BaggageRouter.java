package com.burda;

import java.util.*;

/**
 * The brains of the routing system. Takes in a list of baggageLocations (gates, baggageClaim, etc...) which represents
 * the bi-directional graph of the system.
 *
 * This class is NOT thread safe
 *
 */
final class BaggageRouter {

    private final Map<LocationId, BaggageLocation> locations = new HashMap<>();
    private final Map<CacheKey, Route> cachedOptimizedRoutes = new HashMap<>();

    BaggageRouter(Collection<BaggageLocation> baggageLocations) {
        assert(baggageLocations != null);
        for (BaggageLocation bl: baggageLocations) {
            locations.put(bl.getId(), bl);
        }
    }

    /**
     * Given a set of bags to route, print out the optimized route to get the bags to their destinations.
     * @param bags
     */
    void printRoutes(List<Bag> bags) {
        assert(bags != null);
        for (Bag b: bags) {
            BaggageLocation start = locations.get(b.getStartingLocation());
            BaggageLocation dest = locations.get(b.getDestination());
            Route optimizedRoute = findOptimizedRoute(start, dest);
            if (optimizedRoute != null) {
                System.out.println(String.format("%1$s %2$s", b.getId(), optimizedRoute.print()));
            }
            else {
                System.out.println(
                        String.format("No optimzedRoute from %1$s to %2$s for bag %3$s",
                                b.getStartingLocation(), b.getDestination(), b.getId()));
            }
        }
    }


    /**
     * finds an optimized route. Uses Dikstra's algorithm.
     * @param start
     * @param requestedDest
     * @return
     */
    Route findOptimizedRoute(BaggageLocation start, BaggageLocation requestedDest) {
        CacheKey key = new CacheKey(start.getId(), requestedDest.getId());

        if (cachedOptimizedRoutes.containsKey(key)) {
            return cachedOptimizedRoutes.get(key);
        }

        Queue<Route> queue = new AddIfBetterPriorityQueue<>(Route.ROUTE_DISTANCE_COMPARATOR);
        for (Path p: start.getAdjacentPaths()) {
            queue.add(Route.create(p));
        }

        while (!queue.isEmpty()) {
            Route bestRouteToCurLoc = queue.poll();
            LocationId curLoc = bestRouteToCurLoc.getDestination();
            CacheKey optimizedRouteKey = new CacheKey(start.getId(), curLoc);
            cachedOptimizedRoutes.put(optimizedRouteKey, bestRouteToCurLoc);
            if (curLoc.equals(requestedDest.getId())) {
                return bestRouteToCurLoc;
            }

            for (Path p: locations.get(curLoc).getAdjacentPaths()) {
                LocationId dest = p.getDestination();

                //already visited
                if (cachedOptimizedRoutes.containsKey(new CacheKey(start.getId(), dest))) {
                    continue;
                }

                //otherwise, try to add (only if better)
                Route proposedRoute = Route.createFromExisting(bestRouteToCurLoc, p);
                queue.add(proposedRoute);
            }
        }

        //if queue is empty and we didn't find an optimized route to the requestedDest, then there was
        //no route to the destination.
        return null;
    }

    /**
     * An enhanced PriorityQueue meant to be used only internally for this specific use case. Will only add an element
     * to the queue if a) it is not present in the queue OR b) it exists in the queue but the priority of the element
     * being added is higher than the existing.
     * @param <E>
     */
    private static final class AddIfBetterPriorityQueue<E> extends PriorityQueue<E> {

        private AddIfBetterPriorityQueue(Comparator<E> comparator) {
            super(comparator);
        }

        @Override
        public boolean add(E element) {

            if (super.contains(element)) {
                Iterator<E> existingElements = super.iterator();
                E existingElement = null;
                while (existingElements.hasNext()) {
                    E temp = existingElements.next();
                    if (temp.equals(element)) {
                        existingElement = temp;
                        break;
                    }
                }
                if (super.comparator().compare(element, existingElement) < 0) {
                    super.remove(existingElement);
                    super.add(element);
                    return true;
                }
            }
            else {
                super.add(element);
                return true;
            }

            return false;
        }
    }

    /**
     * An internal class that represents a route from start to destination
     */
    private static final class CacheKey {
        private final LocationId start;
        private final LocationId dest;
        private final int hashCode;

        private CacheKey(LocationId s, LocationId d) {
            this.start = s;
            this.dest = d;
            this.hashCode = start.hashCode()+dest.hashCode();
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof CacheKey)) {
                return false;
            }

            CacheKey other = (CacheKey)o;
            return other.start.equals(start) && other.dest.equals(dest);
        }
    }
}