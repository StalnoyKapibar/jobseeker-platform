package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.ReplyDaoI;
import com.jm.jobseekerplatform.model.Reply;
import com.jm.jobseekerplatform.model.comments.Comment;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("replyService")
@Transactional
public class ReplyService extends AbstractService<Reply> {

    @Autowired
    private ReplyDaoI replyDaoI;

    /*public List<Reply> getAllRepliesForComment(Comment currentComment) {
        return replyDaoI.getAllRepliesForComment(currentComment);
    }

    public List<Reply> getAllRepliesByAddress(Long addressId) {
        return replyDaoI.getAllRepliesByAddress(addressId);
    }*/
}
