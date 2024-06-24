package org.senju.eshopeule.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.exceptions.UsernameAlreadyExistsException;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.UserRepository;
import org.senju.eshopeule.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.USER_ALREADY_EXISTS_MSG;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Override
    @Transactional
    public void createAccount(String username, String password, Role role) throws UsernameAlreadyExistsException {
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .isEnabled(true)
                    .isAccountNonLocked(true)
                    .isAccountNonExpired(true)
                    .isCredentialsNonExpired(true)
                    .role(role)
                    .build();
            userRepository.save(user);
            logger.debug("SAVED new staff-admin account: {}", username);
        } else throw new UsernameAlreadyExistsException(USER_ALREADY_EXISTS_MSG);
    }
}
