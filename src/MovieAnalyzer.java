import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MovieAnalyzer {
  List<movie> movies;
  List<Star> stars;

  public MovieAnalyzer(String dataset_path) throws IOException {
    movies = new ArrayList<>();
    stars = new ArrayList<>();
    BufferedReader br =
            new BufferedReader(new InputStreamReader(new FileInputStream(dataset_path), "UTF-8"));
    Pattern pattern = Pattern.compile("(,)?((\"[^\"]*(\"{2})*[^\"]*\")*[^,]*)");

    String line;
    line = br.readLine();
    String[] realArray = new String[16];

    while ((line = br.readLine()) != null) {
      Matcher matcher = pattern.matcher(line);
      int j = 0;
      while (matcher.find()) {
        String cell = matcher.group(2);
        Pattern pattern2 = Pattern.compile("\"((.)*)\"");
        Matcher matcher2 = pattern2.matcher(cell);
        if (matcher2.find()) {
          cell = matcher2.group(1);
        }
        if (j < 16)  {
          realArray[j++] = cell;
        }
      }
      movies.add(new movie(realArray[1], realArray[2], realArray[3], realArray[4],
              realArray[5], realArray[6], realArray[7], realArray[8], realArray[9],
                  realArray[10], realArray[11], realArray[12], realArray[13], realArray[14],
                  realArray[15]));
    }
    createStars();
  }

  public void createStars() {
    List<String> allStars = movies.stream()
            .flatMap(movie -> Arrays.stream(movie.Stars))
            .distinct()
            .sorted()
                .collect(Collectors.toList());

    List<movie> goodMovies = movies.stream()
                .distinct()
                .collect(Collectors.toList());

    for (int i = 0; i < allStars.size(); i++) {
      String name = allStars.get(i);
      if (name.equals("")) {
        continue;
      }
      Star star = new Star(name);
      for (int j = 0; j < goodMovies.size(); j++) {
        if (Arrays.asList(goodMovies.get(j).Stars).contains(name)) {
          star.takeMovie.add(goodMovies.get(j));
        }
      }
      star.averageRating = star.backAverageRating();
      star.averageGross = star.backAverageGross();
      stars.add(star);
    }
    stars.sort((o1, o2) -> o1.name.compareTo(o2.name));

  }

  public Map<Integer, Integer> getMovieCountByYear() {
    Map<Integer, Long> years = movies.stream()
            .distinct()
            .collect(Collectors.groupingBy(movie ->
                    movie.Released_Year, Collectors.counting()));

    List<Map.Entry<Integer, Long>> list = new ArrayList<>(years.entrySet());
    list.sort(new Comparator<Map.Entry<Integer, Long>>() {
          @Override
            public int compare(Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2) {
            return o2.getKey() - o1.getKey();
          }
        });
    Map<Integer, Integer> yearsNew = new LinkedHashMap<>();

    for (int i = 0; i < list.size(); i++) {
      Integer a = list.get(i).getValue().intValue();
      yearsNew.put(list.get(i).getKey(), a);
    }
    return yearsNew;
  }

  public Map<String, Integer> getMovieCountByGenre() {
    Map<String, Long> genres = movies.stream()
                .distinct()
                .flatMap(movie -> Arrays.stream(movie.Genre.split(", ")))
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()));

    List<Map.Entry<String, Long>> list = new ArrayList<>(genres.entrySet());
    list.sort(new Comparator<Map.Entry<String, Long>>() {
          @Override
          public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
          if (!o2.getValue().equals(o1.getValue())) {
            return (int) (o2.getValue() - o1.getValue());
          } else {
            return o1.getKey().compareTo(o2.getKey());
          }
        }
        });
    Map<String, Integer> genresNew = new LinkedHashMap<>();

    for (int i = 0; i < list.size(); i++) {
      Integer a = list.get(i).getValue().intValue();
      genresNew.put(list.get(i).getKey(), a);
    }
    return genresNew;
  }

    public Map<List<String>, Integer> getCoStarCount() {
    List<movie> moviesDistinct = movies.stream()
            .distinct()
            .collect(Collectors.toList());
    List<List<String>> total = new ArrayList<>();
    List<String> temp = new ArrayList<>();
    Map<List<String>, Integer> coStar = new HashMap<>();
    for (int i = 0; i < moviesDistinct.size(); i++) {
      movie movie = moviesDistinct.get(i);
      for (int j = 0; j < 3; j++) {
        for (int k = j + 1; k < 4; k++) {
          temp = new ArrayList<>();
          if (!movie.Stars[j].equals("") || !movie.Stars[k].equals("")) {
            temp.add(movie.Stars[j]);
            temp.add(movie.Stars[k]);
            Collections.sort(temp);
            total.add(temp);
          }
        }
    }
  }

        List<List<String>> tureTotal = total.stream()
                .distinct()
                .collect(Collectors.toList());

        for (int i = 0; i < tureTotal.size(); i++) {
            List<String> cur = tureTotal.get(i);
            long count = total.stream()
                    .filter(o -> o.equals(cur))
                    .count();

            coStar.put(tureTotal.get(i),(int)count);
        }

        List<Map.Entry<List<String>, Integer>> list = new ArrayList<>(coStar.entrySet());
        list.sort(new Comparator<Map.Entry<List<String>, Integer>>() {
            @Override
            public int compare(Map.Entry<List<String>, Integer> o1, Map.Entry<List<String>, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }        });
        Map<List<String> , Integer> coStarNew = new LinkedHashMap<>();
    for (int i = 0; i < list.size(); i++) {
            coStarNew.put(list.get(i).getKey(),list.get(i).getValue()) ;
        }
        return coStarNew;
    }

    public List<String> getTopMovies(int top_k, String by){
        if (by == "runtime") {
            List<String> runtime = movies.stream()
                    .filter(movie -> movie.Runtime != -1)
                    .filter(movie -> !movie.Series_Title .equals(""))
                    .sorted(new Comparator<movie>() {
                        @Override
                        public int compare(movie o1, movie o2) {
                            if (!o1.Runtime.equals(o2.Runtime)) {
                                return o2.Runtime - o1.Runtime;
                            } else return o1.Series_Title.compareTo(o2.Series_Title);
                        }
                    })
                    .limit(top_k)
                    .map(movie -> movie.Series_Title)
                    .collect(Collectors.toList());

            return runtime;

        }else if (by == "overview"){
            List<String> overview = movies.stream()
                    .filter(movie -> !movie.Overview.equals(""))
                    .sorted(new Comparator<movie>() {
                        @Override
                        public int compare(movie o1, movie o2) {
                            if (o1.Overview.length() != o2.Overview.length()) {
                                return o2.Overview.length() - o1.Overview.length();
                            } else return o1.Series_Title.compareTo(o2.Series_Title);
                        }
                    })
                    .limit(top_k)
                    .map(movie -> movie.Series_Title)
                    .collect(Collectors.toList());

            return overview;
        }else return null;
    }

    public List<String> getTopStars(int top_k, String by){
        List<String> result = new ArrayList<>();
        if (by.equals("rating")){
            result = stars.stream()
                    .sorted(new Comparator<Star>() {
                        @Override
                        public int compare(Star o1, Star o2) {
                            if (o2.averageRating > o1.averageRating) return 1;
                            else if (o2.averageRating < o1.averageRating) return -1;
                            else return o1.name.compareTo(o2.name);
                        }
                    })
                    .limit(top_k)
                    .map(star -> star.name)
                    .collect(Collectors.toList());
        }else if (by.equals("gross")){
            result = stars.stream()
                    .sorted(new Comparator<Star>() {
                        @Override
                        public int compare(Star o1, Star o2) {
                            if (o2.averageGross > o1.averageGross) {
                                return 1;
                            }
                            else if (o2.averageGross < o1.averageGross) {
                                return -1;
                            }
                            else return o1.name.compareTo(o2.name) ;
                        }
                    })
                    .limit(top_k)
                    .map(star -> star.name)
                    .collect(Collectors.toList());
        }
        return result;
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime){
        List<String> collect = movies.stream()
                .distinct()
                .filter(movie -> movie.Genre.contains(genre))
                .filter(movie -> movie.IMDB_Rating >=min_rating)
                .filter(movie -> movie.Runtime <= max_runtime)
                .map(movie -> movie.Series_Title)
                .sorted()
                .collect(Collectors.toList());
        return collect;
    }


}



