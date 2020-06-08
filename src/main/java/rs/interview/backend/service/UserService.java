package rs.interview.backend.service;

import rs.interview.backend.domain.User;
import rs.interview.backend.service.dto.UserDTO;

public interface UserService {

    User registerUser(UserDTO userDTO);

    User findOneByUsername(String userName);
}
