package edu.usm.cos420.antenatal.dao.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.usm.cos420.antenatal.model.BloodPressure;
import edu.usm.cos420.antenatal.model.IPT;
import edu.usm.cos420.antenatal.model.PMTCT;
import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.model.Sickling;
import edu.usm.cos420.antenatal.model.TT;
import edu.usm.cos420.antenatal.model.Visit;

public class PatientWrapperSQLDao {
	private String dbUrl;

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private static final String CREATE_PATIENTS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS patients (id SERIAL PRIMARY KEY, nhisid VARCHAR(255), firstName VARCHAR(255), lastName VARCHAR(255), address VARCHAR(255),"
			+ " phone VARCHAR(255), sex VARCHAR(1), guardians VARCHAR(255), dependents VARCHAR(255), birthdate VARCHAR(255))";

	private static final String CREATE_PATIENTWRAPPERS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS patientWrappers (id SERIAL PRIMARY KEY, patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE)";

	private static final String CREATE_PREGNANCY_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS pregnancies (id SERIAL PRIMARY KEY, expectedDateOfDelivery VARCHAR(512), "
			+ "hemoglobinReg VARCHAR(255), hemoglobin36Wks VARCHAR(255), urineTest INTEGER, bloodType VARCHAR(255), vdrl BOOLEAN, itnUse BOOLEAN,"
			+ "complaints VARCHAR(8000), remarks VARCHAR(8000), maleInvolvement BOOLEAN, "
			+ " itpFirstDose BOOLEAN, itpSecondDose BOOLEAN, itpThirdDose BOOLEAN, parity INTEGER, gravidity INTEGER, preTestCounseling BOOLEAN, testResult BOOLEAN, postTestCounseling BOOLEAN, arv BOOLEAN, "
			+ " sickling BOOLEAN, type VARCHAR(255), ttFirstDose BOOLEAN, ttSecondDose BOOLEAN, ttThirdDose BOOLEAN, protected BOOLEAN, wrapper_id INTEGER REFERENCES patientWrappers(id))";

	private static final String CREATE_VISITS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS visits (date VARCHAR(255), systolic INTEGER, diastolic INTEGER, weight INTEGER, patientHeight INTEGER,"
			+ " fundalHeight INTEGER, gestationWeek INTEGER, bloodFilm BOOLEAN, refurred VARCHAR(8000), preg_id INTEGER REFERENCES pregnancies(id))";

	private static final String CREATE_PATIENT_QUERY = "INSERT INTO patients (id, nhisid, firstName, lastName, address,"
			+ " phone, sex, guardians, dependents, birthdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String CREATE_PATIENTWRAPPERS_QUERY = "INSERT INTO patientWrappers (id, patient_id) VALUES (?, ?)";

	private static final String CREATE_PREGNANCY_QUERY = "INSERT INTO pregnancies (id, expectedDateOfDelivery, "
			+ "hemoglobinReg, hemoglobin36Wks, urineTest, bloodType, vdrl, itnUse,"
			+ "complaints, remarks, maleInvolvement, "
			+ " itpFirstDose, itpSecondDose, itpThirdDose, parity, gravidity, preTestCounseling, testResult, postTestCounseling, arv, "
			+ " sickling, type, ttFirstDose, ttSecondDose, ttThirdDose, protected, wrapper_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String CREATE_VISIT_QUERY = "INSERT INTO visits (date, systolic, diastolic, weight, patientHeight,"
			+ " fundalHeight, gestationWeek, bloodFilm, refurred, preg_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String GET_PATIENT_WRAPPER_QUERY = "SELECT * FROM patientWrappers WHERE id = ?";
	private static final String GET_PATIENT_QUERY = "SELECT * FROM patients WHERE id = ?";
	private static final String GET_PREGNANCY_QUERY = "SELECT * FROM pregnancies WHERE wrapper_id = ?";
	private static final String GET_VISIT_QUERY = "SELECT * FROM visits WHERE preg_id = ?";

	private static final String DELETE_PATIENT_WRAPPER_QUERY = "DELETE FROM patientWrappers WHERE id = ?";
	private static final String DELETE_PATIENT_QUERY = "DELETE FROM patients WHERE id = ?";
	private static final String DELETE_PREGNANCY_QUERY = "DELETE FROM pregnancies WHERE wrapper_id = ?";
	private static final String DELETE_VISIT_QUERY = "DELETE FROM visits WHERE preg_id = ?";

	private static final String GET_ALL_WRAPPERS = "SELECT * FROM patientWrappers";


	public PatientWrapperSQLDao(String dbUrl) throws SQLException {
		this.dbUrl = dbUrl;
		this.createTablesIfDontExist();
	}

	private void createTablesIfDontExist() throws SQLException {
		try (Connection conn = DriverManager.getConnection(this.dbUrl)) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(CREATE_PATIENTS_TABLE_QUERY);
			stmt.executeUpdate(CREATE_PATIENTWRAPPERS_TABLE_QUERY);
			stmt.executeUpdate(CREATE_PREGNANCY_TABLE_QUERY);
			stmt.executeUpdate(CREATE_VISITS_TABLE_QUERY);

			if (conn != null)
				conn.close();
		}
	}

	public void createPatientWrapper(PatientWrapper wrapper) throws SQLException {
		PreparedStatement createVisitStmt;
		PreparedStatement createPregnancyStmt;
		PreparedStatement createPatientStmt;
		PreparedStatement createPatientWrapperStmt;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(this.dbUrl);
			createPatientStmt = conn.prepareStatement(CREATE_PATIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			createPatientWrapperStmt = conn.prepareStatement(CREATE_PATIENTWRAPPERS_QUERY, Statement.RETURN_GENERATED_KEYS);
			encodePatient(createPatientStmt,wrapper.getPatient());
			encodePatientWrapper(createPatientWrapperStmt,wrapper);
			createPatientStmt.executeUpdate();
			createPatientWrapperStmt.executeUpdate();
			for (Pregnancy pregnancy: wrapper.getPregnancies()) {
				createPregnancyStmt = conn.prepareStatement(CREATE_PREGNANCY_QUERY,Statement.RETURN_GENERATED_KEYS);
				encodePregnancy(createPregnancyStmt, pregnancy, wrapper);
				createPregnancyStmt.executeUpdate();

				for (Visit v: pregnancy.getSubsequentVisits()) {
					createVisitStmt = conn.prepareStatement(CREATE_VISIT_QUERY,Statement.RETURN_GENERATED_KEYS);
					encodeVisit(createVisitStmt, v, pregnancy);
					createVisitStmt.executeUpdate();
				}
			}
			try (ResultSet keys = createPatientStmt.getGeneratedKeys()) {}
			conn.close();
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private void encodePatient(PreparedStatement createPatientStatement, Patient patient) throws SQLException {
		int i = 0;
		createPatientStatement.setLong(++i, patient.getId());
		createPatientStatement.setString(++i, patient.getNhisId() == null ? ""+0 : ""+patient.getNhisId());
		createPatientStatement.setString(++i, (patient.getFirstName() == null ? "" : patient.getFirstName()));
		createPatientStatement.setString(++i, patient.getLastName() == null ? "" : patient.getLastName());
		createPatientStatement.setString(++i, patient.getAddress() == null ? "" : patient.getAddress());
		createPatientStatement.setString(++i, "" + (patient.getPhoneNumber())); // phone number should be a string ideally
		createPatientStatement.setString(++i, patient.getSex() == '\u0000' ? "" : String.valueOf(patient.getSex()));
		// we're not using guardians or dependents; leaving null
		createPatientStatement.setString(++i, "");
		createPatientStatement.setString(++i, "");
		createPatientStatement.setString(++i, patient.getBirthdate());
	}

	private void encodePatientWrapper(PreparedStatement createPatientWrapperStatement, PatientWrapper pw)
			throws SQLException {
		createPatientWrapperStatement.setLong(1, pw.getId());
		createPatientWrapperStatement.setLong(2, pw.getPatient().getId());
	}

	private void encodePregnancy(PreparedStatement createPregnancyVisit, Pregnancy pregnancy, PatientWrapper patientWrapper) throws SQLException {
		int i = 0;
		createPregnancyVisit.setLong(++i, pregnancy.getId());
		createPregnancyVisit.setString(++i, dateTimeFormatter.format(pregnancy.getExpectedDayOfDelivery()));
		createPregnancyVisit.setString(++i, "" + pregnancy.getHemoglobinLevelAtReg());
		createPregnancyVisit.setString(++i, "" + pregnancy.getHemoglobinLevelAt36Wks());
		createPregnancyVisit.setInt(++i, pregnancy.getUrineTestResult());
		createPregnancyVisit.setString(++i, pregnancy.getBloodGroup());
		createPregnancyVisit.setBoolean(++i, pregnancy.getVDRL());
		createPregnancyVisit.setBoolean(++i, pregnancy.getItnUse());
		createPregnancyVisit.setString(++i, pregnancy.getComplaints());
		createPregnancyVisit.setString(++i, pregnancy.getRemarks());
		createPregnancyVisit.setBoolean(++i, pregnancy.getMaleInvolvement());
		createPregnancyVisit.setBoolean(++i, pregnancy.getIpt().getFirstDose());
		createPregnancyVisit.setBoolean(++i, pregnancy.getIpt().getSecondDose());
		createPregnancyVisit.setBoolean(++i, pregnancy.getIpt().getThirdDose());
		createPregnancyVisit.setInt(++i, pregnancy.getParity().getParity());
		createPregnancyVisit.setInt(++i, pregnancy.getParity().getGravidity());
		createPregnancyVisit.setBoolean(++i, pregnancy.getPmtct().getPreTestCounseling());
		createPregnancyVisit.setBoolean(++i, pregnancy.getPmtct().getTestResult());
		createPregnancyVisit.setBoolean(++i, pregnancy.getPmtct().getPostTestCounseling());
		createPregnancyVisit.setBoolean(++i, pregnancy.getPmtct().getARV());
		createPregnancyVisit.setBoolean(++i, pregnancy.getSickling().getSickling());
		createPregnancyVisit.setString(++i, pregnancy.getSickling().getType());
		createPregnancyVisit.setBoolean(++i, pregnancy.getTt().getFirstDose());
		createPregnancyVisit.setBoolean(++i, pregnancy.getTt().getSecondDose());
		createPregnancyVisit.setBoolean(++i, pregnancy.getTt().getThirdDose());
		createPregnancyVisit.setBoolean(++i, pregnancy.getTt().getProtectedNotDosed());
		createPregnancyVisit.setLong(++i, patientWrapper.getId());
	}

	private void encodeVisit(PreparedStatement createVisitStatement, Visit visit, Pregnancy pregnancy)
			throws SQLException {
		Long pregid = pregnancy.getId();
		int i = 0;
		createVisitStatement.setString(++i, dateTimeFormatter.format(visit.getDate()));
		createVisitStatement.setInt(++i, visit.getBloodPressure().getSystolic());
		createVisitStatement.setInt(++i, visit.getBloodPressure().getDiastolic());
		createVisitStatement.setInt(++i, visit.getWeight());
		createVisitStatement.setInt(++i, visit.getPatientHeight());
		createVisitStatement.setInt(++i, visit.getFundalHeight());
		createVisitStatement.setInt(++i, visit.getGestationWeek());
		createVisitStatement.setBoolean(++i, visit.getBloodFilm());
		createVisitStatement.setString(++i, visit.getRefurred());
		createVisitStatement.setLong(++i, pregid);
	}

	public PatientWrapper readPatientWrapper(Long id) throws SQLException {
		PatientWrapper toReturn = new PatientWrapper(new Patient(999l), new ArrayList<Pregnancy>());
		ArrayList<Pregnancy> pregnancies = new ArrayList<Pregnancy>();
		Long patientID;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(this.dbUrl);
			PreparedStatement readPatientWrapperStmt = conn.prepareStatement(GET_PATIENT_WRAPPER_QUERY);
			readPatientWrapperStmt.setLong(1, id);
			try (ResultSet keys = readPatientWrapperStmt.executeQuery()) {
				keys.next();
				toReturn.setSerialNumber(keys.getLong(1));
				patientID = keys.getLong(2);
			}
			PreparedStatement readPatientStmt = conn.prepareStatement(GET_PATIENT_QUERY);
			readPatientStmt.setLong(1, patientID);
			try (ResultSet keys = readPatientStmt.executeQuery()) {
				keys.next();
				toReturn.getPatient().setID(keys.getLong(1));
				toReturn.getPatient().setNhisId(Long.parseLong(keys.getString(2)));
				toReturn.getPatient().setName(keys.getString(3), keys.getString(4));
				toReturn.getPatient().setAddress(keys.getString(5));
				toReturn.getPatient().setPhoneNumber(Integer.parseInt(keys.getString(6)));
				toReturn.getPatient().setSex(keys.getString(7).compareTo("") == 0 ? '\u0000' : keys.getString(7).charAt(0));
				// leaving guardians and dependents null as the app doesn't use them
				// and this dao doesn't set them
				toReturn.getPatient().setBirthdate(keys.getString(10));
			}
			PreparedStatement readPregnancyStmt;
			PreparedStatement readVisitStmt;

			readPregnancyStmt = conn.prepareStatement(GET_PREGNANCY_QUERY);
			readPregnancyStmt.setLong(1, toReturn.getId());
			try (ResultSet keys = readPregnancyStmt.executeQuery()) {
				while (keys.next()) {
					Pregnancy currentPregnancy = new Pregnancy();
					currentPregnancy.setSerialNumber(keys.getLong(1));
					currentPregnancy.setExpectedDayOfDelivery(keys.getString(2));
					currentPregnancy.setHemoglobinLevelAtReg(Double.parseDouble(keys.getString(3)));
					currentPregnancy.setHemoglobinLevelAt36Wks(Double.parseDouble(keys.getString(4)));
					currentPregnancy.setUrineTestResult(keys.getInt(5));
					currentPregnancy.setBloodGroup(keys.getString(6));
					currentPregnancy.setVDRL(keys.getBoolean(7));
					currentPregnancy.setItnUse(keys.getBoolean(8));
					currentPregnancy.setComplaints(keys.getString(9));
					currentPregnancy.setRemarks(keys.getString(10));
					currentPregnancy.setMaleInvolvement(keys.getBoolean(11));
					IPT ipt = new IPT();
					ipt.setFirstDose(keys.getBoolean(12));
					ipt.setSecondDose(keys.getBoolean(13));
					ipt.setThirdDose(keys.getBoolean(14));
					currentPregnancy.setIpt(ipt);

					currentPregnancy.setParity(keys.getInt(15), keys.getInt(16));

					PMTCT pmtct = new PMTCT();
					pmtct.setPreTestCounseling(keys.getBoolean(17));
					pmtct.setTestResult(keys.getBoolean(18));
					pmtct.setPostTestCounseling(keys.getBoolean(19));
					pmtct.setARV(keys.getBoolean(20));
					currentPregnancy.setPmtct(pmtct);

					Sickling sickling = new Sickling();
					sickling.setSickling(keys.getBoolean(21));
					sickling.setType(keys.getString(22));
					currentPregnancy.setSickling(sickling);

					TT tt = new TT();
					tt.setFirstDose(keys.getBoolean(23));
					tt.setSecondDose(keys.getBoolean(24));
					tt.setThirdDose(keys.getBoolean(25));
					tt.setProtectedNotDosed(keys.getBoolean(26));
					currentPregnancy.setTt(tt);

					readVisitStmt = conn.prepareStatement(GET_VISIT_QUERY);
					readVisitStmt.setLong(1, currentPregnancy.getId());
					ArrayList<Visit> visits = new ArrayList<Visit>();
					try (ResultSet visitKeys = readVisitStmt.executeQuery()) {
						while(visitKeys.next()) {
							Visit currentVisit = new Visit();
							currentVisit.setDate(LocalDate.parse(visitKeys.getString(1),dateTimeFormatter));
							BloodPressure bp = new BloodPressure();
							bp.setSystolic(visitKeys.getInt(2));
							bp.setDiastolic(visitKeys.getInt(3));
							currentVisit.setBloodPressure(bp);
							currentVisit.setWeight(visitKeys.getInt(4));
							currentVisit.setPatientHeight(visitKeys.getInt(5));
							currentVisit.setFundalHeight(visitKeys.getInt(6));
							currentVisit.setGestationWeek(visitKeys.getInt(7));
							currentVisit.setBloodFilm(visitKeys.getBoolean(8));
							currentVisit.setRefurred(visitKeys.getString(9));
							visits.add(currentVisit);
						}
					}  
					currentPregnancy.setSubsequentVisits(visits);
					pregnancies.add(currentPregnancy);
				}
			}
			toReturn.setPregnancies(pregnancies);
			return toReturn;
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void updatePatientWrapper(PatientWrapper wrapper) throws SQLException {
		Long patientID = wrapper.getId();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(this.dbUrl);
			PreparedStatement readPatientWrapperStmt = conn.prepareStatement(GET_PATIENT_WRAPPER_QUERY);
			readPatientWrapperStmt.setLong(1, wrapper.getId());
			try (ResultSet keys = readPatientWrapperStmt.executeQuery()) {
				keys.next();
				patientID = keys.getLong(2);
			}

			PreparedStatement removeVisitStatement;
			PreparedStatement readPregnancyStmt = conn.prepareStatement(GET_PREGNANCY_QUERY);
			readPregnancyStmt.setLong(1, wrapper.getId());
			try (ResultSet keys = readPregnancyStmt.executeQuery()) {
				while (keys.next()) {
					removeVisitStatement = conn.prepareStatement(DELETE_VISIT_QUERY);
					removeVisitStatement.setLong(1, keys.getLong(1));
					removeVisitStatement.executeUpdate();
				}
			}
			PreparedStatement removePregnancyStatement = conn.prepareStatement(DELETE_PREGNANCY_QUERY);
			removePregnancyStatement.setLong(1, wrapper.getId());
			removePregnancyStatement.executeUpdate();

			PreparedStatement removeWrapperStatement = conn.prepareStatement(DELETE_PATIENT_WRAPPER_QUERY);
			removeWrapperStatement.setLong(1, wrapper.getId());
			removeWrapperStatement.executeUpdate();

			PreparedStatement deletePatientStmt = conn.prepareStatement(DELETE_PATIENT_QUERY);
			deletePatientStmt.setLong(1, patientID);
			deletePatientStmt.executeUpdate();
			
			PreparedStatement createPregnancyStmt;
			PreparedStatement createVisitStmt;
			PreparedStatement createPatientStmt = conn.prepareStatement(CREATE_PATIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement createPatientWrapperStmt = conn.prepareStatement(CREATE_PATIENTWRAPPERS_QUERY, Statement.RETURN_GENERATED_KEYS);
			encodePatient(createPatientStmt,wrapper.getPatient());
			encodePatientWrapper(createPatientWrapperStmt,wrapper);
			createPatientStmt.executeUpdate();
			createPatientWrapperStmt.executeUpdate();
			for (Pregnancy pregnancy: wrapper.getPregnancies()) {
				createPregnancyStmt = conn.prepareStatement(CREATE_PREGNANCY_QUERY,Statement.RETURN_GENERATED_KEYS);
				encodePregnancy(createPregnancyStmt, pregnancy, wrapper);
				createPregnancyStmt.executeUpdate();

				for (Visit v: pregnancy.getSubsequentVisits()) {
					createVisitStmt = conn.prepareStatement(CREATE_VISIT_QUERY,Statement.RETURN_GENERATED_KEYS);
					encodeVisit(createVisitStmt, v, pregnancy);
					createVisitStmt.executeUpdate();
				}
			}
			try (ResultSet keys = createPatientStmt.getGeneratedKeys()) {}
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void deletePatientWrapper(Long id) throws SQLException {
		Long patientID;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(this.dbUrl);
			PreparedStatement readPatientWrapperStmt = conn.prepareStatement(GET_PATIENT_WRAPPER_QUERY);
			readPatientWrapperStmt.setLong(1, id);
			try (ResultSet keys = readPatientWrapperStmt.executeQuery()) {
				keys.next();
				patientID = keys.getLong(2);
			}

			PreparedStatement removeVisitStatement;
			PreparedStatement readPregnancyStmt = conn.prepareStatement(GET_PREGNANCY_QUERY);
			readPregnancyStmt.setLong(1, id);
			try (ResultSet keys = readPregnancyStmt.executeQuery()) {
				while (keys.next()) {
					removeVisitStatement = conn.prepareStatement(DELETE_VISIT_QUERY);
					removeVisitStatement.setLong(1, keys.getLong(1));
					removeVisitStatement.executeUpdate();
				}
			}
			PreparedStatement removePregnancyStatement = conn.prepareStatement(DELETE_PREGNANCY_QUERY);
			removePregnancyStatement.setLong(1, id);
			removePregnancyStatement.executeUpdate();

			PreparedStatement removeWrapperStatement = conn.prepareStatement(DELETE_PATIENT_WRAPPER_QUERY);
			removeWrapperStatement.setLong(1, id);
			removeWrapperStatement.executeUpdate();

			PreparedStatement deletePatientStmt = conn.prepareStatement(DELETE_PATIENT_QUERY);
			deletePatientStmt.setLong(1, patientID);
			deletePatientStmt.executeUpdate();
			conn.close();
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public List<PatientWrapper> listWrappers() throws SQLException {
		List<PatientWrapper> toReturnList = new ArrayList<PatientWrapper>();
		Long patientID;
		Long wrapperID;
		Connection conn = null;
		try { 
			conn = DriverManager.getConnection(this.dbUrl);
			PreparedStatement readAllPatientWrappers = conn.prepareStatement(GET_ALL_WRAPPERS);
			try (ResultSet wrapperKeys = readAllPatientWrappers.executeQuery()) {
				while (wrapperKeys.next()) {
					PatientWrapper toReturn = new PatientWrapper(new Patient(999l), new ArrayList<Pregnancy>());
					ArrayList<Pregnancy> pregnancies = new ArrayList<Pregnancy>();
					wrapperID = wrapperKeys.getLong(1);
					toReturn.setSerialNumber(wrapperID);
					patientID = wrapperKeys.getLong(2);
					
					PreparedStatement readPatientStmt = conn.prepareStatement(GET_PATIENT_QUERY);
					readPatientStmt.setLong(1, patientID);
					try (ResultSet keys = readPatientStmt.executeQuery()) {
						keys.next();
						toReturn.getPatient().setID(keys.getLong(1));
						toReturn.getPatient().setNhisId(Long.parseLong(keys.getString(2)));
						toReturn.getPatient().setName(keys.getString(3), keys.getString(4));
						toReturn.getPatient().setAddress(keys.getString(5));
						toReturn.getPatient().setPhoneNumber(Integer.parseInt(keys.getString(6)));
						toReturn.getPatient().setSex(keys.getString(7).compareTo("") == 0 ? '\u0000' : keys.getString(7).charAt(0));
						// leaving guardians and dependents null as the app doesn't use them
						// and this dao doesn't set them
						toReturn.getPatient().setBirthdate(keys.getString(10));
					}
					PreparedStatement readPregnancyStmt;
					PreparedStatement readVisitStmt;

					readPregnancyStmt = conn.prepareStatement(GET_PREGNANCY_QUERY);
					readPregnancyStmt.setLong(1, toReturn.getId());
					try (ResultSet keys = readPregnancyStmt.executeQuery()) {
						while (keys.next()) {
							Pregnancy currentPregnancy = new Pregnancy();
							currentPregnancy.setSerialNumber(keys.getLong(1));
							currentPregnancy.setExpectedDayOfDelivery(keys.getString(2));
							currentPregnancy.setHemoglobinLevelAtReg(Double.parseDouble(keys.getString(3)));
							currentPregnancy.setHemoglobinLevelAt36Wks(Double.parseDouble(keys.getString(4)));
							currentPregnancy.setUrineTestResult(keys.getInt(5));
							currentPregnancy.setBloodGroup(keys.getString(6));
							currentPregnancy.setVDRL(keys.getBoolean(7));
							currentPregnancy.setItnUse(keys.getBoolean(8));
							currentPregnancy.setComplaints(keys.getString(9));
							currentPregnancy.setRemarks(keys.getString(10));
							currentPregnancy.setMaleInvolvement(keys.getBoolean(11));
							IPT ipt = new IPT();
							ipt.setFirstDose(keys.getBoolean(12));
							ipt.setSecondDose(keys.getBoolean(13));
							ipt.setThirdDose(keys.getBoolean(14));
							currentPregnancy.setIpt(ipt);

							currentPregnancy.setParity(keys.getInt(15), keys.getInt(16));

							PMTCT pmtct = new PMTCT();
							pmtct.setPreTestCounseling(keys.getBoolean(17));
							pmtct.setTestResult(keys.getBoolean(18));
							pmtct.setPostTestCounseling(keys.getBoolean(19));
							pmtct.setARV(keys.getBoolean(20));
							currentPregnancy.setPmtct(pmtct);

							Sickling sickling = new Sickling();
							sickling.setSickling(keys.getBoolean(21));
							sickling.setType(keys.getString(22));
							currentPregnancy.setSickling(sickling);

							TT tt = new TT();
							tt.setFirstDose(keys.getBoolean(23));
							tt.setSecondDose(keys.getBoolean(24));
							tt.setThirdDose(keys.getBoolean(25));
							tt.setProtectedNotDosed(keys.getBoolean(26));
							currentPregnancy.setTt(tt);

							
							readVisitStmt = conn.prepareStatement(GET_VISIT_QUERY);
							readVisitStmt.setLong(1, currentPregnancy.getId());
							ArrayList<Visit> visits = new ArrayList<Visit>();
							try (ResultSet visitKeys = readVisitStmt.executeQuery()) {
								while(visitKeys.next()) {
									Visit currentVisit = new Visit();
									currentVisit.setDate(LocalDate.parse(visitKeys.getString(1),dateTimeFormatter));
									BloodPressure bp = new BloodPressure();
									bp.setSystolic(visitKeys.getInt(2));
									bp.setDiastolic(visitKeys.getInt(3));
									currentVisit.setBloodPressure(bp);

									currentVisit.setWeight(visitKeys.getInt(4));
									currentVisit.setPatientHeight(visitKeys.getInt(5));
									currentVisit.setFundalHeight(visitKeys.getInt(6));
									currentVisit.setGestationWeek(visitKeys.getInt(7));
									currentVisit.setBloodFilm(visitKeys.getBoolean(8));
									currentVisit.setRefurred(visitKeys.getString(9));
									visits.add(currentVisit);
								}
							}
							currentPregnancy.setSubsequentVisits(visits);
							pregnancies.add(currentPregnancy);
						}
					}
					toReturn.setPregnancies(pregnancies);
					toReturnList.add(toReturn);
				}
				conn.close();
			}
		} finally {
			if (conn != null)
				conn.close();
		}
		
		return toReturnList;
	}

}
