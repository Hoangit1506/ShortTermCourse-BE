package com.short_term_course.util;

import com.short_term_course.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CodeUtil<T> {

    private static class CodeEntry<T> {
        private T payload;
        private LocalDateTime expiresAt;

        public CodeEntry(T payload, LocalDateTime expiresAt) {
            this.payload = payload;
            this.expiresAt = expiresAt;
        }

        public T getPayload() {
            return payload;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }
    }

    private final Map<String, CodeEntry<T>> verificationCodes = new ConcurrentHashMap<>();

    public void save(String code, T payload, long ttlInMinutes) {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ttlInMinutes);
        verificationCodes.put(code, new CodeEntry<>(payload, expiresAt));
    }

    public T get(String code) {
        CodeEntry<T> entry = verificationCodes.get(code);
        if (entry == null || entry.getExpiresAt().isBefore(LocalDateTime.now())) {
            verificationCodes.remove(code);
            throw new AppException(HttpStatus.NOT_FOUND,"Code not found","auth-e-04");
        }
        return entry.getPayload();
    }

    public void remove(String code) {
        verificationCodes.remove(code);
    }
}
