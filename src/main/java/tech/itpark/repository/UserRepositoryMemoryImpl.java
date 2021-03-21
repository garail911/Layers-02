package tech.itpark.repository;

import tech.itpark.entity.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryMemoryImpl implements UserRepository {
    private long nextId = 1;
    private final Map<Long, UserEntity> idToEntity = new HashMap<>();
    private final Map<String, UserEntity> loginToEntity = new HashMap<>();

    @Override
    public List<UserEntity> findAll() {
        return List.copyOf(idToEntity.values());
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(idToEntity.get(id));
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (entity.getId() == 0) {
            entity.setId(nextId++);
        }
        loginToEntity.put(entity.getLogin(), entity);
        return idToEntity.put(entity.getId(), entity);
    }

    @Override
    public boolean removeById(Long id) {
        UserEntity entity = idToEntity.get(id);
        if (entity !=  null) {
            entity.setRemoved(true);
        }
        return entity != null;
    }

    @Override
    public boolean existByLogin(String login) {
        return loginToEntity.containsKey(login);
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        return Optional.ofNullable(loginToEntity.get(login));
    }
}
