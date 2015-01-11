package com.r4studios.powerdice;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutWindow extends JDialog{
	private static final long serialVersionUID = -1235099906740561881L;
	private JPanel pnlMain;
	private JLabel infoLbl;
	private static final ImageIcon winIcon = new ImageIcon("Resources/icon.png");
	
	public AboutWindow(String versionNumber){
		infoLbl = new JLabel("<html><div style=\"text-align: center\"><font size=6><b>Power Dice</b></font><br>Version "  + versionNumber + "<br><br><br>Developed by Jonathan Hayes<br>\u00a9 R4 Studios 2015</html>");
		pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pnlMain.setLayout(new BorderLayout());
		pnlMain.add(infoLbl);
		
		this.add(pnlMain);
		this.setSize(175,150);
		this.setLocationRelativeTo(null);
		this.setIconImage(winIcon.getImage());
		this.setTitle("About Power Dice");
		this.setVisible(true);
	}
	
}
