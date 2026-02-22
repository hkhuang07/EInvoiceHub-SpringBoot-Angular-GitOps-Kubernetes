package com.einvoicehub.core.service;

import com.einvoicehub.core.exception.ErrorCode;
import com.einvoicehub.core.exception.InvalidDataException;
import com.einvoicehub.core.domain.entity.EinvMerchantUserEntity;
import com.einvoicehub.core.domain.repository.EinvMerchantUserRepository;
import com.einvoicehub.core.dto.response.AuthResponse;
import com.einvoicehub.core.security.SecurityUser;
import com.einvoicehub.core.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EinvMerchantUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(String username, String password) {
        log.info("[Auth] Bắt đầu xử lý đăng nhập cho: {}", username);

        try {
            // Xác thực qua AuthenticationManager Spring Security core
            // Nếu sai passwd, ném ra AuthenticationException
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            log.warn("[Auth] Đăng nhập thất bại: User {} nhập sai thông tin xác thực", username);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        EinvMerchantUserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidDataException(ErrorCode.INVALID_DATA, "Tài khoản không tồn tại trên hệ thống"));

        // Kiểm tra trạng thái Doanh nghiệp
        // Nếu Doanh nghiệp đã bị xóa mềm ,chặn truy cập
        if (Boolean.TRUE.equals(user.getMerchant().getIsDeleted())) {
            log.error("[Auth] Chặn truy cập: Merchant sở hữu {} đã bị xóa hoặc ngừng hoạt động", user.getMerchant().getTaxCode());
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Doanh nghiệp của bạn đã ngừng hoạt động trên hệ thống");
        }

        if (Boolean.FALSE.equals(user.getIsActive())) {
            log.warn("[Auth] Chặn truy cập: Tài khoản {} hiện đang bị khóa", username);
            throw new InvalidDataException(ErrorCode.INVALID_DATA, "Tài khoản của bạn hiện đang bị khóa. Vui lòng liên hệ quản trị viên.");
        }
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        SecurityUser securityUser = new SecurityUser(user);
        String jwtToken = jwtService.generateToken(securityUser);

        log.info("[Auth] User {} đăng nhập thành công cho Doanh nghiệp: {}", username, user.getMerchant().getCompanyName());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .username(user.getUsername())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .merchantId(user.getMerchant().getId())
                .merchantName(user.getMerchant().getCompanyName())
                .build();
    }
}