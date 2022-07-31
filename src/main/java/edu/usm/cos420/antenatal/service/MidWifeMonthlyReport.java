package edu.usm.cos420.antenatal.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.service.impl.PatientWrapperServiceDao;

public class MidWifeMonthlyReport {

    static PatientWrapperServiceDao patientWrapperServiceDao;
    private final static transient DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void outputReport() {
        patientWrapperServiceDao = new PatientWrapperServiceDao();
        String monthlyMidwifeReport = generateReport(patientWrapperServiceDao);
        String fileName = "Monthly_Midwife_Report_" + LocalDate.now().getYear() + "_" + LocalDate.now().getMonth() + "_" + LocalDate.now().getDayOfMonth();
        try {
            File file = new File(".\\" + fileName + ".txt");
            if (!file.exists()) {
               file.createNewFile();
            } 
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(monthlyMidwifeReport);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateReport(PatientWrapperServiceDao dao) {
        StringBuffer string = new StringBuffer();
        ArrayList<PatientWrapper> patientWrappers = (ArrayList<PatientWrapper>) dao.getPatientWrappers();
        int curSize;
        Patient curPatient;
        LocalDate bDay;
        Period period;
        int age;
        int parity;

        int registrants = patientWrappers.size();
        int attendances = 0;
        int num4thVisit = 0;
        int tt2PlusVax = 0;
        int initialGestationWeek;

        int hemoglobinReg11 = 0;
        int hemoglobinReg7 = 0;
        int hemoglobin36Checked = 0;
        int hemoglobin3611 = 0;
        int hemoglobin367 = 0;

        int ipt1 = 0;
        int ipt2 = 0;
        int ipt3 = 0;

        int itnUse = 0;

        int pmtctCounselled = 0;
        int pmtctPositive = 0;
        int pmtctARV = 0;
                

        HashMap<String, Integer> ageBrackets = new HashMap<String, Integer>();
        ageBrackets.put("10-14", 0);
        ageBrackets.put("15-19", 0);
        ageBrackets.put("20-24", 0);
        ageBrackets.put("25-29", 0);
        ageBrackets.put("30-34", 0);
        ageBrackets.put("35+", 0);

        HashMap<String, Integer> parityInfo = new HashMap<String, Integer>();
        parityInfo.put("0", 0);
        parityInfo.put("1-2", 0);
        parityInfo.put("3-4", 0);
        parityInfo.put("5+", 0);

        HashMap<String, Integer> durationOfPregnancy = new HashMap<String, Integer>();
        durationOfPregnancy.put("1", 0);
        durationOfPregnancy.put("2", 0);
        durationOfPregnancy.put("3", 0);

        for (PatientWrapper pw : patientWrappers) {
            curPatient = pw.getPatient();
            bDay = LocalDate.parse(curPatient.getBirthdate(), dateTimeFormatter);
            period = Period.between(bDay, LocalDate.now());
            age = period.getYears();
            if (age >= 10 && age <= 14)
                ageBrackets.put("10-14", ageBrackets.get("10-14") + 1);
            else if (age >= 15 && age <= 19)
                ageBrackets.put("15-19", ageBrackets.get("15-19") + 1);
            else if (age >= 20 && age <= 24)
                ageBrackets.put("20-24", ageBrackets.get("20-24") + 1);
            else if (age >= 25 && age <= 29)
                ageBrackets.put("25-29", ageBrackets.get("25-29") + 1);
            else if (age >= 30 && age <= 34)
                ageBrackets.put("30-34", ageBrackets.get("30-34") + 1);
            else if (age >= 35)
                ageBrackets.put("35+", ageBrackets.get("35+") + 1);

            // NO HEIGHT DATA IN PATIENT

            // Counting the most recently added pregnancy, as we don't have a way of telling
            // if a pregnancy is active
        
            if (pw.getPregnancies().size() == 0)
                continue;

            Pregnancy p = pw.getPregnancies().get(pw.getPregnancies().size()-1);

            curSize = p.getSubsequentVisits().size();
            attendances += curSize;
            if (curSize >= 4)
                num4thVisit++;
            tt2PlusVax = p.getTt().getSecondDose() ? tt2PlusVax++ : tt2PlusVax;

            parity = p.getParity().getParity();

            initialGestationWeek = p.getSubsequentVisits().get(0).getGestationWeek();
            // ~ 4 weeks in a month
            // 9 months in a pregnancy
            // ~ 3 months in a trimester
            // ~ 12 weeks in a trimester
            if (initialGestationWeek <= 12)
                durationOfPregnancy.put("1", durationOfPregnancy.get("1") + 1);
            else if (initialGestationWeek > 12 && initialGestationWeek <= 24)
                durationOfPregnancy.put("2", durationOfPregnancy.get("2") + 1);
            else if (initialGestationWeek > 24)
                durationOfPregnancy.put("3", durationOfPregnancy.get("3") + 1);

            // Hemoglobin at reg required, clients with hemoglobin at reg measured == all of
            // them

            if (p.getHemoglobinLevelAtReg() < 7)
                hemoglobinReg7++;
            else if (p.getHemoglobinLevelAtReg() < 11)
                hemoglobinReg11++;

            if (p.getIpt().getFirstDose())
                ipt1++;
            if (p.getIpt().getSecondDose())
                ipt2++;
            if (p.getIpt().getThirdDose())
                ipt3++;
            
            // # with reaction, not data we have
            
            if (p.getItnUse())
                itnUse++;

            if (p.getPmtct().getPreTestCounseling() || p.getPmtct().getPostTestCounseling())
                pmtctCounselled++;
            if (p.getPmtct().getTestResult())
                pmtctPositive++;
            if (p.getPmtct().getARV())
                pmtctARV++;

            // all PMTCT tested by spec



            if (p.getHemoglobinLevelAt36Wks() != 0.0)
                hemoglobin36Checked++;

            if (p.getHemoglobinLevelAt36Wks() == 0.0)
                ;// nothing
            else if (p.getHemoglobinLevelAt36Wks() < 7)
                hemoglobin367++;
            else if (p.getHemoglobinLevelAt36Wks() < 11)
                hemoglobinReg11++;

            if (parity == 0)
                parityInfo.put("0", parityInfo.get("0") + 1);
            else if (parity >= 1 && parity <= 2)
                parityInfo.put("1-2", parityInfo.get("1-2") + 1);
            else if (parity >= 3 && parity <= 4)
                parityInfo.put("3-4", parityInfo.get("3-4") + 1);
            else if (parity >= 5)
                parityInfo.put("5+", parityInfo.get("5+") + 1);

        }

        string.append("MONTHLY MIDWIFE REPORT - ANTENTAL TEAM\n");
        string.append("Year: " + LocalDate.now().getYear() + " Month: " + LocalDate.now().getMonth());
        string.append("\n\n\n");
        string.append("# of Registrants: " + registrants + "\n");
        string.append("Attendances: " + attendances + "\n");
        string.append("# making 4th visits: " + num4thVisit + "\n");
        string.append("TT 2+ vaccination: " + tt2PlusVax + " \n");
        string.append("\nAGE OF MOTHER REGISTRATION (YEARS)\n");
        string.append("10-14: " + ageBrackets.get("10-14")+"\n");
        string.append("15-19: " + ageBrackets.get("15-19")+"\n");
        string.append("20-24: " + ageBrackets.get("20-24")+"\n");
        string.append("25-29: " + ageBrackets.get("25-29")+"\n");
        string.append("30-34: " + ageBrackets.get("30-34")+"\n");
        string.append("35+: " + ageBrackets.get("35+")+"\n");

        string.append("\nPARITY\n");
        string.append("0: " + parityInfo.get("0")+"\n");
        string.append("1-2: " + parityInfo.get("1-2")+"\n");
        string.append("3-4: " + parityInfo.get("3-4")+"\n");
        string.append("5+: " + parityInfo.get("5+")+"\n");

        string.append("\nDURATION OF PREGNANCY AT REGISTRATION\n");
        string.append("1st Trim: " + durationOfPregnancy.get("1")+"\n");
        string.append("2nd Trim: " + durationOfPregnancy.get("2")+"\n");
        string.append("3rd Trim: " + durationOfPregnancy.get("3")+"\n");

        string.append("\nHEMOGLOBIN AT REGIST. & 36 WKS.\n");
        string.append("Clients with HB checked at Registration: " + registrants+"\n");//all
        string.append("Clients with HB <11 gm/dl at registration: " + hemoglobinReg11+"\n");
        string.append("Clients with HB <7 gm/dl at registration: " + hemoglobinReg7+"\n");
        string.append("Clients with HB checked at 36 weeks: " + hemoglobin36Checked+"\n");
        string.append("Clients with HB <11 gm/dl at 36 weeks: " + hemoglobin3611+"\n");
        string.append("Clients with HB <7 gm/dl at 36 weeks: " + hemoglobin367+"\n");

        string.append("\nIPT\n");
        string.append("ITP 1: " + ipt1+"\n");
        string.append("ITP 2: " + ipt2+"\n");
        string.append("ITP 3: " + ipt3+"\n");

        string.append("\nITN\n");
        string.append("Visits: " + itnUse+"\n");

        string.append("\nPMTCT\n");
        string.append("# Counselled: " + pmtctCounselled + "\n");
        string.append("# Tested: " + registrants + "\n");//all
        string.append("# Positive: " + pmtctPositive + "\n");
        string.append("# Mothers on ARV: " + pmtctARV + "\n");
        // don't have babies on ARV

        return string.toString();
    }

}
