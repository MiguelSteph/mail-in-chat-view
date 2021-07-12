package com.mailchatview.backend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MailDto {
    private String from;
    private final List<String> to = new ArrayList<>();
    private final List<String> mailAttachments = new ArrayList<>();
    private String snippet;
    private String content;
    private String subject;
    private String contentType;
    @JsonFormat(pattern = "yyyy-MM-dd 'at' HH:mm:ss")
    private LocalDateTime dateTime;
    private final List<String> ccs = new ArrayList<>();
}
