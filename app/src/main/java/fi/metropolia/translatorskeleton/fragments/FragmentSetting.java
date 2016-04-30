package fi.metropolia.translatorskeleton.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import fi.metropolia.translatorskeleton.R;
import fi.metropolia.translatorskeleton.model.Setting;
import fi.metropolia.translatorskeleton.utils.CONSTANT;
import fi.metropolia.translatorskeleton.utils.Utils;

/**
 * Created by Bang on 19/04/16.
 */
public class FragmentSetting extends Fragment {
    private NumberPicker np;
    private NumberPicker timeout_np;
    private Setting setting;
    private CONSTANT c;
    private RadioButton r_random;
    private RadioButton r_hard;
    private RadioGroup r_group;
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
        getResources().getString(R.string.setting_pref_key);
        if ( Utils.loadSettingFromSp(getContext()) != null ) {
            setting = Utils.loadSettingFromSp(this.getContext());
        } else {
            setting = new Setting(CONSTANT.DEFAULT_LENGTH, CONSTANT.RANDOM, CONSTANT.DEFAULT_TIMEOUT);
        }

        // set value from quiz_length picker
        np = (NumberPicker) view.findViewById(R.id.quiz_length_picker);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setValue(setting.getLength());

        np.setOnScrollListener(new NumberPicker.OnScrollListener() {

            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    setting.setLength(np.getValue());
                    Utils.saveSettingToSp(setting, getContext());
                }
            }
        });

        // set value for timeout picker
        timeout_np = (NumberPicker) view.findViewById(R.id.timeout_picker);
        timeout_np.setMaxValue(10);
        timeout_np.setMinValue(1);
        timeout_np.setValue(setting.getTime_out());

        timeout_np.setOnScrollListener(new NumberPicker.OnScrollListener() {

            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    setting.setTime_out(timeout_np.getValue());
                    Utils.saveSettingToSp(setting, getContext());
                }
            }
        });

        // set default value for radio group
        r_hard = (RadioButton) view.findViewById(R.id.quiz_type_radio_hard);
        r_random = (RadioButton) view.findViewById(R.id.quiz_type_radio_random);
        if (setting.getType().equals(CONSTANT.HARD)) {
            r_hard.setChecked(true);
        } else {
            r_random.setChecked(true);
        }
        r_group = (RadioGroup) view.findViewById(R.id.quiz_type_radioGroup);
        r_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(r_hard.isChecked()) {
                    setting.setType(CONSTANT.HARD);
                    Utils.saveSettingToSp(setting, getContext());
                } else if(r_random.isChecked()) {
                    setting.setType(CONSTANT.RANDOM);
                    Utils.saveSettingToSp(setting, getContext());
                }
            }
        });
        return view;
    }

}

