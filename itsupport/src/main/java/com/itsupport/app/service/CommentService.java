package com.itsupport.app.service;

import com.itsupport.app.model.Comment;
import com.itsupport.app.model.Ticket;
import com.itsupport.app.model.User;
import com.itsupport.app.repository.CommentRepository;
import com.itsupport.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public List<Comment> getCommentsByTicket(Ticket ticket) {
        return commentRepository.findByTicketOrderByCreatedAtAsc(ticket);
    }

    @Transactional
    public Comment addComment(Long ticketId, String content, User author) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket δεν βρέθηκε"));

        Comment comment = Comment.builder()
                .content(content)
                .ticket(ticket)
                .author(author)
                .build();

        return commentRepository.save(comment);
    }
}
