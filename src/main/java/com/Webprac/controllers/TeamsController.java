package com.Webprac.controllers;

import com.Webprac.DAO.*;
import com.Webprac.tables.*;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class TeamsController {
    @Autowired
    private final TeamDAO teamDAO = new TeamDAO();
    @Autowired
    private final SportsmanDAO sportsmanDAO = new SportsmanDAO();
    @Autowired
    private final CoachDAO coachDAO = new CoachDAO();
    @Autowired
    private final TeamSportsmansDAO teamSportsmansDAO = new TeamSportsmansDAO();
    @Autowired
    private final TeamCoachesDAO teamCoachesDAO = new TeamCoachesDAO();

    @GetMapping("/team")
    public String teamPage(@RequestParam(name="teamID") Long teamID, Model model) {
        Team team = teamDAO.getById(teamID);
        if (team == null) {
            model.addAttribute("error_msg", "Not found by ID  = " + teamID);
            return "error";
        }

        Collection<TeamSportsmans> t_sportsmen = team.getTeamSportsmans();
        Collection<TeamCoaches> t_coaches = team.getTeamCoaches();
        model.addAttribute("team", team);
        model.addAttribute("t_sportsmen", t_sportsmen);
        model.addAttribute("t_coaches", t_coaches);
        return "team";
    }

    @GetMapping("/teams")
    public String filterTeams(@RequestParam(name="teamname", required = false) String teamname,
                               @RequestParam(name="sport", required = false) String sport,
                               @RequestParam(name="country", required = false) String country,
                               Model model) {
        TeamDAOInterface.Filter f = new TeamDAOInterface.Filter(teamname, sport, country);
        Collection<Team> teams = teamDAO.getByFilter(f);

        model.addAttribute("teams", teams);
        return "teams";
    }

    @RequestMapping(value="/add_new_team", method = RequestMethod.POST)
    public String addTeam(@RequestParam(name="teamname", required = false) String teamname,
                           @RequestParam(name="sport", required = false) String sport,
                           @RequestParam(name="country", required = false) String country,
                           @RequestParam(name="description", required = false) String description,
                           Model model) {
        Team newTeam = new Team(teamname, sport, country);
        newTeam.setDescription(description);
        teamDAO.save(newTeam);
        model.addAttribute("teams", teamDAO.getAll());
        return "redirect:/team?teamID=" + newTeam.getId().toString();
    }

    @RequestMapping(value="/edit_team", method = RequestMethod.POST)
    public String editTeam(@RequestParam(name="teamID", required = true) Long teamID,
                            @RequestParam(name="teamname", required = false) String teamname,
                            @RequestParam(name="sport", required = false) String sport,
                            @RequestParam(name="country", required = false) String country,
                            @RequestParam(name="description", required = false) String description,
                            Model model) {
        Team team = teamDAO.getById(teamID);
        team.setTeamName(teamname);
        team.setSport(sport);
        team.setCountry(country);
        team.setDescription(description);
        teamDAO.update(team);

        model.addAttribute("teams", teamDAO.getAll());
        return "redirect:/team?teamID=" + team.getId().toString();
    }

    @RequestMapping(value="/delete_team", method = RequestMethod.POST)
    public String deleteTeam(@RequestParam(name="teamID", required = true) Long teamID,
                              Model model) {
        teamDAO.deleteById(teamID);
        model.addAttribute("teams", teamDAO.getAll());
        return "redirect:/teams";
    }

    @RequestMapping(value = "addteam")
    public String addteam() {
        return "addteam";
    }

    @RequestMapping(value = "editteam")
    public String editteam(@RequestParam(name="teamID") Long teamID, Model model) {
        Team team = teamDAO.getById(teamID);
        if (team == null) {
            model.addAttribute("error_msg", "Not found by ID  = " + teamID);
            return "error";
        }

        model.addAttribute("team", team);
        return "editteam";
    }


    @GetMapping(value = "add_pers_to_team")
    public String addpers2team(@RequestParam(name="teamID") Long teamID,
                                @RequestParam(name="personID") Long personID,
                                @RequestParam(name="type") String type,
                                Model model) {
        Team team = teamDAO.getById(teamID);
        Sportsman sportsman = sportsmanDAO.getById(personID);
        teamSportsmansDAO.save(new TeamSportsmans(team, sportsman, Boolean.TRUE));
        return "redirect:/team?teamID=" + teamID.toString();
    }

    @GetMapping(value = "del_pers_from_team")
    public String delpersfromteam(@RequestParam(name="teamID") Long teamID,
                                   @RequestParam(name="personID") Long personID,
                                   Model model) {
        TeamSportsmans ts = teamSportsmansDAO.getByIDs(teamID, personID).get(0);
        if (ts == null) {
            return "error";
        }
        teamSportsmansDAO.deleteById(ts.getId());
        return "redirect:/team?teamID=" + teamID.toString();
    }



    @GetMapping(value = "add_team_to_coach")
    public String addteam2coach(@RequestParam(name="coachID") Long coachID,
                                @RequestParam(name="teamID") Long teamID,
                                Model model) {
        Coach coach = coachDAO.getById(coachID);
        Team team = teamDAO.getById(teamID);
        teamCoachesDAO.save(new TeamCoaches(coach, team));
        return "redirect:/person?type=coach&personID=" + coachID.toString();
    }

    @GetMapping(value = "del_team_from_coach")
    public String delteamfromcoach(@RequestParam(name="coachID") Long coachID,
                                   @RequestParam(name="teamID") Long teamID,
                                   Model model) {
        TeamCoaches tc = teamCoachesDAO.getByIDs(coachID, teamID).get(0);
        if (tc == null) {
            return "error";
        }
        teamCoachesDAO.deleteById(tc.getId());
        return "redirect:/person?type=coach&personID=" + coachID.toString();
    }
}
