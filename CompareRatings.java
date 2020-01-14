

import java.util.Comparator;

public class CompareRatings implements Comparator<Rating> {
    public CompareRatings() {
    }

    public int compare(Rating r1, Rating r2) {
        double rate1 = r1.getValue();
        double rate2 = r2.getValue();
        return -1 * Double.compare(rate1, rate2);
    }
}
