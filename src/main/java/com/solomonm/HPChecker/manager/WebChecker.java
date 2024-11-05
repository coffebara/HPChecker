package com.solomonm.HPChecker.manager;

import com.solomonm.HPChecker.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 지정된 URL의 웹 페이지에 접속하여 상태 코드와 HTML 타이틀을 확인하는 클래스
 *
 * @author 김상준
 * @version 1.2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebChecker {

    private final GlobalErrorManagerV2 globalErrorManager;
    private final static String URL = "https://www.solomonm.co.kr/"; // 홈페이지 URL
    private final static String WEB_TITLE = "SOLOMON M";

    /**
     * Jsoup Connection 객체를 생성하고 설정하는 메서드
     *
     * @return 설정된 Jsoup Connection 객체
     */
    public Connection getConnection() {
        Connection connection = Jsoup.connect(URL)
                .userAgent("Mozilla")
                .timeout(3000);
        return connection;
    }

    /**
     * 웹페이지의 HTML title 을 확인하는 메서드
     *
     * @param connection Jsoup Connection 객체
     * @throws IOException 연결 오류 발생
     * @return boolean HTML 출력 상태
     */
    public boolean checkHTMLTitle(Connection connection) throws IOException {
        // HTML의 title 가져오기
        String title = connection.get().title();
        log.info("title: {}", title);

        if (title.equalsIgnoreCase(WEB_TITLE)) {
            log.info("=== HTML 점검 성공 ===");

            return true;
        } else {
            log.error("=== 웹 페이지 오류 ===");
//            GlobalErrorManager.getInstance().addError(ErrorCode.HTML_ERROR);
            globalErrorManager.addError(ErrorCode.HTML_ERROR);

            return false;
        }
    }


    /**
     * 웹 페이지의 상태 코드를 확인하는 메서드
     *
     * @param connection Jsoup Connection 객체
     * @throws IOException 연결 오류 발생
     * @return boolean HTTP Status 상태
     */
    public boolean checkStatusCode(Connection connection) throws IOException {
        // 요청 실행
        Connection.Response response = connection.execute();

        // 상태 코드 확인
        int statusCode = response.statusCode();
        log.info("statusCode : {}", statusCode);

        // 200: 정상, 304: 재접근 -> 캐시호출
        if (statusCode == 200 || statusCode == 304) {
            log.info("=== 상태코드 점검 성공 ===");

            return true;

        } else {
            log.error("=== 상태코드 점검 실패 ===");
//            GlobalErrorManager.getInstance().addError(ErrorCode.HTTP_STATUS_ERROR);
            globalErrorManager.addError(ErrorCode.HTTP_STATUS_ERROR);

            return false;
        }
    }

}
