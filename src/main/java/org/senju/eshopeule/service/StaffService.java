package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.exceptions.UserAlreadyExistsException;
import org.senju.eshopeule.exceptions.UserNotExistsException;
import org.senju.eshopeule.exceptions.UsernameAlreadyExistsException;
import org.senju.eshopeule.model.user.Role;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface StaffService {
    List<StaffDTO> getAllStaff();

    StaffDTO getStaffWithId(String id) throws UserNotExistsException;

    void deleteStaffWithId(String id);

    void createAccount(String username, String password, String email, Role role) throws UserAlreadyExistsException;

    void createAccount(StaffDTO staff) throws UserAlreadyExistsException, RoleNotExistsException;

    StaffDTO updateAccount(StaffDTO staff) throws UserNotExistsException, UserAlreadyExistsException, RoleNotExistsException;
}
