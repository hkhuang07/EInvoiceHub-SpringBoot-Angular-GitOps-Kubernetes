package com.einvoicehub.core.security;

import com.einvoicehub.core.domain.entity.EinvMerchantUserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class SecurityUser implements UserDetails {

    private final Long userId;
    private final Long merchantId;
    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(EinvMerchantUserEntity user) {
        this.userId = user.getId();
        this.merchantId = user.getMerchant().getId();
        this.username = user.getUsername();
        this.password = user.getPasswordHash();
        this.active = user.getIsActive();
        // Áp dụng Role từ Enum UserRole trong Database
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return active;
    }
}


/*Chú thích:
Class này đóng vai trò là "Cầu nối" giữa hệ thống thực thể (EinvMerchantUserEntity) và Spring Security.
Đã lồng thêm merchantId vào đây để phục vụ logic Multi-tenant.
**/
