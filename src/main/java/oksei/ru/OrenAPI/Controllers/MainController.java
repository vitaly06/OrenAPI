package oksei.ru.OrenAPI.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import oksei.ru.OrenAPI.Bot.Bot;
import oksei.ru.OrenAPI.DAO.TourDAO;
import oksei.ru.OrenAPI.Models.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tours")
public class MainController {
    @Autowired
    TourDAO tourDAO;
    public Bot bot = new Bot();
    public MainController() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
    @GetMapping("/getAllTours")
    public List<Tour> getAllTours() {
        return tourDAO.getAllTours();
    }
    @GetMapping("/{id}")
    public Tour getAllTours(@PathVariable int id) {
        return tourDAO.getTour(id);
    }

    @PostMapping("/addTour")
    public void addTour(HttpServletRequest request,
                        @RequestParam(value = "name") String  name,
                        @RequestParam("description") String description,
                        @RequestParam("time") int time,
                        @RequestParam("photo") MultipartFile photo) throws IOException{
        Tour tour = new Tour();
        tour.setName(name);
        tour.setDescription(description);
        tour.setTime(time);
        tour.setPhoto(photo.getBytes());
        tourDAO.addTour(tour);
    }

    @PostMapping("/record")
    public void newRecord(HttpServletRequest request, @RequestParam("number") String number,
                          @RequestParam("tour") String tour) {
        try {
            bot.sendAds(number + "\nПоездка: " + tour);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
