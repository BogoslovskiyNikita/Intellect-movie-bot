import com.truedev.kinoposk.api.model.film.FilmExt;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.io.File;


public class Bot extends TelegramLongPollingBot {

    /**
     * Регестрируем нашего бота в сети.
     */
    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new Bot());
    }

    /**
     * Метод для отправки сообщения (с картинкой).
     */
    public void sendMsg(Message message, String text, String posterUrl) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setPhoto(posterUrl);

        execute(sendMessage);
        execute(sendPhoto);
    }

    /**
     * Метод для отправки сообщения (без картинки).
     * Переменная reply указывает, будет ли сообщение ответом на последнее сообщение пользователя
     */
    public void sendMsg(Message message, String text, boolean reply) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        if (reply) {
            sendMessage.setReplyToMessageId(message.getMessageId());
        }
        execute(sendMessage);
    }

    /**
     * Обрабатываем новое поступившее сообщение.
     */
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        FilmExt movie = Kinopoisk.generateMovie();

        if (message != null && message.hasText()) {
            if (message.getText().equals("/start")) {
                try {
                    sendMsg(message, "Привет! Этот бот умеет советовать фильмы, просто напиши " +
                            "слово \'Фильм\'", true);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (message.getText().toLowerCase().matches(".*фильм.*")) {
                try {
                    sendMsg(message, Kinopoisk.formDescription(movie), movie.getData().getBigPosterUrl());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendMsg(message, "Извини, я тебя не понял. Попробуй ещё раз.", true);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * Указываем имя бота
     */
    public String getBotUsername() {
        return "Имя бота";
    }

    /**
     * Указываем токен
     */
    public String getBotToken() {
        return "Токен";
    }

    /**
     * Вспомогательный метод, создает экземпляр класса File по URL
     */
    private static File getFileFromUrl(String link) throws IOException {
        URL url = new URL(link);
        BufferedImage img = ImageIO.read(url);
        File file = new File("image.jpg");
        ImageIO.write(img, "jpg", file);
        return file;
    }
}
