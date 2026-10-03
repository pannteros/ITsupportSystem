package com.itsupport.app.controller;

import com.itsupport.app.model.Ticket;
import com.itsupport.app.model.User;
import com.itsupport.app.service.CommentService;
import com.itsupport.app.service.TicketService;
import com.itsupport.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalTickets", ticketService.getAllTickets().size());
        model.addAttribute("openTickets", ticketService.countByStatus(Ticket.Status.OPEN));
        model.addAttribute("inProgressTickets", ticketService.countByStatus(Ticket.Status.IN_PROGRESS));
        model.addAttribute("resolvedTickets", ticketService.countByStatus(Ticket.Status.RESOLVED));
        model.addAttribute("closedTickets", ticketService.countByStatus(Ticket.Status.CLOSED));
        model.addAttribute("recentTickets", ticketService.getAllTickets().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/tickets")
    public String allTickets(@RequestParam(required = false) String status, Model model) {
        if (status != null && !status.isBlank()) {
            model.addAttribute("tickets", ticketService.getTicketsByStatus(Ticket.Status.valueOf(status)));
            model.addAttribute("selectedStatus", status);
        } else {
            model.addAttribute("tickets", ticketService.getAllTickets());
        }
        model.addAttribute("statuses", Ticket.Status.values());
        return "admin/tickets";
    }

    @GetMapping("/tickets/{id}")
    public String ticketDetail(@PathVariable Long id, Model model, Principal principal) {
        Ticket ticket = ticketService.findById(id).orElseThrow();
        User currentUser = userService.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("ticket", ticket);
        model.addAttribute("comments", commentService.getCommentsByTicket(ticket));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("statuses", Ticket.Status.values());
        return "admin/ticket-detail";
    }

    @PostMapping("/tickets/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam Ticket.Status status,
                               RedirectAttributes redirectAttributes) {
        ticketService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Το status ενημερώθηκε!");
        return "redirect:/admin/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/assign")
    public String assignTicket(@PathVariable Long id, Principal principal,
                               RedirectAttributes redirectAttributes) {
        User admin = userService.findByUsername(principal.getName()).orElseThrow();
        ticketService.assignTicket(id, admin);
        redirectAttributes.addFlashAttribute("success", "Το ticket ανατέθηκε σε εσάς!");
        return "redirect:/admin/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content,
                             Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        commentService.addComment(id, content, user);
        return "redirect:/admin/tickets/" + id;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }
}
