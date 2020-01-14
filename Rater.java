

import java.util.ArrayList;

public interface Rater {
    void addRating(String var1, double var2);

    boolean hasRating(String var1);

    String getID();

    double getRating(String var1);

    int numRatings();

    ArrayList<String> getItemsRated();
}
