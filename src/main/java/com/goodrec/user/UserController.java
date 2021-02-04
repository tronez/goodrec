package com.goodrec.user;

import com.goodrec.user.domain.UserService;
import com.goodrec.user.dto.RegisterRequest;
import com.goodrec.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        final UserResponse savedUser = userService.create(registerRequest);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{uuid}")
                .buildAndExpand(savedUser.getUuid())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(savedUser);
    }

    @GetMapping("/users/{uuid}")
    public ResponseEntity<UserResponse> getUserByUuid(@PathVariable UUID uuid){

        final UserResponse response = userService.findByUUID(uuid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email){

        final UserResponse response = userService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

}
