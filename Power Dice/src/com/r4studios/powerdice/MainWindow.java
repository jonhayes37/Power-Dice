package com.r4studios.powerdice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
// TODO Add high score / hall of fame
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
	private JMenuItem quitItem;
	private JMenuItem helpItem;
	private JMenuItem hallOfFameItem;
	private JMenuItem aboutItem;
	private static final String VERSION_NUMBER = "0.9.1";
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	private static final String[] powerDice = {"Skull Dice", "Chance Die", "All or Nothing Die", "+/- Dice", "Tripler Dice"};
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
	private int challengingPlayer;
	private boolean newRoll = false;
	private boolean rollAgain = false;
	private boolean lastRound = false;
	
	public MainWindow(List<String> players){
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
		pnlScoreArea.setLayout(new GridLayout(1,4,100,0));// - 5 * playerNames.getSize(),0));
		for (int i = 0; i < 4; i++){
			pnlPlayers[i] = new JPanel();
			pnlPlayers[i].setLayout(new GridLayout(2,1,0,0));
			pnlPlrNames[i] = new JPanel();
			pnlPlrNames[i].setLayout(new BorderLayout());
			if (i < this.playerNames.getSize()){
				playerScores.Push(0);
				lblPlayerScores[i] = new JLabel("<html><font size=4>Score: 0</font></html>");
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
			pnlKeepDie[i].setLayout(new BorderLayout());
			lblKeptDice[i] = new JLabel();
			lblKeptDice[i].setIcon(new ImageIcon("Resources/blank.png"));
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
		pnlPowerRoll.setBorder(BorderFactory.createEmptyBorder(70,0,0,0));
		pnlRollPower = new JPanel();
		pnlRollPower.setLayout(new GridLayout(1,5,5,0));
		lblCurPowerDice[0] = new JLabel();
		lblCurPowerDice[1] = new JLabel();
		lblCurPowerDice[2] = new JLabel();
		lblCurPowerDice[0].setIcon(new ImageIcon("Resources/blank.png"));
		lblCurPowerDice[1].setIcon(new ImageIcon("Resources/blank.png"));
		lblCurPowerDice[2].setIcon(new ImageIcon("Resources/blank.png"));
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
		pnlPowerRoll.add(new JPanel(), BorderLayout.NORTH);
		pnlPowerRoll.add(pnlRollPower, BorderLayout.CENTER);
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
			lblCurDice[i].setIcon(new ImageIcon("Resources/blank.png"));
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
		this.setSize(700,750);
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
				regularDice[i] = keptDice[i];
				keptDice[i] = null;
				tempDice[i] = null;
				lblKeptDice[i].setIcon(new ImageIcon("Resources/blank.png"));
				lblCurDice[i].setIcon(new ImageIcon(regularDice[i].GetResultImage(regularDice[i].getLastRoll() - 1)));
				btnReturnDie[i].setEnabled(false);
				btnKeepDie[i].setEnabled(true);
				tempScore = CalcTempPoints();
				newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				if (btnReturnDie[0].isEnabled() == false && btnReturnDie[1].isEnabled() == false && btnReturnDie[2].isEnabled() == false && btnReturnDie[3].isEnabled() == false && btnReturnDie[4].isEnabled() == false){
					btnRollDice.setEnabled(false);
					btnBank.setEnabled(false);
				}
			}else if (e.getSource() == btnKeepDie[i]){
				btnRollDice.setEnabled(true);
				keptDice[i] = regularDice[i];
				tempDice[i] = keptDice[i];
				regularDice[i] = null;
				lblCurDice[i].setIcon(new ImageIcon("Resources/blank.png"));
				lblKeptDice[i].setIcon(new ImageIcon(keptDice[i].GetResultImage(keptDice[i].getLastRoll() - 1)));
				btnKeepDie[i].setEnabled(false);
				btnReturnDie[i].setEnabled(true);
				tempScore = CalcTempPoints();
				if (tempScore > 0){
					btnBank.setEnabled(true);
				}
				newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				CheckReRoll();
			}else if (e.getSource() == btnPickPowerDie[i]){
				lblCurPowerDice[0].setIcon(new ImageIcon("Resources/blank.png"));
				lblCurPowerDice[1].setIcon(new ImageIcon("Resources/blank.png"));
				lblCurPowerDice[2].setIcon(new ImageIcon("Resources/blank.png"));
				if (i == 0){
					curTurnScore += RollSkull();
				}else if (i == 1){ 
					RollYellow();
				}else if (i == 2){
					RollAll();
				}else if (i == 3){
					curTurnScore += RollPlusMinus();
				}else if (i == 4){
					curTurnScore *= RollTripler();
				}
				newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: " + (tempScore + curTurnScore) + "</font></html>");
				btnEndTurn.setEnabled(true);
			}
		}
		
		if (e.getSource() == btnRollDice){
			btnRollDice.setEnabled(false);
			curTurnScore += tempScore;
			tempScore = 0;
			for (int i = 0; i < 5; i++){  // Loop to roll remaining dice
				tempDice[i] = null;
				if (keptDice[i] != null){
					btnReturnDie[i].setEnabled(false);
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
					btnReturnDie[i].setEnabled(false);
				}
			}
			CheckForPoints();  // Enables buttons which can be used
			newRoll = false;			
		}else if (e.getSource() == btnBank){
			curTurnScore += tempScore;
			tempScore = 0;
			for (int i = 0; i < 5; i++){
				btnReturnDie[i].setEnabled(false);
			}
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
		}else if (e.getSource() == btnEndTurn){
			EndPlayerTurn();
		}else if (e.getSource() == quitItem){
			this.dispose();
		}else if (e.getSource() == aboutItem){
			new AboutWindow(VERSION_NUMBER);
		}else if (e.getSource() == newGameItem){
			new EnterPlayersWindow();
			this.dispose();
		}else if (e.getSource() == helpItem){
			int n = JOptionPane.showConfirmDialog(this, "This will open the Power Dice ReadMe file in your default browser.  Do you wish to proceed?", "Opening in Default Browser", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION){
				try {
					OpenWebsite(new URI("https://github.com/jonhayes37/Power-Dice/blob/master/README.md"));
				}catch (URISyntaxException e1) {e1.printStackTrace();}
			}
		}
	}
	
	private void EndPlayerTurn(){
		playerScores.SetValueAt(curPlayerTurn, playerScores.GetValueAt(curPlayerTurn) + curTurnScore);
		lblPlayerScores[curPlayerTurn].setText("<html><font size=4>Score: " + playerScores.GetValueAt(curPlayerTurn) + "</font></html>");
		sortedPlayerScores = playerScores.QuickSort().Reverse();
		if (lastRound && (curPlayerTurn + 1) % 4 == challengingPlayer){  // If the last player's last round is finished
			WonGame(playerNames.GetValueAt(playerScores.GetIndexOf(sortedPlayerScores.GetValueAt(0))), sortedPlayerScores.GetValueAt(0));
		}else if (!lastRound){
			if (playerScores.GetValueAt(curPlayerTurn) >= 20000){
				challengingPlayer = curPlayerTurn;
				lastRound = true;
				JOptionPane.showMessageDialog(this, playerNames.GetValueAt(curPlayerTurn) + " has reached 20,000 points! The final round has begun!", "Final Round Starting!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		curTurnScore = 0;
		tempScore = 0;
		if (rollAgain == false){  // Changes players if no roll again from yellow
			curPlayerTurn = (curPlayerTurn + 1) % playerNames.getSize();
			newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: 0</font></html>");
		}
		for (int i = 0; i < 5; i++){
			if (i < 3){
				lblCurPowerDice[i].setIcon(new ImageIcon("Resources/blank.png"));
			}
			regularDice[i] = new Dice(6);
			keptDice[i] = null;
			tempDice[i] = null;
			lblKeptDice[i].setIcon(null);
			lblCurDice[i].setIcon(new ImageIcon("Resources/blank.png"));
			btnRollDice.setEnabled(true);
			btnEndTurn.setEnabled(false);
			btnPickPowerDie[i].setEnabled(false);
		}
		rollAgain = false;
	}
	
	private void WonGame(String name, int score){
		Object[] options = {"Yes, with the same players", "Yes, but with different players", "No"};
		int n;
		if (score == -1){
			n = JOptionPane.showOptionDialog(this, name + " won the game by rolling a 20 on the All or Nothing die! Would you like to play again?", name + " won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		}else{
			n = JOptionPane.showOptionDialog(this, name + " won the game with a score of " + score + "!  Would you like to play again?", name + " won!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		}
		if (n == JOptionPane.YES_OPTION){
			this.dispose();
			new MainWindow(playerNames);
		}else if (n == JOptionPane.NO_OPTION){
			this.dispose();
			new EnterPlayersWindow();
		}else{
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
			//JOptionPane.showMessageDialog(this, "There are no points to keep!", "No Points on Roll", JOptionPane.INFORMATION_MESSAGE);
			newsLabel.setText("<html><font size=6><b>" + playerNames.GetValueAt(curPlayerTurn) + "'s Turn</b></font><br><font size=5>Turn Score: 0</font></html>");
			tempScore = 0;
			curTurnScore = 0;
			btnRollDice.setEnabled(false);
			btnBank.setEnabled(false);
			btnEndTurn.setEnabled(true);
		}
	}
	
	private void SetDiceButtons(boolean[] enabled){
		for (int i = 0; i < 5; i++){
			btnKeepDie[i].setEnabled(enabled[i]);
		}
	}
	
	// Returns change in pts or1 event code (-1,-2,-3)
	private void RollYellow(){
		int result = yellowDie.Roll();
		lblCurPowerDice[1].setIcon(new ImageIcon(yellowDie.GetResultImage(result - 1)));
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
			lblPlayerScores[curPlayerTurn].setText("<html><font size=4>Score: " + playerScores.GetValueAt(curPlayerTurn) + "</font></html>");
			lblPlayerScores[tradeWin.choice].setText("<html><font size=4>Score: " + playerScores.GetValueAt(tradeWin.choice) + "</font></html>");
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
			return -1000;
		}else if (res1 == 1){
			return -100 * res2;
		}else if (res2 == 1){
			return -100 * res1;
		}else{
			return 200 * (res1 + res2);
		}
	}
	
	private void RollAll(){
		int result = greenDie.Roll();
		lblCurPowerDice[1].setIcon(new ImageIcon(greenDie.GetResultImage(result - 1)));
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
}
