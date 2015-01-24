package com.r4studios.powerdice;

import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.r4studios.DataStructures.List;

public class HallOfFameWindow extends JDialog{
	private static final long serialVersionUID = 4373394421779095987L;
	private JPanel pnlMain;
	private JLabel[] lblRanks;
	private JLabel[] lblScores;
	private JLabel[] lblDates;
	private List<Score> scores;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");

	public HallOfFameWindow(HighScoreTable highScores){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		scores = highScores.getScores();
		lblRanks = new JLabel[scores.getSize()];
		lblScores = new JLabel[scores.getSize()];
		lblDates = new JLabel[scores.getSize()];
		
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlMain.setLayout(new GridLayout(scores.getSize() + 1, 3, 5, 5));
		pnlMain.add(new JLabel("<html><font size=4><b>Player Name</b></font></html>"));
		pnlMain.add(new JLabel("<html><font size=4><b>Score</b></font></html>"));
		pnlMain.add(new JLabel("<html><font size=4><b>Date Achieved</b></font></html>"));
		for (int i = 0; i < scores.getSize(); i++){
			lblRanks[i] = new JLabel("<html><font size=3>" + (i + 1) + ". " + scores.GetValueAt(i).getName() + "</font></html>");
			lblScores[i] = new JLabel("<html><font size=3>" + scores.GetValueAt(i).getScore() + "</font></html>");
			lblDates[i] = new JLabel("<html><font size=3>" + dateFormat.format(scores.GetValueAt(i).getDate()) + "</font></html>");
			pnlMain.add(lblRanks[i]);
			pnlMain.add(lblScores[i]);
			pnlMain.add(lblDates[i]);
		}
		
		this.add(pnlMain);
		this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		this.setSize(225,225 + 30 * scores.getSize());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setIconImage(winIcon.getImage());
		this.setTitle("Power Dice Hall of Fame");
		this.setVisible(true);
	}
}
