package se.gritacademy.service;

import jdk.jfr.Enabled;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.gritacademy.controller.AuthController;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;

import java.beans.Encoder;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        logger.info("User found with username: {}", username);
        return userInfo;
    }

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public Optional<UserInfo> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void save(UserInfo userInfo) {
        userInfo.setPassword(BCrypt.hashpw(userInfo.getPassword(), BCrypt.gensalt()));
        userRepository.save(userInfo);
    }
    public List<String> findEmailUsers(){
        return userRepository.findAll().stream().map(UserInfo::getEmail).toList();

    }
    public List<UserInfo> findAll(){
        return userRepository.findAll();
    }

}
