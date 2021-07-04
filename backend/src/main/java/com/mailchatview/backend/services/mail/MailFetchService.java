package com.mailchatview.backend.services.mail;

import com.mailchatview.backend.dtos.FetchQueryDto;
import com.mailchatview.backend.dtos.FetchResultPage;
import com.mailchatview.backend.entities.User;

public interface MailFetchService {

    FetchResultPage fetchMails(User user, FetchQueryDto fetchQuery);

}
