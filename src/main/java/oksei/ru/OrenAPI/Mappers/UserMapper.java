package oksei.ru.OrenAPI.Mappers;

import oksei.ru.OrenAPI.Models.Tour;
import oksei.ru.OrenAPI.Models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        try{
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
        }
        catch (Exception e){
            System.out.println("Exception => " + e.getMessage());
            return new User();
        }
        return user;
    }
}
