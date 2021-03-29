package tech.itpark;

import tech.itpark.entity.UserEntity;
import tech.itpark.repository.UserRepository;
import tech.itpark.repository.UserRepositoryJDBCImpl;
import tech.itpark.service.UserService;
import tech.itpark.service.UserServiceDefaultImpl;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        String dsn = "jdbc:postgresql://localhost:5400/appdb?user=app&password=pass";
        try (Connection connection = DriverManager.getConnection(dsn)
        ) {
            UserRepository repository = new UserRepositoryJDBCImpl(connection);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            List<UserEntity> users = repository.findAll();

//            System.out.println(repository.existByLogin("admin"));
//            System.out.println(repository.findById(2L));
//            System.out.println(repository.removeById(1l));

//            System.out.println(repository.save(new UserEntity(0L, "sa", "123456", "Sa", "Default_Sql_pass", Set.of("ROLE_USER"), false, timestamp.toInstant().toEpochMilli())));
//            System.out.println(repository.save(new UserEntity(3L, "doom2", "IDDQD", "Doom2", "God_mode", Set.of("ROLE_ADMIN"), false, timestamp.toInstant().toEpochMilli())));

//            System.out.println(repository.existByLogin("doom2"));
//
            System.out.println(repository.findByLogin("doom2"));
//

//
//            System.out.println("nightmare");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
