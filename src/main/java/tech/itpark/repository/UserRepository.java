package tech.itpark.repository;

import tech.itpark.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    boolean existByLogin(String login);
    Optional<UserEntity> findByLogin(String login);

}
