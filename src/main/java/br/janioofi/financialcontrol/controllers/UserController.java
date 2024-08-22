package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.dtos.UserResponseDto;
import br.janioofi.financialcontrol.domain.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User API")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private static final String ID  = "/{id}";
    private static final String USERNAME  = "/username/{username}";

    @DeleteMapping(ID)
    @Operation(summary = "Deletes a user from the system")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletResponse response){
        service.delete(id, response);
        return ResponseEntity.ok().build();
    }

    @PutMapping(ID)
    @Operation(summary = "Update a system user")
    public ResponseEntity<UserResponseDto> update(
            @RequestBody UserRegisterDto userRegisterDto,
            @PathVariable Long id,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.update(id, userRegisterDto, response));
    }

    @GetMapping(ID)
    @Operation(summary = "Search user by id")
    public ResponseEntity<UserResponseDto> findById(
            @PathVariable Long id,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.findById(id, response));
    }

    @GetMapping(USERNAME)
    @Operation(summary = "Search user by username")
    public ResponseEntity<UserResponseDto> findByUsername(
            @PathVariable String username,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.findByUsername(username, response));
    }
}
