package com.uth.BE.Service;

import com.uth.BE.Entity.User;
import com.uth.BE.Entity.model.CustomUserDetails;
import com.uth.BE.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements  UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Optional<User> user =  userRepository.findByUsername(username);
        if(user.isPresent()){
            var objUser = user.get();
//            return org.springframework.security.core.userdetails.User.builder()
//                .username(objUser.getUsername())
//                .password(objUser.getPassword())
//                .roles(getRoles(objUser))
//                .build();
            return new CustomUserDetails(objUser);
        }else{
            throw new UsernameNotFoundException(username);
        }

    }

    private String[] getRoles(User objUser) {
        if(objUser.getRole() == null){
            return new String[]{"CLIENT"};
        }
        return objUser.getRole().split(",");
    }
}
