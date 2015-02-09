package com.r4studios.powerdice;

import java.io.Serializable;

import com.r4studios.DataStructures.List;

/**
 * @author Jonathan
 * An individual high score has the player's name and score,
 * as well as the date on which they achieved the score.
 */
public class HighScoreTable implements Serializable{

	private static final long serialVersionUID = 1408151645280678445L;
	private List<Score> scores;
	
	public HighScoreTable(){
		scores = new List<Score>();
	}
	
	public HighScoreTable(List<Score> scores){
		this.scores = scores;
		this.scores = this.scores.QuickSort().Reverse();
	}
	
	public List<Score> getScores(){
		return this.scores;
	}
	
	// Returns true if score added
	public boolean AddScore(Score score){
		if (scores.getSize() == 10 && score.compareTo(scores.GetValueAt(9)) > 0 && score.getScore() > 20000){ // Only keeps top 10 scores
			scores.Pop();
			scores.Push(score);
			scores = scores.QuickSort().Reverse();
			return true;
		}else if (scores.getSize() < 10 && score.getScore() > 20000){
			scores.Push(score);
			scores = scores.QuickSort().Reverse();
			return true;
		}
		return false;
	}
}
