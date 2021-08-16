package kz.toko.app.controller.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private Error error;

    public ErrorResponse(int status, String message, String exceptionClass, Map<String, Object> params) {
        this.error = new Error(status, message, exceptionClass, params);
    }

    @Data
    @RequiredArgsConstructor
    static class Error {
        private final int status;
        private final String message;
        private final String exceptionClass;
        private final Map<String, Object> params;
    }
}


