package org.msf.records.msfmedicalrecords.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gil on 03/10/2014.
 */
public class Patient {


    public String id;
    public String forename;
    public String surname;
    public Date dob;

    public Patient(String forename, String surname){

    }

    public static ArrayList<Patient> GETDUMMYCONTENT(){
        ArrayList<Patient> patients = new ArrayList<Patient>();

        return patients;
    }
}
