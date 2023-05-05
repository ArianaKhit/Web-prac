package com.Webprac.controllers;

import com.Webprac.DAO.*;
import com.Webprac.jsons.JSONConverter;
import com.Webprac.tables.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@Controller
public class EventsController {
    @Autowired
    private final EventDAO eventDAO = new EventDAO();
    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAO();
    @Autowired
    private final TeamDAO teamDAO = new TeamDAO();
    @Autowired
    private final EventSportsmansDAO eventSportsmansDAO = new EventSportsmansDAO();
    @Autowired
    private final EventTeamsDAO eventTeamsDAO = new EventTeamsDAO();

    @GetMapping("/event")
    public String eventPage(@RequestParam(name="eventID") Long eventID, Model model) {
        SportEvent event = eventDAO.getById(eventID);
        if (event == null) {
            model.addAttribute("error_msg", "Not found by ID  = " + eventID);
            return "error";
        }

        Collection<EventSportsmans> e_sportsmen = event.getEventSportsmans();
        Collection<EventTeams> e_teams = event.getEventTeams();
        model.addAttribute("event", event);
        model.addAttribute("eventDAO", eventDAO);
        model.addAttribute("e_sportsmen", e_sportsmen);
        model.addAttribute("e_teams", e_teams);
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

    @RequestMapping(value="/add_new_event", method = RequestMethod.POST)
    public String addEvent(@RequestParam(name="title", required = false) String title,
                           @RequestParam(name="sport", required = false) String sport,
                           @RequestParam(name="tournament", required = false) String tournament,
                           @RequestParam(name="description", required = false) String description,
                           @RequestParam(name="venue", required = false) String venue,
                           @RequestParam(name="date", required = false) LocalDate date,
                           @RequestParam(name="seats", required = false) String seats,
                           @RequestParam(name="results", required = false) String results,
                           Model model) {
        SportEvent newEvent = new SportEvent(title, sport, tournament, description,venue, date, null, null);
        eventDAO.save(newEvent);
        model.addAttribute("events", eventDAO.getAll());
        model.addAttribute("eventDAO", eventDAO);
        return "redirect:/event?eventID=" + newEvent.getId().toString();
    }

    @RequestMapping(value="/edit_event", method = RequestMethod.POST)
    public String editEvent(@RequestParam(name="eventID", required = true) Long eventID,
                           @RequestParam(name="title", required = false) String title,
                           @RequestParam(name="sport", required = false) String sport,
                           @RequestParam(name="tournament", required = false) String tournament,
                           @RequestParam(name="description", required = false) String description,
                           @RequestParam(name="venue", required = false) String venue,
                           @RequestParam(name="date", required = false) LocalDate date,
                           @RequestParam(name="seats", required = false) String seats,
                           @RequestParam(name="results", required = false) String results,
                           Model model) {
        JSONConverter jsonConverter = new JSONConverter();
        JsonNode seats_json = jsonConverter.convertToEntityAttribute(seats);
        JsonNode results_json = jsonConverter.convertToEntityAttribute(results);

        SportEvent event = eventDAO.getById(eventID);
        event.setTitle(title);
        event.setSport(sport);
        event.setTournament(tournament);
        event.setDescription(description);
        event.setVenue(venue);
        event.setDate(date);
        event.setSeats(seats_json);
        event.setResults(results_json);
        eventDAO.update(event);

        model.addAttribute("events", eventDAO.getAll());
        model.addAttribute("eventDAO", eventDAO);
        return "redirect:/event?eventID=" + event.getId().toString();
    }

    @RequestMapping(value="/delete_event", method = RequestMethod.POST)
    public String deleteEvent(@RequestParam(name="eventID", required = true) Long eventID,
                           Model model) {
        eventDAO.deleteById(eventID);
        model.addAttribute("events", eventDAO.getAll());
        model.addAttribute("eventDAO", eventDAO);
        return "redirect:/events";
    }

    @RequestMapping(value = "addevent")
    public String addevent() {
        return "addevent";
    }

    @RequestMapping(value = "editevent")
    public String editevent(@RequestParam(name="eventID") Long eventID, Model model) {
        SportEvent event = eventDAO.getById(eventID);
        if (event == null) {
            model.addAttribute("error_msg", "Not found by ID  = " + eventID);
            return "error";
        }

        model.addAttribute("event", event);
        model.addAttribute("eventDAO", eventDAO);
        return "editevent";
    }


    @GetMapping(value = "add_pers_to_event")
    public String addpers2event(@RequestParam(name="eventID") Long eventID,
                            @RequestParam(name="personID") Long personID,
                            @RequestParam(name="type") String type,
                            Model model) {
        SportEvent event = eventDAO.getById(eventID);
        Sportsman sportsman = sportsmanDAO.getById(personID);
        eventSportsmansDAO.save(new EventSportsmans(event, sportsman));
        return "redirect:/event?eventID=" + eventID.toString();
    }

    @GetMapping(value = "del_pers_from_event")
    public String delpersfromevent(@RequestParam(name="eventID") Long eventID,
                            @RequestParam(name="personID") Long personID,
                            @RequestParam(name="type") String type,
                            Model model) {
        EventSportsmans es = eventSportsmansDAO.getByIDs(eventID, personID).get(0);
        if (es == null) {
            return "error";
        }
        eventSportsmansDAO.deleteById(es.getId());
        return "redirect:/event?eventID=" + eventID.toString();
    }


    @GetMapping(value = "add_team_to_event")
    public String addteam2event(@RequestParam(name="eventID") Long eventID,
                                @RequestParam(name="teamID") Long teamID,
                                Model model) {
        SportEvent event = eventDAO.getById(eventID);
        Team team = teamDAO.getById(teamID);
        eventTeamsDAO.save(new EventTeams(event, team));
        return "redirect:/event?eventID=" + eventID.toString();
    }

    @GetMapping(value = "del_team_from_event")
    public String delteamfromevent(@RequestParam(name="eventID") Long eventID,
                                   @RequestParam(name="teamID") Long teamID,
                                   Model model) {
        EventTeams et = eventTeamsDAO.getByIDs(eventID, teamID).get(0);
        if (et == null) {
            return "error";
        }
        eventTeamsDAO.deleteById(et.getId());
        return "redirect:/event?eventID=" + eventID.toString();
    }
}
