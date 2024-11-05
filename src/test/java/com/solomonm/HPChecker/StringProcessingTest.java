package com.solomonm.HPChecker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class StringProcessingTest {

    @Test
    void catchStatus() {
        // given
        StringBuilder sb = new StringBuilder();
        sb.append("┌────┬─────────────┬─────────────┬─────────┬─────────┬──────────┬────────┬──────┬───────────┬──────────┬──────────┬──────────┬──────────┐\n" +
                "│ id │ name        │ namespace   │ version │ mode    │ pid      │ uptime │ ↺    │ status    │ cpu      │ mem      │ user     │ watching │\n" +
                "├────┼─────────────┼─────────────┼─────────┼─────────┼──────────┼────────┼──────┼───────────┼──────────┼──────────┼──────────┼──────────┤\n" +
                "│ 0  │ solomonm    │ default     │ N/A     │ fork    │ 9628     │ 2M     │ 5    │ offline    │ 0%       │ 19.8mb   │ guser    │ disabled │\n" +
                "└────┴─────────────┴─────────────┴─────────┴─────────┴──────────┴────────┴──────┴───────────┴──────────┴──────────┴──────────┴──────────┘\n");

        // when
        // 정규 표현식 패턴을 사용하여 "status" 값을 추출
        Pattern pattern = Pattern.compile("│\\s*\\d+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*\\d+\\s*│\\s*[^│]+\\s*│\\s*[^│]+\\s*│\\s*([^│]+)\\s*│");
        Matcher matcher = pattern.matcher(sb.toString());

        String status = "";

        if (matcher.find()) {
            status = matcher.group(1).trim();
            System.out.println(status);
            System.out.println("Status: " + status);
        } else {
            System.out.println("Status not found.");
        }


        // then
        Assertions.assertEquals(status,"online");
    }
}
