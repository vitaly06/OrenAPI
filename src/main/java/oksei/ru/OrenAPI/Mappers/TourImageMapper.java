package oksei.ru.OrenAPI.Mappers;


import oksei.ru.OrenAPI.Models.TourImage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TourImageMapper implements RowMapper<TourImage> {
    @Override
    public TourImage mapRow(ResultSet resultSet, int i) throws SQLException {
        TourImage ti = new TourImage();
        try{
            ti.setPhotoUrl(resultSet.getString("photoUrl"));
            ti.setTourId(resultSet.getInt("tourId"));
        }
        catch (Exception e){
            System.out.println("Exception => " + e.getMessage());
            return new TourImage();
        }
        return ti;
    }
}
