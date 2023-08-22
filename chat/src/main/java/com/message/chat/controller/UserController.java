package com.message.chat.controller;

import com.message.chat.repository.Response;
import com.message.chat.repository.User;
import com.message.chat.repository.UserInfo;
import com.message.chat.repository.UserRepository;
import com.message.chat.service.UserDetailsServiceImpl;
import com.message.chat.service.UserService;
import com.message.chat.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/api")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";



    @RequestMapping("/test")
    public ResponseEntity<Object> testPage(){
        return new ResponseEntity<>("{\"message\",\"success\"}", HttpStatus.OK);
    }




    @PostMapping("/v1/login")
    public ResponseEntity<Response> authenticateUser(@RequestBody User user) {
        try {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
      }
        catch (Exception e){
            return new ResponseEntity<>(new Response("🔑 Incorrect Username or Password",true,new UserInfo(user.getUsername(),null)),HttpStatus.BAD_REQUEST);
        }

       final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
       Optional<User> user1 = userRepository.findByUsername(user.getUsername());
        
       final String jwt = jwtUtil.generateToken(userDetails);
       log.info("after generating token", user1);
       return new ResponseEntity<Response>(new Response("Logged In",false,new UserInfo(user.getUsername(),jwt)),HttpStatus.OK);
//       Response response = null;
//        if(response.isError())
//        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//        else
//            return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/v1/register")
    public ResponseEntity<Response> signup(@RequestBody User user){
        Response response = userService.signUp(user);
        if(response.isError())
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
