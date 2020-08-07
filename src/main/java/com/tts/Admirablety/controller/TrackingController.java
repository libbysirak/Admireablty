package com.tts.Admirablety.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.tts.Admirablety.model.User;
import com.tts.Admirablety.service.UserService;



@Controller
public class TrackingController {
    @Autowired
    private UserService userService;
    
    @PutMapping(value = "/track/{username}")
    public String follow(@PathVariable(value="username") String username, HttpServletRequest request) {
    	User loggedInUser = userService.getLoggedInUser();
    	User userToTrack = userService.findByUsername(username);
    	List<User> trackers = userToTrack.getTrackers();
    	trackers.add(loggedInUser);
    	userToTrack.setTrackers(trackers);
        userService.save(userToTrack);
        return "redirect:" + request.getHeader("Referer");
    }
    
    @PostMapping(value = "/purge/{username}")
    public String unfollow(@PathVariable(value="username") String username, HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToPurge = userService.findByUsername(username);
        List<User> trackers = userToPurge.getTrackers();
        trackers.remove(loggedInUser);
        userToPurge.setTrackers(trackers);
        userService.save(userToPurge);
        return "redirect:" + request.getHeader("Referer");
    }    
}