package com.DharleyEcommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DharleyEcommerce.jwt.JwtController;
import com.DharleyEcommerce.jwt.JwtFilter;
import com.DharleyEcommerce.jwt.JwtUtils;
import com.DharleyEcommerce.model.UserM;
import com.DharleyEcommerce.repository.UserRepository;
import com.DharleyEcommerce.service.UserRegisterService;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins="http://localhost:3000/")
public class User {
	
	@Autowired
	private UserRepository userRepository;
	
 
	@Autowired
	private JwtController jwtController;
	
	@Autowired
    private JwtUtils jwtUtils;
	
	@Autowired
	private UserRegisterService userRegisterService;
	
	
	
    @PostMapping("/users")
    public ResponseEntity<?> add(@Valid @RequestBody UserM userInfo) {
	 
        UserM existingUser = userRepository.findByEmail(userInfo.getEmail());
        
        if(existingUser != null) {
            return new ResponseEntity<>("User already existing", HttpStatus.BAD_REQUEST);
        }

        UserM user = userRegisterService.saveUser(userInfo);
        // On enregistre le user dans le context de spring Security 
        Authentication authentication = jwtController.logUser(userInfo.getEmail(), userInfo.getPassword());
        //On génere le token et les headers
        String jwt = jwtUtils.generateToken(authentication);
        System.out.println(jwt);
        //On ajoute le jwt token dans le header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        System.out.println(httpHeaders);
        return new ResponseEntity<>(user, httpHeaders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/isConnected")
    public ResponseEntity<?> getUSerConnected() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
        	System.out.println("je suis connecté");
            return new ResponseEntity<>(((UserDetails) principal).getUsername(), HttpStatus.OK);
        }
        System.out.println(principal);
        System.out.println("je ne suis pas connecté");
        return new ResponseEntity<>("User is not connected", HttpStatus.FORBIDDEN);
    }
    

	@GetMapping("/show/{email}")
	public UserM show(@PathVariable(value="email") String userEmail) {
		UserM user = userRepository.findByEmail(userEmail);
		return user;	
	}
    
    
    
	  
//	private UserM saveUser(UserM userInfo) {
//		UserM user = new UserM();
//	    user.setEmail(userInfo.getEmail());
//	    user.setPassword(new BCryptPasswordEncoder().encode(userInfo.getPassword()));
//	    user.setLastname(StringUtils.capitalize(userInfo.getLastname()));
//	    user.setFirstname(StringUtils.capitalize(userInfo.getFirstname()));
//	    user.setStreet(StringUtils.capitalize(userInfo.getStreet()));
//	    user.setCity(StringUtils.capitalize(userInfo.getCity()));
//	    user.setPostal_code(StringUtils.capitalize(userInfo.getPostal_code()));
//	    user.setCountry(StringUtils.capitalize(userInfo.getCountry()));
//	    user.setPhone(StringUtils.capitalize(userInfo.getPhone()));
//	    
//	    userRepository.save(user);
//	    return user;
//	}
	
}
