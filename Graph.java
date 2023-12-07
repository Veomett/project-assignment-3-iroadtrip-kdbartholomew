import java.util.HashMap;
public class Graph {
    public HashMap<String, HashMap<String, Integer>> map;

    Graph(){
        map = new HashMap<String, HashMap<String, Integer>>();

    }
//    //i was trying to make a getter bc map.get() was throwing errors when getdistance tried thta in the other class
//    int getDist(String country1, String country2){
//       int x = map.get(country1).get(country2);
//       return x;
//
//    }
// can i move this to here?
    public int getDistance (String country1, String country2) {
        // Replace with your code
        // This function provides the shortest path distance between the capitals of the two countries passed as arguments.
        // If either of the countries does not exist or if the countries do not share a land border, this function must
        // return a value of -1. Examples are as found in Table 3.

        //this function should return the distance between 2 adj countries

        if(map.get(country1) != null && map.get(country1).get(country2) != null){
            return map.get(country1).get(country2);
        }
        else{
            return -1;
        }
    }


    void addEdge(String vertex1, String vertex2, int weight){
        //case1: already have v1
        if(map.containsKey(vertex1)){
            map.get(vertex1).put(vertex2, weight);
        }
        //case 2:
        else{
            map.put(vertex1, new HashMap<String, Integer>());
            map.get(vertex1).put(vertex2, weight);
        }
    }
    void addVertex(String vertex1){//for handling islands
        if(!map.containsKey(vertex1)){
            map.put(vertex1, new HashMap<String, Integer>());
        }
    }

}
