package co.com.bancolombia.security;

import co.com.bancolombia.model.exceptions.BusinessUnAuthorizedException;
import co.com.bancolombia.model.responsecode.ResponseCode;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class SecurityHelper {

    private SecurityHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static Mono<String> getBearerToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth != null && auth.getDetails() != null)
                .switchIfEmpty(Mono.error(new BusinessUnAuthorizedException(ResponseCode.UNAUTHORIZED)))
                .map(auth -> auth.getCredentials().toString());
    }

    public static Mono<String> getLoggedUserIdentification() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth != null && auth.getDetails() != null)
                .switchIfEmpty(Mono.error(new BusinessUnAuthorizedException(ResponseCode.UNAUTHORIZED)))
                .map(auth -> auth.getDetails().toString());
    }
}

