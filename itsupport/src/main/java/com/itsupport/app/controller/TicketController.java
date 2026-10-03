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
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CommentService commentService;

    // Λίστα tickets του user
    @GetMapping
    public String myTickets(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("tickets", ticketService.getTicketsByUser(user));
        model.addAttribute("openCount", ticketService.countByStatus(Ticket.Status.OPEN));
        model.addAttribute("inProgressCount", ticketService.countByStatus(Ticket.Status.IN_PROGRESS));
        model.addAttribute("resolvedCount", ticketService.countByStatus(Ticket.Status.RESOLVED));
        return "tickets/list";
    }

    // Φόρμα νέου ticket
    @GetMapping("/new")
    public String newTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("priorities", Ticket.Priority.values());
        model.addAttribute("categories", Ticket.Category.values());
        return "tickets/form";
    }

    // Δημιουργία ticket
    @PostMapping("/new")
    public String createTicket(@ModelAttribute Ticket ticket, Principal principal,
                               RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        ticketService.createTicket(ticket, user);
        redirectAttributes.addFlashAttribute("success", "Το ticket δημιουργήθηκε επιτυχώς!");
        return "redirect:/tickets";
    }

    // Λεπτομέρειες ticket
    @GetMapping("/{id}")
    public String ticketDetail(@PathVariable Long id, Model model, Principal principal) {
        Ticket ticket = ticketService.findById(id).orElseThrow();
        User currentUser = userService.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("ticket", ticket);
        model.addAttribute("comments", commentService.getCommentsByTicket(ticket));
        model.addAttribute("currentUser", currentUser);
        return "tickets/detail";
    }

    // Προσθήκη comment
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content,
                             Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        commentService.addComment(id, content, user);
        return "redirect:/tickets/" + id;
    }
}
