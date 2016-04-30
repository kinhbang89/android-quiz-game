package fi.metropolia.translatorskeleton;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fi.metropolia.translatorskeleton.fragments.FragmentDictionaryHard;
import fi.metropolia.translatorskeleton.fragments.FragmentInput;
import fi.metropolia.translatorskeleton.fragments.FragmentQuiz;
import fi.metropolia.translatorskeleton.fragments.FragmentSetting;
import fi.metropolia.translatorskeleton.model.Dictionary;
import fi.metropolia.translatorskeleton.utils.Mapper;
import fi.metropolia.translatorskeleton.utils.SharedPrefManager;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Gson gson;
    private Dictionary dictFinEng;
    private Dictionary engToFin;
    private String API_URL;
    private Dictionary dictEngFin;
    private boolean timeIsOut;
    private String DICTIONARY_PREF;
    private String FIN_TO_EN_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();

        dictFinEng = new Dictionary("fin","eng");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        API_URL = this.getResources().getString(R.string.API_URL);
        DICTIONARY_PREF = this.getResources().getString(R.string.DICTIONARY_PREF);
        FIN_TO_EN_KEY = this.getResources().getString(R.string.FIN_TO_EN_KEY);

        Gson gson = new Gson();
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObject = new JsonArrayRequest
                (API_URL + "/word", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String fin = response.getJSONObject(i).getString("fin");
                                String en = response.getJSONObject(i).getString("en");
                                String image_url = response.getJSONObject(i).getString("img_url");

                                dictFinEng
                                        .addPair(fin, en);

                                Mapper.getInstance().getMap().put(fin, image_url);

                            }

                            if (dictFinEng != null) {
                                SharedPrefManager
                                        .getInstance(getActivity())
                                        .saveToPref(dictFinEng, DICTIONARY_PREF, FIN_TO_EN_KEY);
                            }

                            viewPager = (ViewPager) findViewById(R.id.viewpager);
                            setupViewPager(viewPager);

                            tabLayout = (TabLayout) findViewById(R.id.tabs);
                            tabLayout.setupWithViewPager(viewPager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObject);
    }

    private Activity getActivity() {
        return this;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentInput(), "Input");
        adapter.addFragment(new FragmentDictionaryHard(), "Words");
        adapter.addFragment(new FragmentQuiz(), "Quiz");
        adapter.addFragment(new FragmentSetting(), "Setting");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}