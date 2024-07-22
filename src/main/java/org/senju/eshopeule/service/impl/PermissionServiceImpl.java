package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.mappers.PermissionMapper;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.repository.jpa.PermissionRepository;
import org.senju.eshopeule.service.PermissionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.senju.eshopeule.constant.exceptionMessage.PermExceptionMsg.PERMISSION_NON_EXISTS_WITH_ID_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @Cacheable(value = "permissionCache", key = "#id")
    public PermissionDTO getById(String id) {
        return permissionMapper.convertToDTO(
                permissionRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format(PERMISSION_NON_EXISTS_WITH_ID_MSG, id)
                        )
                )
        );
    }

    @Override
    public List<PermissionDTO> getAllPermission() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::convertToDTO)
                .toList();
    }

    @Override
    public Permission bootstrapPerm(String name) {
        Permission perm = permissionRepository.findByName(name).orElse(null);
        if (perm == null) {
            perm = new Permission(name);
            return permissionRepository.save(perm);
        }
        return perm;
    }
}
