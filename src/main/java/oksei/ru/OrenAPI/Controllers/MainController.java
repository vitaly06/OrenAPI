package oksei.ru.OrenAPI.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import oksei.ru.OrenAPI.Bot.Bot;
import oksei.ru.OrenAPI.DAO.TourDAO;
import oksei.ru.OrenAPI.DAO.TourImageDAO;
import oksei.ru.OrenAPI.Models.Tour;
import oksei.ru.OrenAPI.Models.TourImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tours")
public class MainController {
    @Autowired
    TourDAO tourDAO;
    @Autowired
    TourImageDAO tourImageDAO;
    public Bot bot = new Bot();
    private final String uploadDir = "uploads/";
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

    @GetMapping("/photos/{id}")
    public List<TourImage> getTourImage(@PathVariable int id) {
        return tourImageDAO.getImagesById(id);
    }

    @PostMapping("/addTour")
    public ResponseEntity<String> addTour(HttpServletRequest request,
                        @RequestParam(value = "photo") MultipartFile photo,
                        @RequestParam(value = "photo1", required = false) MultipartFile photo1,
                        @RequestParam(value = "photo2", required = false) MultipartFile photo2,
                        @RequestParam(value = "photo3", required = false) MultipartFile photo3,
                        @RequestParam(value = "photo4", required = false) MultipartFile photo4,
                        @RequestParam(value = "photo5", required = false) MultipartFile photo5,
                        @RequestParam(value = "photo6", required = false) MultipartFile photo6,
                        @RequestParam(value = "name") String  name,
                        @RequestParam("description") String description,
                                          @RequestParam("time") int time) throws IOException{
        try {
            List<MultipartFile> photos = Arrays.asList(photo1, photo2, photo3, photo4, photo5, photo6);
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            String filePath = uploadDir + fileName;
            // Создаем директорию, если она не существует
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Сохраняем файл на сервере
            File file = new File(filePath);
            File dest = new File(file.getAbsolutePath());
            photo.transferTo(dest);
            Tour tour = new Tour();
            tour.setName(name);
            tour.setDescription(description);
            tour.setTime(time);
            tour.setPhotoUrl("/tours/images/" + fileName);
            tourDAO.addTour(tour);
            int id = tourDAO.getMaxId(); // максимальный id в БД
            TourImage ti = new TourImage();
            ti.setPhotoUrl("tours/images/" + fileName);
            ti.setTourId(id);

            // Остальные загруженные фотки
            for(MultipartFile forPhoto : photos){
                if (forPhoto != null) {
                    fileName = System.currentTimeMillis() + "_" + forPhoto.getOriginalFilename();
                    filePath = uploadDir + fileName;
                    file = new File(filePath);
                    dest = new File(file.getAbsolutePath());
                    forPhoto.transferTo(dest);
                    ti = new TourImage();
                    ti.setPhotoUrl("tours/images/" + fileName);
                    ti.setTourId(id);
                    tourImageDAO.addTourImage(ti);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Tour added successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving tour");
        }
    }

    @GetMapping("/images/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header("Content-Type", Files.probeContentType(file))
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
