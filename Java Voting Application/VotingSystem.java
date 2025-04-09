package labs.lab9;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class VotingSystem extends JFrame {
	private static final int FRAME_WIDTH = 550;
	private static final int FRAME_HEIGHT = 230;
	private JLabel electionNameLabel;
    private JLabel candidateALabel;
    private JLabel candidateBLabel;
    private JLabel propositionsLabel;

    private JTextField electionNameField;
    private JTextField candidateAField;
    private JTextField candidateBField;
    private JComboBox<Integer> propositionsComboBox;
    private JButton okButton;
    private JButton cancelButton;

	
	public VotingSystem() {
		createFields();
		createElectionInfoPanel();
		createListeners();
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	private void createFields() {
		final int FIELD_WIDTH = 20;
		
		electionNameLabel = new JLabel("Election Name: ");
		electionNameField = new JTextField(FIELD_WIDTH);
		
		candidateALabel = new JLabel("Candidate A Name: ");
		candidateAField = new JTextField(FIELD_WIDTH);
		
        candidateBLabel = new JLabel("Candidate B Name: ");
        candidateBField = new JTextField(FIELD_WIDTH);
        
        propositionsLabel = new JLabel("Num Propositions: ");
        propositionsComboBox = new JComboBox<>();
        for (int i = 1; i <= 15; i++) {
            propositionsComboBox.addItem(i);
        }
        
        cancelButton = new JButton("Cancel");
        okButton = new JButton("OK");
	}
	
	private void createElectionInfoPanel() {
		JPanel ElectionInfoPanel = new JPanel();
		JPanel ElectionNamePanel = new JPanel();
		JPanel CandANamePanel = new JPanel();
		JPanel CandBNamePanel = new JPanel();
		JPanel PropositionsPanel = new JPanel();
		JPanel OptionsPanel = new JPanel();
		
		ElectionNamePanel.add(electionNameLabel);
		ElectionNamePanel.add(electionNameField);
		
		CandANamePanel.add(candidateALabel);
		CandANamePanel.add(candidateAField);
		
		CandBNamePanel.add(candidateBLabel);
		CandBNamePanel.add(candidateBField);
		
		PropositionsPanel.add(propositionsLabel);
		PropositionsPanel.add(propositionsComboBox);
		
		OptionsPanel.add(cancelButton);
		OptionsPanel.add(okButton);
	
		ElectionInfoPanel.setLayout(new GridLayout(4,1));
		ElectionInfoPanel.add(ElectionNamePanel);
		ElectionInfoPanel.add(CandANamePanel);
		ElectionInfoPanel.add(CandBNamePanel);
		ElectionInfoPanel.add(PropositionsPanel);
		
		
		add(ElectionInfoPanel, BorderLayout.NORTH);
		add(OptionsPanel, BorderLayout.EAST);
		
		}
	
	private void createListeners() {

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	if ((electionNameField != null && !electionNameField.getText().trim().isEmpty()) && 
                        (candidateAField != null && !candidateAField.getText().trim().isEmpty()) && 
                        (candidateBField != null && !candidateBField.getText().trim().isEmpty()) &&
                        propositionsComboBox != null) {
                	dispose();
                	new MenuFrame(electionNameField.getText(),
                			candidateAField.getText(),
                			candidateBField.getText(),
                			(Integer) propositionsComboBox.getSelectedItem());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	System.exit(0);
            }
        });
    }
	
	
	public static void main(String[] args) {
		JFrame frame = new VotingSystem();
		frame.setTitle("Election Info");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}