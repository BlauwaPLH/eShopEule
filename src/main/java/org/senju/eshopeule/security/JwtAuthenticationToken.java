package org.senju.eshopeule.security;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Getter
    private final String accessToken;
    private final Object principal;


    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities, String accessToken) {
        super(authorities);
        Assert.notNull(accessToken, "accessToken cannot be null");
        this.accessToken = accessToken;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public JwtAuthenticationToken(String accessToken) {
        super(null);
        Assert.notNull(accessToken, "accessToken cannot be null");
        this.accessToken = accessToken;
        this.principal = null;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
