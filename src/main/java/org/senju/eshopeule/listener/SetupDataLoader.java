package org.senju.eshopeule.listener;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.BootstrapPerm;
import org.senju.eshopeule.constant.enums.BootstrapRole;
import org.senju.eshopeule.exceptions.UsernameAlreadyExistsException;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.service.PermissionService;
import org.senju.eshopeule.service.RoleService;
import org.senju.eshopeule.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${bootstrap.account.super-admin.username}")
    private String superAdminUsername;

    @Value("${bootstrap.account.super-admin.email}")
    private String superAdminEmail;

    @Value("${bootstrap.account.super-admin.phone}")
    private String superAdminPhone;

    @Value("${bootstrap.account.super-admin.password}")
    private String superAdminPassword;

    private final StaffService staffService;
    private final PermissionService permissionService;
    private final RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);
    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        Permission cusReadPerm = permissionService.bootstrapPerm(BootstrapPerm.CUS_READ.getPermName());
        Permission cusWritePerm = permissionService.bootstrapPerm(BootstrapPerm.CUS_WRITE.getPermName());
        Permission venReadPerm = permissionService.bootstrapPerm(BootstrapPerm.VENDOR_READ.getPermName());
        Permission venWritePerm = permissionService.bootstrapPerm(BootstrapPerm.VENDOR_WRITE.getPermName());
        Permission staffReadPerm = permissionService.bootstrapPerm(BootstrapPerm.STAFF_READ.getPermName());
        Permission staffWritePerm = permissionService.bootstrapPerm(BootstrapPerm.STAFF_WRITE.getPermName());
        Permission adminReadPerm = permissionService.bootstrapPerm(BootstrapPerm.ADMIN_READ.getPermName());
        Permission adminWritePerm = permissionService.bootstrapPerm(BootstrapPerm.ADMIN_WRITE.getPermName());

        Role adminRole = roleService.bootstrapRole(BootstrapRole.ADMIN.getRoleName(), List.of(adminReadPerm, adminWritePerm));
        roleService.bootstrapRole(BootstrapRole.STAFF.getRoleName(), List.of(staffReadPerm, staffWritePerm));
        roleService.bootstrapRole(BootstrapRole.VENDOR.getRoleName(), List.of(venReadPerm, venWritePerm));
        roleService.bootstrapRole(BootstrapRole.CUSTOMER.getRoleName(), List.of(cusReadPerm, cusWritePerm));

        try {
            staffService.createAccount(superAdminUsername, superAdminPassword, adminRole);
        } catch (UsernameAlreadyExistsException ex) {
            logger.debug("Creating super admin account: {}", ex.getMessage());
        }

        alreadySetup = true;
    }
}
