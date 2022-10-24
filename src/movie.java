import java.util.ArrayList;
import java.util.List;

public class movie {
    public String Series_Title;
    public Integer Released_Year;
    public String Certificate;
    public Integer Runtime;
    public String Genre;
    public Float IMDB_Rating;
    public String Overview;
    public Integer Meta_score;
    public String Director;
    public String[] Stars = new String[4];
    public Integer Noofvotes;
    double  Gross;

    public List<String> listStars = new ArrayList<>();

    public movie(String series_Title, String released_Year, String certificate,
                 String runtime, String genre, String IMDB_Rating, String overview,
                 String meta_score, String director, String Stars1, String Stars2, String Stars3, String Stars4,
                 String noofvotes, String gross) {

        Series_Title = series_Title;

        Released_Year = Integer.parseInt(released_Year);

        Certificate = certificate;
        if (!runtime.equals("")){
            Runtime = Integer.parseInt(runtime.replace(" min",""));
        }else Runtime = -1;

        Genre = genre;

        if (!IMDB_Rating.equals("")){
            this.IMDB_Rating = Float.parseFloat(IMDB_Rating);
        }else this.IMDB_Rating = (float) -1;

        Overview = overview;

        if (!meta_score.equals("")){
            Meta_score = Integer.parseInt(meta_score);
        }else Meta_score = 0;
        Director = director;
        Stars[0] = Stars1;
        Stars[1] = Stars2;
        Stars[2] = Stars3;
        Stars[3] = Stars4;
        listStars.add(Stars1);
        listStars.add(Stars2);
        listStars.add(Stars3);
        listStars.add(Stars4);

        Noofvotes = Integer.parseInt(noofvotes);
        if (!gross.equals("")) {
            String[] a = gross.replace("\"","").split(",");
            double num = 0;
            for (int i = 0; i < a.length; i++) {
                num = num + Integer.parseInt(a[i])*Math.pow(10,(a.length-i-1)*3);
            }
            Gross = num;
        }else Gross = -1;
    }
}
