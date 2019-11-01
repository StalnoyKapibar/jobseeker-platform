/*
 * Copyright (c) 2019. by ASD
 */

package com.jm.jobseekerplatform.dao.impl.chats;

import com.jm.jobseekerplatform.model.chats.NewChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewChatDAO extends JpaRepository<NewChat, Long> {

}
