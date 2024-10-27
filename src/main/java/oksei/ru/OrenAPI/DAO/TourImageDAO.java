package oksei.ru.OrenAPI.DAO;

import oksei.ru.OrenAPI.Mappers.TourImageMapper;
import oksei.ru.OrenAPI.Models.TourImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TourImageDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public TourImageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TourImageDAO() {
        jdbcTemplate = null;
    }

    public List<TourImage> getImagesById(int id){
        return jdbcTemplate.query("SELECT * FROM tourImages WHERE tourId = ?",
                new Object[]{id}, new TourImageMapper());
    }

    public void addTourImage(TourImage tourImage){
        jdbcTemplate.update("INSERT INTO tourImages(photoUrl, tourId) VALUES(?, ?)", tourImage.getPhotoUrl(),
                tourImage.getTourId());
    }
}
