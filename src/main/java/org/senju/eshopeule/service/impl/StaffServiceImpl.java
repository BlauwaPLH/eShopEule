package org.senju.eshopeule.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.exceptions.UserAlreadyExistsException;
import org.senju.eshopeule.exceptions.UserNotExistsException;
import org.senju.eshopeule.exceptions.UsernameAlreadyExistsException;
import org.senju.eshopeule.mappers.StaffMapper;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.RoleRepository;
import org.senju.eshopeule.repository.UserRepository;
import org.senju.eshopeule.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.senju.eshopeule.constant.exceptionMessage.RoleExceptionMsg.ROLE_NOT_EXISTS_WITH_ID_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.USER_ALREADY_EXISTS_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.USER_NOT_EXISTS_MSG;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);
    private final StaffMapper staffMapper;

    @Override
    @Transactional
    public void createAccount(String username, String password, String email, Role role)  throws UserAlreadyExistsException {
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            user = User.builder()
                    .username(username)
                    .email(email)
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

    @Override
    public void createAccount(StaffDTO staff) throws UserAlreadyExistsException, RoleNotExistsException {
        final User newStaff = staffMapper.convertToEntity(staff);
        final Role role = roleRepository.findById(newStaff.getRole().getId()).orElseThrow(
                () -> new RoleNotExistsException(
                        String.format(ROLE_NOT_EXISTS_WITH_ID_MSG, newStaff.getRole().getId())
                )
        );
        newStaff.setRole(role);
        newStaff.setPassword(passwordEncoder.encode(newStaff.getPassword()));
        boolean isExistingUser = userRepository.checkUserExistsWithUsernameOrEmail(newStaff.getUsername(), newStaff.getEmail());
        if (isExistingUser) throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MSG);
        userRepository.save(newStaff);
    }

    @Override
    public void updateAccount(StaffDTO staff) throws UserNotExistsException, UserAlreadyExistsException, RoleNotExistsException {
        final User updatedUser = staffMapper.convertToEntity(staff);

    }
}
