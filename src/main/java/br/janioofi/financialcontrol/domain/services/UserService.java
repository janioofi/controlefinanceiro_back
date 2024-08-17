package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.Mapper;
import br.janioofi.financialcontrol.domain.dtos.UserRequestDto;
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
    private static final String NO_USER = "No user found with ID: ";
    private static final String WRONG_USER = "Only the user can perform this action";

    public UserResponseDto update(Long id, UserRequestDto userRequestDto, HttpServletResponse response) {
        validateUser(response, id);
        validateUpdate(userRequestDto, id);
        User user = repository.findById(id).map(recordFound -> {
            recordFound.setEmail(userRequestDto.email());
            recordFound.setPassword(new BCryptPasswordEncoder().encode(userRequestDto.password()));
            return repository.save(recordFound);
        }).orElseThrow(() -> new ResourceNotFoundException(NO_USER + id));
        log.info("Performing an update for the user with ID: {}", id);
        return Mapper.toDto(user);

    }

    public void delete(Long id, HttpServletResponse response){
        validateUser(response, id);
        validateDelete(id);
        log.info("Deleting user with ID: {}", id);
        repository.deleteById(id);
    }

    private void validateDelete(Long id){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty()) throw new ResourceNotFoundException(NO_USER + id);
    }

    private void validateUpdate(UserRequestDto userRequestDto, Long id) {
        User obj = repository.findByEmail(userRequestDto.email());
        if (obj != null && !obj.getIdUser().equals(id)) {
            throw new DataIntegrityViolationException("User already exists in the system!");
        }
    }

    private void validateUser(HttpServletResponse response, Long id) {
        User userHeader = repository.findByEmail(response.getHeader("User-Agent"));
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NO_USER + id));
        if(!user.getIdUser().equals(userHeader.getIdUser())) throw new ResourceNotFoundException(WRONG_USER);
    }
}
