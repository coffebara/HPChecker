package com.solomonm.HPChecker.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ENUM 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    HTML_ERROR("E001", "HTML이 정상 출력되지 않습니다."),
    HTML_TIMEOUT_ERROR("E002", "HTML 요청 실패 TIMEOUT 발생."),
    HTTP_STATUS_ERROR("E003", "HTTP status가 200 또는 304가 아닙니다."),
    SERVER_STATUS_ERROR("E004", "서버 Status가 OFFLINE 입니다"),
    PROCESS_CHILD_ERROR("E005", "자식 프로세서가 실행되지 않았습니다.");


    private final String code;
    private final String message;
}
