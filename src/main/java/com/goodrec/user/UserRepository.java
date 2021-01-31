package com.goodrec.user;

import com.goodrec.security.UserPrincipal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserPrincipal, UUID> {

    Optional<UserPrincipal> findByUsername(String username);
}
