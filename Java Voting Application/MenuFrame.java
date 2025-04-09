package labs.lab9;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MenuFrame extends JFrame {
    private JMenuItem exit;

    private JPanel propositionsPanel;
    private JPanel propPanel;
    
    private JLabel electionNameLabel;
    private JLabel candALabel;
    private JLabel candBLabel;
    private JLabel donationsLabel;
    private JLabel notesLabel;
    private JTextArea notesField;
    private JButton castButton;
    private JLabel candSelect;
    
    private JFrame voting;
    private JRadioButton radioA;
    private JRadioButton radioB;
    private JCheckBox donationCheck;
    private JLabel donationLabel;
    private JTextField donationField;
    private JButton cancelButton;
    private JButton okButton;

    private String electionName;
    private String candidateA;
    private String candidateB;
    private Integer propNum;
    
    Integer Avotes;
    Integer Bvotes;
    ArrayList<Pair> votes;
    
    private Double donationsTotal;

    public MenuFrame(String name, String A, String B, Integer num) {
        this.electionName = name;
        this.candidateA = A;
        this.candidateB = B;
        this.propNum = num;
        this.Avotes = 0;
        this.Bvotes = 0;
        this.votes = new ArrayList<>();
        for (int i = 1; i <= propNum; i++) {
            votes.add(new Pair(0, 0)); 
        }
        this.donationsTotal = 0.0;
        this.voting = votingFrame();
        
        setTitle("Voting System - Carson Truong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenu();
        setupPanels();
        setupActions();
        pack();
        setVisible(true);
        
    }
    
    
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        exit = new JMenuItem("Exit");
        menu.add(exit);
        menuBar.add(menu);
        setJMenuBar(menuBar);  
    }

    private void setupPanels() {
    	JPanel menuPanel = new JPanel();
    	BoxLayout layout = new BoxLayout(menuPanel, BoxLayout.Y_AXIS);
    	menuPanel.setLayout(layout);
    	menuPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    	JPanel bigPanel = new JPanel();
    	bigPanel.setLayout(new GridLayout(2,1));
        
        electionNameLabel = new JLabel(electionName, SwingConstants.LEFT);
        JPanel titlePanel = new JPanel(new BorderLayout()); 
        titlePanel.add(electionNameLabel, BorderLayout.WEST);
        
        JPanel candidatesPanel = new JPanel(new GridLayout(2, 1)); 
        candidatesPanel.setBorder(new TitledBorder(new EtchedBorder(), "Candidates:"));

        candALabel = new JLabel(candidateA + ": " + Avotes);
        candBLabel = new JLabel(candidateB + ": " + Bvotes);

        JPanel candidateAPanel = new JPanel(new BorderLayout());
        candidateAPanel.add(candALabel, BorderLayout.WEST);

        JPanel candidateBPanel = new JPanel(new BorderLayout());
        candidateBPanel.add(candBLabel, BorderLayout.WEST);

        candidatesPanel.add(candidateAPanel);
        candidatesPanel.add(Box.createVerticalStrut(50));
        candidatesPanel.add(candidateBPanel);

        propositionsPanel = new JPanel(new GridLayout(propNum, 1)); 
        propositionsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Propositions:"));
        
        for (int i = 0; i < propNum; i++) {
        	String pairText = "<html>" + (i + 1) + ": <b>YES: </b><b>" + votes.get(i).getYes() + "</b><b> votes, </b>"
                    + "<b>NO:</b> <b>" + votes.get(i).getNo() + "</b> <b>votes</b></html>";
            
            JLabel label = new JLabel(pairText, SwingConstants.LEFT);
            propositionsPanel.add(label); 
        }
        
        String formattedValue = String.format("Donation total: $%.2f", donationsTotal);
        donationsLabel = new JLabel(formattedValue, SwingConstants.CENTER);
        JPanel donationsPanel = new JPanel(new BorderLayout()); 
        donationsPanel.add(donationsLabel, BorderLayout.CENTER);
        
        notesLabel = new JLabel("Notes: ");
        JTextArea notesArea = new JTextArea(5, 50);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notesArea);
        
        JPanel notesPanel = new JPanel(); 
        notesPanel.add(notesLabel, BorderLayout.WEST);
        notesPanel.add(scrollPane, BorderLayout.CENTER); 

        castButton = new JButton("Cast Vote");
        JPanel OptionsPanel = new JPanel();
        OptionsPanel.add(castButton);
        
        menuPanel.add(titlePanel); 
        menuPanel.add(candidatesPanel);
        menuPanel.add(propositionsPanel);
        menuPanel.add(donationsPanel);
        menuPanel.add(notesPanel);
        menuPanel.add(OptionsPanel);

        add(menuPanel, BorderLayout.CENTER);
    }
    
    private JFrame votingFrame() {
    	JFrame votingFrame = new JFrame();
    	JPanel votingPanel = new JPanel();
        BoxLayout layout = new BoxLayout(votingPanel, BoxLayout.Y_AXIS);
        votingPanel.setLayout(layout);
        votingPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        
        candSelect = new JLabel("Candidate: ");
        radioA = new JRadioButton(candidateA);
        radioB = new JRadioButton(candidateB);
        ButtonGroup candGroup = new ButtonGroup();
        JPanel candidatesPanel = new JPanel();
        candidatesPanel.setBorder(new TitledBorder(new EtchedBorder(), "Candidates:"));
        candidatesPanel.add(candSelect);
        candGroup.add(radioA);
        candGroup.add(radioB);
        candidatesPanel.add(radioA);
        candidatesPanel.add(radioB);

        
        propPanel = new JPanel(new GridLayout(propNum, 1));
        propPanel.setBorder(new TitledBorder(new EtchedBorder(), "Propositions:"));
        for (int i = 1; i <= propNum; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel label = new JLabel("Prop " + i + ": ");
            JRadioButton yesButton = new JRadioButton("YES");
            JRadioButton noButton = new JRadioButton("NO");

            ButtonGroup group = new ButtonGroup();
            group.add(yesButton);
            group.add(noButton);

            rowPanel.add(label);
            rowPanel.add(yesButton);
            rowPanel.add(noButton);

            propPanel.add(rowPanel);
        }

        
        JPanel donationPanel = new JPanel();
        donationPanel.setBorder(new TitledBorder(new EtchedBorder(), "Donation:"));
        donationCheck = new JCheckBox();
        donationLabel = new JLabel("I would like to make a donation ");
        donationField = new JTextField("0.00",candidateA.length() + candidateB.length());
        donationField.setEnabled(false); 
        donationPanel.add(donationCheck);
        donationPanel.add(donationLabel);
        donationPanel.add(donationField);

        
        cancelButton = new JButton("Cancel");
        okButton = new JButton("OK");
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        optionsPanel.add(cancelButton);
        optionsPanel.add(okButton);

        
        votingPanel.add(candidatesPanel);
        votingPanel.add(propPanel);
        votingPanel.add(donationPanel);
        votingPanel.add(optionsPanel);

       
        votingFrame.add(votingPanel);
        votingFrame.pack();
        votingFrame.setVisible(false);
        
        
        donationCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (donationCheck.isSelected()) {
            		donationField.setEnabled(true); 
            	}
            	else {
            		donationField.setEnabled(false);
            		donationField.setText("0.00");
            	}
            		
            }});
        
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	boolean loop = true;
                while (loop) {
                	if (donationCheck.isSelected()) {
		            	try {
		                    Double parsedDonation = Double.parseDouble(donationField.getText());
		                    if (parsedDonation < 0.0 || Math.round(parsedDonation * 100) != parsedDonation * 100) {
		                    	JOptionPane.showMessageDialog(voting, "Please Enter a valid donation amount", "Error", JOptionPane.ERROR_MESSAGE);
		                    	voting.dispose();
		                    	loop = false;
		                    	continue;
		                    	
		                    }
		                    else {
		                    	Double don = Double.parseDouble(donationField.getText());
		                    	donationsTotal += don;
		                    	String formattedValue = String.format("Donation total: $%.2f", donationsTotal);
		                    	donationsLabel.setText(formattedValue);
		                    	loop = false;
		                    } 
		                } catch (NumberFormatException ex) {
		                	JOptionPane.showMessageDialog(voting, "Please Enter a valid donation amount", "Error", JOptionPane.ERROR_MESSAGE);
		                	voting.dispose();
		                	loop = false;
		                	continue;
		                }
                	}
                	
                
	        	if (radioA.isSelected()) {
	        		Avotes ++;
	        	}
	        	else if (radioB.isSelected()) {
	        		Bvotes ++;
	        	}
	        	
	        	candALabel.setText((candidateA + ": " + Avotes));
	    		candBLabel.setText((candidateB + ": " + Bvotes));
	        	if (Avotes==Bvotes) {
	        		candALabel.setFont(candALabel.getFont().deriveFont(Font.BOLD));
	        		candBLabel.setFont(candBLabel.getFont().deriveFont(Font.BOLD));
	        	}
	        	else if (Avotes>Bvotes) {
	        		candALabel.setFont(candALabel.getFont().deriveFont(Font.BOLD));
	        		candBLabel.setFont(candBLabel.getFont().deriveFont(Font.PLAIN));
	        	}
	        	else if (Avotes<Bvotes) {
	        		candALabel.setFont(candALabel.getFont().deriveFont(Font.PLAIN));
	        		candBLabel.setFont(candBLabel.getFont().deriveFont(Font.BOLD));
	        	}
	        	
	 	
	        	
	        	
	        	int index = 0;
	        	for (Component comp : propPanel.getComponents()) {
	        	    if (comp instanceof JPanel) {
	        	        JPanel rowPanel = (JPanel) comp; 
	        	        
	        	        for (Component rowComp : rowPanel.getComponents()) {
	        	            if (rowComp instanceof JRadioButton) {
	        	                JRadioButton radioButton = (JRadioButton) rowComp;
	        	                
	        	                if (radioButton.getText().equals("YES") && radioButton.isSelected()) {
	        	                    votes.get(index).setYes();
	        	                }
	        	                else if (radioButton.getText().equals("NO") && radioButton.isSelected()) {
	        	                    votes.get(index).setNo();
	        	                }
	        	            }
	        	        }
	        	        index++;
	        	    }
	        	}
	        	
	        	index = 0;
	            for (Component comp : propositionsPanel.getComponents()) {
	            		JLabel label = (JLabel) comp;
	            		if (votes.get(index).getYes()==votes.get(index).getNo()) {
	            			label.setFont(label.getFont().deriveFont(Font.PLAIN));
	            			String pairText = "<html>" + (index + 1) + ": <b>YES: " + votes.get(index).getYes() + " votes, "
	                                + "NO: " + votes.get(index).getNo() + " votes</b></html>";
	            			label.setText(pairText);
	            		}
	            		else if (votes.get(index).getYes()>votes.get(index).getNo()) {
	            			label.setFont(label.getFont().deriveFont(Font.PLAIN));
	            			String pairText = "<html>" + (index + 1) + ": <b>YES: " + votes.get(index).getYes() + " votes,</b> "
	                                + "NO: " + votes.get(index).getNo() + " votes</html>";
	            			label.setText(pairText);
	            		}
	            		else if (votes.get(index).getYes()<votes.get(index).getNo()) {
	            			label.setFont(label.getFont().deriveFont(Font.PLAIN));
	            			String pairText = (index + 1) + ": YES: " + votes.get(index).getYes() + " votes, "
	                                + "<b>NO: " + votes.get(index).getNo() + " votes</b>";
	            			label.setText("<html>"+pairText+"</html>");

	            		}
	            		index++;
	            	}
	            	
	        	voting.dispose();
	        	loop = false;
                }
            }});
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	voting.dispose(); 
            }
        });
        
        
        return votingFrame;
        
    }
    
    private void setupActions() {
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }});
        
        castButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	voting = votingFrame();
            	voting.setVisible(true);
            }});
        
    }
}


