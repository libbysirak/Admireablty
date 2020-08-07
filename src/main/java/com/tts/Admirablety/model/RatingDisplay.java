package com.tts.Admirablety.model;

public class RatingDisplay {
    private User rater;
    private User subject;
    private Integer stars;
    private String rater_rating;
    private String date;
      
    public RatingDisplay() {} 
	
	public RatingDisplay(User rater, User subject, Integer stars, String rater_rating, String date) {
		super();
		this.rater = rater;
		this.subject = subject;
		this.stars = stars;
		this.rater_rating = rater_rating;
		this.date = date;
	}

	public User getRater() {
		return rater;
	}
	public void setRater(User rater) {
		this.rater = rater;
	}
	public User getSubject() {
		return subject;
	}
	public void setSubject(User subject) {
		this.subject = subject;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getRater_rating() {
		return rater_rating;
	}

	public void setRater_rating(String rater_rating) {
		this.rater_rating = rater_rating;
	}

	@Override
	public String toString() {
		return "RatingDisplay [rater=" + rater + ", subject=" + subject + ", stars=" + stars + ", date=" + date + "]";
	}
    
    
    
    
}

