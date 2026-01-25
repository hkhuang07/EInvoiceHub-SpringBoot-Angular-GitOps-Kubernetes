package com.einvoicehub.core.security;

import com.einvoicehub.core.entity.jpa.ApiCredential;
import com.einvoicehub.core.entity.jpa.Merchant;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String clientId;
    private final String apiKey;
    private final ApiCredential credential;
    private final Merchant merchant;

    public ApiKeyAuthenticationToken(String clientId, String apiKey,
                                     ApiCredential credential, Merchant merchant,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.clientId = clientId;
        this.apiKey = apiKey;
        this.credential = credential;
        this.merchant = merchant;
        setAuthenticated(authorities != null && !authorities.isEmpty());
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        return clientId;
    }

    public Long getMerchantId() {
        return merchant != null ? merchant.getId() : null;
    }
}