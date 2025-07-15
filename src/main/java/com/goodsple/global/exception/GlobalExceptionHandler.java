//package com.goodsple.global.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice  // 모든 컨트롤러에서 발생하는 예외를 잡아주는 역할
//public class GlobalExceptionHandler {
//    // 전역 공통 예외처 기능
//
//    // @Valid 붙은 DTO 검증 실패 시 (예: 아이디 빈값, 비밀번호 형식 오류) 발생
//    // 오류가 발생한 필드 이름과 메시지를 Map 형태로 묶어서 반환.
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex,HttpServletRequest request) {
//        if (isSwaggerRequest(request)) return null;
//
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
//    }
//
//    // 서비스 로직에서 직접 throw new IllegalArgumentException("메시지") 했을 때.
//    // 단일 메시지 반환. 예: "이미 사용 중인 아이디입니다."
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex,HttpServletRequest request) {
//        if (isSwaggerRequest(request)) return null;
//        return ResponseEntity.badRequest().body(ex.getMessage());
//    }
//
//    // 그 외 예외처리
//    // 의도하지 않은 모든 예외를 잡는 마지막 방어선.
//    // 사용자에게 너무 자세한 서버 에러를 노출하지 않고, 간단하게 500 응답을 줌.
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGenericException(Exception ex,HttpServletRequest request) {
//        if (isSwaggerRequest(request)) return null;
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류가 발생했습니다.");
//    }
//
//    // Swagger 요청인지 확인하는 메서드
//    private boolean isSwaggerRequest(HttpServletRequest request) {
//        String uri = request.getRequestURI();
//        return uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui");
//    }
//}
