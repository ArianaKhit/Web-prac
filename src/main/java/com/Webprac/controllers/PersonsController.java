package com.Webprac.controllers;

import com.Webprac.DAO.*;
import com.Webprac.tables.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@Controller
public class PersonsController {
    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAO();
    @Autowired
    private final CoachDAO coachDAO = new CoachDAO();
    @Autowired
    private final SportsmanCoachesDAO sportsmanCoachesDAO = new SportsmanCoachesDAO();

    @GetMapping("/person")
    public String personPage(@RequestParam(name = "type", required = false) String type,
                             @RequestParam(name="personID", required = false) Long personID,
                            Model model) {
        if ("sportsman".equals(type)) {
            Sportsman sportsman = sportsmanDAO.getById(personID);
            if (sportsman == null) {
                model.addAttribute("error_msg", "Not found by ID  = " + personID);
                return "error";
            }
            Collection<SportsmanCoaches> c_sportsmen = sportsman.getSportsmanCoaches();
            model.addAttribute("person", sportsman);
            model.addAttribute("c_sportsmen", c_sportsmen);
        } else if ("coach".equals(type)) {
            Coach coach = coachDAO.getById(personID);
            if (coach == null) {
                model.addAttribute("error_msg", "Not found by ID  = " + personID);
                return "error";
            }
            Collection<SportsmanCoaches> c_sportsmen = coach.getSportsmanCoaches();
            Collection<TeamCoaches> c_teams = coach.getTeamCoaches();
            model.addAttribute("person", coach);
            model.addAttribute("c_sportsmen", c_sportsmen);
            model.addAttribute("c_teams", c_teams);
        } else {
            model.addAttribute("error_msg", "Could not parse person type " + type);
            return "error";
        }
        return "person";
    }

    @GetMapping("/persons")
    public String filterPersons(@RequestParam(name="name", required = false) String name,
                               @RequestParam(name="sport", required = false) String sport,
                               @RequestParam(name="country", required = false) String country,
                               @RequestParam(name="type", required = false) String type,
                               Model model) {
        if ("sportsman".equals(type)) {
            SportsmanDAOInterface.Filter f = new SportsmanDAOInterface.Filter(name, sport, country);
            Collection<Sportsman> sportsmen = sportsmanDAO.getByFilter(f);
            model.addAttribute("persons", sportsmen);
            model.addAttribute("sportsmanDAO", sportsmanDAO);
        } else if ("coach".equals(type)) {
            CoachDAOInterface.Filter f = new CoachDAOInterface.Filter(name, sport, country);
            Collection<Coach> coaches = coachDAO.getByFilter(f);
            model.addAttribute("persons", coaches);
            model.addAttribute("coachDAO", coachDAO);
        } else {
            model.addAttribute("error_msg", "Could not understand person type " + type);
            return "error";
        }
        return "persons";
    }

    @RequestMapping(value="/add_new_person", method = RequestMethod.POST)
    public String addPerson(@RequestParam(name="name", required = false) String name,
                            @RequestParam(name="sport", required = false) String sport,
                            @RequestParam(name="country", required = false) String country,
                            @RequestParam(name="birthday", required = false) LocalDate birthday,
                            @RequestParam(name="description", required = false) String description,
                            @RequestParam(name="type", required = false) String type,
                           Model model) {
        if ("sportsman".equals(type)) {
            Sportsman sportsman = new Sportsman(name, sport, birthday, country);
            sportsman.setDescription(description);
            sportsmanDAO.save(sportsman);
            model.addAttribute("person", sportsman);
            model.addAttribute("sportsmanDAO", sportsmanDAO);
            return "redirect:/person?type=" + type + "&personID=" + sportsman.getId().toString();
        } else if ("coach".equals(type)) {
            Coach coach = new Coach(name, sport, birthday, country);
            coach.setDescription(description);
            coachDAO.save(coach);
            model.addAttribute("persons", coach);
            model.addAttribute("coachDAO", coachDAO);
            return "redirect:/person?type=" + type + "&personID=" + coach.getId().toString();
        } else {
            model.addAttribute("error_msg", "Could not understand person type " + type);
            return "error";
        }
    }

    @RequestMapping(value="/edit_person", method = RequestMethod.POST)
    public String editPerson(@RequestParam(name="personID", required = true) Long personID,
                             @RequestParam(name="name", required = false) String name,
                             @RequestParam(name="sport", required = false) String sport,
                             @RequestParam(name="country", required = false) String country,
                             @RequestParam(name="birthdate", required = false) LocalDate birthday,
                             @RequestParam(name="description", required = false) String description,
                             @RequestParam(name="type", required = true) String type,
                            Model model) {
        if ("sportsman".equals(type)) {
            Sportsman sportsman = sportsmanDAO.getById(personID);
            sportsman.setName(name);
            sportsman.setSport(sport);
            sportsman.setCountry(country);
            sportsman.setBirthDate(birthday);
            sportsman.setDescription(description);
            sportsmanDAO.update(sportsman);

            model.addAttribute("persons", sportsmanDAO.getAll());
            model.addAttribute("personDAO", sportsmanDAO);
            return "redirect:/person?type=" + type + "&personID=" + sportsman.getId().toString();
        } else if ("coach".equals(type)) {
            Coach coach = coachDAO.getById(personID);
            coach.setName(name);
            coach.setSport(sport);
            coach.setCountry(country);
            coach.setBirthDate(birthday);
            coach.setDescription(description);
            coachDAO.update(coach);

            model.addAttribute("persons", coachDAO.getAll());
            model.addAttribute("personDAO", coachDAO);
            return "redirect:/person?type=" + type + "&personID=" + coach.getId().toString();
        } else {
            model.addAttribute("error_msg", "Could not understand person type " + type);
            return "error";
        }
    }

    @RequestMapping(value="/delete_person", method = RequestMethod.POST)
    public String deletePerson(@RequestParam(name="personID", required = true) Long personID,
                               @RequestParam(name="type", required = true) String type,
                               Model model) {
        if ("sportsman".equals(type)) {
            sportsmanDAO.deleteById(personID);
            model.addAttribute("persons", sportsmanDAO.getAll());
            model.addAttribute("sportsmanDAO", sportsmanDAO);
            return "redirect:/persons?type=" + type;
        } else if ("coach".equals(type)) {
            coachDAO.deleteById(personID);
            model.addAttribute("persons", coachDAO.getAll());
            model.addAttribute("coachDAO", coachDAO);
            return "redirect:/persons?type=" + type;
        } else {
            return "error";
        }
    }

    @GetMapping(value = "addperson")
    public String addperson(@RequestParam(name="type", required = false) String type, Model model) {
        return "addperson";
    }

    @GetMapping(value = "editperson")
    public String editperson(@RequestParam(name="personID") Long personID,
                             @RequestParam(name="type") String type,
                             Model model) {
        if ("sportsman".equals(type)) {
            Sportsman sportsman = sportsmanDAO.getById(personID);
            if (sportsman == null) {
                model.addAttribute("error_msg", "Not found by ID  = " + personID);
                return "error";
            }
            model.addAttribute("person", sportsman);
            model.addAttribute("personDAO", sportsmanDAO);
            return "editperson";
        } else if ("coach".equals(type)) {
            Coach coach = coachDAO.getById(personID);
            if (coach == null) {
                model.addAttribute("error_msg", "Not found by ID  = " + personID);
                return "error";
            }
            model.addAttribute("person", coach);
            model.addAttribute("personDAO", coachDAO);
            return "editperson";
        } else {
            model.addAttribute("error_msg", "Not found by type " + type);
            return "error";
        }
    }

    @GetMapping(value = "add_pers_to_coach")
    public String addpers2coach(@RequestParam(name="coachID") Long coachID,
                                @RequestParam(name="personID") Long personID,
                                @RequestParam(name="type") String type,
                                Model model) {
        Coach coach = coachDAO.getById(coachID);
        Sportsman sportsman = sportsmanDAO.getById(personID);
        sportsmanCoachesDAO.save(new SportsmanCoaches(coach, sportsman));
        return "redirect:/person?type=coach&personID=" + coachID.toString();
    }

    @GetMapping(value = "del_pers_from_coach")
    public String delpersfromcoach(@RequestParam(name="coachID") Long coachID,
                                   @RequestParam(name="personID") Long personID,
                                   Model model) {
        SportsmanCoaches sc = sportsmanCoachesDAO.getByIDs(coachID, personID).get(0);
        if (sc == null) {
            return "error";
        }
        sportsmanCoachesDAO.deleteById(sc.getId());
        return "redirect:/person?type=coach&personID=" + coachID.toString();
    }
}
