package org.senju.eshopeule.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BootstrapRole {
    ADMIN("ADMIN"),
    STAFF("STAFF"),
    VENDOR("VENDOR"),
    CUSTOMER("CUSTOMER");

    private final String roleName;
}
