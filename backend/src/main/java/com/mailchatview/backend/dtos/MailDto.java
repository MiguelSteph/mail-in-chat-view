package com.mailchatview.backend.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MailDto {
    private String from;
    private final List<String> to = new ArrayList<>();
    private final List<String> mailAttachments = new ArrayList<>();
    private String content;
    private String subject;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;
    private final List<String> ccs = new ArrayList<>();
}
