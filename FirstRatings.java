

import edu.duke.FileResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.csv.CSVRecord;

public class FirstRatings {
    public FirstRatings() {
    }

    public ArrayList<Movie> loadMovies(String filename) {
        FileResource fr = new FileResource(filename);
        ArrayList<Movie> movies = new ArrayList();
        Iterator var4 = fr.getCSVParser().iterator();

        while(var4.hasNext()) {
            CSVRecord rec = (CSVRecord)var4.next();
            if (!movies.contains(new Movie(rec.get(0), rec.get(1), rec.get(2), rec.get(4), rec.get(5), rec.get(3), rec.get(7), Integer.parseInt(rec.get(6))))) {
                movies.add(new Movie(rec.get(0), rec.get(1), rec.get(2), rec.get(4), rec.get(5), rec.get(3), rec.get(7), Integer.parseInt(rec.get(6))));
            }
        }

        return movies;
    }

    public void testLoadMovies() {
        ArrayList<Movie> movies = this.loadMovies("data/ratedmoviesfull.csv");
        System.out.println("Printing out Movies: ");
        System.out.println("Number of movies in file: " + movies.size());
        int countComedy = 0;
        int countTime = 0;
        int countDirectors = false;

        for(int i = 0; i < movies.size(); ++i) {
            if (((Movie)movies.get(i)).getGenres().contains("Comedy")) {
                ++countComedy;
            }

            if (((Movie)movies.get(i)).getMinutes() > 150) {
                ++countTime;
            }
        }

        Movie m = (Movie)movies.get(10);
        System.out.println(m);
        System.out.println("Number of Comedy movies: " + countComedy);
        System.out.println("Number of Movies with time greater than 150 Minutes: " + countTime);
        HashMap<String, Integer> map = this.getGreatest(movies);
        int countDir = 0;
        Iterator var8 = map.keySet().iterator();

        String s;
        while(var8.hasNext()) {
            s = (String)var8.next();
            int count = (Integer)map.get(s);
            if (count > countDir) {
                countDir = count;
            }
        }

        var8 = map.keySet().iterator();

        while(var8.hasNext()) {
            s = (String)var8.next();
            if ((Integer)map.get(s) == countDir) {
                System.out.println(s + "\t" + map.get(s));
            }
        }

    }

    private HashMap<String, Integer> getGreatest(ArrayList<Movie> movies) {
        HashMap<String, Integer> list = new HashMap();

        for(int i = 0; i < movies.size(); ++i) {
            String dirTemp = ((Movie)movies.get(i)).getDirector();
            if (dirTemp.contains(",")) {
                String[] dir = dirTemp.split(",");

                for(int k = 0; k < dir.length; ++k) {
                    if (!list.containsKey(dir[k])) {
                        list.put(dir[k], 1);
                    } else {
                        list.get(dir[k]);
                        list.put(dir[k], (Integer)list.get(dir[k]) + 1);
                    }
                }
            } else if (!list.containsKey(dirTemp)) {
                list.put(dirTemp, 1);
            } else {
                list.get(dirTemp);
                list.put(dirTemp, (Integer)list.get(dirTemp) + 1);
            }
        }

        return list;
    }

    public ArrayList<Rater> loadRaters(String filename) {
        ArrayList<Rater> raters = new ArrayList();
        FileResource fr = new FileResource(filename);
        Iterator var4 = fr.getCSVParser().iterator();

        while(var4.hasNext()) {
            CSVRecord rec = (CSVRecord)var4.next();
            if (!this.checkRater(rec.get(0), raters)) {
                Rater r = new EfficientRater(rec.get(0));
                r.addRating(rec.get(1), (double)Integer.parseInt(rec.get(2)));
                raters.add(r);
            } else {
                Rater r = (Rater)raters.get(this.getRaterIndex(rec.get(0), raters));
                r.addRating(rec.get(1), (double)Integer.parseInt(rec.get(2)));
            }
        }

        return raters;
    }

    private boolean checkRater(String id, ArrayList<Rater> raters) {
        for(int k = 0; k < raters.size(); ++k) {
            if (((Rater)raters.get(k)).getID().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public int getRaterIndex(String id, ArrayList<Rater> raters) {
        for(int k = 0; k < raters.size(); ++k) {
            if (((Rater)raters.get(k)).getID().equals(id)) {
                return k;
            }
        }

        return -1;
    }

    public void testLoadRaters(String id, String movie) {
        ArrayList<Rater> raters = this.loadRaters("data/ratings_short.csv");
        System.out.println("Number of Raters: " + raters.size());

        for(int i = 0; i < raters.size(); ++i) {
            System.out.println(((Rater)raters.get(i)).getID() + "\t" + ((Rater)raters.get(i)).numRatings());
            ArrayList<String> list = this.getRating((Rater)raters.get(i));
            System.out.println(list);
            int max = this.findMax(raters);
            if (this.getRating((Rater)raters.get(i)).size() == max) {
                System.out.println("rater " + ((Rater)raters.get(i)).getID() + " has a maximum number of ratings, which is : " + this.getRating((Rater)raters.get(i)).size());
            }
        }

        ArrayList<String> rates = this.getRates(id, raters);
        System.out.println("Number of movies " + id + " rated: " + rates.size());
        System.out.println("Number of People who rated the movie " + movie + " is: " + this.findNoRatingsMovie(movie, raters));
        System.out.println("Number of different movies rated by all raters: " + this.countUniqueMovies(raters));
    }

    public ArrayList<String> getRating(Rater r) {
        ArrayList<String> list = r.getItemsRated();
        ArrayList<String> ratings = new ArrayList();

        for(int k = 0; k < list.size(); ++k) {
            double rate = r.getRating((String)list.get(k));
            String ratingdata = (String)list.get(k) + " " + rate;
            ratings.add(ratingdata);
        }

        return ratings;
    }

    public ArrayList<String> getRates(String id, ArrayList<Rater> raters) {
        int idx = this.getRaterIndex(id, raters);
        ArrayList<String> rates = this.getRating((Rater)raters.get(idx));
        return rates;
    }

    public int findMax(ArrayList<Rater> raters) {
        int max = 0;

        for(int k = 0; k < raters.size(); ++k) {
            if (this.getRating((Rater)raters.get(k)).size() > max) {
                max = this.getRating((Rater)raters.get(k)).size();
            }
        }

        return max;
    }

    public int findNoRatingsMovie(String movie, ArrayList<Rater> raters) {
        int count = 0;

        for(int k = 0; k < raters.size(); ++k) {
            if (((Rater)raters.get(k)).getItemsRated().contains(movie)) {
                ++count;
            }
        }

        return count;
    }

    public int countUniqueMovies(ArrayList<Rater> raters) {
        ArrayList<String> movies = new ArrayList();

        for(int k = 0; k < raters.size(); ++k) {
            ArrayList<String> list = ((Rater)raters.get(k)).getItemsRated();

            for(int i = 0; i < list.size(); ++i) {
                if (!movies.contains(list.get(i))) {
                    movies.add(list.get(i));
                }
            }
        }

        return movies.size();
    }

    public double sumRatingsMovie(ArrayList<Rater> raters, String movie) {
        double sum = 0.0D;

        for(int k = 0; k < raters.size(); ++k) {
            if (((Rater)raters.get(k)).getItemsRated().contains(movie)) {
                sum += ((Rater)raters.get(k)).getRating(movie);
            }
        }

        return sum;
    }

    public double getAverage(ArrayList<Rater> raters, String movie) {
        double sum = 0.0D;
        int count = 0;

        for(int k = 0; k < raters.size(); ++k) {
            if (((Rater)raters.get(k)).getItemsRated().contains(movie)) {
                sum += ((Rater)raters.get(k)).getRating(movie);
                ++count;
            }
        }

        return sum / (double)count;
    }
}
