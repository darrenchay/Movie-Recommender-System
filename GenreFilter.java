
public class GenreFilter implements Filter {
    private String[] genre;

    public GenreFilter(String agenre) {
        if (agenre.contains(",")) {
            this.genre = agenre.split(",");
        } else {
            this.genre = new String[1];
            this.genre[0] = agenre;
        }

    }

    public boolean satisfies(String id) {
        boolean ans = false;

        for(int k = 0; k < this.genre.length; ++k) {
            if (MovieDatabase.getCountry(id).contains(this.genre[k])) {
                ans = true;
            }
        }

        return ans;
    }
}
