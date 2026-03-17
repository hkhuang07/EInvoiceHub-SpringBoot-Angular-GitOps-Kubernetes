package vn.softz.app.einvoicehub.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public final class Common {

    private Common() {
        // Utility class – không instantiate
    }
    
    public static Optional<UserInfo> getCurrentUser() {
        try {
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof UserInfo userInfo) {
                return Optional.of(userInfo);
            }

            // Hỗ trợ trường hợp principal là String (username chỉ) –
            // build UserInfo tối giản để không NPE trong service layer
            if (principal instanceof String username
                    && !"anonymousUser".equals(username)) {
                return Optional.of(UserInfo.builder()
                        .userId(username)
                        .username(username)
                        .build());
            }

            return Optional.empty();

        } catch (Exception ex) {
            // Không throw – tránh crash trong context không có security
            return Optional.empty();
        }
    }

    /**
     * Lấy {@code userId} của user hiện tại.
     * Trả về {@code null} nếu chưa đăng nhập.
     */
    public static String getCurrentUserId() {
        return getCurrentUser().map(UserInfo::getUserId).orElse(null);
    }

    /**
     * Lấy {@code tenantId} của user hiện tại.
     * Trả về {@code null} nếu chưa đăng nhập hoặc không có tenant context.
     */
    public static String getCurrentTenantId() {
        return getCurrentUser().map(UserInfo::getTenantId).orElse(null);
    }

    /**
     * Lấy {@code locId} (storeId) của user hiện tại.
     * Trả về {@code null} nếu chưa đăng nhập hoặc không có location context.
     */
    public static String getCurrentLocId() {
        return getCurrentUser().map(UserInfo::getLocId).orElse(null);
    }
}
