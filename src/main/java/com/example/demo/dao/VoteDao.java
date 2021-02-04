package com.example.demo.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VoteDao {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String pollName;
	private int choice1;
	private int choice2;
	private int choice3;
	private int choice4;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPollName() {
		return pollName;
	}
	public void setPollName(String pollName) {
		this.pollName = pollName;
	}
	public int getChoice1() {
		return choice1;
	}
	public void setChoice1(int choice1) {
		this.choice1 = choice1;
	}
	public int getChoice2() {
		return choice2;
	}
	public void setChoice2(int choice2) {
		this.choice2 = choice2;
	}
	public int getChoice3() {
		return choice3;
	}
	public void setChoice3(int choice3) {
		this.choice3 = choice3;
	}
	public int getChoice4() {
		return choice4;
	}
	public void setChoice4(int choice4) {
		this.choice4 = choice4;
	}
	public String getVoterName() {
		return voterName;
	}
	public void setVoterName(String voterName) {
		this.voterName = voterName;
	}
	private String voterName;
}
