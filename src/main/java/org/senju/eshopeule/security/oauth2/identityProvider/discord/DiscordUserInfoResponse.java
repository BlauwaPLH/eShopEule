package org.senju.eshopeule.security.oauth2.identityProvider.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public final class DiscordUserInfoResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 5802418338461729722L;

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "discriminator")
    private String discriminator;

    @JsonProperty(value = "global_name")
    private String globalName;

    @JsonProperty(value = "avatar")
    private String avatar;

    @JsonProperty(value = "bot")
    private boolean bot;

    @JsonProperty(value = "system")
    private boolean system;

    @JsonProperty(value = "mfa_enabled")
    private boolean mfaEnabled;

    @JsonProperty(value = "banner")
    private String banner;

    @JsonProperty(value = "accent_color")
    private int accentColor;

    @JsonProperty(value = "locale")
    private String locale;

    @JsonProperty(value = "verified")
    private boolean verified;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "flags")
    private int flags;

    @JsonProperty(value = "premium_type")
    private int premiumType;

    @JsonProperty(value = "public_flags")
    private int publicFlags;

    @JsonProperty(value = "avatar_decoration_data")
    private AvatarDecorationData avatarDecorationData;


    @Getter
    @AllArgsConstructor
    private static class AvatarDecorationData {
        @JsonProperty(value = "asset")
        private String asset;

        @JsonProperty(value = "sku_id")
        private String skuId;
    }
}
