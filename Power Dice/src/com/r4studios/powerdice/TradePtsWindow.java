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

public class TradePtsWindow extends JDialog implements ActionListener{
	private static final long serialVersionUID = -5705530812580177667L;
	private JPanel pnlMain;
	private JPanel[] pnlOtherPlrs;
	private JPanel[] pnlLbls;
	private JLabel[] lblOtherPlrInfo;
	private JButton[] btnPickPlr;
	private int numOthers;
	public int choice = 0;
	private List<String> plrNames;
	private List<String> plrChooseFrom = new List<String>();
	private int curPnl = 0;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");

	public TradePtsWindow(int curPlr, List<String> playerNames, List<Integer> playerScores){
		plrNames = playerNames;
		numOthers = playerNames.getSize() - 1;
		pnlOtherPlrs = new JPanel[numOthers];
		lblOtherPlrInfo = new JLabel[numOthers];
		btnPickPlr = new JButton[numOthers];
		pnlLbls = new JPanel[numOthers];
		
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlMain.setLayout(new GridLayout(1, numOthers, 10, 0));
		for (int i = 0; i <= numOthers; i++){
			if (i != curPlr){
				plrChooseFrom.Push(playerNames.GetValueAt(i));
				pnlOtherPlrs[curPnl] = new JPanel();
				pnlLbls[curPnl] = new JPanel();
				lblOtherPlrInfo[curPnl] = new JLabel("<html><font size=5>" + playerNames.GetValueAt(i) + "</font><br><font size=4>" + playerScores.GetValueAt(i) + " Points</font></html>");
				pnlLbls[curPnl].add(lblOtherPlrInfo[curPnl], JLabel.CENTER);
				btnPickPlr[curPnl] = new JButton("Trade Points with " + playerNames.GetValueAt(i));
				btnPickPlr[curPnl].addActionListener(this);
				pnlOtherPlrs[curPnl].setLayout(new BorderLayout());
				pnlOtherPlrs[curPnl].add(pnlLbls[curPnl]);
				pnlOtherPlrs[curPnl].add(btnPickPlr[curPnl], BorderLayout.SOUTH);
				pnlMain.add(pnlOtherPlrs[curPnl]);
				curPnl++;
			}
		}
		
		this.add(pnlMain);
		this.setSize(50 + 185 * numOthers, 150);
		this.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		this.setLocationRelativeTo(null);
		this.setIconImage(winIcon.getImage());
		this.setTitle("Trade Points");
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < numOthers; i++){
			if (e.getSource() == btnPickPlr[i]){
				choice = plrNames.GetIndexOf(plrChooseFrom.GetValueAt(i));
				this.dispose();
			}
		}
	}
}
