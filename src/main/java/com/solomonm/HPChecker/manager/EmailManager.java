package com.solomonm.HPChecker.manager;

import com.solomonm.HPChecker.config.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 지정된 이메일로 발생한 에러코드와 메세지를 담아 전송하는 클래스
 *
 * @author 김상준
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class EmailManager {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.properties.mail.to}")
    private String to;
    @Value("${spring.mail.properties.mail.from}")
    private String from;
    private final String SUBJECT = "[솔로몬엠] 홈페이지 오류 보고서 ";

    /**
     * 에러 코드들을 html로 담아 지정된 이메일로 메일을 전송하는 메서드
     *
     * @param errorCodeList 발생한 에러코드 리스트
     * @throws MessagingException 메일 송신중 발생한 에러
     */
    public void sendSimpleMessage(List<ErrorCode> errorCodeList) throws MessagingException {

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedNow = now.format(formatter);

        // html 형식 에러 코드
        String errorHtml = getText(errorCodeList);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(SUBJECT + formattedNow);
        helper.setText(errorHtml, true);
        mailSender.send(message);
    }

    /**
     * 에러코드들을 html 형식으로 만들어 반환하는 메서드
     *
     * @param errorCodeList 에러 코드들을 담은 리스트
     * @return String 에러코드를 담은 html 형식
     */
    private String getText(List<ErrorCode> errorCodeList) {

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h1>[솔로몬엠 홈페이지] 오류 보고서</h1>");

        // 오류 항목 추가
        for (ErrorCode error : errorCodeList) {
            htmlContent.append("<li><strong>코드:</strong> ").append(error.getCode())
                    .append(", <strong>메시지:</strong> ").append(error.getMessage()).append("</li>");
        }

        htmlContent.append("</ul>")
                .append("<p>감사합니다.</p>")
                .append("</body>")
                .append("</html>");

        return htmlContent.toString();
    }
}
