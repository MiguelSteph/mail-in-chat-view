package com.mailchatview.backend.rest;

import com.mailchatview.backend.dtos.FetchQueryDto;
import com.mailchatview.backend.dtos.FetchResultPage;
import com.mailchatview.backend.entities.User;
import com.mailchatview.backend.services.UserService;
import com.mailchatview.backend.services.mail.MailFetchService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/private")
public class MailResource {

    private final UserService userService;
    private final MailFetchService mailFetchService;

    @PostMapping("/mail")
    public FetchResultPage fetchMails(Authentication authentication,
                                      @RequestBody FetchQueryDto fetchQueryDto) {
        User user = userService.findByUsername(authentication.getName());
        return mailFetchService.fetchMails(user, fetchQueryDto);
    }
}
