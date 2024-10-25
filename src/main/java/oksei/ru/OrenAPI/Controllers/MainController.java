package oksei.ru.OrenAPI.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import oksei.ru.OrenAPI.DAO.TourDAO;
import oksei.ru.OrenAPI.Models.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/tours")
public class MainController {
    @Autowired
    TourDAO tourDAO;

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
                        @RequestParam("price") int price,
                        @RequestPart("photo") MultipartFile photo) throws IOException{
        Tour tour = new Tour();
        tour.setName(name);
        tour.setDescription(description);
        tour.setTime(time);
        tour.setPrice(price);
        byte[] imageData = photo.getBytes();
        tour.setPhoto(imageData);
        tourDAO.addTour(tour);
    }
}
