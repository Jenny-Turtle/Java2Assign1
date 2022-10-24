import java.util.ArrayList;
import java.util.List;

public class Star {
    String name;
    List<movie> takeMovie = new ArrayList<>();
    double averageRating = 0;
    double averageGross = 0;
    public Star(String name){
        this.name = name;
    }

    public void addMovie(movie movie){
        takeMovie.add(movie);
    }

    public double backAverageRating(){
        double total = 0;
        double num = 0;
        for (int i = 0; i < takeMovie.size(); i++) {
            if (takeMovie.get(i).IMDB_Rating != -1) {
                total += takeMovie.get(i).IMDB_Rating;
                num++;
            }
        }
        if (num == 0) return 0;
        else return total/num;
    }

    public double backAverageGross(){
        double total = 0;
        double num = 0;
        for (int i = 0; i < takeMovie.size(); i++) {
            if (takeMovie.get(i).Gross != -1){
                total += takeMovie.get(i).Gross ;
                num++;
            }

        }
        if (num == 0) return 0;
        else return total/num;
    }

}
