package com.home.app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class UsuarioDetailsService implements UserDetailsService {
  @Value("${jwtSecurity.encryptedPassword}")
  private String encryptedPassword;

  @Value("${jwtSecurity.users.user1}")
  private String user1;
  @Value("${jwtSecurity.users.user2}")
  private String user2;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Map<String, String> usuarios = Map.of(
        user1, "USER",
        user2, "ADMIN");
    var rol = usuarios.get(username);
    if (rol != null) {
      User.UserBuilder userBuilder = User.withUsername(username);

      userBuilder.password(encryptedPassword).roles(rol);
      return userBuilder.build();
    } else {
      throw new UsernameNotFoundException(username);
    }

  }
}