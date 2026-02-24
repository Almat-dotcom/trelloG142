package kz.bitlab.trelloG142.service.impl;

import kz.bitlab.trelloG142.dto.UserResponse;
import kz.bitlab.trelloG142.model.User;
import kz.bitlab.trelloG142.repository.UserRepository;
import kz.bitlab.trelloG142.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;

    @Override
    public UserResponse me(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<String> roles = user.getRoles().stream().map(GrantedAuthority::getAuthority).toList();
        return new UserResponse(user.getId(), user.getUsername(), user.getName(), user.getSurname(), roles);
    }
}
