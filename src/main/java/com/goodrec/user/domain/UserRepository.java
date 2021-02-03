package com.goodrec.user.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface UserRepository extends CrudRepository<User, UUID>  {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);
}
