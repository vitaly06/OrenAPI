package oksei.ru.OrenAPI.DAO;

import oksei.ru.OrenAPI.Mappers.TourMapper;
import oksei.ru.OrenAPI.Models.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TourDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TourDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TourDAO() {
        jdbcTemplate = null;
    }

    public void addTour(Tour tour) {
        assert jdbcTemplate != null;
        jdbcTemplate.update("INSERT INTO tours(name, description, time, photo) VALUES(?, ?, ?, ?)",
                tour.getName(), tour.getDescription(), tour.getTime(), tour.getPhoto());
    }

    public Tour getTour(int id) {
        assert jdbcTemplate != null;
        return jdbcTemplate.query("SELECT * FROM tours WHERE id = ?", new Object[]{id}, new TourMapper())
                .stream().findAny().orElseThrow(null);
    }

    public List<Tour> getAllTours() {
        assert jdbcTemplate != null;
        return jdbcTemplate.query("SELECT * FROM tours", new TourMapper());
    }
}
