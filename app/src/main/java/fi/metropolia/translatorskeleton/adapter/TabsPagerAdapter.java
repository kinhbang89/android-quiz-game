package fi.metropolia.translatorskeleton.adapter;

/**
 * Created by Bang on 19/04/16.
 */
    import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

    import fi.metropolia.translatorskeleton.fragments.FragmentDictionaryHard;
    import fi.metropolia.translatorskeleton.fragments.FragmentInput;
    import fi.metropolia.translatorskeleton.fragments.FragmentQuiz;
    import fi.metropolia.translatorskeleton.fragments.FragmentSetting;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new FragmentInput();
            case 1:
                return new FragmentDictionaryHard();
            case 2:
                return new FragmentQuiz();
            case 3:
                return new FragmentSetting();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
}
