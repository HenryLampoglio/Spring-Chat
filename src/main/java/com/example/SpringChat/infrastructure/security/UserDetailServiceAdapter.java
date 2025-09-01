package com.example.SpringChat.infrastructure.security;

// infrastructure/security/UserDetailServiceAdapter.java

import com.example.SpringChat.infrastructure.user.persistence.repository.SpringUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceAdapter implements UserDetailsService {

    // 1. Declare uma variável para a instância do repositório
    private final SpringUserRepository springUserRepository;

    // 2. Use a injeção por construtor para receber a instância
    public UserDetailServiceAdapter(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }

    // 3. Chame o método na sua instância, não na interface
    @Override
    public UserDetails loadUserByUsername(String email) {
        return springUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
}
