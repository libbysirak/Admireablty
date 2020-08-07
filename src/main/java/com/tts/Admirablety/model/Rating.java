package com.tts.Admirablety.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "tracking_id")
	public Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "rater_id", referencedColumnName = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public User rater;
	
	public Integer stars_given;
		
	@CreationTimestamp 
	private Date createdAt;
	
	public Rating() {}
	
	public Rating(Long id, User rater, User subject, Integer stars_given, Date createdAt) {
		super();
		this.id = id;
		this.rater = rater;
		this.stars_given = stars_given;
		this.createdAt = createdAt;
	}

	public User getRater() {
		return rater;
	}

	public void setRater(User rater) {
		this.rater = rater;
	}

	public Integer getStars_given() {
		return stars_given;
	}

	public void setStars_given(Integer stars_given) {
		this.stars_given = stars_given;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Rating [id=" + id + ", stars_given=" + stars_given
				+ ", createdAt=" + createdAt + "]";
	}

	
	
	

}
