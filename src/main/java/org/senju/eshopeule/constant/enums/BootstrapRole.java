package org.senju.eshopeule.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BootstrapRole {
    ADMIN("ADMIN"),
    STAFF("STAFF"),
    CUSTOMER("CUSTOMER");

    private final String roleName;
}
