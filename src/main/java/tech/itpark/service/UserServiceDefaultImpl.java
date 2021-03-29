package tech.itpark.service;

import tech.itpark.entity.UserEntity;
import tech.itpark.exception.PasswordInvalidException;
import tech.itpark.exception.SecretInvalidException;
import tech.itpark.exception.UsernameAlreadyExistsException;
import tech.itpark.exception.UsernameNotExistsException;
import tech.itpark.model.*;
import tech.itpark.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Set;

public class UserServiceDefaultImpl implements UserService {
    private final UserRepository repository;

    public UserServiceDefaultImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserModel register(RegistrationModel model) {
            if (repository.existByLogin(model.getLogin())) {
                throw new UsernameAlreadyExistsException(model.getLogin());
            }
            UserEntity entity = repository.save(new UserEntity(
                    0,
                    model.getLogin(),
                    model.getPassword(),
                    model.getName(),
                    model.getSecret(),
                    Set.of("ROLE_USER"),
                    false,
                    OffsetDateTime.now().toEpochSecond()
            ));

            return new UserModel(
                    entity.getId(),
                    entity.getLogin(),
                    entity.getName(),
                    entity.getRoles(),
                    entity.isRemoved(),
                    entity.getCreated()
            );
    }

    @Override
    public UserModel login(AuthenticationModel model) {
            UserEntity entity = repository
                    .findByLogin(model.getLogin())
                    .orElseThrow(() -> new UsernameNotExistsException(model.getLogin()));

            if (entity.isRemoved()) {
                throw new UsernameNotExistsException(model.getLogin());
            }

            if (!entity.getPassword().equals(model.getPassword())) {

                throw new PasswordInvalidException();
            }
            return new UserModel(
                    entity.getId(),
                    entity.getLogin(),
                    entity.getName(),
                    entity.getRoles(),
                    entity.isRemoved(),
                    entity.getCreated()
            );
    }

    @Override
    public UserModel reset(ResetModel model) {
            UserEntity entity = repository
                    .findByLogin(model.getLogin())
                    .orElseThrow(() -> new UsernameNotExistsException(model.getLogin()));
            if (entity.isRemoved()) {
                throw new UsernameNotExistsException(model.getLogin());
            }
            if (!model.getSecret().equals(entity.getSecret())) {
                throw new SecretInvalidException();
            }
            entity.setPassword(model.getNewPassword());
//            repository.save(entity);

            return new UserModel(
                    entity.getId(),
                    entity.getLogin(),
                    entity.getName(),
                    entity.getRoles(),
                    entity.isRemoved(),
                    entity.getCreated()
            );
    }

    @Override
    public boolean remove(RemovalModel model) {
            UserEntity entity = repository
                    .findByLogin(model.getLogin())
                    .orElseThrow(() -> new UsernameNotExistsException(model.getLogin()));

            if (entity.isRemoved()) {
                throw new UsernameNotExistsException(model.getLogin());
            }
        if (!entity.getPassword().equals(model.getPassword())) {
            throw new PasswordInvalidException(model.getLogin());
        }
        entity.setRemoved(true);
        return entity.isRemoved();
    }
}
