package com.Webprac.controllers;

import com.Webprac.DAO.EventDAO;
import com.Webprac.DAO.EventDAOInterface;
import com.Webprac.tables.SportEvent;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class EventsController {
    @Autowired
    private final EventDAO eventDAO = new EventDAO();

//    @GetMapping("/events")
//    public String listEvent(Model model) {
//        Collection<SportEvent> events = eventDAO.getAll();
//
//        model.addAttribute("events", events);
//        model.addAttribute("eventDAO", eventDAO);
//        return "events";
//    }
    @GetMapping("/event")
    public String eventPage(@RequestParam(name="eventID") Long eventID, Model model) {
        SportEvent event = eventDAO.getById(eventID);
        if (event == null) {
            model.addAttribute("error_msg", "Not found by ID  = " + eventID);
            return "error";
        }

        model.addAttribute("event", event);
        model.addAttribute("eventDAO", eventDAO);
        return "event";
    }

    @GetMapping("/events")
    public String filterEvents(@RequestParam(name="title", required = false) String title,
                               @RequestParam(name="sport", required = false) String sport,
                               @RequestParam(name="tournament", required = false) String tournament,
                               @RequestParam(name="venue", required = false) String venue,
                               @RequestParam(name="startDate", required = false) LocalDate startDate,
                               @RequestParam(name="endDate", required = false) LocalDate endDate,
                               Model model) {
        EventDAOInterface.Filter f = new EventDAOInterface.Filter(title, sport, tournament,venue, startDate, endDate);
        Collection<SportEvent> events = eventDAO.getByFilter(f);

        model.addAttribute("events", events);
        model.addAttribute("eventDAO", eventDAO);
        return "events";
    }

}
