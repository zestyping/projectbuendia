package org.msf.records.msfmedicalrecords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

/**
 * A fragment representing a single Patient detail screen.
 * This fragment is either contained in a {@link PatientListActivity}
 * in two-pane mode (on tablets) or a {@link PatientDetailActivity}
 * on handsets.
 */
public class PatientDetailFragment extends Fragment {

    private static final String TAG = PatientDetailFragment.class.getSimpleName();

    public static final String ARG_ITEM_ID = "ARG_ITEM_ID";

    private static final int COUNT = 4;
    private static final int OVERVIEW = 0, BLOOD = 1, NOTES = 2, LOGS = 3;

    ViewPager mPager;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_detail, container, false);

        FragmentPagerAdapter adapter = new PatientDetailsAdapter(getChildFragmentManager());

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null){
            Log.d(TAG, bundle.toString());
        } else {
            Log.d(TAG, "bundle is null");
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.patient_detail, menu);
    }


    class PatientDetailsAdapter extends FragmentPagerAdapter {
        public PatientDetailsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case OVERVIEW:
                    return PatientDetailOverviewFragment.newInstance();
                case BLOOD:
                    return PatientDetailBloodFragment.newInstance();
                case NOTES:
                    return PatientDetailNotesFragment.newInstance();
                case LOGS:
                    return PatientDetailLogsFragment.newInstance();
                default:
                    return null;

            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case OVERVIEW:
                    return getString(R.string.detail_pager_overview);
                case BLOOD:
                    return getString(R.string.detail_pager_blood);
                case NOTES:
                    return getString(R.string.detail_pager_notes);
                case LOGS:
                    return getString(R.string.detail_pager_logs);
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }
}