package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.exceptions.*;
import org.senju.eshopeule.model.user.Role;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface StaffService {
    List<StaffDTO> getAllStaff();

    StaffDTO getStaffWithId(String id) throws NotFoundException;

    void deleteStaffWithId(String id);

    void createAccount(String username, String password, String email, Role role) throws ObjectAlreadyExistsException;

    void createAccount(StaffDTO staff) throws ObjectAlreadyExistsException, NotFoundException;

    StaffDTO updateAccount(StaffDTO staff) throws NotFoundException, ObjectAlreadyExistsException;
}
