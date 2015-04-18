package com.burda;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class is the public interface to the system. This code has a main() which expects a single argument. That
 * argument should be the fully qualified path to a file that has syntax that matches the expected input.
 */
public class Driver {

    private static final String SECTION_1_MARKER = "# Section: Conveyor System";
    private static final String SECTION_2_MARKER = "# Section: Departures";
    private static final String SECTION_3_MARKER = "# Section: Bags";

    public static void main (String args[]) throws Exception {

        Scanner inputScanner = new Scanner(new File(args[0]));
        String firstLine = inputScanner.nextLine();
        if (!firstLine.equals(SECTION_1_MARKER)) {
            throw new IllegalArgumentException(
                    String.format("input must begin with %1$s", SECTION_1_MARKER));
        }

        boolean section2Found = false;
        boolean section3Found = false;
        List<String> rawPaths = new ArrayList<String>();
        while (section2Found == false && inputScanner.hasNext()) {
            String line = inputScanner.nextLine();
            if (line.equals(SECTION_2_MARKER)) {
                section2Found = true;
            } else {
                rawPaths.add(line);
            }
        }
        List<BaggageLocation> baggageLocations = constructBaggageLocations(rawPaths);
        if (!section2Found) {
            throw new IllegalArgumentException("no departures detected");
        }

        List<String> rawDepartures = new ArrayList<>();
        while (section3Found == false  && inputScanner.hasNext()) {
            String line = inputScanner.nextLine();
            if (line.equals(SECTION_3_MARKER)) {
                section3Found = true;
            }
            else {
                rawDepartures.add(line);
            }
        }

        HashMap<String, LocationId> departureToLocationMap = constructDepartureToLocationMap(rawDepartures);
        if (!section3Found) {
            throw new IllegalArgumentException("no bag list detected");
        }
        List<String> rawBags = new ArrayList<>();
        while (inputScanner.hasNext()) {
            rawBags.add(inputScanner.nextLine());
        }

        BaggageRouter br = new BaggageRouter(baggageLocations);
        br.printRoutes(constructBagList(rawBags, departureToLocationMap));
    }

    private static List<Bag> constructBagList(List<String> rawBags, HashMap<String, LocationId> depToLocMap) {
        List<Bag> bags = new ArrayList<>();
        for (String s: rawBags) {
            String[] tokens = s.split(" ");
            if (tokens.length != 3) {
                throw new IllegalArgumentException("Bag input must have 3 tokens");
            }
            LocationId start = new LocationId(tokens[1]);
            LocationId dest =
                    LocationId.isEquivalenToBaggageClaim(tokens[2]) ? LocationId.BAGGAGE_CLAIM : depToLocMap.get(tokens[2]);
            bags.add(new Bag(tokens[0], start, dest));
        }
        return bags;
    }

    private static HashMap<String, LocationId> constructDepartureToLocationMap(List<String> rawDepartures) {
        HashMap<String, LocationId> departureToLocationMap = new HashMap<>();
        for (String s: rawDepartures) {
            String[] tokens = s.split(" ");
            if (tokens.length != 4) {
                throw new IllegalArgumentException("Departure input must have 4 tokens");
            }
            departureToLocationMap.put(tokens[0], new LocationId(tokens[1]));
        }
        return departureToLocationMap;
    }

    private static List<BaggageLocation> constructBaggageLocations(List<String> rawPaths) {
        HashMap<LocationId, ArrayList<Path>> bagLocPaths = new HashMap<>();

        List<BaggageLocation> locations = new ArrayList<>();
        for (String s: rawPaths) {
            String[] tokens = s.split(" ");
            if (tokens.length != 3) {
                throw new IllegalArgumentException("Conveyer input lines must have 3 tokens");
            }
            LocationId loc1 = new LocationId(tokens[0]);
            LocationId loc2 = new LocationId(tokens[1]);
            Integer distance = Integer.valueOf(tokens[2]);
            Path p1 = new Path(loc1, loc2, distance);
            Path p2 = new Path(loc2, loc1, distance);
            ArrayList<Path> pathsForLoc1 = bagLocPaths.get(loc1);
            if (pathsForLoc1 == null) {
                pathsForLoc1 = new ArrayList<>();
                bagLocPaths.put(loc1, pathsForLoc1);
            }
            ArrayList<Path> pathsForLoc2 = bagLocPaths.get(loc2);
            if (pathsForLoc2 == null) {
                pathsForLoc2 = new ArrayList<>();
                bagLocPaths.put(loc2, pathsForLoc2);
            }
            pathsForLoc1.add(p1);
            pathsForLoc2.add(p2);
        }
        for (LocationId loc: bagLocPaths.keySet()) {
            locations.add(new BaggageLocation(loc, bagLocPaths.get(loc)));
        }
        return locations;
    }
}