
import java.util.ArrayList;
import java.util.Random;

public class RecommendationRunner implements Recommender {
    public RecommendationRunner() {
    }

    public ArrayList<String> getItemsToRate() {
        MovieDatabase.initialize("ratedmoviesfull.csv");
        AllFilters af = new AllFilters();
        Random rand = new Random();
        af.addFilter(new YearAfterFilter(2010));
        ArrayList<String> temp = MovieDatabase.filterBy(af);
        ArrayList<String> movies = new ArrayList();
        ArrayList<Integer> inte = new ArrayList();

        for(int k = 0; k < 20; ++k) {
            int idx = rand.nextInt(temp.size());
            inte.add(idx);

            while(inte.contains(idx)) {
                idx = rand.nextInt(temp.size());
            }

            movies.add(temp.get(idx));
        }

        return movies;
    }

    public void printRecommendationsFor(String webRaterID) {
        RaterDatabase.initialize("ratings.csv");
        MovieDatabase.initialize("ratedmoviesfull.csv");

        try {
            FourthRatings fr = new FourthRatings();
            ArrayList<Rating> ratings = fr.getSimilarRatings(webRaterID, 20, 5);
            System.out.println("<html><h2><b>Movie Recommender</b></h2>");
            System.out.println("<style>.table{text-align:center;}h3 {font-size: 15px;text-align: justify;} .header{font-family:Arial;text-align:center; font-size:15px; border: 3px solid black} .movie {border: 3px solid black;border-collapse: collapse; border-radius:1px } h2 {font-family: Arial;font-size:50px;color: teal;text-align: center;} body {background-image: url('https://media0.giphy.com/media/26BRyql7J3iOx875u/200w.gif');background-color: lightsteelblue;animation: colorchange 50s;-webkit-animation: colorchange 50s;}</style>");
            System.out.println("<h3>Found " + ratings.size() + " movies matching your taste.</h3>");
            if (ratings.size() > 15) {
                System.out.println("<p>Printing out the first 15 movies.</p>");
            }

            System.out.println("<table class = 'movie'><th class='header'>Movie</th> <th class='header'>Poster</th><th class='header'>Release Date</th> <th class = 'header'>Genre(s)</th> <th class='header'>Director(s)</th> <th class ='header'>Duration(Mins)</th>");

            for(int k = 0; k < ratings.size() && k < 15; ++k) {
                System.out.println("<tr><td class='table'>" + MovieDatabase.getTitle(((Rating)ratings.get(k)).getItem()) + "</td>");
                System.out.print("<td class='table'> <img src=\"" + MovieDatabase.getPoster(((Rating)ratings.get(k)).getItem()) + "\"; height=100px;width=100px;/></td>");
                System.out.print("<td class='table'>" + MovieDatabase.getYear(((Rating)ratings.get(k)).getItem()) + "</td>");
                System.out.print("<td class='table'>" + MovieDatabase.getCountry(((Rating)ratings.get(k)).getItem()) + "</td>");
                System.out.print("<td class='table'>" + MovieDatabase.getGenres(((Rating)ratings.get(k)).getItem()) + "</td>");
                System.out.println("<td class='table'>" + MovieDatabase.getMinutes(((Rating)ratings.get(k)).getItem()) + "</td></tr>");
            }

            System.out.println("</table>");
        } catch (Exception var5) {
            System.out.println("<p>Error: No recommendations found. Please go back to the previous page and rate at least 15 movies(if there are no movies you watched, refresh that page)</p>");
        }

    }
}
