package api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import java.util.Random;
import java.util.HashMap;

public class APIRequest {
    private Gson gson;
    private Random rand;
    private static HashMap<String, Restaurant[]> cache = new HashMap<>();
    public static int cacheHits = 0;
    public static int cacheMisses = 0;
    public static String term;
    public static String location;
    private static final String[] LOCATIONS = 
            new String[]{"Pasadena, CA", "Woodland Hills, CA", "Glendale, CA", "Santa Monica, CA", "Anaheim, CA"};
    private static final String[] CUISINES =
            new String[]{"tacos", "sushi", "pizza", "burgers", "american", "sandwiches"};

    public APIRequest() {
        this.rand = new Random();
        this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    /**
     * Sends a request to the API and returns a list of Restaurant objects created by Gson
     * @return an array of Restaurant objects
     */
    public Restaurant[] send() {
        YelpAPI api = new YelpAPI();

        // First randomly choose our search params
        term = getRandomCuisine();
        location = getRandomLocation();

        // Check the cache before sending a request to Yelp
        if(cache.get(term + location) != null) {
            cacheHits++;
            
            // We've already made this request, so load it from cache instead of calling Yelp
            return cache.get(term + location);
        } else {
            // This is a new request so we have to retrieve it from the API
            String[] query = {"--term", term, "--location", location};
            String responseString = api.query(query);
            APIResponse response = gson.fromJson(responseString, APIResponse.class);
    
            Restaurant[] restaurants = response.getBusinesses();

            // Cache these results for quicker retrieval later
            cache.put(term + location, restaurants);

            cacheMisses++;
            return restaurants;
        }
    }

    /**
     * Chooses a random location from our list
     * @return the location
     */
    public String getRandomLocation() {
        int index = rand.nextInt(LOCATIONS.length);
        return LOCATIONS[index];
    }

    /**
     * Chooses a random search term from the list
     * @return the search term
     */
    public String getRandomCuisine() {
        int index = rand.nextInt(CUISINES.length);
        return CUISINES[index];
    }
}
