package edu.usm.cos420.antenatal.view;

import java.awt.Container;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;

import edu.usm.cos420.antenatal.messengers.*;
import java.awt.*;
import java.util.List;

import edu.usm.cos420.antenatal.model.*;
/*
 * This class implements the view for The registerView.
 * It displays the patient pregnacny information
 */
public class PregnancyView extends JFrame{

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private HashMap<String, Component> componentMap;

    private JPanel mainPanel;
	private JPanel content;
	private JTextField gravidity,
						parity,
						expectedDayOfDelivery,
						hemoglobinReg,
						hemoglobin36Wks,
						sicklingType;

	private JTextField facility,
						subdistrict,
						name,
						registration,
						age,
						address,
						hiNiNum,
						bloodGroup;

	private JTextArea complaints,
	 					remarks;


	private JCheckBox sickling,
						VLDR,
						pmtctPreTestCounseling,
						pmtctTestResult,
						pmtctPostTestCounseling,
						pmtctARV,
						maleInvolvement,
						itnUse,
						ttFirstDose,
						ttSecondDose,
						ttThirdDose,
						ttProtectedNotDosed,
						iptFirstDose,
						iptSecondDose,
						iptThirdDose;

	private JComboBox bloodTypeBox,
						sicklingTypeBox;

	private String[] bloodTypes = {"","A+","A-","B+","B-","AB+","AB-","O+","O-"};

	private String[] sicklingTypes = {"","AS","SS","SC",};

	private JTabbedPane selectVisitTab;

	private JButton newVisit;

	private JButton saveInitial;

	private LocalDate dateF = LocalDate.now();
	private String sDate = dateF.format(dateTimeFormatter);

	private String pH = "this is just place holder text ";

	final private int guiHeight = 825,
						guiWidth = 800;

	public PregnancyView() {
		this.setTitle("Antenatal");
		this.setDefaultCloseOperation(3);
		this.setSize(guiWidth, guiHeight);

		mainPanel = new JPanel();
		this.add(mainPanel);

		//had to move these here so they are created for the action listener in the controller
		selectVisitTab = new JTabbedPane();
		saveInitial = new JButton("save");
		newVisit = new JButton("New Visit");
	}

	
	public void initializePatientInformation(Patient p){

		facility = new JTextField();
		facility.setName("facility");
		facility.setText(pH);


		subdistrict = new JTextField();
		subdistrict.setName("subdistrict");
		subdistrict.setText(pH);

		name = new JTextField();
		name.setName("name");
		name.setText(p.getFirstName() + " " + p.getLastName());

		registration = new JTextField();
		registration.setName("registration");
		registration.setText(pH);

		age = new JTextField();
		age.setName("age");
		age.setText(getAge(p.getBirthdate()));

		address =  new JTextField();
		address.setName("address");
		address.setText(p.getAddress());

		hiNiNum =  new JTextField();
		hiNiNum.setName("hiNiNum");
		hiNiNum.setText(String.valueOf(p.getNhisId()));


		//everything above shouldn't be edited by user
		facility.setEditable(false);
		subdistrict.setEditable(false);
		name.setEditable(false);
		registration.setEditable(false);
		age.setEditable(false);
		address.setEditable(false);
		hiNiNum.setEditable(false);
	}

	/**
	 *
	 * @param msg
	 */
	public void initializePregnancyInformation(Pregnancy msg){
		//should not be edited after first visit
		sickling.setSelected(msg.getSickling().getSickling());
		sickling.setEnabled(false);

		sicklingType.setText(msg.getSickling().getType());
		sicklingType.setEnabled(false);

		parity.setText(String.format("%d", msg.getParity().getParity()));
		parity.setEnabled(false);

		gravidity.setText(String.format("%d", msg.getParity().getGravidity()));
		gravidity.setEnabled(false);

		expectedDayOfDelivery.setText(msg.getExpectedDayOfDelivery().format(dateTimeFormatter)); // Date in next commit
		expectedDayOfDelivery.setEnabled(false);

		hemoglobinReg.setText("" + msg.getHemoglobinLevelAtReg());
		hemoglobinReg.setEnabled(false);

		bloodTypeBox.setSelectedItem(msg.getBloodGroup());
		bloodTypeBox.setEnabled(false);

		maleInvolvement.setSelected(msg.isMaleInvolvement());
		maleInvolvement.setEnabled(false);

		VLDR.setSelected(msg.getVDRL());
		VLDR.setEnabled(!VLDR.isSelected());

		itnUse.setSelected(msg.getItnUse());

		pmtctPreTestCounseling.setSelected(msg.getPmtct().getPreTestCounseling());
		pmtctPreTestCounseling.setEnabled(!pmtctPreTestCounseling.isSelected());

		pmtctPostTestCounseling.setSelected(msg.getPmtct().getPostTestCounseling());
		pmtctPostTestCounseling.setEnabled(!pmtctPostTestCounseling.isSelected());

		pmtctTestResult.setSelected(msg.getPmtct().getTestResult());
		pmtctTestResult.setEnabled(!pmtctTestResult.isSelected());

		pmtctARV.setSelected(msg.getPmtct().getARV());
		pmtctARV.setEnabled(!pmtctARV.isSelected());

		hemoglobin36Wks.setText("" + msg.getHemoglobinLevelAt36Wks());
		hemoglobin36Wks.setEnabled(hemoglobin36Wks.getText().equals(""));

		ttProtectedNotDosed.setSelected(msg.getTt().getProtectedNotDosed());
		ttProtectedNotDosed.setEnabled(!ttProtectedNotDosed.isSelected() && !msg.getTt().getFirstDose());

		ttFirstDose.setSelected(msg.getTt().getFirstDose());
		ttFirstDose.setEnabled(!ttFirstDose.isSelected() && !ttProtectedNotDosed.isSelected());

		ttSecondDose.setSelected(msg.getTt().getSecondDose());
		ttSecondDose.setEnabled(!ttSecondDose.isSelected() && !ttProtectedNotDosed.isSelected());

		ttThirdDose.setSelected(msg.getTt().getThirdDose());
		ttThirdDose.setEnabled(!ttThirdDose.isSelected() && !ttProtectedNotDosed.isSelected());

		iptFirstDose.setSelected(msg.getIpt().getFirstDose());
		iptFirstDose.setEnabled(!iptFirstDose.isSelected());

		iptSecondDose.setSelected(msg.getIpt().getSecondDose());
		iptSecondDose.setEnabled(!iptSecondDose.isSelected());

		iptThirdDose.setSelected(msg.getIpt().getThirdDose());
		iptThirdDose.setEnabled(!iptThirdDose.isSelected());

		hemoglobin36Wks.setText("" + msg.getHemoglobinLevelAt36Wks());
		hemoglobin36Wks.setEnabled(hemoglobin36Wks.getText().equals(""));

		complaints.setText(msg.getComplaints());
		remarks.setText(msg.getRemarks());


	}

	public void showRegistryForm(){
        Date timestamp = new Date(); // VSC was behaving odd so I added this to verify it was the latest
        this.setTitle("Registry Form: "+timestamp.toString());

        content = new JPanel();
        content.setBackground(Color.darkGray);
        content.setSize(guiWidth,guiHeight); // GridBag completely overwrites

		GridBagLayout gblContent = new GridBagLayout();
		gblContent.columnWidths = new int[] {100, 0};
        gblContent.rowHeights = new int[]{0, 0, 0};
        gblContent.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gblContent.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        content.setLayout(gblContent);

		GridBagConstraints constraints = new GridBagConstraints();

		JMenuBar mb = new JMenuBar();
		JMenu m1 = new JMenu("General menu stuff");
		JMenu m2 = new JMenu("Help");
		mb.add(m1);
		mb.add(m2);
		JMenuItem m11 = new JMenuItem("menu STUFF");
		JMenuItem m22 = new JMenuItem("MORE menu STUFF");
		m1.add(m11);
		m1.add(m22);
		this.getContentPane().add(BorderLayout.NORTH, mb);

		JPanel patientInformationPanel = patientInformationPanel();
		constraints.insets = new Insets(5, 0, 5, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
		content.add(patientInformationPanel,constraints);

		JPanel secondPanel = firstVisitOnlyInformationPanel();
		constraints.insets = new Insets(5, 0, 5, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;
		content.add(secondPanel,constraints);

		JPanel thirdPanel = editAnyVisitPanel();
		constraints.insets = new Insets(5, 0, 5, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 2;
		content.add(thirdPanel, constraints);


		JPanel fifthPanel = commentsAndRemarksPanel();
		constraints.insets = new Insets(5, 0, 5, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 3;
		content.add(fifthPanel, constraints);


		constraints.insets = new Insets(0, 0, 5, 0);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 4;
		content.add(saveInitial, constraints);

		mainPanel.add(content);
		this.setVisible(true);
    }

	private JPanel patientInformationPanel(){
		JPanel generalInfoPanel = new JPanel();

		GridBagLayout gblPatientInfoPanel = new GridBagLayout();
        gblPatientInfoPanel.columnWidths = new int[] {100, 0};
        gblPatientInfoPanel.rowHeights = new int[]{0, 0, 0};
        gblPatientInfoPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblPatientInfoPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        generalInfoPanel.setLayout(gblPatientInfoPanel);

		GridBagConstraints constraints = new GridBagConstraints();

		JPanel facilityI = textField("FACILITY:", facility, "facility");
        constraints.insets = new Insets(0, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		generalInfoPanel.add(facilityI, constraints);

		JPanel subDistrict = textField("SUBDISTRICT:", subdistrict, "subdistrict");
        constraints.insets = new Insets(0, 0, 5, 0);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 2;
        constraints.gridy = 0;
		generalInfoPanel.add(subDistrict, constraints);

		/////////////////////////////////////////////////////////

		JPanel patientName = textField("NAME:", name, "name");
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
        constraints.gridy = 1;
		generalInfoPanel.add(patientName, constraints);

		JPanel addressjPanel = textField("ADDRESS: ", address, "address");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridx = 1;
		constraints.gridy = 1;
		generalInfoPanel.add(addressjPanel, constraints);

		JPanel agejJPanel = textField("AGE: ", age, "age");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 2;
		constraints.gridy = 1;
		generalInfoPanel.add(agejJPanel, constraints);

		////////////////////////////////////////

		JPanel regNumber = textField("REGISTRATION #:", registration, "registration");
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 0;
		constraints.gridy = 2;
		generalInfoPanel.add(regNumber, constraints);

		JPanel hInI = textField("HI/NI #:", hiNiNum, "hiNiNum");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 2;
		constraints.gridy = 2;
		generalInfoPanel.add(hInI, constraints);

		return generalInfoPanel;
	}

	private JPanel firstVisitOnlyInformationPanel(){
		JPanel secondPanel = new JPanel();

		GridBagLayout gblFirstVisitOnly = new GridBagLayout();
        gblFirstVisitOnly.columnWidths = new int[] {100, 0};
        gblFirstVisitOnly.rowHeights = new int[]{0, 0, 0};
        gblFirstVisitOnly.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblFirstVisitOnly.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        secondPanel.setLayout(gblFirstVisitOnly);

		GridBagConstraints constraints = new GridBagConstraints();

		JPanel sicklingP = sicklingPanel();
		constraints.insets = new Insets(0, 5, 0, 0);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		secondPanel.add(sicklingP, constraints);

		JPanel partiy = parityPanel();
		constraints.insets = new Insets(0, 10, 0, 10); //up,
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 0;
		secondPanel.add(partiy, constraints);

		JPanel pmtct = pmtctPanel();
		constraints.insets = new Insets(5, 0, 5, 5);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 2;
        constraints.gridy = 0;
		secondPanel.add(pmtct, constraints);

		expectedDayOfDelivery = new JTextField("",4);
		expectedDayOfDelivery.setName("expectedDayOfDelivery");

		hemoglobinReg = new JTextField("",5);
		hemoglobinReg.setName("hemoglobinReg");

		JPanel firstVisitSubPanel = stackPanels(
			textField(
				"Expected Day of Delivery:",
				expectedDayOfDelivery,
					"expectedDayOfDelivery"
				),
			textField(
				"Hemoglobin at Registration:",
				hemoglobinReg,
					"hemoglobinReg"
			)
		);
		constraints.insets = new Insets(0, 0, 5, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;
		secondPanel.add(firstVisitSubPanel,constraints);

		VLDR = new JCheckBox("VLDR Reactive");
		VLDR.setName("VLDR");

		maleInvolvement = new JCheckBox("Male involvement");
		maleInvolvement.setName("maleInvolvement");

		JPanel firstVisitSubPanel1 =stackPanels(VLDR, maleInvolvement);

		constraints.insets = new Insets(0, 5, 5, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 1;
		secondPanel.add(firstVisitSubPanel1, constraints);


		bloodTypeBox = new JComboBox(bloodTypes);
		bloodTypeBox.setName("bloodGroup");
		//TODO add a title for this box

		// bloodGroup = new JTextField("", 3);
		// bloodGroup.setName("bloodGroup");
		//JPanel bloodGroupJPanel = //textField("Blood Group:", bloodGroup, "bloodGroup");
		constraints.insets = new Insets(5, 0, 5, 5);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridx = 2;
		constraints.gridy = 1;
		secondPanel.add(bloodTypeBox, constraints);

		return secondPanel;
	}

	private JPanel editAnyVisitPanel(){
		JPanel anyVisitPanel = new JPanel();

		GridBagLayout gblEditAnyVisitPanel = new GridBagLayout();
        gblEditAnyVisitPanel.columnWidths = new int[] {100, 0};
        gblEditAnyVisitPanel.rowHeights = new int[]{0, 0, 0};
        gblEditAnyVisitPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblEditAnyVisitPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        anyVisitPanel.setLayout(gblEditAnyVisitPanel);

		GridBagConstraints constraints = new GridBagConstraints();

		//TODO disable this button if it is the first visit or if it has already been presses
		constraints.insets = new Insets(5, 5, 0, 0);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		anyVisitPanel.add(newVisit, constraints);

		JPanel sub = editAnyTimeSubPanel();
		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		anyVisitPanel.add(sub,constraints);

		JPanel editAnyVisitSubPanel = selectVisitJPanel();
		constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;
		anyVisitPanel.add(editAnyVisitSubPanel, constraints);

		JPanel dosePanel = doseJPanel();
		constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 1;
		anyVisitPanel.add(dosePanel, constraints);
		return anyVisitPanel;
	}

	private JPanel commentsAndRemarksPanel(){
		JPanel fifthPanel = new JPanel();

		GridBagLayout gblCommentAndRemarksPanel = new GridBagLayout();
        gblCommentAndRemarksPanel.columnWidths = new int[] {100, 0};
        gblCommentAndRemarksPanel.rowHeights = new int[]{0, 0, 0};
        gblCommentAndRemarksPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblCommentAndRemarksPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        fifthPanel.setLayout(gblCommentAndRemarksPanel);

		GridBagConstraints constraints = new GridBagConstraints();

		JLabel title = new JLabel("Comments:");
		constraints.insets = new Insets(10, 10,0, 0);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		fifthPanel.add(title,constraints);

		complaints = new JTextArea("");
		complaints.setName("complaints");
		complaints.setColumns(20);
		complaints.setRows(5);
		complaints.setWrapStyleWord(true);
		constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 1;
		fifthPanel.add(complaints,constraints);

		JLabel title1 = new JLabel("Remarks:");
		constraints.insets = new Insets(10, 10, 0, 0);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
		fifthPanel.add(title1,constraints);

		remarks = new JTextArea("");
		remarks.setName("remarks");
		remarks.setColumns(20);
		remarks.setRows(5);
		remarks.setWrapStyleWord(true);
		constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 1;
		fifthPanel.add(remarks,constraints);

		return fifthPanel;
	}

	private JPanel sicklingPanel(){
		JPanel sicklingPanel = new JPanel();

		sicklingPanel.setBorder(BorderFactory.createTitledBorder("Sickling"));

		GridBagLayout gblSickling = new GridBagLayout();
        gblSickling.columnWidths = new int[] {100, 0};
        gblSickling.rowHeights = new int[]{0, 0, 0};
        gblSickling.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblSickling.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        sicklingPanel.setLayout(gblSickling);

		GridBagConstraints constraints = new GridBagConstraints();

		sickling = new JCheckBox("Positive");
		sickling.setName("sickling");

		sicklingType = new JTextField("",3);
		sicklingType.setName("sicklingType");

		//TODO change to box

		constraints.insets = new Insets(0, 0, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		sicklingPanel.add(sickling, constraints);

		
		sicklingTypeBox = new JComboBox(sicklingTypes);
		sicklingTypeBox.setName("sicklingType");
		// JPanel sicklingTypeJPanle = textField("Sickling Type", sicklingType, "sicklingType");
		constraints.insets = new Insets(0, 0, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 0;
		sicklingPanel.add(sicklingTypeBox, constraints);


		return sicklingPanel;
	}

	private JPanel parityPanel(){
		JPanel parityPanel = new JPanel();

		parityPanel.setBorder(BorderFactory.createTitledBorder("Parity"));

		GridBagLayout gblParity = new GridBagLayout();
        gblParity.columnWidths = new int[] {100, 0};
        gblParity.rowHeights = new int[]{0, 0, 0};
        gblParity.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblParity.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        parityPanel.setLayout(gblParity);

		gravidity  = new JTextField("",3);
		gravidity.setName("gravidity");

		parity = new JTextField("", 3);
		parity.setName("parity");

		GridBagConstraints constraints = new GridBagConstraints();
		JPanel pPanel = textField("Parity", parity, "parity");
		JPanel gPanel = textField("Gravidity", gravidity, "gravidity");

		JPanel parityP = stackPanels(pPanel, gPanel);

		constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 1;
		parityPanel.add(parityP, constraints);

		return parityPanel;

	}

	private JPanel pmtctPanel(){
		JPanel pmtctPanel = new JPanel();

		pmtctPanel.setBorder(BorderFactory.createTitledBorder("PMTCT"));

		pmtctPreTestCounseling = new JCheckBox("Pre-test Counseling");
		pmtctPreTestCounseling.setName("pmtctPreTestCounseling");

		pmtctTestResult = new JCheckBox("Test Positive");
		pmtctTestResult.setName("pmtctTestResult");

		pmtctPostTestCounseling = new JCheckBox("Post Test Counseling");
		pmtctPostTestCounseling.setName("pmtctPostTestCounseling");

		pmtctARV = new JCheckBox("ARV");
		pmtctARV.setName("pmtctARV");

		GridBagLayout gblPmtctPanel = new GridBagLayout();
        gblPmtctPanel.columnWidths = new int[] {100, 0};
        gblPmtctPanel.rowHeights = new int[]{0, 0, 0};
        gblPmtctPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblPmtctPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        pmtctPanel.setLayout(gblPmtctPanel);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 1;
		pmtctPanel.add(pmtctPreTestCounseling, constraints);

		constraints.insets = new Insets(0, 0, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 2;
		pmtctPanel.add(pmtctTestResult, constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 1;
        constraints.gridy = 1;
		pmtctPanel.add(pmtctPostTestCounseling, constraints);

		constraints.insets = new Insets(0, 0, 5, 0);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 1;
        constraints.gridy = 2;
		pmtctPanel.add(pmtctARV, constraints);

		return pmtctPanel;
	}

	private JPanel editAnyTimeSubPanel(){
		JPanel editAnyTimeSubPanel = new JPanel();

		GridBagLayout gblEditAnyTimeSubPanel = new GridBagLayout();
        gblEditAnyTimeSubPanel.columnWidths = new int[] {100, 0};
        gblEditAnyTimeSubPanel.rowHeights = new int[]{0, 0, 0};
        gblEditAnyTimeSubPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblEditAnyTimeSubPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        editAnyTimeSubPanel.setLayout(gblEditAnyTimeSubPanel);

		GridBagConstraints constraints = new GridBagConstraints();

		hemoglobin36Wks = new JTextField("", 5);
		hemoglobin36Wks.setName("hemoglobin36Wks");
		itnUse = new JCheckBox("ITN in use");
		itnUse.setName("itnUse");

		JPanel editAnyVisitSubPanel = stackPanels(
			itnUse,
			textField(
				"Hemoglobin at 36 weeks:",
				hemoglobin36Wks,
				"hemoglobin36Wks"
			)
		);
		constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
		editAnyTimeSubPanel.add(editAnyVisitSubPanel, constraints);

		return editAnyTimeSubPanel;
	}

	private JPanel selectVisitJPanel(){
		JPanel selectVisit = new JPanel();

		selectVisit.setBorder(BorderFactory.createTitledBorder("Visit"));

		selectVisit.add(selectVisitTab);

		return selectVisit;
	}

	public PatientPregnancyMessage initialVisit(){
	 	PatientPregnancyMessage validate = new PatientPregnancyMessage();
	 	validate.setGravidity(gravidity.getText());
	 	validate.setParity(parity.getText());
	 	validate.setBloodGroup((String)bloodTypeBox.getSelectedItem()); //this needs to fixed
	 	validate.setExpectedDayOfDelivery(expectedDayOfDelivery.getText());
	 	validate.setHemoglobinReg(hemoglobinReg.getText());
	 	validate.setSickling(sickling.isSelected());
	 	validate.setSicklingType(sicklingType.getText());
	 	return validate;
	 }

	public SubsequentVisitMessage subsequentVisit(int visitIndex){
		SubsequentVisitMessage validate = new SubsequentVisitMessage();
		JTextField systolic = (JTextField)getComponentByName("systolic"+String.valueOf(visitIndex));
		JTextField diastolic = (JTextField)getComponentByName("diastolic"+String.valueOf(visitIndex));
		JTextField weight = (JTextField)getComponentByName("weight"+String.valueOf(visitIndex));
		JTextField patientHeight = (JTextField)getComponentByName("patientHeight"+String.valueOf(visitIndex));
		JTextField fundalHeight = (JTextField)getComponentByName("fundalHeight"+String.valueOf(visitIndex));
		JTextField date = (JTextField)getComponentByName("date"+String.valueOf(visitIndex));
		JTextField gestation = (JTextField)getComponentByName("gestation"+String.valueOf(visitIndex));
		JCheckBox bloodFilm = (JCheckBox)getComponentByName("bloodFilm"+String.valueOf(visitIndex));

		//TODO add a bunch of catches for null arguments
		validate.setTtFirstDose(ttFirstDose.isSelected());
		validate.setTtSecondDose(ttSecondDose.isSelected());
		validate.setTtThirdDose(ttThirdDose.isSelected());
		validate.setTtProtectedNotDosed(ttProtectedNotDosed.isSelected());
		validate.setIptFirstDose(iptFirstDose.isSelected());
		validate.setIptSecondDose(iptSecondDose.isSelected());
		validate.setIptThirdDose(iptThirdDose.isSelected());

		validate.setSystolic(systolic==null?"":systolic.getText());
		validate.setDiastolic(diastolic==null?"":diastolic.getText());
		validate.setWeight(weight==null?"":weight.getText());
		validate.setFundalHeight(fundalHeight==null?"":fundalHeight.getText());
		validate.setBloodFilm(bloodFilm!= null && bloodFilm.isSelected());
		validate.setGestationWeek(gestation==null?"":gestation.getText());
		validate.setPatientHeight(patientHeight==null?"":patientHeight.getText());
		validate.setDate(date==null?"":date.getText());
		validate.setHemoglobin36Wks(hemoglobin36Wks.getText());
		validate.setComplaints(complaints.getText());
		validate.setRemarks(remarks.getText());
		validate.setPmtctPreTestCounseling(pmtctPreTestCounseling.isSelected());
		validate.setPmtctTestResult(pmtctTestResult.isSelected());
		validate.setPmtctPostTestCounseling(pmtctPostTestCounseling.isSelected());
		validate.setPmtctARV(pmtctARV.isSelected());
//		validate.setRefurred(); 	// No info on refurred
		return validate;
	}

	public void constructVisit(SubsequentVisitMessage msg, int visitIndex){
		JPanel constructVisitPanel = new JPanel();

		GridBagLayout gblConstructVisitPanel = new GridBagLayout();
        gblConstructVisitPanel.columnWidths = new int[] {100, 0};
        gblConstructVisitPanel.rowHeights = new int[]{0, 0, 0};
        gblConstructVisitPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblConstructVisitPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        constructVisitPanel.setLayout(gblConstructVisitPanel);

		JTextField systolic = new JTextField(4);
		systolic.setName("systolic"+ String.valueOf(visitIndex));
		systolic.setText(msg.getSystolic());

		JTextField diastolic = new JTextField(4);
		diastolic.setName("diastolic"+ String.valueOf(visitIndex));
		diastolic.setText(msg.getDiastolic());

		JTextField patientHeight = new JTextField(4);
		patientHeight.setName("patientHeight"+ String.valueOf(visitIndex));
		patientHeight.setText(msg.getPatientHeight());

		JTextField patientWeight = new JTextField(4);
		patientWeight.setName("weight"+ String.valueOf(visitIndex));
		patientWeight.setText(msg.getWeight());

		JTextField gestation = new JTextField(4);
		gestation.setName("gestation"+ String.valueOf(visitIndex));
		gestation.setText(msg.getGestationWeek());

		JTextField fundalHeight = new JTextField(4);
		fundalHeight.setName("fundalHeight"+ String.valueOf(visitIndex));
		fundalHeight.setText(msg.getFundalHeight());

		JTextField date = new JTextField();
		date.setName("date"+String.valueOf(visitIndex));
		date.setText(msg.getDate());

		JCheckBox bloodFilm = new JCheckBox("Blood Film");
		bloodFilm.setName("bloodFilm"+String.valueOf(visitIndex));
		bloodFilm.setSelected(msg.getBloodFilm());


		GridBagConstraints constraints = new GridBagConstraints();

		//row 1
		JPanel dateJ = indexedTextField("Date :", date, "gestationWeek", visitIndex);
		date.setEnabled(false);
		constraints.insets = new Insets(2, 2, 0, 0);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		constructVisitPanel.add(dateJ,constraints);

		//row 2

		JPanel weightPanel = indexedTextField("Weight(kg)", patientWeight, "weight", visitIndex);
		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;
		constructVisitPanel.add(weightPanel,constraints);

		JPanel heightPanel = indexedTextField("Height(cm)", patientHeight, "patientHeight", visitIndex);
		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 1;
		constructVisitPanel.add(heightPanel,constraints);


		JPanel fundalHeightPanel = indexedTextField("Fundal Height(cm)", fundalHeight, "fundalHeight", visitIndex);
		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 2;
        constraints.gridy = 1;
		constructVisitPanel.add(fundalHeightPanel,constraints);

		// row 3

		JPanel gestationPanel = indexedTextField("Gestation Week", gestation, "gestationWeek", visitIndex);
		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 2;
		constructVisitPanel.add(gestationPanel,constraints);

		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 2;
		constructVisitPanel.add(bloodFilm,constraints);

		JPanel bloodPressure = bloodPJPanel(systolic, diastolic, visitIndex);

		constraints.insets = new Insets(0, 0, 0, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 2;
        constraints.gridy = 2;
		constructVisitPanel.add(bloodPressure,constraints);


		selectVisitTab.add(constructVisitPanel, String.valueOf(visitIndex));
	}

	private JPanel ttJPanel() {
		JPanel ttPanel = new JPanel();
		ttPanel.setLayout(new GridLayout(0,1)); // 0 lets me do as many as wanted, could be a 5 though

		ttPanel.setBorder(BorderFactory.createTitledBorder("TT Doses"));
		
		ttProtectedNotDosed = new JCheckBox("Protected");
		ttProtectedNotDosed.setName("ttProtectedNotDosed");

		ttFirstDose = new JCheckBox("First Dose");
		ttFirstDose.setName("ttFirstDose");

		ttSecondDose = new JCheckBox("Second Dose");
		ttSecondDose.setName("ttSecondDose");

		ttThirdDose = new JCheckBox("Third Dose");
		ttThirdDose.setName("ttThirdDose");

		ttPanel.add(ttProtectedNotDosed);
		ttPanel.add(ttFirstDose);
		ttPanel.add(ttSecondDose);
		ttPanel.add(ttThirdDose);

		//should this go in controller?
		if(ttProtectedNotDosed.isSelected()){
			ttPanel.setEnabled(false);
			ttPanel.setEnabled(false);
			ttPanel.setEnabled(false);
		}
		return ttPanel;
	}

	private JPanel iptJPanel() {
		JPanel iptPanel = new JPanel();
		iptPanel.setLayout(new GridLayout(0,1));

		iptPanel.setBorder(BorderFactory.createTitledBorder("ITP Doses"));


		iptFirstDose = new JCheckBox("First Dose");
		iptFirstDose.setName("iptFirstDose");

		iptSecondDose = new JCheckBox("Second Dose");
		iptSecondDose.setName("iptSecondDose");

		iptThirdDose = new JCheckBox("Third Dose");
		iptThirdDose.setName("iptThirdDose");

		iptPanel.add(iptFirstDose);
		iptPanel.add(iptSecondDose);
		iptPanel.add(iptThirdDose);

		return iptPanel;
	}

	private JPanel bloodPJPanel(JTextField systolic, JTextField diastolic, int index){
		JPanel bloodPanel = new JPanel();

		bloodPanel.setBorder(BorderFactory.createTitledBorder("Blood Pressure"));

		GridBagLayout gblBloodPanel = new GridBagLayout();
        gblBloodPanel.columnWidths = new int[] {100, 0};
        gblBloodPanel.rowHeights = new int[]{0, 0, 0};
        gblBloodPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblBloodPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        bloodPanel.setLayout(gblBloodPanel);

		GridBagConstraints constraints = new GridBagConstraints();


		JPanel sys = indexedTextField("Systolic: ", systolic, "systolic", index);
		JPanel dia = indexedTextField("Diastolic: ", diastolic, "diastolic", index);

		JPanel bloodSubPanel = stackPanels(sys, dia);
		constraints.insets = new Insets(0, 0, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
		bloodPanel.add(bloodSubPanel,constraints);

		return bloodPanel;
	}

	/* Methods that create a component with some values set */

	/**
	 * Creates a JPanel holding a JLabel associated with a JTextField
	 * @param labelIn Text for the label
	 * @param jField the JField the label is tied to
	 * @return the JPanel with a text field and label
	 */
	private JPanel textField(String labelIn, JTextField jField, String compName){
		JPanel panel= new JPanel();
		JLabel label = new JLabel(labelIn);
		label.setName(compName + "Label");
		panel.add(label);
		panel.add(jField);
		return panel;
	}

	/**
	 * Creates a JPanel holding a JLabel associated with a JTextField
	 * @param labelIn Text for the label
	 * @param jField the JField the label is tied to
	 * @return the JPanel with a text field and label
	 */
	private JPanel indexedTextField(String labelIn, JTextField jField, String compName, int index){
		JPanel panel= new JPanel();
		JLabel label = new JLabel(labelIn);
		label.setName(compName + index + "Label");
		panel.add(label);
		panel.add(jField);
		return panel;
	}

	private JPanel stackPanels(Component p1, Component p2){
		JPanel contentPanel = new JPanel();

		GridBagLayout gblContentPane = new GridBagLayout();
        gblContentPane.columnWidths = new int[] {100, 0};
        gblContentPane.rowHeights = new int[]{0, 0, 0};
        gblContentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblContentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        contentPanel.setLayout(gblContentPane);

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.gridy = 0;
		contentPanel.add(p1,constraints);

		constraints.gridy = 1;
		contentPanel.add(p2,constraints);

		return contentPanel;
	}


	private JPanel doseJPanel(){
		JPanel sub = new JPanel();

		GridBagLayout gblDosePanel = new GridBagLayout();
        gblDosePanel.columnWidths = new int[] {100, 0};
        gblDosePanel.rowHeights = new int[]{0, 0, 0};
        gblDosePanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gblDosePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        sub.setLayout(gblDosePanel);

		GridBagConstraints constraints = new GridBagConstraints();

		JPanel ttDoses = ttJPanel();
		constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
		sub.add(ttDoses,constraints);

		JPanel iptDose = iptJPanel();
		constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 0;
		sub.add(iptDose,constraints);

		return sub;
	}

	//Action listeners

	//TODO rename these
    public void addVisitSubmissionListener(ActionListener actionListener) {
        saveInitial.addActionListener(actionListener);
        saveInitial.setActionCommand("initial");
    }

	public void markAsSubsequentVisit() {
		saveInitial.setActionCommand("subsequent");
	}

	public void addSubsequentVisitListener(ActionListener actionListener) {
		newVisit.addActionListener(actionListener);
	}


	public void createComponentMap() {
		componentMap = new HashMap<String, Component>();
		List<Component> components = getAllComponents(this);
		for (Component component : components) {
			componentMap.put(component.getName(), component);
		}
	}

	private Component getComponentByName(String name){
		if (componentMap.containsKey(name)) {
			return componentMap.get(name);
		}
		else return null;
	}

	private List<Component> getAllComponents(Container c) {
		Component[] components = c.getComponents();

		List<Component> compList = new ArrayList<Component>();
		for (Component component : components) {
			compList.add(component);
			if (component instanceof Container) {
				compList.addAll(getAllComponents((Container) component));
			}
		}
		return compList;
	}

	/**
	 * Removal of component names broke the validation
	 * @param controllerToInterface Object containing the map we
	 * @param currentVisitIndex index of the visit currently being validated
	 */
	public void displayValidationErrors(ControllerToInterface controllerToInterface, int currentVisitIndex) {
		clearValidationErrors();
		if (!controllerToInterface.isAllFieldsValid()) {
			controllerToInterface.getErrorMessages().entrySet().stream().forEach(e -> {
				if (componentMap.containsKey(e.getKey())) {
					JComponent jcomp = (JComponent) componentMap.get(e.getKey());
					jcomp.setToolTipText(e.getValue().getMessage());
				}
				if (componentMap.containsKey(e.getKey() + currentVisitIndex)) {
					JComponent jVisitComp = (JComponent) componentMap.get(e.getKey() + currentVisitIndex);
					jVisitComp.setToolTipText(e.getValue().getMessage());
				}
				if (componentMap.get(e.getKey()+"Label") != null) {
					JLabel jcompLabel = (JLabel) componentMap.get(e.getKey() + "Label");
					jcompLabel.setText(jcompLabel.getText() + "*");
					jcompLabel.setForeground(Color.red);
				}
				if (componentMap.get(e.getKey() + currentVisitIndex + "Label") != null) {
					JLabel jcompLabel = (JLabel) componentMap.get(e.getKey() + currentVisitIndex + "Label");
					jcompLabel.setText(jcompLabel.getText() + "*");
					jcompLabel.setForeground(Color.red);
				}
			});
		}
	}

	public void clearValidationErrors() {
		for (Map.Entry<String, Component> entry : componentMap.entrySet()) {
			if (entry.getValue() instanceof JLabel) {
				JLabel jcompLabel = (JLabel) entry.getValue();
				if (jcompLabel.getText().charAt(jcompLabel.getText().length()-1) == '*') {
					jcompLabel.setText(jcompLabel.getText().substring(0, jcompLabel.getText().length()-1));
					jcompLabel.setForeground(Color.black);
				}
			}
			if (entry.getValue() instanceof JComponent) {
				((JComponent) entry.getValue()).setToolTipText("");
			}
		}
	}

	public void disableNewVisit(){
		newVisit.setEnabled(false);
	}

	//MISC

	private String getAge(String birthdate){
		LocalDate appointmentDate = LocalDate.now();
		//TODO IF THE DATE COMES IN INCORRECTLY FORMATTED IT WILL CRASH FIX IT
		LocalDate dBirthDate = LocalDate.parse(birthdate, dateTimeFormatter);

		int years = Period.between(dBirthDate, appointmentDate).getYears();

		return String.valueOf(years);
	}

	public void resetPregnancyGUI(){
		mainPanel.remove(content);
		newVisit.setEnabled(true);
		selectVisitTab.removeAll();
		revalidate();
		repaint();
	}

}