package com.myproject.carrental.repository;

import com.myproject.carrental.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(long id);

    @Override
    User save(User user);

    @Override
    List<User> findAll();

}
