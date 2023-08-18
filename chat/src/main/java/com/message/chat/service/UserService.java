package com.message.chat.service;

import com.message.chat.repository.Response;
import com.message.chat.repository.User;
import com.message.chat.repository.UserInfo;
import com.message.chat.repository.User;
import com.message.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        try {
//            return (UserDetails) userRepository.findByUsername(username)
//                    .orElseThrow(() -> new Exception("user Not found "));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Response signUp(User user) {
        Optional<User> user1 = this.userRepository.findByUsername(user.getUsername());
        if(user1.isPresent()){

            return new Response("already Exist",true,new UserInfo(user1.get().getUsername(),null));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new Response("success",false, new UserInfo(user.getUsername(),null));
    }

//    public Response login(User user) {
//        Optional<User> user1 = userRepository.findByUsername(user.getUsername());
//        if (user1.isEmpty()) {
//
//            return new Response("User not exist", true, null);
//        }
//        if (passwordEncoder.matches(user.getPassword(), user1.get().getPassword())) {
//            return new Response("Logged in", false, null);
//        }
//            return new Response("Password not matched",true,null);
//    }
}
