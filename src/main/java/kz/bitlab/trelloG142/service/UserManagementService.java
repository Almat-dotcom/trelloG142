package kz.bitlab.trelloG142.service;

import kz.bitlab.trelloG142.dto.UserResponse;

public interface UserManagementService {
    UserResponse me(String username);
}
