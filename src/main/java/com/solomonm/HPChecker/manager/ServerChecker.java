package com.solomonm.HPChecker.manager;

import com.jcraft.jsch.*;
import com.solomonm.HPChecker.config.ErrorCode;
import com.solomonm.HPChecker.config.ServerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 지정된 호스트 정보를 가지고 서버에 접속하여 서버 상태와 자식 서버를 점검하는 클래스
 *
 * @author 김상준
 * @version 1.2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerChecker {

    private final ServerConfig serverConfig;
    private final GlobalErrorManagerV2 globalErrorManager;

    /**
     * 자식 서버 상태를 점검하여 비정상이면 메일링하는 메서드
     *
     * @param outputByPs ps 명령어로 실행한 결과로 자식 프로세스
     * @return boolean 자식 프로세서 점검 결과
     */
    public boolean checkChildServer(String outputByPs) {
        String[] lines = outputByPs.split("\n");
        int lineCount = lines.length;

        // 결과 출력
        // 4줄 = ps 명령어 1줄 + processChild 3줄
        if (lineCount == 4) {
            log.info("=== 자식 프로세서 상태 점검 성공 ===");

            return true;
        } else {
            log.error("=== 자식 프로세서 점검 실패 ===");
            globalErrorManager.addError(ErrorCode.PROCESS_CHILD_ERROR);

            return false;
        }
    }

    /**
     * 서버 상태값을 점검하여 비정상이면 메일링하는 메서드
     *
     * @param status 서버 상태값
     * @return boolean 서버 점검 결과
     */
    public boolean checkServerStatus(String status) {
        if (status.equalsIgnoreCase("online")) {
            log.info("=== 서버 상태 점검 성공 ===");

            return true;
        } else {
            log.error("=== 서버 상태 점검 실패 ===");
            globalErrorManager.addError(ErrorCode.SERVER_STATUS_ERROR);

            return false;
        }
    }

    /**
     * 명령어 실행 결과를 정규식을 이용해 Status 값을 추출하는 메서드
     *
     * @param output 명령어 실행 결과로 읽어온 결과
     * @return String 서버 상태값
     */
    public String extractStatus(String output) {
        Pattern pattern = Pattern.compile("│\\s*\\d+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*\\d+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*([^│]+)\\s*│");
        Matcher matcher = pattern.matcher(output.toString());

        String status = "";

        if (matcher.find()) {
            status = matcher.group(1).trim();
            log.info("server status: {}", status);
        } else {
            log.error("Status not found.");
        }
        return status;
    }

    /**
     * SSH 세션을 통해 원격 서버에서 명령어를 실행하고 그 결과를 반납하는 메서드
     *
     * @param session 연결된 SSH 객체
     * @param command 실행할 명령어
     * @return String 명령어 실행 결과
     * @throws JSchException SSH 연결 과정에서 발생한 예외
     * @throws IOException   명령어 실행 결과를 읽는 과정에서 생기는 입출력 예외
     */
    public String executeCommand(Session session, String command) throws JSchException, IOException {
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();

        StringBuilder outputBuffer = new StringBuilder();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                outputBuffer.append(new String(tmp, 0, i));
            }

            if (channel.isClosed()) {
                log.info("Exit status: " + channel.getExitStatus());
                break;
            }
        }

        channel.disconnect();
        return outputBuffer.toString();
    }

    /**
     * SSH 서버에 연결하고 세션을 반환하는 메서드
     * JSch 라이브러리를 사용하여 SSH 연결설정
     *
     * @return 연결된 서버 세션을 반납
     * @throws JSchException SSH 연결 실패시 생기는 예외
     */
    public Session connectToServer() throws JSchException {
        log.info("Connecting to " + serverConfig.getHost());
        JSch jsch = new JSch();
        Session session = jsch.getSession(serverConfig.getUser(), serverConfig.getHost(), serverConfig.getPort());
        session.setPassword(serverConfig.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(30000); // 30초 타임아웃

        log.info("Connected to " + serverConfig.getHost());
        return session;
    }

    /**
     * 예외처리를 위한 메서드
     *
     * @param e 발생한 예외 객체
     */
    public void exceptionHandler(Exception e) {
        //공통 처리
        System.out.println("=== 개발자용 디버깅 메세지 ===");
        e.printStackTrace(System.out); //스택 트레이스 출력

    }
}
