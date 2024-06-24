package org.senju.eshopeule.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.exceptions.PermissionNotExistsException;
import org.senju.eshopeule.mappers.PermissionMapper;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.repository.PermissionRepository;
import org.senju.eshopeule.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.senju.eshopeule.constant.exceptionMessage.PermExceptionMsg.PERMISSION_NON_EXISTS_WITH_NAME_MSG;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private static final PermissionMapper permissionMapper = PermissionMapper.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    @Override
    public PermissionDTO getDTOByName(String name) throws PermissionNotExistsException {
        return permissionMapper.convertToDTO(this.getByName(name));
    }

    @Override
    public Permission getByName(String name) throws PermissionNotExistsException{
        return permissionRepository.findByName(name).orElseThrow(
                () -> new PermissionNotExistsException(
                        String.format(PERMISSION_NON_EXISTS_WITH_NAME_MSG, name)
                )
        );
    }

    @Override
    @Transactional
    public Permission bootstrapPerm(String name) {
        Permission perm = permissionRepository.findByName(name).orElse(null);
        if (perm == null) {
            perm = new Permission(name);
            return permissionRepository.save(perm);
        }
        return perm;
    }
}
