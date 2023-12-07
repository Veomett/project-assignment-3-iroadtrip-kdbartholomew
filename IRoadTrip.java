import java.util.HashMap;
import java.util.*;
import java.io.*;

public class IRoadTrip {
    Graph map;
    //building 3 hashmaps
    HashMap<String, ArrayList<String>> borders = new HashMap<String, ArrayList<String>>();
    HashMap<String, HashMap<String, Integer>> capDist = new HashMap<String, HashMap<String, Integer>>();
    HashMap<String, String> stateID = new HashMap<String, String>();

    public IRoadTrip (String [] args) {
        // Replace with your code
        Scanner borderScan = null;
        Scanner capDistScan = null;
        Scanner stateIDScan = null;
        map = new Graph();
//        System.out.println(args[0]);
//        System.out.println(args[1]);
//        System.out.println(args[2]);
        try{
            borderScan = new Scanner(new File(args[0]));
            capDistScan = new Scanner(new File(args[1]));
            stateIDScan = new Scanner(new File(args[2]));
        }
        catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }

//        //building 3 hashmaps
//        HashMap<String, ArrayList<String>> borders = new HashMap<String, ArrayList<String>>();
//        HashMap<String, HashMap<String, Integer>> capDist = new HashMap<String, HashMap<String, Integer>>();
//        HashMap<String, String> stateID = new HashMap<String, String>();

        //build stateID: note*if the later date is not 2020-12-31, we dont care about that entry
        while(stateIDScan.hasNext()) {
            String getLine = stateIDScan.nextLine(); //skips over header
            String[] StringArray = getLine.split("\t");//splits on tab
            if(StringArray[1].equals("UKG")){ //note: in state name we have UKG in cap dist we have UK
                StringArray[1] = "UK";
            }
            if (StringArray[4].equals("2020-12-31")) {//if currently a country
                //handle removing of weird parenthetical statements from the country name

                if(StringArray[2].contains("(") && StringArray[2].contains(")")){//if

                    int location = StringArray[2].indexOf(" (");
                    StringArray[2] = StringArray[2].substring(0,location);
//                    String junk = "(";
//                    String[] stArr = StringArray[2].split(junk);
//                    StringArray[2] = stArr[0];
                }


                stateID.put(StringArray[2], StringArray[1]);// adds name and code to state id
            }
           //System.out.println("Country: " + StringArray[2] + ", State ID: " + StringArray[1]);
        }
        //PUT IN ALIASES!!!!!!!!!
        stateID.put("Bahamas, The", "BHM");
        stateID.put("Cabo Verde", "CAP");
        stateID.put("Bosnia and Herzegovina", "BOS");
        stateID.put("Congo, Democratic Republic of the", "DRC");
        stateID.put("Democratic Republic of the Congo", "DRC");
        stateID.put("Congo, Republic of", "CON");
        stateID.put("Czechia", "CZR");
        stateID.put("Eswatini", "SWA");
        stateID.put("Gambia, The", "GAM");
        stateID.put("The Gambia", "GAM");
        stateID.put("Germany", "GFR");
        stateID.put("Italy", "ITA");
        stateID.put("Korea, North", "PRK");
        stateID.put("North Korea", "PRK");
        stateID.put("Korea, South", "ROK");
        stateID.put("South Korea", "ROK");
        stateID.put("North Macedonia", "MAC");
        stateID.put("United States", "USA");
        stateID.put("Vietnam", "DRV");
        stateID.put("Suriname", "SUR");
        stateID.put("Tanzania", "TAZ");
        stateID.put("UK", "UK");
        stateID.put("United Kingdom", "UK");

        // weird comma corrections
        stateID.put("The Bahamas", "BHM");
        stateID.put("Republic of Congo", "CON");
        stateID.put("Republic of the Congo", "CON");
        stateID.put("Congo, Republic of the", "CON");
        stateID.put("Democratic Republic of Congo", "DRC");
        stateID.put("US", "USA");
        stateID.put("The United States", "USA");
        stateID.put("USA", "USA");



        //weird fucking capitaliztion
        stateID.put("Cote d'Ivoire", "CDI");

        //does not exist in state name
        stateID.put("Liechtenstein", "LIE");
        stateID.put("Kyrgyzstan", "KGZ");
        stateID.put("Burma", "MYA");
        stateID.put("Russia (Kaliningrad)", "RUS");
        stateID.put("Russia (Kaliningrad Oblast)", "RUS");
        stateID.put("Romania", "ROU");
        stateID.put("Gibraltar", "GIB");
        stateID.put("Macau", "MAC");
        stateID.put("Andorra", "AND");
        stateID.put("San Marino", "SMR");
        stateID.put("Monaco", "MCO");
        stateID.put("UAE", "UAE");
        stateID.put("French Guiana", "GUF");
        stateID.put("Holy See", "VAT");
        stateID.put("Holy See (Vatican City)", "VAT");
        stateID.put("Denmark (Greenland)", "DNK");
        stateID.put("Timor-Leste", "TLS");
        stateID.put("Poland (Kaliningrad Oblast)", "POL");
        stateID.put("Lithuania (Kaliningrad Oblast)", "LTU");
        stateID.put("Spain (Ceuta)", "ESP");
        stateID.put("Morocco (Ceuta)", "MAR");
        stateID.put("Sint Maarten", "SXM");
        stateID.put("Saint Martin (France)", "MAF");
        stateID.put("Gaza Strip", "GAZ");
        stateID.put("West Bank", "XWB");
        stateID.put("Dhekelia", "XXD");
        stateID.put("Akrotiri", "XQZ");









        //parenthetical form exists in borders but not in stateID
        stateID.put("Turkey (Turkiye)", "TUR");


        //build capdist..
        capDistScan.nextLine();
        while(capDistScan.hasNext()) {
            String getLine = capDistScan.nextLine();
            String[] StringArray = getLine.split(",");
            if(capDist.containsKey(StringArray[1])){
                capDist.get(StringArray[1]).put(StringArray[3], Integer.parseInt(StringArray[4]));
            }
            //case 2:
            else{
                capDist.put(StringArray[1], new HashMap<String, Integer>());
                capDist.get(StringArray[1]).put(StringArray[3], Integer.parseInt(StringArray[4]));
            }
        }
//fuck the distances, just care abt the bordering countries
        //stringArray[0] = name
        //US is not contained in the borders keyset, but United States is
        while(borderScan.hasNext()) {
            String getLine = borderScan.nextLine();//skip over header
            String[] StringArray = getLine.split("[=;]");//will need to then split args 1-n on space and throw away garbage
            borders.put(StringArray[0], new ArrayList<String>());// populates with key and empty list for adj countries
            //System.out.println("debugging: "+ StringArray[0]);
            String[] RHS = Arrays.copyOfRange(StringArray, 1, StringArray.length);// copies right hand side (not key), need to identify and throw out garbage
            for (String s : RHS) { //recall we previously split  on the semi colons
               // System.out.println(s);
                String[] fuckingStringArray = s.split(" ");// split on spaces
                String booty = ""; //empty string
                for(String fuck : fuckingStringArray){// a string in the fucking string array,, throwing out garbage
                    //its garbage if string contains a number ,. or km
                    if(!fuck.contains("0") && !fuck.contains("1") && !fuck.contains("2") && !fuck.contains("3") && !fuck.contains("4")&& !fuck.contains("5")&& !fuck.contains("6") && !fuck.contains("7") && !fuck.contains("8") && !fuck.contains("9")&& !fuck.contains(",") && !fuck.contains(".") && !fuck.equals("km")){//some countries are many words long
                        //System.out.println(fuck);
                        booty+= " " + fuck;// if not garbage, add to empty string, loops thru every string in the fsa
                        //System.out.println(booty);
                    }
                }
                booty = booty.strip(); //  :)) yeah.
                borders.get(StringArray[0]).add(booty); //IN EACH SEMICOLON BLOCK I STRIP THE NAME OF THE BORDERING COUNTRY AND THEN ADD IT TO BORDERS MAP
            }
        }
        //building 4th hashmap..here its a graph, in graph its a hashmap ,, called map

        for(String s : borders.keySet()){ //for a country in borders
            for(String p : borders.get(s)){//and for each bordering nation
                s = s.strip();
                p = p.strip();
///trying to handle parenthetical statements in the borders names,, they dont match state id
//                if(s.contains("(") && s.contains(")")){//if
//
//                    int loc = s.indexOf(" (");
//                    s = s.substring(0,loc);
//                    System.out.println("this one: " +s);
////                    String junk = "(";
////                    String[] stArr = StringArray[2].split(junk);
////                    StringArray[2] = stArr[0];
//                }
                if(p.equals("")){ //if an island
                    map.addVertex(s); //no edge to add, but need to add to map as vertex
                }
                else{ //p must be something
                    //s is key, p is a border,,, i can get the name of s but not its country code//state id
                    String fuckingStateIDs = stateID.get(s); //gets the state id for a country named in the borders file
                    //System.out.println("fuckingStateIDs: " + fuckingStateIDs);
                    String fuckingStateIDp = stateID.get(p);
                    //System.out.println("fuckingStateIDp: " + fuckingStateIDp);
                   // System.out.println("s: " + s);
                   // System.out.println("p: " + p);

                    //builds hashmap map, country, a border, km dist

                    if(fuckingStateIDs != null && fuckingStateIDp != null && capDist.containsKey(fuckingStateIDs) && capDist.containsKey(fuckingStateIDp)){//
                        if(s.equals("United States")){
                            s = "US";

                        }
                        map.addEdge(s,p, capDist.get(fuckingStateIDs).get(fuckingStateIDp));//bug
                    }

                    //System.out.println(capDist.get(fuckingStateIDs).get(fuckingStateIDp));


                }
            }
        }

    }//end of const.



    public int getDistance (String country1, String country2) {
        // Replace with your code
        // This function provides the shortest path distance between the capitals of the two countries passed as arguments.
        // If either of the countries does not exist or if the countries do not share a land border, this function must
        // return a value of -1. Examples are as found in Table 3.

        //this function should return the distance between 2 adj countries

        return map.getDistance(country1, country1);
    }


    public List<String> findPath (String country1, String country2) {
        //should use dijkstras to find the shortest path from country A to country B. A and B do not have to be adj,
        //but the shortest path needs to traverse through adj countries in order to link A and B
        // Replace with your code
        //repeat:
//priority queue, list of strings
        //v = least_cost_unknown_vertex()
        //known(v) = true
        //foreach n : adjacent(v)
        //if (cost(n) > cost(v) + edge_weight(v, n)
        //update_distance(n, v)
        //update_path(n, v)
        //... while v not null

        //select vertex w least cost, update know n to true, update paths and dist outgoing from

        // least cost vertex is the smallest dist in map, update distance, update known to true,

        //find smallest weight not already known


        HashMap<String, Integer> distances = new HashMap<>(); // Store distances
        HashMap<String, String> previous = new HashMap<>(); // Store previous nodes
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get)); // Priority queue to get the minimum distance

        for (String vertex : map.map.keySet()) {
            // Set the distance to the starting country to 0 and other countries to infinity
            distances.put(vertex, vertex.equals(country1) ? 0 : Integer.MAX_VALUE);
        }

        // Add the starting country to the priority queue
        priorityQueue.add(country1);

        while (!priorityQueue.isEmpty()) {
            String current = priorityQueue.poll();
            //debugging:
            //System.out.println("current: " + current);
//if and else for debugging
            // Loop through neighbors of the current country
            if (map.map.containsKey(current)) { // Check if the current country exists in the graph
                for (String neighbor : map.map.get(current).keySet()) {
                    //debugging:
                    //System.out.println("neighbor: " + neighbor);

                    int alt = distances.get(current) + map.getDistance(current, neighbor); // Calculate the alternative distance

                    if (alt < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        distances.put(neighbor, alt); // Update distance to the neighbor
                        previous.put(neighbor, current); // Set the previous node for the neighbor
                        priorityQueue.add(neighbor); // Add the neighbor to the priority queue
                    }
                }
            }
            else{
                System.out.println("Country " + current + " does not exist in the graph.");
                //put the country and its borders in map...
                //map.map.put(current,);
            }
        }

        // Reconstruct the path
        List<String> path = new ArrayList<>();
        String current = country2;

        while (current != null) {
            path.add(current);
            current = previous.getOrDefault(current, null);
        }

        Collections.reverse(path); // Reverse the path to get the correct order

        if (!path.isEmpty() && path.get(0).equals(country1)) {
            return path; // Return the path if found
        } else {
            return new ArrayList<>(); // Return an empty list if no path exists
        }
    }


    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the name of the first country (type EXIT to quit):");
            String country1 = scanner.nextLine().trim();

            if (country1.equalsIgnoreCase("EXIT")) {
                break;
            }

            if (!stateID.containsValue(country1) && !stateID.containsKey(country1)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            System.out.println("Enter the name of the second country (type EXIT to quit):");
            String country2 = scanner.nextLine().trim();

            if (country2.equalsIgnoreCase("EXIT")) {
                break;
            }

            if (!stateID.containsValue(country2) && !stateID.containsKey(country2)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            List<String> path = findPath(country1, country2);
            if (!path.isEmpty()) {
                System.out.println("Route from " + country1 + " to " + country2 + ":");
                for (int i = 0; i < path.size() - 1; i++) {
                    int distance = map.getDistance(path.get(i), path.get(i + 1));
                    System.out.println("* " + path.get(i) + " --> " + path.get(i + 1) + " (" + distance + " km.)");
                }
            } else {
                System.out.println("No path found between " + country1 + " and " + country2);
            }
        }
    }



//does it need to be able to handle both inputted country names and codes?

    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        //System.out.println("hi");
        a3.acceptUserInput();
    }

}
//weirdly, US is never an S value , and is not added as a vertex to map?,,, map isnt being built correctly?
//how do i ignore case in user input

//do i need to be able to ignore case in input?, do i need to be able to accept both counbtry codes and names?
//do i need to output total distance?