package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.Mapper;
import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.dtos.UserResponseDto;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String NO_USER_ID = "No user found with ID: ";
    private static final String NO_USER_USERNAME = "Nenhum usuário registrado com o username: ";
    private static final String WRONG_USER = "Only the user can perform this action";
    private static final String USER_AGENT = "User-Agent";

    public UserResponseDto update(Long id, UserRegisterDto userRegisterDto, HttpServletRequest request) {
        validateUserById(request, id); // Verifica o cabeçalho e o usuário
        validateUpdate(userRegisterDto, id); // Verifica se o username já existe e se é o mesmo
        validatePassword(userRegisterDto); // Verifica se as senhas são iguais
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_USER_ID + id));
        user.setUsername(userRegisterDto.username());
        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));
        user = repository.save(user);
        log.info("Performing an update for the user with ID: {}", id);
        return Mapper.toDto(user);
    }

    public UserResponseDto findById(Long id, HttpServletRequest request){
        validateUserById(request, id);
        log.info("Seeking user with ID: {}", id);
        return repository.findById(id).map(Mapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_USER_ID + id));
    }

    public UserResponseDto findByUsername(String username, HttpServletRequest request){
        validateUserByUsername(request, username);
        log.info("Seeking user with Username: {}", username);
        return repository.findByUsername(username).map(Mapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + username));
    }

    public void delete(Long id, HttpServletRequest request){
        validateUserById(request, id);
        validateDelete(id);
        log.info("Deleting user with ID: {}", id);
        repository.deleteById(id);
    }

    private void validateDelete(Long id){
        if(repository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(NO_USER_ID + id);
        }
    }

    private void validateUpdate(UserRegisterDto userRegisterDto, Long id) {
        User existingUser = repository.findByUsername(userRegisterDto.username()).orElse(null);
        if (existingUser != null && !existingUser.getIdUser().equals(id)) {
            throw new DataIntegrityViolationException("Usuário já existe no sistema!");
        }
    }

    private void validatePassword(UserRegisterDto user){
        if(!user.password().equals(user.confirmPassword())){
            throw new DataIntegrityViolationException("As senhas precisam ser iguais!");
        }
    }

    private void validateUserById(HttpServletRequest request, Long id) {
        String usernameFromHeader = request.getHeader(USER_AGENT);
        validateUserFromHeader(usernameFromHeader, id);
    }

    private void validateUserByUsername(HttpServletRequest request, String username) {
        String usernameFromHeader = request.getHeader(USER_AGENT);
        validateUserFromHeader(usernameFromHeader, username);
    }

    private void validateUserFromHeader(String usernameFromHeader, Object idOrUsername) {
        if (usernameFromHeader == null) {
            throw new ResourceNotFoundException("O cabeçalho do usuário está ausente");
        }

        User userHeader = repository.findByUsername(usernameFromHeader)
                .orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + usernameFromHeader));

        User user = idOrUsername instanceof Long
                ? repository.findById((Long) idOrUsername).orElseThrow(() -> new ResourceNotFoundException(NO_USER_ID + idOrUsername))
                : repository.findByUsername((String) idOrUsername).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + idOrUsername));

        if (!user.getIdUser().equals(userHeader.getIdUser())) {
            throw new ResourceNotFoundException(WRONG_USER);
        }
    }
}
