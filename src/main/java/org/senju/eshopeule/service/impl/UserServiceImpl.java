package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;
import org.senju.eshopeule.repository.UserRepository;
import org.senju.eshopeule.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.senju.eshopeule.constant.exceptionMessage.UserExceptionMsg.USER_NOT_EXISTS_WITH_USERNAME_MSG;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    @Cacheable()
    public UserDetails loadUserDetailsByUsername(String username) {
        logger.debug("loadUserDetailsByUsername in {}", UserServiceImpl.class.getName());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format(USER_NOT_EXISTS_WITH_USERNAME_MSG, username)
                ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), this.getAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return this.getGrantedAuthorities(this.getPermission(role));
    }

    private List<String> getPermission(Role role) {
        List<String> strPerms = new ArrayList<>();
        List<Permission> permissions = new ArrayList<>(role.getPermissions());
        for (Permission perm: permissions) strPerms.add(perm.getName());
        strPerms.add("ROLE_" + role.getName());
        return strPerms;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String perm : permissions) authorities.add(new SimpleGrantedAuthority(perm));
        return authorities;
    }
}
