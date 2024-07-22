package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.mappers.StaffMapper;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.jpa.RoleRepository;
import org.senju.eshopeule.repository.jpa.UserRepository;
import org.senju.eshopeule.service.StaffService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.enums.BootstrapRole.*;
import static org.senju.eshopeule.constant.exceptionMessage.RoleExceptionMsg.ROLE_NOT_EXISTS_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StaffMapper staffMapper;

    @Override
    public List<StaffDTO> getAllStaff() {
        return userRepository.getAllStaff().stream()
                .map(staffMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "staffCache", key = "#id")
    public StaffDTO getStaffWithId(String id) {
        final User loadedUser = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(USER_NOT_EXISTS_WITH_ID_MSG, id))
        );
        String roleName = loadedUser.getRole().getName();
        if (roleName.equals(CUSTOMER.getRoleName()) || roleName.equals(ADMIN.getRoleName()))
            throw new NotFoundException(USER_NOT_EXISTS_MSG);
        return staffMapper.convertToDTO(loadedUser);
    }

    @Override
    @CacheEvict(value = "staffCache", key = "#id")
    public void deleteStaffWithId(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void createAccount(String username, String password, String email, Role role)  {
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
        } else throw new ObjectAlreadyExistsException(USER_ALREADY_EXISTS_MSG);
    }

    @Override
    public void createAccount(StaffDTO staff) {
        final User newStaff = staffMapper.convertToEntity(staff);

        boolean isExistingUser = userRepository.checkUserExistsWithUsernameOrEmail(newStaff.getUsername(), newStaff.getEmail());
        if (isExistingUser) throw new ObjectAlreadyExistsException(USER_ALREADY_EXISTS_MSG);

        boolean isExistingRole = roleRepository.existsById(newStaff.getRole().getId());
        if (!isExistingRole) throw new NotFoundException(ROLE_NOT_EXISTS_MSG);

        newStaff.setPassword(passwordEncoder.encode(newStaff.getPassword()));
        newStaff.setAccountNonExpired(true);
        newStaff.setAccountNonLocked(true);
        newStaff.setCredentialsNonExpired(true);
        userRepository.save(newStaff);
    }

    @Override
    @CachePut(value = "staffCache", key = "#dto.id")
    public StaffDTO updateAccount(StaffDTO dto) {
        User loadedStaff = userRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException(USER_NOT_EXISTS_MSG)
        );
        loadedStaff = staffMapper.updateFromDTO(dto, loadedStaff);

        boolean isUserExisting = userRepository.checkUserExistsWithUsernameOrEmailExpectId(loadedStaff.getUsername(), loadedStaff.getEmail(), loadedStaff.getId());
        if (isUserExisting) throw new ObjectAlreadyExistsException(USER_ALREADY_EXISTS_MSG);

        boolean isRoleExisting = roleRepository.existsById(loadedStaff.getRole().getId());
        if (!isRoleExisting) throw new NotFoundException(ROLE_NOT_EXISTS_MSG);

        if (dto.getPassword() != null) loadedStaff.setPassword(passwordEncoder.encode(dto.getPassword()));
        return staffMapper.convertToDTO(userRepository.save(loadedStaff));
    }
}
