

import java.util.ArrayList;
import java.util.Collections;

public class FourthRatings {
    public FourthRatings() {
    }

    private double getAverageByID(String mID, int minimalRaters) {
        FirstRatings fr = new FirstRatings();
        if (fr.findNoRatingsMovie(mID, RaterDatabase.getRaters()) < minimalRaters) {
            return 0.0D;
        } else {
            double avg = fr.getAverage(RaterDatabase.getRaters(), mID);
            return avg;
        }
    }

    public double getAverage(String mID, int minimalRaters) {
        return this.getAverageByID(mID, minimalRaters);
    }

    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        ArrayList<Rating> avgMovies = new ArrayList();

        for(int k = 0; k < movies.size(); ++k) {
            double avg = this.getAverageByID((String)movies.get(k), minimalRaters);
            if (avg > 0.0D) {
                avgMovies.add(new Rating((String)movies.get(k), avg));
            }
        }

        return avgMovies;
    }

    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
        ArrayList<String> filteredmovies = MovieDatabase.filterBy(filterCriteria);
        ArrayList<Rating> avgMovies = new ArrayList();

        for(int k = 0; k < filteredmovies.size(); ++k) {
            double avg = this.getAverageByID((String)filteredmovies.get(k), minimalRaters);
            if (avg > 0.0D) {
                avgMovies.add(new Rating((String)filteredmovies.get(k), avg));
            }
        }

        return avgMovies;
    }

    private double dotProduct(Rater me, Rater r) {
        double sum = 0.0D;
        ArrayList<String> myratings = me.getItemsRated();
        ArrayList<String> Rratings = r.getItemsRated();
        ArrayList<Rating> myrating = new ArrayList();
        ArrayList<Rating> Rrating = new ArrayList();

        int k;
        double temp;
        double val;
        for(k = 0; k < myratings.size(); ++k) {
            temp = me.getRating((String)myratings.get(k));
            val = temp - 5.0D;
            myrating.add(new Rating((String)myratings.get(k), val));
        }

        for(k = 0; k < Rratings.size(); ++k) {
            temp = r.getRating((String)Rratings.get(k));
            val = temp - 5.0D;
            Rrating.add(new Rating((String)Rratings.get(k), val));
        }

        for(k = 0; k < myratings.size(); ++k) {
            if (Rratings.contains(((Rating)myrating.get(k)).getItem())) {
                int idx = this.index(Rrating, myrating, k);
                double val = ((Rating)myrating.get(k)).getValue() * ((Rating)Rrating.get(idx)).getValue();
                sum += val;
            }
        }

        return sum;
    }

    private int index(ArrayList<Rating> Rrating, ArrayList<Rating> myrating, int k) {
        int idx = 0;

        for(int i = 0; i < Rrating.size(); ++i) {
            if (((Rating)Rrating.get(i)).getItem().equals(((Rating)myrating.get(k)).getItem())) {
                idx = i;
            }
        }

        return idx;
    }

    private ArrayList<Rating> getSimilarities(String id) {
        Rater ID = RaterDatabase.getRater(id);
        ArrayList<Rater> rater = RaterDatabase.getRaters();
        ArrayList<Rating> ratings = new ArrayList();

        for(int k = 0; k < rater.size(); ++k) {
            if (!((Rater)rater.get(k)).getID().equals(id)) {
                double dp = this.dotProduct(ID, (Rater)rater.get(k));
                if (dp > 0.0D) {
                    ratings.add(new Rating(((Rater)rater.get(k)).getID(), dp));
                }
            }
        }

        Collections.sort(ratings, new CompareRatings());
        return ratings;
    }

    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) {
        ArrayList<Rating> SimilarRaters = this.getSimilarities(id);
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        ArrayList<Rating> avgMovies = new ArrayList();

        for(int k = 0; k < movies.size(); ++k) {
            double weightedavg = 0.0D;
            int count = 0;

            for(int i = 0; i < numSimilarRaters; ++i) {
                Rater r = RaterDatabase.getRater(((Rating)SimilarRaters.get(i)).getItem());
                if (r.hasRating((String)movies.get(k))) {
                    double avg = r.getRating((String)movies.get(k)) * ((Rating)SimilarRaters.get(i)).getValue();
                    weightedavg += avg;
                    ++count;
                }
            }

            if (count >= minimalRaters) {
                double newavg = weightedavg / (double)count;
                if (!RaterDatabase.getRater(id).getItemsRated().contains(movies.get(k))) {
                    avgMovies.add(new Rating((String)movies.get(k), newavg));
                }
            }
        }

        Collections.sort(avgMovies, new CompareRatings());
        return avgMovies;
    }

    public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters, int minimalRaters, Filter filterCriteria) {
        ArrayList<Rating> SimilarRaters = this.getSimilarities(id);
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        ArrayList<Rating> avgMovies = new ArrayList();

        for(int k = 0; k < movies.size(); ++k) {
            double weightedavg = 0.0D;
            int count = 0;

            for(int i = 0; i < numSimilarRaters; ++i) {
                Rater r = RaterDatabase.getRater(((Rating)SimilarRaters.get(i)).getItem());
                if (r.hasRating((String)movies.get(k))) {
                    double avg = r.getRating((String)movies.get(k)) * ((Rating)SimilarRaters.get(i)).getValue();
                    weightedavg += avg;
                    ++count;
                }
            }

            if (count >= minimalRaters) {
                double newavg = weightedavg / (double)count;
                avgMovies.add(new Rating((String)movies.get(k), newavg));
            }
        }

        Collections.sort(avgMovies, new CompareRatings());
        return avgMovies;
    }
}
