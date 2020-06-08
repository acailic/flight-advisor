package rs.interview.backend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.interview.backend.domain.Authority;
import rs.interview.backend.domain.User;
import rs.interview.backend.repository.AuthorityRepository;
import rs.interview.backend.repository.UserRepository;
import rs.interview.backend.security.AuthoritiesConstants;
import rs.interview.backend.service.UserService;
import rs.interview.backend.service.dto.UserDTO;
import rs.interview.backend.web.rest.errors.UserResourceException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;

    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(encryptedPassword);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    @Override
    public User findOneByUsername(String userName) {
        Optional<User> userOptional = userRepository.findOneByUsername(userName);
        if (!userOptional.isPresent()) {
            throw new UserResourceException("User login not found");
        }
        return userOptional.get();
    }


}
