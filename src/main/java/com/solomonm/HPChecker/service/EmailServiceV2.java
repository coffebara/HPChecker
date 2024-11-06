package com.solomonm.HPChecker.service;

import com.solomonm.HPChecker.dto.EmailRequestDto;
import com.solomonm.HPChecker.manager.EmailManagerV2;
import com.solomonm.HPChecker.manager.GlobalErrorManagerV2;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 메일 서버로 API 요청을 보내는 클래스
 *
 * @author 김상준
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceV2 {

    private final EmailManagerV2 emailManager;
    private final GlobalErrorManagerV2 globalErrorManager;

    /**
     * GlobalErrorManager에 에러가 있을 경우 메일을 보내고, 에러를 다시 Clear하는 메서드
     *
     * @return 메일 전송 성공 여부
     * @throws MessagingException 메일 전송 실패 에러
     */
    public boolean sendErrorReport() throws MessagingException {

        if (!globalErrorManager.isEmpty()) {
            log.info("=== 오류 레포트를 이메일로 전송합니다 ===");

            EmailRequestDto emailRequestDto = emailManager.createEmailRequestDto(globalErrorManager.getErrors());
            emailManager.sendErrorReport(emailRequestDto);
            globalErrorManager.clearErrors();

            return true;
        }

        return false;
    }
}
