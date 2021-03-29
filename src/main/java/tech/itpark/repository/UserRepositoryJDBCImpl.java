package tech.itpark.repository;

import tech.itpark.entity.UserEntity;
import tech.itpark.exception.DataAccessException;
import tech.itpark.jdbc.RowMapper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserRepositoryJDBCImpl implements UserRepository {

    private final Connection connection;
    private final RowMapper<UserEntity> mapper = rs -> {
        try {
            return new UserEntity(
                    rs.getLong("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("secret"),
                    Set.of((String[]) rs.getArray("roles").getArray()),
                    rs.getBoolean("removed"),
                    rs.getLong("created")
            );
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    };

    public UserRepositoryJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<UserEntity> findAll() {
        try (
                final Statement stmt = connection.createStatement();
                final ResultSet rs = stmt.executeQuery(
                        "SELECT id, login, password, name, secret, roles, EXTRACT(EPOCH FROM created) created, removed FROM users ORDER BY id"
                );
        ) {
            List<UserEntity> result = new LinkedList<>();
            while (rs.next()) {
                final UserEntity entity = mapper.map(rs);
                result.add(entity);
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        try (
                final PreparedStatement stmt = connection.prepareStatement(
                        "SELECT id, login, password, name, secret, roles, EXTRACT(EPOCH FROM created) created, removed FROM users WHERE id = ?"
                );
        ) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (entity.getId() == 0) {
            try (
                    final PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO users (login, password, name, secret, roles) VALUES (?, ?, ?, ?, ?)" +
                                    " RETURNING id, login, password, name, secret, roles, removed, EXTRACT(EPOCH FROM created) created"
                    )
            ) {
                int index = 0;
                stmt.setString(++index, entity.getLogin());
                stmt.setString(++index, entity.getPassword());
                stmt.setString(++index, entity.getName());
                stmt.setString(++index, entity.getSecret());
                stmt.setArray(++index, connection.createArrayOf("TEXT", entity.getRoles().toArray()));

                stmt.execute();
                return entity;

            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        }

        try (
                final PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE users SET login = ?, password = ?, name =?, secret = ?, roles = ? WHERE  id =?" +
                                "RETURNING id, login, password, name, secret, roles, removed, EXTRACT(EPOCH FROM created) created"
                )
        ) {
            int index = 0;
            stmt.setString(++index, entity.getLogin());
            stmt.setString(++index, entity.getPassword());
            stmt.setString(++index, entity.getName());
            stmt.setString(++index, entity.getSecret());
            stmt.setArray(++index, connection.createArrayOf("TEXT", entity.getRoles().toArray()));
            stmt.setLong(++index, entity.getId());

            stmt.execute();
            return entity;

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean removeById(Long id) {
        try (
                final PreparedStatement stmt = connection.prepareStatement(
                        "DELETE FROM users WHERE id = ?")
        ) {
            stmt.setLong(1, id);
            return stmt.execute();

//            stmt.executeUpdate();
//            return mapper.map(stmt.getResultSet()).isRemoved();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean existByLogin(String login) {
        try (
                final PreparedStatement stmt = connection.prepareStatement(
                        "SELECT login FROM users WHERE login = ?"
                )
        ) {
            stmt.setString(1, login);
            try (final ResultSet rs = stmt.executeQuery()) {

                return rs.next();

            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        try (
                final PreparedStatement stmt = connection.prepareStatement(
                        "SELECT id, login, password, name, secret, roles, EXTRACT(EPOCH FROM created) created, removed FROM users WHERE login = ?"
                );

        ) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
