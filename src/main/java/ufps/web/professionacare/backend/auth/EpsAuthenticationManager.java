package ufps.web.professionacare.backend.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ufps.web.professionacare.backend.model.SsptUsuario;
import ufps.web.professionacare.backend.service.SsptUsuarioService;

@Service
public class EpsAuthenticationManager implements AuthenticationManager {

	@Autowired
	private SsptUsuarioService usuarioService;

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();

		SsptUsuario respuesta = usuarioService.buscarPorUsername(username);

		if (respuesta == null) {
			throw new BadCredentialsException("Datos de acceso invalidos");
		}

		if (!respuesta.getEnable()) {
			throw new BadCredentialsException("Usuario inhabilitado");
		}

		boolean validez = passwordEncoder.matches(password, respuesta.getPassword());

		if (!validez) {
			throw new BadCredentialsException("Contraseña o nombre de usuario invalidos");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		HashMap<String, Object> details = new HashMap<>();

		details.put("token", respuesta);

		if (respuesta.getRol() != null) {
			authorities.add(new SimpleGrantedAuthority(respuesta.getRol().getRol()));
			details.put("role", respuesta.getRol().getRol());
		} else {
			throw new BadCredentialsException("No cuenta con ningun rol");
		}

		return new EpsAuthenticationToken(username, null, details, authorities);
	}

}
