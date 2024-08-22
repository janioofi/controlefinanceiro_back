package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.Mapper;
import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.dtos.UserResponseDto;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private static final String NO_USER_ID = "No user found with ID: ";
    private static final String NO_USER_USERNAME = "Nenhum usuário registrado com o username: ";
    private static final String WRONG_USER = "Only the user can perform this action";
    private static final String USER = "User-Agent";

    public UserResponseDto update(Long id, UserRegisterDto userRegisterDto, HttpServletResponse response) {
        validateUserById(response, id);
        validateUpdate(userRegisterDto, id);
        validatePassword(userRegisterDto);
        User user = repository.findById(id).map(recordFound -> {
            recordFound.setUsername(userRegisterDto.username());
            recordFound.setPassword(new BCryptPasswordEncoder().encode(userRegisterDto.password()));
            return repository.save(recordFound);
        }).orElseThrow(() -> new ResourceNotFoundException(NO_USER_ID + id));
        log.info("Performing an update for the user with ID: {}", id);
        return Mapper.toDto(user);

    }

    public UserResponseDto findById(Long id, HttpServletResponse response){
        validateUserById(response, id);
        log.info("Seeking user with ID: {}", id);
        return repository.findById(id).map(Mapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_USER_ID + id));
    }

    public UserResponseDto findByUsername(String username, HttpServletResponse response){
        validateUserByUsername(response, username);
        log.info("Seeking user with Username: {}", username);
        return repository.findByUsername(username).map(Mapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + username));
    }

    public void delete(Long id, HttpServletResponse response){
        validateUserById(response, id);
        validateDelete(id);
        log.info("Deleting user with ID: {}", id);
        repository.deleteById(id);
    }

    private void validateDelete(Long id){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty()) throw new ResourceNotFoundException(NO_USER_ID + id);
    }

    private void validateUpdate(UserRegisterDto userLoginDto, Long id) {
        User obj = repository.findByUsername(userLoginDto.username()).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + userLoginDto.username()));
        if (obj != null && !obj.getIdUser().equals(id)) {
            throw new DataIntegrityViolationException("User already exists in the system!");
        }
    }

    private void validatePassword(UserRegisterDto user){
        if(!user.password().equals(user.confirmPassword())){
            throw new DataIntegrityViolationException("As senhas precisam ser iguais!");
        }
    }

    private void validateUserById(HttpServletResponse response, Long id) {
        User userHeader = repository.findByUsername(response.getHeader(USER)).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + response.getHeader(USER)));
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NO_USER_ID + id));
        if(!user.getIdUser().equals(userHeader.getIdUser())) throw new ResourceNotFoundException(WRONG_USER);
    }

    private void validateUserByUsername(HttpServletResponse response, String username) {
        User userHeader = repository.findByUsername(response.getHeader(USER)).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + response.getHeader(USER)));
        User user = repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + username));
        if(!user.getIdUser().equals(userHeader.getIdUser())) throw new ResourceNotFoundException(WRONG_USER);
    }
}
