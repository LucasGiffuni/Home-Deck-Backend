# Home-deck


### Autenticación Básica
La aplicacioón cuenta con un servicio de autenticación basada en [JWT](https://auth0.com/docs/secure/tokens/json-web-tokens), concretamente en [Spring JWT](https://www.baeldung.com/spring-security-oauth-jwt)
Por lo que, para la utilizacón de los microservicios será necesario primero autenticarse y obtener un **Token**.


#### Configuración: 

Tenemos varios aspectos a configurar:
1. Usuarios 
2. Roles
3. Metodos Publicos / Privados



La configuración de **Usuarios** se hará Hardcodeada (WIP) en la clase **UsuarioDetailsService.java**.

Los datos a tener en cuenta son, Usuario, Clave (Encrypted) y Rol.
Esto se modicará en el siguiente método:

```java
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Map<String, String> usuarios = Map.of(

        "User1", "USER",

        "admin", "ADMIN"

    );
    var rol = usuarios.get(username);

    if (rol != null) {

      User.UserBuilder userBuilder = User.withUsername(username);

      // "secreto" => [BCrypt] =>     
     //$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq

	  String encryptedPassword = 
      "$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq";

      userBuilder.password(encryptedPassword).roles(rol);
      return userBuilder.build();
    } else {
      throw new UsernameNotFoundException(username);
    }
  }
```
El sistema es bastante sencillo pero eficaz.


Lo siguiente a poder configurar es el uso de rutas basadas en roles, esto lo podemos configurar en la clase **WebSecurityConfig.java** en el siguiente método:

```java
protected void configure(HttpSecurity http) throws Exception {

    http
        //.httpBasic(withDefaults())  // (1)
        .csrf().disable() // (2)
        .authorizeRequests()
        .antMatchers("/public/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/**").hasRole("USER")
        .anyRequest().authenticated()
        .and().cors()
        .and()
        .sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
```

---
### Spotify-Api

Utilizaremos las integraciones hacia Spotify provistas en el siguiente [GitHub](https://github.com/spotify-web-api-java/spotify-web-api-java/tree/master)


#### Guia de uso:
Esta guia se basará en la forma de obtener el access-token y refresh-token necesarios para que el correcto funcionamiento de la api.

##### **Access-Token**

Primero es necesario obtener nuestro clientID y clientSecret para posteriormente asignar una url de redireccionamiento.
Esto lo haremos desde el [Developer Dashboard](https://developer.spotify.com/dashboard/applications) provisto por Spotify.


Con estos datos en cuenta, iremos a la implementacion del servicio de autenticacion de nuestra api y modificaremos los valores de las variables **clientID**, **clientSecret** y **redirectUri** de la clase **TokenAuthorization**

```java
{
	 private final String clientId = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

	 private final String clientSecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

	private final URI redirectUri = SpotifyHttpManager
	.makeUri("http://localhost:8080/redirectUri");
}
```

Un punto importante a tener en cuenta es la utilizacion de los [Autorization Scopes](https://developer.spotify.com/documentation/general/guides/authorization/scopes/) necesarios para la utilizacion de servicios concretos, como por ejemplo modificar el estado de el player.

Una vez decidido los scopes necesarios se modificará a continuación: 

```java
{
		 private final AuthorizationCodeUriRequest authorizationCodeUriRequest 
											= spotifyApi.authorizationCodeUri()
            .state("")
            .scope("user-read-email,user-modify-playback-state")
            .show_dialog(true)
            .build();
}
```

Esto nos dará las bases para obtener el **Access-Token**.


##### **Refresh-Token**
Una utilidad mas que necesaria es el uso de los Refresh-Tokens, la cual nos facilitará el uso de la api. 
Para ello, símil al Access-Token, modificaremos los datos necesarios para obtener este token.

La diferencia entre ellos es que, mientras que el **Access-Token** se modifica cada vez que querramos, el **Refresh-Token** trabaja con una base provista por el **Access-Token** y va refrescandolo para no vovler a obtenerlo.





---
### Microservicio
El proyecto en si es arquitectura basada en Microservicios, por lo que, la utilizacion de **Spotify** o **X** será mediante estos mismos.

#### Obtención de los tokens.
El orden es importante por lo que recomiendo seguir los siguientes pasos: 
 1.  **/spotify/getCodeFromURL**
Consulta que lanzará un applet de Spotify para obtener el **Access-Token**

 2. **/spotify/getRefreshToken**
Utilizando el **Access-Token** obtenido previamente se devuelve el **Refresh-Token**

3. **/spotify/refreshToken**
Se actualiza la duración del **Refresh-Token**