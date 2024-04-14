package com.rungroup.web.controller;

import com.rungroup.web.dto.EventDto;
import com.rungroup.web.models.Event;
import com.rungroup.web.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EventController {
    private EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping("/events")
    public String eventList(Model model){
        List<EventDto> events = eventService.findAllEvents();
        model.addAttribute("events",events);
        return "events-list";
    }

    @GetMapping("/events/{id}")
    public String viewEvent(@PathVariable("id") Long eventId,Model model){
        EventDto eventDto = eventService.findByEventId(eventId);
        model.addAttribute("event",eventDto);
        return "events-detail";
    }
    @GetMapping("/events/{id}/new")
    public String createEventForm(@PathVariable("id") Long clubId, Model model){
        Event event = new Event();
        model.addAttribute("id",clubId);
        model.addAttribute("event",event);

        return "events-create";
    }


    @PostMapping("/events/{id}")
    public String createEvent(@PathVariable("id") Long clubId,@Valid @ModelAttribute("event") EventDto eventDto,Model model,BindingResult result){
        if(result.hasErrors()){
            model.addAttribute("event",eventDto);
            return "clubs-create";
        }
        eventService.createEvent(clubId,eventDto);
        return "redirect:/clubs/" + clubId;
    }
    @GetMapping("events/{id}/delete")
    public String deleteEvent(@PathVariable("id") Long eventId){
        eventService.delete(eventId);
        return "redirect:/events";
    }
    @GetMapping("/events/{id}/edit")
    public String editEventForm(@PathVariable("id") Long eventId,Model model){
        EventDto event = eventService.findByEventId(eventId);
        model.addAttribute("event",event);
        return "events-edit";
    }
    @PostMapping("/events/{id}/edit")
    public String updateEvent(@PathVariable("id") Long eventId, @Valid @ModelAttribute("event") EventDto event, BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("event",event);
            return "events-edit";
        }
        EventDto eventDto = eventService.findByEventId(eventId);
        event.setId(eventId);
        event.setClub(eventDto.getClub());
        eventService.updateEvent(event);
        return "redirect:/events";
    }

}
