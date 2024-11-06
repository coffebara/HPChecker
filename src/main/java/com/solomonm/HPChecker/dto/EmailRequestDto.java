package com.solomonm.HPChecker.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class EmailRequestDto {
    private String sender;
    private List<String> recipients;
    private String title;
    private String content;
    private String deliveryType;
}
