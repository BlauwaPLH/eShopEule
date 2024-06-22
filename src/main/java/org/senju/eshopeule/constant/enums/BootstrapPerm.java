package org.senju.eshopeule.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BootstrapPerm {
    ADMIN_READ("ADMIN:READ"),
    ADMIN_WRITE("ADMIN:WRITE"),
    STAFF_READ("STAFF:READ"),
    STAFF_WRITE("STAFF:WRITE"),
    VENDOR_READ("VENDOR:READ"),
    VENDOR_WRITE("VENDOR:WRITE"),
    CUS_READ("CUSTOMER:READ"),
    CUS_WRITE("CUSTOMER:WRITE");

    private final String permName;

}
