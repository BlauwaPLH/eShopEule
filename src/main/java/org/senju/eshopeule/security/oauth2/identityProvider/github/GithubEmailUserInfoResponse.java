package org.senju.eshopeule.security.oauth2.identityProvider.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class GithubEmailUserInfoResponse {

    private String email;
    private boolean verified;
    private boolean primary;
    private String visibility;
}
