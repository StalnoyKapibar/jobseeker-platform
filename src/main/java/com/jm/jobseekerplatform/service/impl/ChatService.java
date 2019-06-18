package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.VacancyDAO;
import com.jm.jobseekerplatform.model.*;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("chatService")
@Transactional
public class ChatService extends AbstractService<Chat> {
}