package com.r4studios.powerdice;
// TODO After bank, need to disable keep / return die labels
// TODO Mouse click listener chagne to other listener
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import com.r4studios.DataStructures.List;
import com.r4studios.powerdice.Dice;

public class MainWindow extends JFrame implements ActionListener, MouseListener{

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
	private JPanel pnlPowerMessage;
	private JPanel pnlTurnMessage;
	private JPanel[] pnlPowerDiceInd = new JPanel[3];  // Sub panels for the skulls, the +/-, and the double / triple dice
	private JPanel[] pnlPowerIcons = new JPanel[7];
	private JPanel[] pnlKeepIcons = new JPanel[5];
	private JPanel[] pnlPowerRollIcons = new JPanel[3];
	private JLabel[] lblPlayerNames = new JLabel[4];
	private JLabel[] lblPlayerScores = new JLabel[4];
	private JLabel[] lblKeptDice = new JLabel[5];
	private JLabel[] lblCurDice = new JLabel[5];
	private JLabel[] lblCurPowerDice = new JLabel[3];
	private JLabel lblPowerMessage;
	private JLabel lblTurnMessage;
	private static JLabel[] lblPowerDice = new JLabel[7];    // Holds all pictures of power dice for right side (last 3 are from last index)
	private JLabel newsLabel;
	private JButton btnRollDice;
	private JButton btnBank;
	private JButton btnEndTurn;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem newGameItem;
	private JMenuItem quitItem;
	private JMenuItem helpItem;
	private JMenuItem hallOfFameItem;
	private JMenuItem aboutItem;
	private static int WINNING_SCORE;
	private static final String VERSION_NUMBER = "1.0";
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private Dice[] keptDice = new Dice[5];
	private Dice[] tempDice = new Dice[5];
	private Dice[] regularDice = new Dice[5];
	private Dice[] skullDice = new Dice[2];
	private Dice[] plusMinusDice = new Dice[2];
	private Dice[] tripleDice = new Dice[3];
	private Dice yellowDie;
	private Dice powerDie;
	private Dice greenDie; 
	private List<String> playerNames;
	private List<Integer> playerScores = new List<Integer>();
	private List<Integer> sortedPlayerScores = new List<Integer>();
	private int curPlayerTurn;
	private int curTurnScore = 0;
	private int tempScore = 0;
	private int rollOffScore = 0;
	private int challengingPlayer;
	private int gamesNeededToWin;
	private int curGame;
	private List<Integer> plrWins = new List<Integer>();
	private boolean[] canKeep = {false, false, false, false, false};
	private boolean[] canReturn = {false, false, false, false, false};
	private boolean[] canRollPower = {false, false, false, false, false, false, false};
	private boolean canRollPowerDie = false;
	private boolean newRoll = false;
	private boolean rollAgain = false;
	private boolean lastRound = false;
	private boolean canRollOff = false;
	private boolean firstRollOff = false;
	private boolean newTurn = false;
	private HighScoreTable highScores;
	
	public MainWindow(List<String> players, int gamesToWin, int gameNum, List<Integer> wins, int ptsToWin){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		
		// ----- Menu Bar ----- //
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(this);
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);
		fileMenu.add(newGameItem);
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
		this.gamesNeededToWin = gamesToWin;
		this.curGame = gameNum;
		this.plrWins = wins;
		MainWindow.WINNING_SCORE = ptsToWin;
		this.highScores = new HighScoreTable();
		try{  // Loads high scores
			FileInputStream fileIn = new FileInputStream("Resources/high_scores.scr");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			highScores = (HighScoreTable) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException | ClassNotFoundException e) {e.printStackTrace();}
		
		// ----- UI Elements ----- //
		
		for (int i = 0; i < 7; i++){
			pnlPowerIcons[i] = new JPanel();
			lblPowerDice[i] = new JLabel();
			lblPowerDice[i].setIcon(new ImageIcon("Resources/power_" + i + ".png"));
			lblPowerDice[i].addMouseListener(this);
			pnlPowerIcons[i].add(lblPowerDice[i], JLabel.CENTER);
		}
		
		// Score Area
		pnlScoreArea = new JPanel();
		pnlScoreArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlScoreArea.setLayout(new GridLayout(1,4,100,0));
		for (int i = 0; i < 4; i++){
			pnlPlayers[i] = new JPanel();
			pnlPlayers[i].setLayout(new GridLayout(2,1,0,0));
			pnlPlrNames[i] = new JPanel();
			pnlPlrNames[i].setLayout(new BorderLayout());
			if (i < this.playerNames.getSize()){
				playerScores.Push(0);
				if (gamesNeededToWin > 1){
					lblPlayerScores[i] = new JLabel("<html><font size=4>Score: 0<br>" + plrWins.GetValueAt(i) + " Win(s)</font></html>");
				}else{
					lblPlayerScores[i] = new JLabel("<html><font size=4>Score: 0</font></html>");
				}
				lblPlayerNames[i] = new JLabel("<html><font size=6>" + this.playerNames.GetValueAt(i) + "</font></html>");
			}else{
				lblPlayerScores[i] = new JLabel();
				lblPlayerNames[i] = new JLabel();
			}
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
			lblKeptDice[i] = new JLabel();
			lblKeptDice[i].setIcon(new ImageIcon("Resources/blank.png"));
			lblKeptDice[i].addMouseListener(this);
			pnlKeepDie[i].add(lblKeptDice[i]);
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
			pnlPowerDice.add(pnlPowerDie[i]);
		}
		
		// Power Dice Roll Panel
		pnlPowerRoll = new JPanel();
		pnlPowerRoll.setLayout(new BorderLayout());
		pnlPowerRoll.setBorder(BorderFactory.createEmptyBorder(50,0,0,0));
		pnlRollPower = new JPanel();
		pnlRollPower.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		pnlRollPower.setLayout(new GridLayout(1,5,5,0));
		lblCurPowerDice[0] = new JLabel();
		lblCurPowerDice[1] = new JLabel();
		lblCurPowerDice[2] = new JLabel();
		lblCurPowerDice[0].setIcon(new ImageIcon("Resources/blank.png"));
		lblCurPowerDice[1].setIcon(new ImageIcon("Resources/blank.png"));
		lblCurPowerDice[1].addMouseListener(this);
		lblCurPowerDice[2].setIcon(new ImageIcon("Resources/blank.png"));
		pnlRollPower.add(new JLabel());
		for (int i = 0; i < 3; i++){
			pnlPowerRollIcons[i] = new JPanel();
			pnlPowerRollIcons[i].add(lblCurPowerDice[i]);
			pnlRollPower.add(pnlPowerRollIcons[i]);
		}
		pnlRollPower.add(new JLabel());
		pnlRollPowerBtn = new JPanel();
		pnlRollPowerBtn.setBorder(BorderFactory.createEmptyBorder(40, 75, 0, 75));
		pnlRollPowerBtn.setLayout(new GridLayout(1,1,0,0));
		btnEndTurn = new JButton("End Turn");
		btnEndTurn.addActionListener(this);
		btnEndTurn.setEnabled(false);
		pnlRollPowerBtn.add(btnEndTurn);
		
		pnlPowerMessage = new JPanel();
		pnlPowerMessage.setMinimumSize(new Dimension(300,35));
		pnlPowerMessage.setMaximumSize(new Dimension(300,35));
		pnlPowerMessage.setPreferredSize(new Dimension(300,35));
		lblPowerMessage = new JLabel("<html><font size=4>     </font></html>");
		pnlPowerMessage.add(lblPowerMessage, JLabel.CENTER);
		pnlPowerRoll.add(pnlPowerMessage, BorderLayout.NORTH);
		pnlPowerRoll.add(pnlRollPower, BorderLayout.CENTER);
		pnlPowerRoll.add(pnlRollPowerBtn, BorderLayout.SOUTH);
		
		// Main Roll Panel and News Label
		newsLblPnl = new JPanel();
		newsLabel = new JLabel();
		newsLblPnl.add(newsLabel, JLabel.CENTER);
		pnlMainArea = new JPanel();
		pnlMainArea.setLayout(new BorderLayout());
		pnlMainRoll = new JPanel();
		pnlMainRoll.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
		pnlMainRoll.setLayout(new GridLayout(1,5,5,0));
		for (int i = 0; i < 5; i ++){
			pnlRollDie[i] = new JPanel();
			lblCurDice[i] = new JLabel();
			lblCurDice[i].setIcon(new ImageIcon("Resources/blank.png"));
			lblCurDice[i].addMouseListener(this);
			pnlRollDie[i].add(lblCurDice[i]);
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
		
		pnlTurnMessage = new JPanel();
		pnlTurnMessage.setMinimumSize(new Dimension(300,35));
		pnlTurnMessage.setPreferredSize(new Dimension(300,35));
		pnlTurnMessage.setMaximumSize(new Dimension(300,35));
		lblTurnMessage = new JLabel("<html><font size=4>     </font></html>");
		pnlTurnMessage.add(lblTurnMessage, JLabel.CENTER);
		pnlMainArea.add(pnlTurnMessage, BorderLayout.NORTH);
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
		this.setSize(710,730);
		this.setTitle("Power Dice v" + VERSION_NUMBER);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		FirstRollWindow rollWin = new FirstRollWindow(this.playerNames);
		curPlayerTurn = rollWin.winner;
		newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: 0</font></html>");
	}
	
	// Event Handling
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnRollDice){
			if (newRoll || rollAgain || newTurn){
				for (int i = 0; i < 5; i++){
					if (i < 3){
						lblCurPowerDice[i].setIcon(new ImageIcon("Resources/blank.png"));
					}
					regularDice[i] = new Dice(6);
					canKeep[i] = false;
					keptDice[i] = null;
					tempDice[i] = null;
					lblKeptDice[i].setIcon(new ImageIcon("Resources/blank.png"));
					lblCurDice[i].setIcon(new ImageIcon("Resources/blank.png"));
				}
			}
			newTurn = false;
			btnBank.setText("Bank");
			btnBank.setEnabled(false);
			btnRollDice.setEnabled(false);
			curTurnScore += tempScore;
			tempScore = 0;
			for (int i = 0; i < 5; i++){  // Loop to roll remaining dice
				tempDice[i] = null;
				if (keptDice[i] != null){
					canReturn[i] = false;
				}
				if (regularDice[i] != null){
					int roll = regularDice[i].Roll();
					lblCurDice[i].setIcon(new ImageIcon(regularDice[i].sidePics[roll - 1]));
				}else if (newRoll){
					regularDice[i] = keptDice[i];
					keptDice[i] = null;
					lblKeptDice[i].setIcon(null);
					regularDice[i].Roll();
					lblCurDice[i].setIcon(new ImageIcon(regularDice[i].GetResultImage(regularDice[i].getLastRoll() - 1)));
					canReturn[i] = false;
				}
			}
			CheckForPoints();  // Enables buttons which can be used
			newRoll = false;
			rollAgain = false;
			firstRollOff = false;
		}else if (e.getSource() == btnBank){
			if (canRollOff && firstRollOff){
				curTurnScore = rollOffScore;
				newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				btnRollDice.setEnabled(false);
				for (int i = 0; i < 5; i++){  // Loop to roll remaining dice
					tempDice[i] = null;
					if (keptDice[i] != null){
						canReturn[i] = false;
					}
					if (regularDice[i] != null){
						int roll = regularDice[i].Roll();
						lblCurDice[i].setIcon(new ImageIcon(regularDice[i].sidePics[roll - 1]));
					}else if (newRoll){
						regularDice[i] = keptDice[i];
						keptDice[i] = null;
						lblKeptDice[i].setIcon(null);
						regularDice[i].Roll();
						lblCurDice[i].setIcon(new ImageIcon(regularDice[i].GetResultImage(regularDice[i].getLastRoll() - 1)));
						canReturn[i] = false;
					}
				}
				CheckForPoints();  // Enables buttons which can be used
				newRoll = false;			
				btnBank.setText("Bank");
				btnBank.setEnabled(false);
				firstRollOff = false;
				newTurn = false;
			}else{
				curTurnScore += tempScore;
				tempScore = 0;
				for (int i = 0; i < 5; i++){
					canReturn[i] = false;
					canKeep[i] = false;
				}
				lblCurPowerDice[1].setIcon(new ImageIcon("Resources/power_die.png"));
				canRollPowerDie = true;
				btnBank.setEnabled(false);
				btnRollDice.setEnabled(false);
				canRollOff = false;
			}
		}else if (e.getSource() == btnEndTurn){
			lblTurnMessage.setText("<html><div style=\"text-align: center;\"><font size=4>     </font></html>");
			EndPlayerTurn();
		}else if (e.getSource() == quitItem){
			this.dispose();
		}else if (e.getSource() == aboutItem){
			new AboutWindow(VERSION_NUMBER);
		}else if (e.getSource() == newGameItem){
			new EnterPlayersWindow(null);
			this.dispose();
		}else if (e.getSource() == helpItem){
			int n = JOptionPane.showConfirmDialog(this, "This will open the Power Dice ReadMe file in your default browser.  Do you wish to proceed?", "Opening in Default Browser", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION){
				try {
					OpenWebsite(new URI("https://github.com/jonhayes37/Power-Dice/blob/master/README.md"));
				}catch (URISyntaxException e1) {e1.printStackTrace();}
			}
		}else if (e.getSource() == hallOfFameItem){
			new HallOfFameWindow(highScores);
		}
	}
	
	// Mouse events for keeping and returning dice
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < 5; i++){
			if (e.getSource() == lblCurDice[i] && canKeep[i]){
				btnRollDice.setEnabled(true);
				keptDice[i] = regularDice[i];
				tempDice[i] = keptDice[i];
				regularDice[i] = null;
				lblCurDice[i].setIcon(new ImageIcon("Resources/blank.png"));
				lblKeptDice[i].setIcon(new ImageIcon(keptDice[i].GetResultImage(keptDice[i].getLastRoll() - 1)));
				canKeep[i] = false;
				canReturn[i] = true;
				tempScore = CalcTempPoints();
				if (tempScore > 0){
					btnBank.setEnabled(true);
				}
				if (regularDice[0] == null && regularDice[1] == null && regularDice[2] == null && regularDice[3] == null && regularDice[4] == null){
					btnBank.setEnabled(false);
					canRollOff = false;
				}
				lblTurnMessage.setText(TurnMessage(tempScore + curTurnScore));
				newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				CheckReRoll();
				break;
			}else if (e.getSource() == lblKeptDice[i] && canReturn[i]){
				regularDice[i] = keptDice[i];
				keptDice[i] = null;
				tempDice[i] = null;
				lblKeptDice[i].setIcon(new ImageIcon("Resources/blank.png"));
				lblCurDice[i].setIcon(new ImageIcon(regularDice[i].GetResultImage(regularDice[i].getLastRoll() - 1)));
				canReturn[i] = false;
				canKeep[i] = true;
				tempScore = CalcTempPoints();
				lblTurnMessage.setText(TurnMessage(tempScore + curTurnScore));
				newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				if (!canReturn[0] && !canReturn[1] && !canReturn[2] && !canReturn[3] && !canReturn[4]){
					btnRollDice.setEnabled(false);
					btnBank.setEnabled(false);
				}
				break;
			}
		}
		for (int i = 0; i < 7; i++){
			if (e.getSource() == lblPowerDice[i] && canRollPower[i]){
				for (int j = 0; j < 7; j++){
					canRollPower[j] = false;
				}
				lblCurPowerDice[0].setIcon(new ImageIcon("Resources/blank.png"));
				lblCurPowerDice[1].setIcon(new ImageIcon("Resources/blank.png"));
				lblCurPowerDice[2].setIcon(new ImageIcon("Resources/blank.png"));
				if (i == 0 || i == 1){
					curTurnScore += RollSkull();
				}else if (i == 2){ 
					RollYellow();
				}else if (i == 3){
					RollAll();
				}else if (i == 4 || i == 5){
					curTurnScore += RollPlusMinus();
				}else if (i == 6){
					curTurnScore *= RollTripler();
				}
				lblTurnMessage.setText(TurnMessage(curTurnScore));
				newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				btnEndTurn.setEnabled(true);
				break;
			}
		}
		if (e.getSource() == lblCurPowerDice[1] && canRollPowerDie){
			canRollPowerDie = false;
			int result = powerDie.Roll();
			lblCurPowerDice[1].setIcon(new ImageIcon(powerDie.GetResultImage(result - 1)));
			if (result == 1){
				canRollPower[0] = true;
				canRollPower[1] = true;
			}else if (result == 2){
				canRollPower[2] = true;
			}else if (result == 3){
				canRollPower[3] = true;
			}else if (result == 4){
				canRollPower[4] = true;
				canRollPower[5] = true;
			}else if (result == 5){
				canRollPower[6] = true;
			}else{
				for (int j = 0; j < 7; j++){
					canRollPower[j] = true;
				}
			}
		}
	}
	
	private void EndPlayerTurn(){
		playerScores.SetValueAt(curPlayerTurn, playerScores.GetValueAt(curPlayerTurn) + curTurnScore);
		if (gamesNeededToWin > 1){
			lblPlayerScores[curPlayerTurn].setText("<html><font size=4>Score: " + playerScores.GetValueAt(curPlayerTurn) + "<br>" + plrWins.GetValueAt(curPlayerTurn) + " Win(s)</font></html>");
		}else{
			lblPlayerScores[curPlayerTurn].setText("<html><font size=4>Score: " + playerScores.GetValueAt(curPlayerTurn) + "</font></html>");
		}
		sortedPlayerScores = playerScores.QuickSort().Reverse();
		if (lastRound && (curPlayerTurn + 1) % playerNames.getSize() == challengingPlayer && !rollAgain){  // If the last player's last round is finished
			WonGame(playerNames.GetValueAt(playerScores.GetIndexOf(sortedPlayerScores.GetValueAt(0))), sortedPlayerScores.GetValueAt(0));
		}else if (!lastRound){
			if (playerScores.GetValueAt(curPlayerTurn) >= WINNING_SCORE){
				challengingPlayer = curPlayerTurn;
				lastRound = true;
				JOptionPane.showMessageDialog(this, playerNames.GetValueAt(curPlayerTurn) + " has reached " + WINNING_SCORE + " points! The final round has begun!", "Final Round Starting!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		rollOffScore = curTurnScore;
		curTurnScore = 0;
		tempScore = 0;
		if (rollAgain == false){  // Changes players if no roll again from yellow
			newTurn = true;
			if (rollOffScore > 0){
				btnBank.setText("Roll off of " + playerNames.GetValueAt(curPlayerTurn) + " (" + rollOffScore + " points)");
				canRollOff = true;
				firstRollOff = true;
				btnBank.setEnabled(true);
			}else{
				canRollOff = false;
				btnBank.setEnabled(false);
			}
			curPlayerTurn = (curPlayerTurn + 1) % playerNames.getSize();
		}
		lblPowerMessage.setText("<html><font size=4>     </font></html>");
		newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: 0</font></html>");
		for (int i = 0; i < 3; i++){
			lblCurPowerDice[i].setIcon(new ImageIcon("Resources/blank.png"));
		}
		btnRollDice.setEnabled(true);
		btnEndTurn.setEnabled(false);
	}
	
	private void WonGame(String name, int score){
		Score newScore = new Score(name, score);
		String extraText = "";
		if (highScores.AddScore(newScore)){
			extraText = ", and earned a spot in the Hall of Fame";
			FileOutputStream fileOut;  // Saving the new high scores
			try {
				fileOut = new FileOutputStream("Resources/high_scores.scr");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(highScores);
				out.close();
				fileOut.close();
			} catch (IOException e) {e.printStackTrace();}
			
		}

		plrWins.SetValueAt(playerNames.GetIndexOf(name), plrWins.GetValueAt(playerNames.GetIndexOf(name)) + 1);  // Adds the win
		if (plrWins.GetValueAt(playerNames.GetIndexOf(name)) == gamesNeededToWin){ // If the player won the series
			Object[] options = {"Yes, with the same players", "Yes, but with different players", "No"};
			int n;
			if (gamesNeededToWin > 1){
				if (score == -1){
					n = JOptionPane.showOptionDialog(this, name + " won the Series by rolling a 20 on the All or Nothing die" + extraText + "! Would you like to play again?", name + " won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				}else{
					n = JOptionPane.showOptionDialog(this, name + " won the Series with a score of " + score + extraText + "! Would you like to play again?", name + " won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				}
			}else{
				if (score == -1){
					n = JOptionPane.showOptionDialog(this, name + " won the game by rolling a 20 on the All or Nothing die" + extraText + "! Would you like to play again?", name + " won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				}else{
					n = JOptionPane.showOptionDialog(this, name + " won the game with a score of " + score + extraText + "! Would you like to play again?", name + " won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				}
			}
			if (n == JOptionPane.YES_OPTION){
				this.dispose();
				new EnterPlayersWindow(playerNames);
			}else if (n == JOptionPane.NO_OPTION){
				this.dispose();
				new EnterPlayersWindow(null);
			}else{
				this.dispose();
			}
		}else{
			if (score == -1){
				JOptionPane.showMessageDialog(this, name + " won Game " + curGame + " by rolling a 20 on the All or Nothing die" + extraText + "! Are you ready to start Game " + (curGame + 1) + "?", name + " won!", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(this, name + " won Game " + curGame + " with a score of " + score + extraText + "! Are you ready to start Game " + (curGame + 1) + "?", name + " won!", JOptionPane.INFORMATION_MESSAGE);
			}
			this.dispose();
			new MainWindow(playerNames, gamesNeededToWin, curGame + 1, plrWins, WINNING_SCORE);
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
	
	private int CalcTempPoints(){
		int points = 0;
		List<Integer> freqs = new List<Integer>(new Integer[]{0,0,0,0,0,0});
		for (int i = 0; i < 5; i++){
			if (tempDice[i] != null){
				freqs.SetValueAt(tempDice[i].getLastRoll() - 1, freqs.GetValueAt(tempDice[i].getLastRoll() - 1) + 1);
			}
		}
		// Straight or quints
		if (freqs.GetValueAt(0) == 1 && freqs.GetValueAt(1) == 1 && freqs.GetValueAt(2) == 1 && freqs.GetValueAt(3) == 1 && freqs.GetValueAt(4) == 1 || freqs.GetValueAt(1) == 1 && freqs.GetValueAt(2) == 1 && freqs.GetValueAt(3) == 1 && freqs.GetValueAt(4) == 1 && freqs.GetValueAt(5) == 1){
			points += 1000;
		}else if (freqs.FindMaxValue() == 5){
			if (freqs.FindMaxIndex() == 0){
				points += 4000;
			}else{
				points += 400 * (freqs.FindMaxIndex() + 1);
			}
		}else if (freqs.FindMaxValue() > 2){  // Trips or quads
			for (int i = 0; i < 5; i++){
				if (tempDice[i] != null){
					if (tempDice[i].getLastRoll() != freqs.FindMaxIndex() + 1){
						if (tempDice[i].getLastRoll() == 1){
							points += 100;
						}else if (tempDice[i].getLastRoll() == 5){
							points += 50;
						}
					}
				}
			}
			if (freqs.FindMaxIndex() == 0){
				points += (1000 * (freqs.FindMaxValue() - 2));
			}else{
				points += (100 * (freqs.FindMaxValue() - 2) * (freqs.FindMaxIndex() + 1));
			}
		}else{
			for (int i = 0; i < 5; i++){
				if (tempDice[i] != null){
					if (tempDice[i].getLastRoll() == 1){
						points += 100;
					}else if (tempDice[i].getLastRoll() == 5){
						points += 50;
					}
				}
			}
		}
		return points;
	}
	
	// Checks what combinations of points there are, and what buttons can be enabled
	private void CheckForPoints(){
		boolean[] btnEnabled = new boolean[5];
		List<Integer> freqs = new List<Integer>(new Integer[]{0,0,0,0,0,0});
		for (int i = 0; i < 5; i++){
			if (regularDice[i] != null){
				freqs.SetValueAt(regularDice[i].getLastRoll() - 1, freqs.GetValueAt(regularDice[i].getLastRoll() - 1) + 1);
			}
		}
		// Straight or quints
		if (freqs.FindMaxValue() == 5 || freqs.GetValueAt(0) == 1 && freqs.GetValueAt(1) == 1 && freqs.GetValueAt(2) == 1 && freqs.GetValueAt(3) == 1 && freqs.GetValueAt(4) == 1 || freqs.GetValueAt(1) == 1 && freqs.GetValueAt(2) == 1 && freqs.GetValueAt(3) == 1 && freqs.GetValueAt(4) == 1 && freqs.GetValueAt(5) == 1){
			btnEnabled = new boolean[]{true, true, true, true, true};
		}else if (freqs.FindMaxValue() > 2){  // Trips or quads
			for (int i = 0; i < 5; i++){
				if (regularDice[i] != null){
					if (regularDice[i].getLastRoll() == freqs.FindMaxIndex() + 1){
						btnEnabled[i] = true;
					}else if (regularDice[i].getLastRoll() == 1 || regularDice[i].getLastRoll() == 5){
						btnEnabled[i] = true;
					}else{
						btnEnabled[i] = false;
					}
				}else{
					btnEnabled[i] = false;
				}
			}
		}else{
			for (int i = 0; i < 5; i++){
				if (regularDice[i] != null){
					btnEnabled[i] = (regularDice[i].getLastRoll() == 1 || regularDice[i].getLastRoll() == 5);
				}else{
					btnEnabled[i] = false;
				}
			}
		}
		SetDiceButtons(btnEnabled);
		if (!btnEnabled[0] && !btnEnabled[1] && !btnEnabled[2] && !btnEnabled[3] && !btnEnabled[4]){
			newsLabel.setText("<html><div style=\"text-align: center;\"><font size=6><b>Game " + curGame + " - " + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: 0</font></html>");
			tempScore = 0;
			curTurnScore = 0;
			btnRollDice.setEnabled(false);
			btnBank.setEnabled(false);
			btnEndTurn.setEnabled(true);
		}
	}
	
	private void SetDiceButtons(boolean[] enabled){
		for (int i = 0; i < 5; i++){
			canKeep[i] = enabled[i];
		}
	}
	
	private void RollYellow(){
		int result = yellowDie.Roll();
		lblCurPowerDice[1].setIcon(new ImageIcon(yellowDie.GetResultImage(result - 1)));
		lblPowerMessage.setText(PowerMessage("yellow", result));
		if (result == 1){
			curTurnScore += 500;
		}else if (result == 2){
			curTurnScore += 2000;
		}else if (result == 3){
			curTurnScore += -1000;
		}else if (result == 4){ // Lost turn
			curTurnScore = 0; 
		}else if (result == 5){ // Trade Pts
			TradePtsWindow tradeWin = new TradePtsWindow(curPlayerTurn, playerNames, playerScores);
			int tempPts = playerScores.GetValueAt(tradeWin.choice);
			playerScores.SetValueAt(tradeWin.choice, playerScores.GetValueAt(curPlayerTurn));
			playerScores.SetValueAt(curPlayerTurn, tempPts);
			if (gamesNeededToWin > 1){
				lblPlayerScores[curPlayerTurn].setText("<html><font size=4>Score: " + playerScores.GetValueAt(curPlayerTurn) + "<br>" + plrWins.GetValueAt(curPlayerTurn) + " Win(s)</font></html>");
				lblPlayerScores[tradeWin.choice].setText("<html><font size=4>Score: " + playerScores.GetValueAt(tradeWin.choice) + "<br>" + plrWins.GetValueAt(tradeWin.choice) + " Win(s)</font></html>");
			}else{
				lblPlayerScores[curPlayerTurn].setText("<html><font size=4>Score: " + playerScores.GetValueAt(curPlayerTurn) + "</font></html>");
				lblPlayerScores[tradeWin.choice].setText("<html><font size=4>Score: " + playerScores.GetValueAt(tradeWin.choice) + "</font></html>");
			}
			curTurnScore = 0;
			tempScore = 0;
		}else{ // Roll again
			rollAgain = true;
		}
		
	}
	
	// Returns the change in points
	private int RollSkull(){
		int res1 = skullDice[0].Roll();
		int res2 = skullDice[1].Roll();
		lblCurPowerDice[0].setIcon(new ImageIcon(skullDice[0].GetResultImage(res1 - 1)));
		lblCurPowerDice[2].setIcon(new ImageIcon(skullDice[0].GetResultImage(res2 - 1)));
		if (res1 == 1 && res2 == 1){
			lblPowerMessage.setText(PowerMessage("skull", -1000));
			return -1000;
		}else if (res1 == 1){
			return -100 * res2;
		}else if (res2 == 1){
			return -100 * res1;
		}else{
			return 100 * (res1 + res2);
		}
	}
	
	private void RollAll(){
		int result = greenDie.Roll();
		lblCurPowerDice[1].setIcon(new ImageIcon(greenDie.GetResultImage(result - 1)));
		lblPowerMessage.setText(PowerMessage("all", result));
		if (result == 1){  // Lost all points
			curTurnScore = 0;
			tempScore = 0;
			playerScores.SetValueAt(curPlayerTurn, 0);
		}else if (result == 20){  // Won game
			WonGame(playerNames.GetValueAt(curPlayerTurn), -1);
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
		lblPowerMessage.setText(PowerMessage("tripler", freqs.FindMaxValue()));
		return freqs.FindMaxValue();
	}
	
	// Returns the change in points
	private int RollPlusMinus(){
		int res1 = plusMinusDice[0].Roll();
		int res2 = plusMinusDice[1].Roll();
		lblCurPowerDice[0].setIcon(new ImageIcon(plusMinusDice[0].GetResultImage(res1 - 1)));
		lblCurPowerDice[2].setIcon(new ImageIcon(plusMinusDice[1].GetResultImage(res2 - 1)));
		if (res1 == 2){
			return -100 * res2;
		}else{
			return 100 * res2;
		}
	}
	
	private String TurnMessage(int score){
		if (score > WINNING_SCORE){
			return "<html><div style=\"text-align: center;\"><font size=5>ONE TURN WIN!</font></html>";
		}else if (score >= 8000){
			return "<html><div style=\"text-align: center;\"><font size=5>Legendary!</font></html>";
		}else if (score >= 4000){
			return "<html><div style=\"text-align: center;\"><font size=5>Unbelievable!</font></html>";
		}else if (score >= 2000){
			return "<html><div style=\"text-align: center;\"><font size=5>Amazing!</font></html>";
		}else if (score >= 1000){
			return "<html><div style=\"text-align: center;\"><font size=5>Great!</font></html>";
		}else if (score >= 500){
			return "<html><div style=\"text-align: center;\"><font size=5>Nice!</font></html>";
		}else{
			return "";
		}
	}
	
	private String PowerMessage(String id, int result){
		if (id == "yellow"){
			switch (result){
				case 2:
					return "<html><div style=\"text-align: center;\"><font size=5>High Roller!</font></html>";
				case 3:
					return "<html><div style=\"text-align: center;\"><font size=5>Better Luck Next Time!</font></html>";
				case 4:
					return "<html><div style=\"text-align: center;\"><font size=5>Better Luck Next Time!</font></html>";
				case 5:
					return "<html><div style=\"text-align: center;\"><font size=5>Trade Points!</font></html>";
				default:
					return "";
			}
		}else if (id == "all"){
			if (result == 20){
				return "<html><div style=\"text-align: center;\"><font size=5>Winner!</font></html>";
			}else if (result == 1){
				return "<html><div style=\"text-align: center;\"><font size=5>Lost All Points!</font></html>";
			}
			return "";
		}else if (id == "tripler"){  // Here result is the highest frequency
			if (result == 2){
				return "<html><div style=\"text-align: center;\"><font size=5>Doubles!</font></html>";
			}else if (result == 3){
				return "<html><div style=\"text-align: center;\"><font size=5>Triples!</font></html>";
			}
			return "";
		}else if (id == "skull"){  // Here result is the score received
			if (result == -1000){
				return "<html><div style=\"text-align: center;\"><font size=4>Double Trouble!</font></html>";
			}
			return "";
		}
		return "";
	}
	
	public static void OpenWebsite(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        }catch (Exception e) {e.printStackTrace();}
	    }
	}

	public static void OpenWebsite(URL url) {
	    try {
	        OpenWebsite(url.toURI());
	    }catch (URISyntaxException e) {e.printStackTrace();}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
