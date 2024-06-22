package org.senju.eshopeule.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaims {
    ROLE("role"),
    TYPE("token_type");

    private final String claimName;
}
