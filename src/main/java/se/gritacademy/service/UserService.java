package se.gritacademy.service;

import jdk.jfr.Enabled;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.gritacademy.model.UserInfo;
import se.gritacademy.repository.UserRepository;

import java.beans.Encoder;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
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

}
