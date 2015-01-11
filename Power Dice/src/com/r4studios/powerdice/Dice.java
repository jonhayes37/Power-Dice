package com.r4studios.powerdice;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.r4studios.Utilities.RandomNumber;

public class Dice implements Comparable<Dice>{
	private int sides;
	private int lastRoll = -1;
	public Image[] sidePics;
	private static final Image[] DEFAULT_DIE = {new ImageIcon("Resources/0.png").getImage(), new ImageIcon("Resources/1.png").getImage(), new ImageIcon("Resources/2.png").getImage(), new ImageIcon("Resources/3.png").getImage(), new ImageIcon("Resources/4.png").getImage(), new ImageIcon("Resources/5.png").getImage()};
	
	public Dice(int numSides){
		this.sides = numSides;
		if (numSides == 6){
			this.sidePics = DEFAULT_DIE;
		}else{
			this.sidePics = new Image[sides];
		}
	}
	
	public Dice(int numSides, String id){
		this.sides = numSides;
		this.sidePics = DefaultPics(numSides, id);
	}
	
	public Dice(int numSides, ImageIcon[] pics){
		this.sides = numSides;
		this.sidePics = ConvertPics(pics);
	}
	
	public Dice(int numSides, Image[] pics){
		this.sides = numSides;
		this.sidePics = pics;
	}
	
	/**
	 * @return Returns a random number, the roll, 
	 * between 1 and the number of sides on the die.
	 */
	public int Roll(){
		RandomNumber rand = new RandomNumber();
		lastRoll = rand.RandomNum(1, sides);
		return lastRoll;
	}
	
	public Image GetResultImage(int result){
		return sidePics[result];
	}
	
	public int getNumSides(){
		return this.sides;
	}
	
	private Image[] ConvertPics(ImageIcon[] pics){
		Image[] newImg = new Image[pics.length];
		for (int i = 0; i < pics.length; i++){
			newImg[i] = pics[i].getImage();
		}
		return newImg;
	}
	
	private Image[] DefaultPics(int sides, String id){
		Image[] newImg = new Image[sides];
		for (int i = 0; i < sides; i++){
			newImg[i] = new ImageIcon("Resources/" + id + "_" + i + ".png").getImage();
		}
		return newImg;
	}

	public int compareTo(Dice d2) {
		if (this.sides > d2.sides){
			return 1;
		}else if (this.sides == d2.sides){
			return 0;
		}else{
			return -1;
		}
	}
	
	public int getLastRoll(){
		return lastRoll;
	}
}
