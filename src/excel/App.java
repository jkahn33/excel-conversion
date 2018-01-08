package excel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;

public class App {

	private JFrame frame;
	private JTextField txtCity;
	private JTextField txtState;
	private JTextField txtSave;
	private JTextField txtName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 511, 359);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSubmit = new JButton("Convert Menu");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtSave.getText().equals("")){
					JOptionPane.showMessageDialog(frame, "Please enter a save location.");
				}
				else if(txtName.getText().equals("")){
					JOptionPane.showMessageDialog(frame, "Please enter a file name.");
				}
				else if(txtState.getText().equals("")){
					JOptionPane.showMessageDialog(frame, "Please enter a state abbreviation.");
				}
				else if(txtCity.getText().equals("")){
					JOptionPane.showMessageDialog(frame, "Please enter a city.");
				}
				else{
					Conversion.webError = false;
			        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
			        ArrayList<String> links = Conversion.getRestaurants(txtState.getText(), txtCity.getText());
			        if(Conversion.webError == true){
			        	JOptionPane.showMessageDialog(frame, "Error connecting. Please check spelling of state abbreviation and city or internet connection.");
			        }
			        else{
				        for (String url : links) {
				            Restaurant currentRest = Conversion.getMenuInfo(url);
				            if (currentRest != null) {
				                restaurants.add(currentRest);
				            }
				        }
				        Conversion.writeToExcel(restaurants, txtSave.getText(), txtName.getText());
				        if(Conversion.webError == true){
				        	JOptionPane.showMessageDialog(frame, "An error occurred during conversion. One or more menus may not have been converted.");
				        }
			        }
			        txtName.setText("");
			        txtState.setText("");
			        txtCity.setText("");
			        JOptionPane.showMessageDialog(null , "Complete!");
				}
			}	
		});
		btnSubmit.setBounds(34, 242, 112, 23);
		frame.getContentPane().add(btnSubmit);
		
		JLabel lblNewLabel = new JLabel("Enter Two Letter State Abbr.");
		lblNewLabel.setBounds(34, 186, 165, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Enter City");
		lblNewLabel_1.setBounds(240, 186, 63, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		txtCity = new JTextField();
		txtCity.setBounds(236, 211, 165, 20);
		frame.getContentPane().add(txtCity);
		txtCity.setColumns(10);
		
		txtState = new JTextField();
		txtState.setBounds(34, 211, 165, 20);
		frame.getContentPane().add(txtState);
		txtState.setColumns(10);
		
		txtSave = new JTextField();
		txtSave.setBounds(34, 36, 367, 20);
		frame.getContentPane().add(txtSave);
		txtSave.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Copy spreadsheet save location or look it up by pressing \"search\"");
		lblNewLabel_2.setBounds(34, 11, 398, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("."));
				fc.setDialogTitle("Save Location");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fc.showOpenDialog(btnSearch) == JFileChooser.APPROVE_OPTION){
					//
				}
				txtSave.setText(fc.getSelectedFile().getAbsolutePath());
			}
		});
		btnSearch.setBounds(34, 70, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		txtName = new JTextField();
		txtName.setBounds(34, 139, 364, 20);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Enter name of spreadsheet");
		lblNewLabel_3.setBounds(34, 114, 290, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		frame.getRootPane().setDefaultButton(btnSubmit);
	}
}
