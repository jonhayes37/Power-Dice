package com.r4studios.powerdice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import com.r4studios.DataStructures.List;
import com.r4studios.powerdice.Dice;

public class MainWindow extends JFrame implements ActionListener{

	private static final long serialVersionUID = -4703822372966597157L;
	private JPanel pnlMain;
	private JPanel pnlGameArea;
	private JPanel pnlScoreArea;
	private JPanel[] pnlPlayers = new JPanel[4];
	private JPanel[] pnlPlrNames = new JPanel[4];
	private JPanel pnlKeepDice;
	private JPanel pnlPowerDice;
	private JPanel pnlMainRoll;
	private JPanel pnlPowerRoll;
	private JPanel[] pnlKeepDie = new JPanel[5];
	private JPanel[] pnlRollDie = new JPanel[5];
	private JPanel[] pnlPowerDie = new JPanel[5];
	private JPanel pnlRollPower;
	private JPanel pnlMainArea;
	private JPanel pnlMainRollBtn;
	private JPanel pnlRollPowerBtn;
	private JPanel pnlRoll;
	private JPanel newsLblPnl;
	private JPanel pnlCentre;
	private JPanel[] pnlPowerDiceInd = new JPanel[3];  // Sub panels for the skulls, the +/-, and the double / triple dice
	private JPanel[] pnlPowerIcons = new JPanel[7];
	private JPanel[] pnlKeepIcons = new JPanel[5];
	private JPanel[] pnlPowerRollIcons = new JPanel[3];
	private JLabel[] lblPlayerNames = new JLabel[4];
	private JLabel[] lblPlayerPosns = new JLabel[4];
	private JLabel[] lblPlayerScores = new JLabel[4];
	private JLabel[] lblKeptDice = new JLabel[5];
	private JLabel[] lblCurDice = new JLabel[5];
	private JLabel[] lblCurPowerDice = new JLabel[3];
	private static JLabel[] lblPowerDice = new JLabel[7];    // Holds all pictures of power dice for right side (last 3 are from last index)
	private JLabel newsLabel;
	private JButton[] btnReturnDie = new JButton[5];
	private JButton[] btnKeepDie = new JButton[5];
	private JButton[] btnPickPowerDie = new JButton[5];
	private JButton btnRollPowerDice;
	private JButton btnRollDice;
	private JButton btnBank;
	private JButton btnEndTurn;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem newGameItem;
	private JMenuItem saveGameItem;
	private JMenuItem quitItem;
	private JMenuItem helpItem;
	private JMenuItem hallOfFameItem;
	private JMenuItem aboutItem;
	private static final String VERSION_NUMBER = "0.3.1";
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private static final String[] powerDice = {"Skull Dice", "Chance Die", "All or Nothing Die", "+/- Dice", "Tripler Dice"};
	private Dice[] keptDice = new Dice[5];
	private Dice[] regularDice = new Dice[5];
	private Dice[] skullDice = new Dice[2];
	private Dice[] plusMinusDice = new Dice[2];
	private Dice[] tripleDice = new Dice[3];
	private Dice yellowDie;
	private Dice powerDie;
	private Dice greenDie; 
	private List<String> playerNames;
	private List<Integer> playerScores = new List<Integer>();
	private int curPlayerTurn;
	private int curTurnScore = 0;
	private boolean gameWon = false;
	private boolean turnAlive = true;
	private boolean newRoll = false;
	
	public MainWindow(List<String> players){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		
		// ----- Menu Bar ----- //
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(this);
		saveGameItem = new JMenuItem("Save Current Game");
		saveGameItem.addActionListener(this);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		fileMenu.add(newGameItem);
		fileMenu.add(saveGameItem);
		fileMenu.add(quitItem);
		menuBar.add(fileMenu);
		helpMenu = new JMenu("Help");
		helpItem = new JMenuItem("Power Dice Help");
		helpItem.addActionListener(this);
		hallOfFameItem = new JMenuItem("View Hall of Fame");
		hallOfFameItem.addActionListener(this);
		aboutItem = new JMenuItem("About Power Dice");
		aboutItem.addActionListener(this);
		helpMenu.add(helpItem);
		helpMenu.add(hallOfFameItem);
		helpMenu.add(aboutItem);
		menuBar.add(helpMenu);
		
		// ----- Dice Initialization ----- //
		
		for (int i = 0; i < 5; i++){
			pnlKeepIcons[i] = new JPanel();
			regularDice[i] = new Dice(6);
		}
		for (int i = 0; i < 2; i++){
			skullDice[i] = new Dice(6, "skull");
		}
		for (int i = 0; i < 3; i++){
			tripleDice[i] = new Dice(6, "tripler");
		}
		plusMinusDice[0] = new Dice(2, "plus_minus");
		plusMinusDice[1] = new Dice(12, "plus");
		yellowDie = new Dice(6, "yellow");
		powerDie = new Dice(6, "power_dice");
		greenDie = new Dice(20, "all"); 
		this.playerNames = players;
		
		// ----- UI Elements ----- //
		
		for (int i = 0; i < 7; i++){
			pnlPowerIcons[i] = new JPanel();
			lblPowerDice[i] = new JLabel();
			lblPowerDice[i].setIcon(new ImageIcon("Resources/power_" + i + ".png"));
			pnlPowerIcons[i].add(lblPowerDice[i], JLabel.CENTER);
		}
		
		// Score Area
		pnlScoreArea = new JPanel();
		pnlScoreArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlScoreArea.setLayout(new GridLayout(1,4,50 - 5 * playerNames.getSize(),0));
		for (int i = 0; i < 4; i++){
			pnlPlayers[i] = new JPanel();
			pnlPlayers[i].setLayout(new GridLayout(2,1,0,0));
			pnlPlrNames[i] = new JPanel();
			pnlPlrNames[i].setLayout(new BorderLayout());
			if (i < this.playerNames.getSize()){
				playerScores.Push(0);
				lblPlayerScores[i] = new JLabel("<html><font size=4>Score: 0</font></html>");
				lblPlayerNames[i] = new JLabel("<html><font size=6>" + this.playerNames.GetValueAt(i) + "</font></html>");
				lblPlayerPosns[i] = new JLabel("<html><font size=6>1st</font></html>");
			}else{
				lblPlayerScores[i] = new JLabel();
				lblPlayerNames[i] = new JLabel();
				lblPlayerPosns[i] = new JLabel();
			}
			pnlPlrNames[i].add(lblPlayerPosns[i], BorderLayout.EAST);
			pnlPlrNames[i].add(lblPlayerNames[i], BorderLayout.WEST);
			pnlPlayers[i].add(pnlPlrNames[i]);
			pnlPlayers[i].add(lblPlayerScores[i]);
			pnlScoreArea.add(pnlPlayers[i]);
		}
		
		// Left Dice Panel
		TitledBorder leftTitle = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Dice to Keep");
		leftTitle.setTitleJustification(TitledBorder.CENTER);
		pnlKeepDice = new JPanel();
		pnlKeepDice.setLayout(new GridLayout(5,1,0,25));
		pnlKeepDice.setBorder(leftTitle);
		for (int i = 0; i < 5; i++){
			pnlKeepDie[i] = new JPanel();
			pnlKeepDie[i].setLayout(new BorderLayout());
			lblKeptDice[i] = new JLabel();
			pnlKeepIcons[i].add(lblKeptDice[i]);
			btnReturnDie[i] = new JButton("Return Die");
			btnReturnDie[i].setEnabled(false);
			btnReturnDie[i].addActionListener(this);
			pnlKeepDie[i].add(pnlKeepIcons[i]);
			pnlKeepDie[i].add(btnReturnDie[i], BorderLayout.SOUTH);
			pnlKeepDice.add(pnlKeepDie[i]);
		}
		
		// Right Dice Panel
		TitledBorder rightTitle = new TitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Power Dice");
		rightTitle.setTitleJustification(TitledBorder.CENTER);
		pnlPowerDice = new JPanel();
		pnlPowerDice.setLayout(new GridLayout(5,1,0,25));
		pnlPowerDice.setBorder(rightTitle);
		for (int i = 0; i < 5; i++){
			pnlPowerDie[i] = new JPanel();
			pnlPowerDie[i].setLayout(new BorderLayout());
		}
		pnlPowerDiceInd[0] = new JPanel();
		pnlPowerDiceInd[0].setLayout(new GridLayout(1,2,5,0));
		pnlPowerDiceInd[0].add(pnlPowerIcons[0]);
		pnlPowerDiceInd[0].add(pnlPowerIcons[1]);
		pnlPowerDie[0].add(pnlPowerDiceInd[0]);
		pnlPowerDie[1].add(pnlPowerIcons[2]);
		pnlPowerDie[2].add(pnlPowerIcons[3]);
		pnlPowerDiceInd[1] = new JPanel();
		pnlPowerDiceInd[1].setLayout(new GridLayout(1,2,0,5));
		pnlPowerDiceInd[1].add(pnlPowerIcons[4]);
		pnlPowerDiceInd[1].add(pnlPowerIcons[5]);
		pnlPowerDie[3].add(pnlPowerDiceInd[1]);
		pnlPowerDie[4].add(pnlPowerIcons[6]);
		for (int i = 0; i < 5; i++){
			btnPickPowerDie[i] = new JButton("Roll " + powerDice[i]);
			btnPickPowerDie[i].setEnabled(false);
			btnPickPowerDie[i].addActionListener(this);
			pnlPowerDie[i].add(btnPickPowerDie[i], BorderLayout.SOUTH);
			pnlPowerDice.add(pnlPowerDie[i]);
		}
		
		// Power Dice Roll Panel
		pnlPowerRoll = new JPanel();
		pnlPowerRoll.setLayout(new BorderLayout());
		pnlRollPower = new JPanel();
		pnlRollPower.setLayout(new GridLayout(1,5,5,0));
		lblCurPowerDice[0] = new JLabel();
		lblCurPowerDice[1] = new JLabel();
		lblCurPowerDice[2] = new JLabel();
		pnlRollPower.add(new JLabel());
		for (int i = 0; i < 3; i++){
			pnlPowerRollIcons[i] = new JPanel();
			pnlPowerRollIcons[i].add(lblCurPowerDice[i]);
			pnlRollPower.add(pnlPowerRollIcons[i]);
		}
		pnlRollPower.add(new JLabel());
		pnlRollPowerBtn = new JPanel();
		pnlRollPowerBtn.setLayout(new GridLayout(1,2,10,0));
		btnRollPowerDice = new JButton("Roll Power Dice");
		btnEndTurn = new JButton("End Turn");
		btnEndTurn.addActionListener(this);
		btnEndTurn.setEnabled(false);
		btnRollPowerDice.addActionListener(this);
		btnRollPowerDice.setEnabled(false);
		pnlRollPowerBtn.add(btnRollPowerDice);
		pnlRollPowerBtn.add(btnEndTurn);
		pnlPowerRoll.add(pnlRollPower);
		pnlPowerRoll.add(pnlRollPowerBtn, BorderLayout.SOUTH);
		
		// Main Roll Panel and News Label
		newsLblPnl = new JPanel();
		newsLabel = new JLabel();
		newsLblPnl.add(newsLabel, JLabel.CENTER);
		pnlMainArea = new JPanel();
		pnlMainArea.setLayout(new BorderLayout());
		pnlMainRoll = new JPanel();
		pnlMainRoll.setLayout(new GridLayout(1,5,5,0));
		for (int i = 0; i < 5; i ++){
			pnlRollDie[i] = new JPanel();
			pnlRollDie[i].setLayout(new BorderLayout());
			lblCurDice[i] = new JLabel();
			btnKeepDie[i] = new JButton("Keep");
			btnKeepDie[i].addActionListener(this);
			btnKeepDie[i].setEnabled(false);
			pnlRollDie[i].add(lblCurDice[i]);
			pnlRollDie[i].add(btnKeepDie[i], BorderLayout.SOUTH);
			pnlMainRoll.add(pnlRollDie[i]);
		}
		pnlMainRollBtn = new JPanel();
		pnlMainRollBtn.setLayout(new GridLayout(1,2,10,0));
		pnlMainRollBtn.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
		btnRollDice = new JButton("Roll");
		btnRollDice.addActionListener(this);
		btnBank = new JButton("Bank");
		btnBank.addActionListener(this);
		btnBank.setEnabled(false);
		pnlMainRollBtn.add(btnRollDice);
		pnlMainRollBtn.add(btnBank);
		pnlMainArea.add(pnlMainRoll);
		pnlMainArea.add(pnlMainRollBtn, BorderLayout.SOUTH);
		
		pnlRoll = new JPanel();
		pnlRoll.setLayout(new GridLayout(2,1,0,5));
		pnlRoll.add(pnlMainArea);
		pnlRoll.add(pnlPowerRoll);
		
		pnlCentre = new JPanel();
		pnlCentre.setLayout(new BorderLayout());
		pnlCentre.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		pnlCentre.add(newsLblPnl, BorderLayout.NORTH);
		pnlCentre.add(pnlRoll, BorderLayout.CENTER);
		
		pnlGameArea = new JPanel();
		pnlGameArea.setLayout(new BorderLayout());
		pnlGameArea.add(pnlKeepDice, BorderLayout.WEST);
		pnlGameArea.add(pnlPowerDice, BorderLayout.EAST);
		pnlGameArea.add(pnlCentre, BorderLayout.CENTER);
				
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlMain.add(pnlScoreArea, BorderLayout.NORTH);
		pnlMain.add(pnlGameArea);
		
		this.setJMenuBar(menuBar);
		this.add(pnlMain);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(winIcon.getImage());
		this.setSize(700,800);
		this.setTitle("Power Dice v" + VERSION_NUMBER);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		FirstRollWindow rollWin = new FirstRollWindow(this.playerNames);
		curPlayerTurn = rollWin.winner;
		newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: 0</font></html>");
	}
	
	// Event Handling
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 5; i++){
			if (e.getSource() == btnReturnDie[i]){
				int value = keptDice[i].getLastRoll();
				if (value == 1){
					curTurnScore -= 100;
				}else if (value == 5){
					curTurnScore -= 50;
				}
				newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + curTurnScore + "</font></html>");
				regularDice[i] = keptDice[i];
				keptDice[i] = null;
				lblKeptDice[i].setIcon(null);
				lblCurDice[i].setIcon(new ImageIcon(regularDice[i].GetResultImage(regularDice[i].getLastRoll() - 1)));
				btnReturnDie[i].setEnabled(false);
				btnKeepDie[i].setEnabled(true);
			}else if (e.getSource() == btnKeepDie[i]){
				int value = regularDice[i].getLastRoll();
				if (value == 1){
					curTurnScore += 100;
				}else if (value == 5){
					curTurnScore += 50;
				}
				newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + curTurnScore + "</font></html>");
				keptDice[i] = regularDice[i];
				regularDice[i] = null;
				lblCurDice[i].setIcon(null);
				lblKeptDice[i].setIcon(new ImageIcon(keptDice[i].GetResultImage(keptDice[i].getLastRoll() - 1)));
				btnKeepDie[i].setEnabled(false);
				btnReturnDie[i].setEnabled(true);
				CheckReRoll();
			}else if (e.getSource() == btnPickPowerDie[i]){
				lblCurPowerDice[0].setIcon(null);
				lblCurPowerDice[1].setIcon(null);
				lblCurPowerDice[2].setIcon(null);
				if (i == 0){
					curTurnScore += RollSkull();
				}else if (i == 1){ // TODO Add code to handle other dice rolls
					curTurnScore += RollYellow();
				}else if (i == 2){
					curTurnScore += RollAll();
				}else if (i == 3){
					curTurnScore += RollPlusMinus();
				}else if (i == 4){
					curTurnScore *= RollTripler();
				}
				newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + curTurnScore + "</font></html>");
				btnEndTurn.setEnabled(true);
			}
		}
		
		if (e.getSource() == btnRollDice){
			btnBank.setEnabled(true);
			int numValid = 0;
			List<Integer> freqs = new List<Integer>(new Integer[]{0,0,0,0,0,0});
			for (int i = 0; i < 5; i++){
				if (regularDice[i] != null){
					freqs.SetValueAt(regularDice[i].getLastRoll() - 1, freqs.GetValueAt(regularDice[i].getLastRoll() - 1) + 1);
					int roll = regularDice[i].Roll();
					lblCurDice[i].setIcon(new ImageIcon(regularDice[i].sidePics[roll - 1]));
					if (roll == 1 || roll == 5){
						btnKeepDie[i].setEnabled(true);
						numValid++;
					}
				}else if (newRoll){
					regularDice[i] = keptDice[i];
					keptDice[i] = null;
					lblKeptDice[i].setIcon(null);
					regularDice[i].Roll();
					lblCurDice[i].setIcon(new ImageIcon(regularDice[i].GetResultImage(regularDice[i].getLastRoll() - 1)));
					btnReturnDie[i].setEnabled(false);
					if (regularDice[i].getLastRoll() == 1 || regularDice[i].getLastRoll() == 5){
						btnKeepDie[i].setEnabled(true);
						numValid++;
					}
				}
			}
			newRoll = false;
			if (numValid == 0){
				// TODO Change to Next turn (share function with btnEndTurn)
			}
			// Checking for other point sources
			if (freqs.GetValueAt(0) == 1 && freqs.GetValueAt(1) == 1 && freqs.GetValueAt(2) == 1 && freqs.GetValueAt(3) == 1 && freqs.GetValueAt(4) == 1 || freqs.GetValueAt(1) == 1 && freqs.GetValueAt(2) == 1 && freqs.GetValueAt(3) == 1 && freqs.GetValueAt(4) == 1 && freqs.GetValueAt(5) == 1){
				// TODO Code for a straight
			}else if (freqs.FindMaxValue() >= 5){
				// TODO Code for trips / quads / quints
			}
		}else if (e.getSource() == btnBank){
			lblCurPowerDice[1].setIcon(new ImageIcon("Resources/power_die.png"));
			btnRollPowerDice.setEnabled(true);
			btnBank.setEnabled(false);
			btnRollDice.setEnabled(false);
		}else if (e.getSource() == btnRollPowerDice){
			btnRollPowerDice.setEnabled(false);
			int result = powerDie.Roll();
			lblCurPowerDice[1].setIcon(new ImageIcon(powerDie.GetResultImage(result - 1)));
			if (result == 1){
				btnPickPowerDie[0].setEnabled(true);
			}else if (result == 2){
				btnPickPowerDie[1].setEnabled(true);
			}else if (result == 3){
				btnPickPowerDie[2].setEnabled(true);
			}else if (result == 4){
				btnPickPowerDie[3].setEnabled(true);
			}else if (result == 5){
				btnPickPowerDie[4].setEnabled(true);
			}else if (result == 6){
				for (int i = 0; i < 5; i++){
					btnPickPowerDie[i].setEnabled(true);
				}
			}
			btnPickPowerDie[4].setEnabled(true);
		}else if (e.getSource() == btnEndTurn){
			// TODO CHANGE TURN
		}else if (e.getSource() == quitItem){
			this.dispose();
		}else if (e.getSource() == aboutItem){
			new AboutWindow(VERSION_NUMBER);
		}else if (e.getSource() == newGameItem){
			new EnterPlayersWindow();
			this.dispose();
		}
	}
	
	private void CheckReRoll(){
		int numLeft = 0;
		for (int i = 0; i < 5; i++){
			if (regularDice[i] != null){
				numLeft++;
			}
		}
		if (numLeft == 0){  // If all 5 are kept, prepares for reset next roll
			btnRollDice.setText("Roll Again");
			newRoll = true;
		}else{
			btnRollDice.setText("Roll");
		}
	}
	
	// Returns change in pts or event code (-1,-2,-3)
	private int RollYellow(){
		int result = yellowDie.Roll();
		lblCurPowerDice[1].setIcon(new ImageIcon(yellowDie.GetResultImage(result - 1)));
		if (result == 1){
			return 500;
		}else if (result == 2){
			return 2000;
		}else if (result == 3){
			return -1000;
		}else if (result == 4){ // Lost turn
			return -1; // TODO Patch up this functionality for notification
		}else if (result == 5){ // Trade Pts
			return -2;
		}else{ // Roll again
			return -3;
		}
	}
	
	// Returns the change in points
	private int RollSkull(){
		int res1 = skullDice[0].Roll();
		int res2 = skullDice[1].Roll();
		lblCurPowerDice[0].setIcon(new ImageIcon(skullDice[0].GetResultImage(res1 - 1)));
		lblCurPowerDice[2].setIcon(new ImageIcon(skullDice[0].GetResultImage(res2 - 1)));
		if (res1 == 1 && res2 == 1){
			return -1000;
		}else if (res1 == 1){
			return -100 * res2;
		}else if (res2 == 1){
			return -100 * res1;
		}else{
			return 200 * (res1 + res2);
		}
	}
	
	// Returns 1 if they win, 0 if they lose pts, and -1 for nothing
	private int RollAll(){
		int result = greenDie.Roll();
		lblCurPowerDice[1].setIcon(new ImageIcon(greenDie.GetResultImage(result - 1)));
		if (result == 1){  // Lost all points
			return 0;
		}else if (result == 20){  // Won game
			return 1;
		}else{  // Nothing happens
			return -1;
		}
	}
	
	// Returns the factor by which the score is multiplied
	private int RollTripler(){
		int res1 = tripleDice[0].Roll();
		int res2 = tripleDice[1].Roll();
		int res3 = tripleDice[2].Roll();
		lblCurPowerDice[0].setIcon(new ImageIcon(tripleDice[0].GetResultImage(res1 - 1)));
		lblCurPowerDice[1].setIcon(new ImageIcon(tripleDice[1].GetResultImage(res2 - 1)));
		lblCurPowerDice[2].setIcon(new ImageIcon(tripleDice[2].GetResultImage(res3 - 1)));
		List<Integer> freqs = new List<Integer>(new Integer[]{0,0,0,0,0,0});
		freqs.SetValueAt(res1 - 1, freqs.GetValueAt(res1 - 1) + 1);
		freqs.SetValueAt(res2 - 1, freqs.GetValueAt(res2 - 1) + 1);
		freqs.SetValueAt(res3 - 1, freqs.GetValueAt(res3 - 1) + 1);
		return freqs.FindMaxValue();
	}
	
	// Returns the change in points
	private int RollPlusMinus(){
		int res1 = plusMinusDice[0].Roll();
		int res2 = plusMinusDice[1].Roll();
		lblCurPowerDice[0].setIcon(new ImageIcon(plusMinusDice[0].GetResultImage(res1 - 1)));
		lblCurPowerDice[2].setIcon(new ImageIcon(plusMinusDice[1].GetResultImage(res2 - 1)));
		if (res1 == 0){
			return -100 * res2;
		}else{
			return 100 * res2;
		}
	}
}
