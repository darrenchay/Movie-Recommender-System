
public class Rating implements Comparable<Rating> {
    private String item;
    private double value;

    public Rating(String anItem, double aValue) {
        this.item = anItem;
        this.value = aValue;
    }

    public String getItem() {
        return this.item;
    }

    public double getValue() {
        return this.value;
    }

    public String toString() {
        return "[" + this.getItem() + ", " + this.getValue() + "]";
    }

    public int compareTo(Rating other) {
        if (this.value < other.value) {
            return -1;
        } else {
            return this.value > other.value ? 1 : 0;
        }
    }
}
