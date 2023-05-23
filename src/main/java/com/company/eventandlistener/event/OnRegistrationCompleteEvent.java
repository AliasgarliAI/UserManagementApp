package com.company.eventandlistener.event;

import com.company.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public OnRegistrationCompleteEvent(User user, String url) {
        super(user);
        this.user = user;
        this.applicationUrl = url;
    }


}
