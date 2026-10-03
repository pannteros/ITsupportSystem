package com.itsupport.app.service;

import com.itsupport.app.model.Ticket;
import com.itsupport.app.model.User;
import com.itsupport.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByCreatedByOrderByCreatedAtDesc(user);
    }

    public List<Ticket> getTicketsByStatus(Ticket.Status status) {
        return ticketRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    @Transactional
    public Ticket createTicket(Ticket ticket, User user) {
        ticket.setCreatedBy(user);
        ticket.setStatus(Ticket.Status.OPEN);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void updateStatus(Long id, Ticket.Status status) {
        ticketRepository.findById(id).ifPresent(t -> {
            t.setStatus(status);
            ticketRepository.save(t);
        });
    }

    @Transactional
    public void assignTicket(Long id, User admin) {
        ticketRepository.findById(id).ifPresent(t -> {
            t.setAssignedTo(admin);
            t.setStatus(Ticket.Status.IN_PROGRESS);
            ticketRepository.save(t);
        });
    }

    public long countByStatus(Ticket.Status status) {
        return ticketRepository.countByStatus(status);
    }
}
