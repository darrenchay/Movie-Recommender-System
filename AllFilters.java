
import java.util.ArrayList;
import java.util.Iterator;

public class AllFilters implements Filter {
    ArrayList<Filter> filters = new ArrayList();

    public AllFilters() {
    }

    public void addFilter(Filter f) {
        this.filters.add(f);
    }

    public boolean satisfies(String id) {
        Iterator var2 = this.filters.iterator();

        Filter f;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            f = (Filter)var2.next();
        } while(f.satisfies(id));

        return false;
    }
}
