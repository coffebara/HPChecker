package com.solomonm.HPChecker.manager;

import com.solomonm.HPChecker.dto.EmailRequestDto;
import com.solomonm.HPChecker.config.EmailConfig;
import com.solomonm.HPChecker.config.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 지정된 이메일로 발생한 에러코드와 메세지를 담아 전송하는 클래스
 * SpringEmail -> MAIL API SERVER
 *
 * @author 김상준
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailManagerV2 {

    private final WebClient webClient;
    private final EmailConfig emailConfig;
    private final String SUBJECT = "[솔로몬엠] 홈페이지 오류 보고서 ";

    /**
     * 에러 리포트를 이메일로 전송하는 메서드
     *
     * @param emailRequestDto 이메일 요청 데이터를 담은 DTO
     */
    public void sendErrorReport(EmailRequestDto emailRequestDto) {

        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(emailRequestDto) // JSON 형식으로 변환해줌
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Email sent successfully: {}", response))
                .doOnError(error -> log.error("Error sending email: ", error))
                .subscribe();
    }


    /**
     * 이메일 요청 DTO를 생성하는 메서드
     *
     * @return EmailRequestDto 이메일 요청 데이터를 담은 DTO
     */
    public EmailRequestDto createEmailRequestDto(List<ErrorCode> errorCodeList) {

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedNow = now.format(formatter);

        // html 형식 에러 코드
        String errorHtml = getText(errorCodeList);

        EmailRequestDto requestDto = new EmailRequestDto();
        requestDto.setSender(emailConfig.getSender());
        requestDto.setRecipients(emailConfig.getRecipients());
        requestDto.setTitle(SUBJECT + " " + formattedNow);
        requestDto.setContent(errorHtml);
        requestDto.setDeliveryType("EMAIL");

        return requestDto;
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
