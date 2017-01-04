package dk.magenta.bitmagasinet;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dk.magenta.bitmagasinet.configuration.ConfigurationHandlerImpl;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

public class Main extends JFrame {

	private JPanel contentPane;
	private JButton btnGetConfiguration;
	private JLabel lblCurrentConfiguration;
	private JPanel currentConfigurationPane;
	private JList bitRepoList;
	private DefaultListModel<String> bitRepoListModel;
	private GUIFacade guiFacade;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblTom;
	private String repoName;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		
		guiFacade = new GUIFacadeImpl(new ConfigurationHandlerImpl());
		
		loadRepoConfsIntoListModel();
		
		initComponents();
		createEvents();
		
	}
	
	private void loadRepoConfsIntoListModel() {
		bitRepoListModel = new DefaultListModel<String>();
		List<String> repoNames = guiFacade.getRepositoryConfigurationNames();
		for (String name : repoNames) {
			bitRepoListModel.addElement(name);
		}
	}

	private void initComponents() {

		
//		bitRepoListModel.addElement("Statsbiblioteket");
//		bitRepoListModel.addElement("Det Kongelige Bibliotek");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1012, 642);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE))
		);
		
		JPanel pnlInputList = new JPanel();
		pnlInputList.setName("");
		tabbedPane.addTab("Bitmagasiner", null, pnlInputList, null);
		
		JLabel lblVlgBitmagasin = new JLabel("Vælg Bitmagasin");
		lblVlgBitmagasin.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JScrollPane bitRepoScrollPane = new JScrollPane();
		
		btnGetConfiguration = new JButton("Hent konfiguration");
		if (bitRepoListModel.isEmpty()) {
			btnGetConfiguration.setVisible(false);
		}
		btnGetConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String repositoryName = (String) bitRepoList.getSelectedValue();
				// lblCurrentConfiguration.setText(repositoryName);
				setConfigurationPaneVisibility(true);
			}
		});
		
		currentConfigurationPane = new JPanel();
		currentConfigurationPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		currentConfigurationPane.setVisible(false);
		
		lblCurrentConfiguration = new JLabel("Konfiguration");
		lblCurrentConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		lblCurrentConfiguration.setVisible(false);
		
		JButton btnTest = new JButton("test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					bitRepoListModel.addElement("hurra");
				
			}
		});
		
		JButton btnTilfjNy = new JButton("Tilføj ny");
		btnTilfjNy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				repoName = JOptionPane.showInputDialog("Indtast navn på konfiguration");
				lblCurrentConfiguration.setText("Konfiguration for " + repoName);
				lblCurrentConfiguration.setVisible(true);
				currentConfigurationPane.setVisible(true);
			}
		});
		
		GroupLayout gl_pnlInputList = new GroupLayout(pnlInputList);
		gl_pnlInputList.setHorizontalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInputList.createSequentialGroup()
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlInputList.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_pnlInputList.createSequentialGroup()
									.addComponent(btnTilfjNy)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnGetConfiguration))
								.addComponent(bitRepoScrollPane, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblVlgBitmagasin)))
						.addGroup(gl_pnlInputList.createSequentialGroup()
							.addGap(101)
							.addComponent(btnTest)))
					.addGap(18)
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addComponent(currentConfigurationPane, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
						.addComponent(lblCurrentConfiguration))
					.addContainerGap())
		);
		gl_pnlInputList.setVerticalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInputList.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVlgBitmagasin)
						.addComponent(lblCurrentConfiguration))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlInputList.createSequentialGroup()
							.addComponent(bitRepoScrollPane, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_pnlInputList.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnTilfjNy)
								.addComponent(btnGetConfiguration))
							.addGap(157)
							.addComponent(btnTest))
						.addComponent(currentConfigurationPane, GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblStiTilRepositorysettingxml = new JLabel("Sti til mappe indeholdende RepositorySetting.xml og ReferenceSettings.xml");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblStiTilCertifikat = new JLabel("Sti til certifikat");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		lblTom = new JLabel("Tom");
		GroupLayout gl_currentConfigurationPane = new GroupLayout(currentConfigurationPane);
		gl_currentConfigurationPane.setHorizontalGroup(
			gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_currentConfigurationPane.createSequentialGroup()
					.addGroup(gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_currentConfigurationPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
								.addComponent(lblStiTilRepositorysettingxml)
								.addComponent(lblStiTilCertifikat)
								.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)))
						.addGroup(gl_currentConfigurationPane.createSequentialGroup()
							.addGap(168)
							.addComponent(lblTom)))
					.addContainerGap())
		);
		gl_currentConfigurationPane.setVerticalGroup(
			gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_currentConfigurationPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStiTilRepositorysettingxml)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblStiTilCertifikat)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(146)
					.addComponent(lblTom)
					.addContainerGap(208, Short.MAX_VALUE))
		);
		currentConfigurationPane.setLayout(gl_currentConfigurationPane);
		
		bitRepoList = new JList();
		bitRepoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bitRepoList.setModel(bitRepoListModel);
		bitRepoList.setSelectedIndex(0);
		bitRepoScrollPane.setViewportView(bitRepoList);
		pnlInputList.setLayout(gl_pnlInputList);
		
		JPanel pnlChecksumList = new JPanel();
		tabbedPane.addTab("Kontrolsummer", null, pnlChecksumList, null);
		GroupLayout gl_pnlChecksumList = new GroupLayout(pnlChecksumList);
		gl_pnlChecksumList.setHorizontalGroup(
			gl_pnlChecksumList.createParallelGroup(Alignment.LEADING)
				.addGap(0, 697, Short.MAX_VALUE)
		);
		gl_pnlChecksumList.setVerticalGroup(
			gl_pnlChecksumList.createParallelGroup(Alignment.LEADING)
				.addGap(0, 552, Short.MAX_VALUE)
		);
		pnlChecksumList.setLayout(gl_pnlChecksumList);
		contentPane.setLayout(gl_contentPane);
		
	}

	private void setConfigurationPaneVisibility(boolean b) {
		lblCurrentConfiguration.setVisible(b);
		currentConfigurationPane.setVisible(b);
	}
	
	private void createEvents() {
		// TODO Auto-generated method stub
		
	}
}
