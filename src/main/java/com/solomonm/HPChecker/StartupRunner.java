package com.solomonm.HPChecker;

import com.solomonm.HPChecker.config.ErrorCode;
import com.solomonm.HPChecker.config.ServerConfig;
import com.solomonm.HPChecker.manager.*;
import com.solomonm.HPChecker.service.CheckerService;
import com.solomonm.HPChecker.service.EmailService;
import com.solomonm.HPChecker.service.EmailServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Startup runner.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner {

    private final EmailServiceV2 emailService;
    private final CheckerService checkerService;
    private final GlobalErrorManagerV2 globalErrorManager;

    /**
     * 10분마다 웹페이지와 서버를 점검 후 에러가 있을 경우,
     * 현재 시간 기준으로 지정된 메일 주소로 전송하는 메서드
     *
     * @throws Exception timeout
     */
    @Scheduled(cron = "0 */10 * * * *")
//    @Scheduled(cron = "*/10 * * * * *")
    public void run() throws Exception {

        try {

            // 웹 점검 결과
            if (!checkerService.isRunningWeb()) {
                emailService.sendErrorReport();
            } else {
                // 서버 점검 결과
                if (!checkerService.isRunningServer()) {
                    emailService.sendErrorReport();
                }
            }

        } catch (SocketTimeoutException e) {
            log.error("=== HTML 요청 실패 TIMEOUT ===");
            globalErrorManager.addError(ErrorCode.HTML_TIMEOUT_ERROR);

        } catch (Exception e) {
            exceptionHandler(e);
        } finally {
            log.info("=== 점검 종료 ===");
        }

    }

    /**
     * 예외처리를 위한 메서드
     *
     * @param e 발생한 예외 객체
     */
    private static void exceptionHandler(Exception e) {
        //공통 처리
        System.out.println("사용자 메세지: 죄송합니다. 알 수 없는 문제가 발생했습니다.");
        System.out.println("=== 개발자용 디버깅 메세지 ===");
        e.printStackTrace(System.out); //스택 트레이스 출력

    }

}
