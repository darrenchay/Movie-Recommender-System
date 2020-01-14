//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EfficientRater implements Rater {
    private String myID;
    private HashMap<String, Rating> myRatings;

    public EfficientRater(String id) {
        this.myID = id;
        this.myRatings = new HashMap();
    }

    public void addRating(String item, double rating) {
        this.myRatings.put(item, new Rating(item, rating));
    }

    public boolean hasRating(String item) {
        return this.myRatings.containsKey(item);
    }

    public String getID() {
        return this.myID;
    }

    public double getRating(String item) {
        return this.myRatings.containsKey(item) ? ((Rating)this.myRatings.get(item)).getValue() : -1.0D;
    }

    public int numRatings() {
        return this.myRatings.size();
    }

    public ArrayList<String> getItemsRated() {
        ArrayList<String> list = new ArrayList();
        Iterator var2 = this.myRatings.keySet().iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            list.add(((Rating)this.myRatings.get(s)).getItem());
        }

        return list;
    }
}
