package org.msf.records.msfmedicalrecords.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gil on 03/10/2014.
 */
public class Patient implements Serializable {


    public String id;
    public String forename;
    public String surname;
    public Date dob;

    public Patient(String id, String forename, String surname){
        this.id = id;
        this.forename = forename;
        this.surname = surname;
    }

    public static ArrayList<Patient> GETDUMMYCONTENT(){
        ArrayList<Patient> patients = new ArrayList<Patient>();

        patients.add(new Patient("HG78190", "Gil", "Julio"));
        patients.add(new Patient("HG78190", "Pim", "de witte"));
        patients.add(new Patient("HG78190", "Gil", "Julio"));
        patients.add(new Patient("HG78190", "Gil", "Julio"));
        return patients;
    }
}
