package org.senju.eshopeule.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.mappers.RoleMapper;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.repository.jpa.RoleRepository;
import org.senju.eshopeule.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static org.senju.eshopeule.constant.exceptionMessage.RoleExceptionMsg.ROLE_NOT_EXISTS_WITH_NAME_MSG;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private static final RoleMapper roleMapper = RoleMapper.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);


    @Override
    public RoleDTO findByName(String name) throws RoleNotExistsException {
        return roleMapper.convertToDTO(roleRepository.findByName(name).orElseThrow(
                () -> new RoleNotExistsException(
                        String.format(ROLE_NOT_EXISTS_WITH_NAME_MSG, name)
                )
        ));
    }

    @Override
    public RoleDTO save(RoleDTO role) {
        return roleMapper.convertToDTO(roleRepository.save(roleMapper.convertToEntity(role)));
    }

    @Override
    public void deleteById(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Role bootstrapRole(String name, Collection<Permission> permissions) {
        var role = roleRepository.findByName(name).orElse(null);
        if (role == null) {
            role = new Role(name, permissions);
            return roleRepository.save(role);
        }
        return role;
    }
}
