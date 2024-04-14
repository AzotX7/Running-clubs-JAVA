package com.rungroup.web.controller;

import com.rungroup.web.dto.ClubDto;
import com.rungroup.web.models.Club;
import com.rungroup.web.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClubController {
    private ClubService clubService;
    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;

    }

    @GetMapping("/clubs")
    public String listClubs(Model model){
        List<ClubDto> clubs = clubService.findAll();
        model.addAttribute("clubs",clubs);
        return "clubs-list";
    }

    @GetMapping("/clubs/new")
    public String createClubForm(Model model){
        Club club = new Club();
        model.addAttribute("club",club);

        return "clubs-create";
    }

    @GetMapping("clubs/{id}")
    public String clubDetail(@PathVariable("id") Long clubId,Model model){
        ClubDto clubDto = clubService.findClubById(clubId);
        model.addAttribute("club",clubDto);

        return "clubs-detail";
    }
    @GetMapping("clubs/{id}/delete")
    public String deleteClub(@PathVariable("id") Long clubId){
        clubService.delete(clubId);

        return "redirect:/clubs";
    }

    @GetMapping("/clubs/search")
    public String searchClub(@RequestParam(value = "query") String query,Model model){
        List<ClubDto> clubs = clubService.searchClubs(query);
        model.addAttribute("clubs",clubs);

        return "clubs-list";
    }

    @PostMapping("/clubs/new")
    public String saveClub(@Valid @ModelAttribute("club") ClubDto clubDto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("club",clubDto);
            return "clubs-create";
        }

        clubService.saveClub(clubDto);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/{id}/edit")
    public String editClubForm(@PathVariable("id") Long clubId,Model model){
        ClubDto club = clubService.findClubById(clubId);
        model.addAttribute("club",club);

        return "clubs-edit";

    }

    @PostMapping("/clubs/{id}/edit")
    public String updateClub(@PathVariable("id") Long clubId, @Valid @ModelAttribute("club") ClubDto club, BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("club",club);
            return "clubs-edit";
        }
        club.setId(clubId);
        clubService.updateClub(club);
        return "redirect:/clubs";
    }

}
