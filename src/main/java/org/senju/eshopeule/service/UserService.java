package org.senju.eshopeule.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails loadUserDetailsByUsername(String username);
}
