package fi.metropolia.translatorskeleton.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import fi.metropolia.translatorskeleton.R;

/**
 * Created by Bang on 19/04/16.
 */
public class FragmentSetting extends Fragment {
    private NumberPicker np;
    public FragmentSetting() {
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        np = (NumberPicker) view.findViewById(R.id.quiz_length_picker);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setValue(10);
        return view;
    }
}

