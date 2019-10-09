package com.jm.jobseekerplatform.model.chats;

import com.jm.jobseekerplatform.model.EmployerReviews;
import com.jm.jobseekerplatform.model.profiles.Profile;
import com.jm.jobseekerplatform.model.users.User;
import javax.persistence.Entity;
import java.util.List;

@Entity
public class ChatWithTopicReview extends ChatWithTopic<EmployerReviews> {
    public ChatWithTopicReview() {
    }

    public ChatWithTopicReview(Profile creatorProfile, EmployerReviews about) {
        super(creatorProfile, about);
    }

    public ChatWithTopicReview(Profile creatorProfile, List<User> chatMembers, EmployerReviews topic) {
        super(creatorProfile, chatMembers, topic);
    }
}
