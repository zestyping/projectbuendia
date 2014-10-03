package org.msf.records.msfmedicalrecords;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientDetailBloodFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PatientDetailBloodFragment extends Fragment {

    public static PatientDetailBloodFragment newInstance() {
        PatientDetailBloodFragment fragment = new PatientDetailBloodFragment();
        return fragment;
    }
    public PatientDetailBloodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_detail_blood, container, false);
    }


}
