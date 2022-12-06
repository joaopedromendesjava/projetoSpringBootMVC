package com.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.model.Usuario;
import com.springboot.repository.UsuarioRepository;

//// ---- Classe service que vai auxiliar na consulta e validacao no banco de dados com o spring security ---- \\\\

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository; 
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		
			Usuario usuario = usuarioRepository.findUserByLogin(username);
		
			if(usuario == null ) {
				
				throw new UsernameNotFoundException("Usuário não foi encontrado");
			}
			
		return new User(usuario.getLogin(), usuario.getSenha(), usuario.isEnabled(),true , true, true, usuario.getAuthorities());
		// apos logar carrega usuario senha e autoridades, liberando o que for permitido
	}

}
