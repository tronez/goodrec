package com.goodrec.recipe;

import com.goodrec.recipe.domain.RecipeFacade;
import com.goodrec.recipe.dto.NewRecipeRequest;
import com.goodrec.recipe.dto.RecipeDto;
import com.goodrec.security.TokenProvider;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static com.goodrec.security.JwtConstants.HEADER_STRING;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeFacade facade;
    private final TokenProvider tokenProvider;

    public RecipeController(RecipeFacade facade, TokenProvider tokenProvider) {
        this.facade = facade;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<RecipeDto> create(@RequestPart(value = "image") MultipartFile image,
                                            @RequestPart @Valid NewRecipeRequest request,
                                            @RequestHeader(HEADER_STRING) String header) {


        final String token = tokenProvider.getJwtFromHeader(header);
        final RecipeDto dto = facade.createRecipe(image, request, token);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(dto.getUuid())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(dto);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<RecipeDto> getRecipeByUUID(@PathVariable UUID uuid) {

        final RecipeDto recipe = facade.getRecipeByUUID(uuid);
        return ResponseEntity.ok(recipe);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteRecipe(@PathVariable UUID uuid,
                                          @RequestHeader(HEADER_STRING) String header) {

        final String token = tokenProvider.getJwtFromHeader(header);
        facade.deleteRecipe(uuid, token);
        return ResponseEntity
                .ok()
                .build();
    }
}