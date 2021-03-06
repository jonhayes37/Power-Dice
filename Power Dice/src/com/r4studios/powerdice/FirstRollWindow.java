package com.r4studios.powerdice;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.r4studios.DataStructures.List;
import com.r4studios.powerdice.Dice;

public class FirstRollWindow extends JDialog implements ActionListener{
	private static final long serialVersionUID = -1449737054368181791L;
	private JLabel[] rollLbls;
	private JLabel[] plrRollsLbls;
	private JLabel winnerLbl;
	private JButton rollBtn;
	private JButton startBtn;
	private JPanel pnlMain;
	private JPanel btnPnl;
	private JPanel rollPnl;
	private JPanel[] indRollPnl;
	private JPanel[] iconPnl;
	private int rolls = 0;
	private List<Integer> results;
	private List<String> plrs;
	private String title;
	public String winnerName;
	private int playerSize;
	public int winner = 0;
	private Dice die = new Dice(20, "all");
	private boolean tied = false;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	
	public FirstRollWindow(List<String> players, String title){
		this.plrs = players;
		this.title = title;
		this.playerSize = players.getSize();
		rollLbls = new JLabel[playerSize];
		plrRollsLbls = new JLabel[playerSize];
		indRollPnl = new JPanel[playerSize];
		iconPnl = new JPanel[playerSize];
		results = new List<Integer>();
		
		rollPnl = new JPanel();
		rollPnl.setLayout(new GridLayout(1,players.getSize(),5,0));
		rollPnl.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		JPanel[] indLblPnls = new JPanel[players.getSize()];
		for (int i = 0; i < players.getSize(); i++){
			indRollPnl[i] = new JPanel();
			indRollPnl[i].setLayout(new BorderLayout());
			iconPnl[i] = new JPanel();
			rollLbls[i] = new JLabel();
			iconPnl[i].add(rollLbls[i], JLabel.CENTER);
			indLblPnls[i] = new JPanel();
			plrRollsLbls[i] = new JLabel("<html><font size=4>" + plrs.GetValueAt(i) + "'s Roll:</font></html>");
			indLblPnls[i].add(plrRollsLbls[i], JLabel.CENTER);
			indRollPnl[i].add(iconPnl[i]);
			indRollPnl[i].add(indLblPnls[i], BorderLayout.SOUTH);
			rollPnl.add(indRollPnl[i]);
		}
		
		JPanel winnerPnl = new JPanel();
		winnerLbl = new JLabel();
		winnerPnl.add(winnerLbl);
		rollBtn = new JButton("Roll");
		rollBtn.addActionListener(this);
		startBtn = (title == "First Roll") ? new JButton("Start Game") : new JButton("End Game");
		startBtn.addActionListener(this);
		startBtn.setEnabled(false);
		btnPnl = new JPanel();
		btnPnl.setLayout(new GridLayout(1,3,5,0));
		btnPnl.add(rollBtn);
		btnPnl.add(winnerPnl);
		btnPnl.add(startBtn);
		
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlMain.setLayout(new BorderLayout());
		pnlMain.add(rollPnl);
		pnlMain.add(btnPnl, BorderLayout.SOUTH);
		
		this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		this.add(pnlMain);
		this.pack();
		this.setIconImage(winIcon.getImage());
		this.setSize(500,200);
		this.setTitle(title + " Roll");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rollBtn){
			int roll = die.Roll();
			results.Push(roll);
			plrRollsLbls[rolls].setText("<html><font size=4>" + plrs.GetValueAt(rolls) + "'s Roll: <b>" + roll + "</b></font></html>");
			if (roll == 1 || roll == 20){
				rollLbls[rolls].setIcon(new ImageIcon("Resources/d20_" + (roll - 1) + ".png"));
			}else{
				rollLbls[rolls].setIcon(new ImageIcon(die.sidePics[roll - 1]));
			}
			rolls++;
			if (this.rolls == playerSize){
				rollBtn.setEnabled(false);
				startBtn.setEnabled(true);
				int maxRoll = results.FindMaxValue();
				if (results.isValueRepeated(maxRoll)){
					tied = true;
					startBtn.setText("Go To Tiebreaker!");
					winnerLbl.setText("<html><div align=\"center\">Tie!</div></html>");
				}else{
					winnerLbl.setText("<html><div align=\"center\">" + plrs.GetValueAt(results.FindMaxIndex()) + " goes first!</div></html>");
				}
			}
		}else if (e.getSource() == startBtn){
			if (tied){
				int maxRoll = results.FindMaxValue();
				List<String> tiePlrs = new List<String>();
				for (int i = 0; i < playerSize; i++){
					if (results.GetValueAt(i) == maxRoll){
						tiePlrs.Push(plrs.GetValueAt(i));
					}
				}
				this.winner = plrs.GetIndexOf(tiePlrs.GetValueAt(new FirstRollWindow(tiePlrs, this.title).winner));
				this.dispose();
			}else{
				this.winner = results.FindMaxIndex();
				this.winnerName = plrs.GetValueAt(winner);
				this.dispose();
			}
		}
		
	}
	
}
