package com.r4studios.powerdice;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.r4studios.DataStructures.List;

public class EnterPlayersWindow extends JDialog implements ActionListener{
	private static final long serialVersionUID = -8532042394503075212L;
	private List<String> players = new List<String>();
	private JPanel pnlMain;
	private JLabel[] lblPlayers = new JLabel[4];
	private JRadioButton[] radBestOf = new JRadioButton[4];
	private ButtonGroup rads = new ButtonGroup();
	private JTextField[] txtPlayers = new JTextField[4];
	private JButton btnOk;
	private int numGames;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");

	public EnterPlayersWindow(List<String> names){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlMain.setLayout(new GridLayout(7,2,0,5));
		for (int i = 0; i < 4; i++){
			lblPlayers[i] = new JLabel("Player " + (i + 1) + "'s name:");
			txtPlayers[i] = new JTextField(15);
			pnlMain.add(lblPlayers[i]);
			pnlMain.add(txtPlayers[i]);
			if (i == 0){
				radBestOf[i] = new JRadioButton("Single Game");				
			}else{
				radBestOf[i] = new JRadioButton("First to " + (i + 1) + " Wins");
			}
			radBestOf[i].addActionListener(this);
			rads.add(radBestOf[i]);
		}
		if (names != null){
			for (int i = 0; i < names.getSize(); i++){
				txtPlayers[i].setText(names.GetValueAt(i));
			}
		}
		btnOk = new JButton("Start Game!");
		btnOk.addActionListener(this);
		btnOk.setEnabled(false);
		for (int i = 0; i < 4; i++){
			pnlMain.add(radBestOf[i]);
		}
		pnlMain.add(new JLabel());
		pnlMain.add(btnOk);
		
		this.add(pnlMain);
		this.pack();
		this.setIconImage(winIcon.getImage());
		this.setSize(250,250);
		this.setTitle("New Game");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOk){
			for (int i = 0; i < 4; i++){
				if (txtPlayers[i].getText().length() > 0){
					this.players.Push(txtPlayers[i].getText());
				}else{
					break;
				}
			}
			if (players.getSize() < 2){
				JOptionPane.showMessageDialog(this, "You need at least 2 players to play!", "Not Enough Players", JOptionPane.INFORMATION_MESSAGE);
			}else{
				List<Integer> wins = new List<Integer>();
				for (int i = 0; i < players.getSize(); i++){
					wins.Push(0);
				}
				new MainWindow(this.players, numGames, 1, wins);
				this.dispose();
			}
		}
		for (int i = 0; i < 4; i++){
			if (e.getSource() == radBestOf[i]){
				numGames = i + 1;
				btnOk.setEnabled(true);
			}
		}
	}

	public static void main(String[] args){
		new EnterPlayersWindow(null);
	}
}
