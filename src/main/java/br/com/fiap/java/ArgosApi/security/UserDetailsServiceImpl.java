package br.com.fiap.java.ArgosApi.security;

import br.com.fiap.java.ArgosApi.entity.Usuario;
import br.com.fiap.java.ArgosApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado: " + email));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (u.getTipoUsuario() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + u.getTipoUsuario().name()));
        }

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getSenhaHash(), u.isAtivo(), true, true, true, authorities);
    }
}
