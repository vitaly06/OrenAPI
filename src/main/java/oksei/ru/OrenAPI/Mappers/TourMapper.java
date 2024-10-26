package oksei.ru.OrenAPI.Mappers;

import oksei.ru.OrenAPI.Models.Tour;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class TourMapper implements RowMapper<Tour> {
    @Override
    public Tour mapRow(ResultSet resultSet, int i) throws SQLException {
        Tour tour = new Tour();
        try{
            tour.setId(resultSet.getInt("id"));
            tour.setName(resultSet.getString("name"));
            tour.setDescription(resultSet.getString("description"));
            tour.setTime(resultSet.getInt("time"));
            tour.setPhoto(resultSet.getBytes("photo"));
        }
        catch (Exception e){
            System.out.println("Exception => " + e.getMessage());
            return new Tour();
        }
        return tour;
    }
}
