package com.tts.Admirablety.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tts.Admirablety.model.Rating;
import com.tts.Admirablety.model.RatingDisplay;
import com.tts.Admirablety.model.Role;
import com.tts.Admirablety.model.User;
import com.tts.Admirablety.repository.RoleRepository;
import com.tts.Admirablety.repository.UserRepository;


@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, 
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder
                       ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public void deleteById(Long id) {
    	userRepository.deleteById(id);
    }
        
    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }
        
    public void save(User user) {
        userRepository.save(user);
    }
    
    
    
    public void findById(Long id) {
    	userRepository.findById(id);
    }
    
    //method for password hashing 
    public User saveNewUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }
    
    public User getLoggedInUser() {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        
        return findByUsername(loggedInUsername);
    }
    
    
    //to determine a user's overall rating and designation
    public void determineUsersWorth(User subject) {
    	ArrayList<Rating> tempAL = subject.getRatingsRecieved();
    	Double totalWeight = 0.0;
    	Double runningTotal = 0.0;
    	for(Rating rating : tempAL) {
    		Double ratersRating = rating.rater.getOverall_rating();
    		Double weight = Math.pow(2, ratersRating);
    		Integer stars = rating.stars_given;
    		Double points = weight*stars;
    		totalWeight = totalWeight+weight;
    		runningTotal = runningTotal+points;
    	}
    	Double overallRating = runningTotal / totalWeight;
    	subject.setOverall_rating(overallRating);
    	String designation;
    	if(overallRating < 0.5) {
    		designation = "Swab";
    	} else if (overallRating < 1.5) {
    		designation = "Rear Admirable - Lower Half";
    	} else if (overallRating < 2.5) {
    		designation = "Rear Admirable - Upper Half";
    	} else if (overallRating < 3.5) {
    		designation = "Vice Admirable";
    	} else if (overallRating < 4.5) {
    		designation = "Admirable";
    	} else {
    		designation = "Fleet Admirable";
    	}
    	subject.setDesignation(designation);
    }
    
    
    //Method to get last five ratings received for user and pass to User Controller
    /*
    public List<Rating> findLastFiveForSubject(User subject) {
    	List<Rating>ratingsForSubjectList = subject.getRatingsRecieved();
    	List<Rating> sortedRatingsForSubjectList = ratingsForSubjectList.sort(Comparator.comparing(Rating::getCreatedAt()));
    	List<Rating> lastFiveRatingsForSubject = ratingsForSubjectList.subList(0, 5);
    	return lastFiveRatingsForSubject;
    }
    */
    
    
    
    



    // Needs adjustment
    /* Long-winded way to make the time format look nice (through a RatingDislpay model) */
    private List<RatingDisplay> formatTimestamps(List<Rating> ratings) {
    	List<RatingDisplay> response = new ArrayList<>();
    	PrettyTime prettyTime = new PrettyTime();
    	SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
    	Date now = new Date();
    	for(Rating rating : ratings) {
    		RatingDisplay ratingDisplay = new RatingDisplay();
    		ratingDisplay.setRater(rating.getRater());
    		ratingDisplay.setStars(rating.getStars_given());
    		long diffInMillies = Math.abs(now.getTime() - rating.getCreatedAt().getTime());
    		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    		if (diff > 3) {
    			ratingDisplay.setDate(simpleDate.format(rating.getCreatedAt()));
    		} else {
    			ratingDisplay.setDate(prettyTime.format(rating.getCreatedAt()));
    		}
    		response.add(ratingDisplay);
    	}
    	return response;
    }
    
    
    
    
    
    
    
    
}