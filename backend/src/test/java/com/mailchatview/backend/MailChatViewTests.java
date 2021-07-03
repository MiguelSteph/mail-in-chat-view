package com.mailchatview.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(args= {"--google.web.id=sampleID", "--google.web.secret=sampleSecret"})
class MailChatViewTests {

    @Test
    void contextLoads() {
    }

}
