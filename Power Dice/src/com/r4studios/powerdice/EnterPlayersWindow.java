package com.r4studios.powerdice;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.r4studios.DataStructures.List;

public class EnterPlayersWindow extends JDialog implements ActionListener{
	private static final long serialVersionUID = -8532042394503075212L;
	private List<String> players = new List<String>();
	private JPanel pnlMain;
	private JLabel[] lblPlayers = new JLabel[4];
	private JTextField[] txtPlayers = new JTextField[4];
	private JButton btnOk;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");

	public EnterPlayersWindow(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1){e1.printStackTrace();}
		
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		pnlMain.setLayout(new GridLayout(5,2,0,5));
		for (int i = 0; i < 4; i++){
			lblPlayers[i] = new JLabel("Player " + (i + 1) + "'s name:");
			txtPlayers[i] = new JTextField(15);
			pnlMain.add(lblPlayers[i]);
			pnlMain.add(txtPlayers[i]);
		}
		btnOk = new JButton("Start Game!");
		btnOk.addActionListener(this);
		pnlMain.add(new JLabel());
		pnlMain.add(btnOk);
		
		this.add(pnlMain);
		this.pack();
		this.setIconImage(winIcon.getImage());
		this.setSize(250,200);
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
			new MainWindow(this.players);
			this.dispose();
		}
		
	}

	public static void main(String[] args){
		new EnterPlayersWindow();
	}
}
