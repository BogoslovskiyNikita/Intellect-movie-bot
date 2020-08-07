import com.truedev.kinoposk.api.model.film.FilmExt;
import com.truedev.kinoposk.api.service.KinopoiskApiService;

public class Kinopoisk {

    /**
     * Этот метод генерирует случайный фильм
     */
    public static FilmExt generateMovie() {
        try {
            KinopoiskApiService kinopoiskApiService = new KinopoiskApiService();
            int id = generateID(1, 10000);
            FilmExt film = kinopoiskApiService.getFilmInfo(id);
            return film;
        } catch (NullPointerException e) {
            return generateMovie();
        }
    }

    /**
     * Этот метод возвращает описание фильма
     */
    public static String formDescription(FilmExt movie) {
        String result = "";
        if (movie.getData().getNameRU() == null) {
            result += movie.getData().getNameEN();
        } else {
            result += movie.getData().getNameRU() + " (" + movie.getData().getNameEN() + ")" + "\n";
        }
        result += "Год выхода: " + movie.getData().getYear() + "\n";
        result += "Жанр: " + movie.getData().getGenre() + "\n";
        if (movie.getData().getRatingAgeLimits() != null) {
            result += "Возрастное ограничение: " + movie.getData().getRatingAgeLimits() + "+";
        }
        return result;
    }

    /**
     * Этот метод генерирует случайное число в диапазоне от a до b
     */
    private static int generateID(int a, int b) {
        return a + (int) (Math.random() * ((b - a) + 1));
    }
}
