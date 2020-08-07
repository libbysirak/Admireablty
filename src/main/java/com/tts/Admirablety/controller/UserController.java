package com.tts.Admirablety.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.Admirablety.model.Rating;
import com.tts.Admirablety.model.User;
import com.tts.Admirablety.service.UserService;


@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping(value = "/users/{username}")
	public String displayUser(@PathVariable(value="username") String username, Model model) {	
	    User user = userService.findByUsername(username);

	    User loggedInUser = userService.getLoggedInUser();
        List<User> tracking = loggedInUser.getTracking();
        boolean isTracking = false;
        for (User trackedUser : tracking) {
            if (trackedUser.getUsername().equals(username)) {
                isTracking = true;
            }
        }
        model.addAttribute("tracking", isTracking);
        
        boolean isSelfPage = loggedInUser.getUsername().equals(username);
        model.addAttribute("isSelfPage", isSelfPage);
	    
	    model.addAttribute("user", user);
	    return "user";
	}
    
    // Edit user details
    @PatchMapping(value = "/edit_user")
    public String editUser(String newFirstName, String newLastName, String newCity, String newState, String newCountry) {
    	User loggedInUser = userService.getLoggedInUser();
    	loggedInUser.setFirstName(newFirstName);
    	loggedInUser.setLastName(newLastName);
    	loggedInUser.setFirstName(newCity);
    	loggedInUser.setState(newState);
    	loggedInUser.setCountry(newCountry);
    	return "user";
    	
    }

	// List of users logged-in user is tracking, else a list of all users
	@GetMapping(value = "/users")
	public String getUsers(@RequestParam(value="filter", required=false) 
		String filter, Model model) {
		List<User> users = new ArrayList<User>();
		User loggedInUser = userService.getLoggedInUser();
		List<User> usersTracking = loggedInUser.getTracking();
		if (filter == null) {
		    filter = "all";
		}
		if (filter.equalsIgnoreCase("tracking")) {
		    users = usersTracking;
		    model.addAttribute("filter", "tracking");
		}
		else {
		    users = userService.findAll();
		    model.addAttribute("filter", "all");
		}
		model.addAttribute("users", users);
		SetTrackingStatus(users, usersTracking, model);
		return "users";
	}
	
	
	private void SetTrackingStatus(List<User> users, List<User> usersTracking, Model model) {
	    HashMap<String,Boolean> trackingStatus = new HashMap<>();
	    String username = userService.getLoggedInUser().getUsername();
	    for (User user : users) {
	        if(usersTracking.contains(user)) {
	            trackingStatus.put(user.getUsername(), true);
	        }else if (!user.getUsername().equals(username)) {
	            trackingStatus.put(user.getUsername(), false);
	    	}
	    }
	    model.addAttribute("trackingStatus", trackingStatus);
	}
    
    // to rate a user or replace/change an existing rating 
    @PostMapping(value = "/rate/{username}/{stars}")
    public String rate(String subjectUsername, Integer stars, HttpServletRequest request) {
    	User rater = userService.getLoggedInUser();
    	User subject = userService.findByUsername(subjectUsername);
    	Rating newRating = new Rating();
    	newRating.rater = rater;
    	newRating.stars_given = stars;		
    	ArrayList<Rating> tempAL = subject.getRatingsRecieved();
    	Long newRatingRaterId = rater.getId();
    	//Check if rater rated subject before; if so override original rating, else add new rating
    	for(Rating ratingToCheck : tempAL ) {
    		User raterToCheck = ratingToCheck.getRater();
    		Long raterIdToCheck = raterToCheck.getId();
    		if (raterIdToCheck == newRatingRaterId) {
    			ratingToCheck = newRating;
    		} else {
    	    	tempAL.add(newRating);
    		}
    	}
    	subject.setRatingsRecieved(tempAL);
        return "redirect:" + request.getHeader("Referer");

    }
    
    //handle search for user
    @GetMapping(value = "/search")
    public String handleSearch(String searchText, Model model) {
        // Break out all parts of the search text by splitting on white space
        String[] searchParts = searchText.toUpperCase().split(" ");
        // Filter out users that don't contain the entered text 
        List<User> users = userService.findAll();
        ArrayList<User> matchingUsers = new ArrayList<User>();
        for(User user: users) {
        	boolean match = true;
        	String nameString = user.getNameString();
        	// The entry needs to contain all portions of the search string but in any order
        	for(String part : searchParts) {
	        	if ( ! nameString.toUpperCase().contains(part) ) {
	                match = false;
	                break;
	        	}
	        }
        	if(match) {
        		matchingUsers.add(user);
        	}
        }
        model.addAttribute(matchingUsers);
        return "results";
    }
        
    

    //Get method for the last five ratings given to the user as the subject
    /*
    @GetMapping
    public String getSubjectsLastFive(User subject, Model model) {
    	List<Rating> ratings = userService.findLastFiveForSubject(subject);
    	model.addAttribute("subjectsRatingList",ratings); 
        return "subjectsLastFive";
    }
    */
    
    /*
  //Get method for the last five ratings given by the user as the rater
    @GetMapping
    public String getRatersLastFive(User rater, Model model) {
    	List<Rating> ratings = ratingService.findLastFiveForRater(rater);
    	model.addAttribute("ratersRatingList",ratings); 
        return "ratersLastFive";
    }
    */

    
    
    
    
  
}

