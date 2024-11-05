package com.solomonm.HPChecker.service;

import com.solomonm.HPChecker.manager.EmailManager;
import com.solomonm.HPChecker.manager.GlobalErrorManager;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * GlobalErrorManager에 에러가 있을 경우 메일을 보내고, 에러를 다시 Clear하는 클래스
 *
 * @author 김상준
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailManager emailManager;

    /**
     * GlobalErrorManager에 에러가 있을 경우 메일을 보내고, 에러를 다시 Clear하는 메서드
     *
     * @return 메일 전송 성공 여부
     * @throws MessagingException 메일 전송 실패 에러
     */
    public boolean sendErrorReport() throws MessagingException {

        if (!GlobalErrorManager.getInstance().getErrors().isEmpty()) {
            log.info("=== 오류 레포트를 이메일로 전송합니다 ===");

            emailManager.sendSimpleMessage(GlobalErrorManager.getInstance().getErrors());
            GlobalErrorManager.getInstance().clearErrors();

            log.info("=== 점검 종료 ===");

            return true;
        }

        return false;
    }

}
