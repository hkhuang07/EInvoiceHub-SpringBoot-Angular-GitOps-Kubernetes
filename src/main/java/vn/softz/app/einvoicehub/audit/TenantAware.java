package vn.softz.app.einvoicehub.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @see vn.softz.app.einvoicehub.common.Common#getCurrentUser()
 * @see vn.softz.app.einvoicehub.common.UserInfo*/
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TenantAware {

    String value() default "";
}
