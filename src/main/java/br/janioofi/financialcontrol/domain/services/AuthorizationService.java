package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {
    private final UserRepository repository;
    private static final String NO_USER_USERNAME = "Nenhum usuário registrado com o username: ";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + username));
    }
}