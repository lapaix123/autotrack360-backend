package rw.autotrack.autotrack360.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(UserDTO::from).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        return userRepository.findById(id).map(UserDTO::from)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
