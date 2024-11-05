package com.solomonm.HPChecker.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.solomonm.HPChecker.config.ServerConfig;
import com.solomonm.HPChecker.manager.EmailManager;
import com.solomonm.HPChecker.manager.ServerChecker;
import com.solomonm.HPChecker.manager.WebChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 웹과 서버를 각각 점검하고 성공 여부를 반환하는 클래스
 *
 * @author 김상준
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckerService {

    private final WebChecker webChecker;
    private final ServerConfig serverConfig;
    private final ServerChecker serverChecker;

    private final String PM2_COMMAND = "pm2 list";
    private final String PS_COMMAND = "ps -ef | grep node | grep processChild";


    /**
     * 웹 HTML의 title과 HTTP Status를 성공 여부를 반환하는 메서드
     *
     * @return boolean 웹 테스트 성공 여부
     * @throws IOException
     */
    public boolean isRunningWeb() throws IOException {
        log.info("=== 점검 시작 ===");

        // 연결 요청
        Connection connection = webChecker.getConnection();
        // 웹 테스트
        if (!webChecker.checkHTMLTitle(connection) || !webChecker.checkStatusCode(connection)) {
            return false;
        }

        return true;
    }

    /**
     * 서버의 상태값과 자식 프로세서의 상태를 테스트 후 성공 여부를 반환하는 메서드
     *
     * @return boolean 서버 테스트 성공 여부
     * @throws JSchException
     * @throws IOException
     */
    public boolean isRunningServer() throws JSchException, IOException {

        // 서버 연결
        Session session = serverChecker.connectToServer();

        // pm2 list
        String output = serverChecker.executeCommand(session, PM2_COMMAND);
        String status = serverChecker.extractStatus(output);

        // "ps -ef | grep node | grep processChild"
        String outputByPs = serverChecker.executeCommand(session, PS_COMMAND);

        // 서버 테스트 실패 시 false 반환
        if (!serverChecker.checkServerStatus(status) || !serverChecker.checkChildServer(outputByPs)) {
            session.disconnect();
            log.info("Disconnected from " + serverConfig.getHost());

            return false;
        }

        session.disconnect();
        log.info("Disconnected from " + serverConfig.getHost());

        return true;
    }


}
