package com.r4studios.powerdice;

import java.io.Serializable;
import java.util.Date;

public class Score implements Comparable<Score>, Serializable{

	private static final long serialVersionUID = -3203337572763961889L;
	private String name;
	private int score;
	private Date date;
	
	public Score(String n, int s){
		name = n;
		score = s;
		date = new Date();
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public Date getDate(){
		return this.date;
	}

	public int compareTo(Score score2) {
		if (this.score < score2.score){
			return -1;
		}else if (this.score == score2.score){
			return 0;
		}else{
			return 1;
		}
	}
}
