package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.exceptions.UserAlreadyExistsException;
import org.senju.eshopeule.exceptions.UserNotExistsException;
import org.senju.eshopeule.exceptions.UsernameAlreadyExistsException;
import org.senju.eshopeule.model.user.Role;

import javax.management.relation.RoleNotFoundException;

public interface StaffService {
    void createAccount(String username, String password, String email, Role role) throws UserAlreadyExistsException;

    void createAccount(StaffDTO staff) throws UserAlreadyExistsException, RoleNotExistsException;

    void updateAccount(StaffDTO staff) throws UserNotExistsException, UserAlreadyExistsException, RoleNotExistsException;
}
