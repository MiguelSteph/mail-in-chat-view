package com.mailchatview.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class FetchResultPage {
    private List<MailDto> mails;
    private String pageToken;
}
