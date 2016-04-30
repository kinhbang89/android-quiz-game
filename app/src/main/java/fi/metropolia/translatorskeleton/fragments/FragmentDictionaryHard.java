package fi.metropolia.translatorskeleton.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import fi.metropolia.translatorskeleton.R;
import fi.metropolia.translatorskeleton.model.Dictionary;
import fi.metropolia.translatorskeleton.utils.CONSTANT;
import fi.metropolia.translatorskeleton.utils.SharedPrefManager;

/**
 * View showing that word are not yet mastered
 */
public class FragmentDictionaryHard extends ListFragment  implements OnItemClickListener {

    Dictionary dict;
    String wordKeys[];
    ArrayAdapter<String> adapter;
    private Object dictPref;

    private String googleTranslateURL ="https://translate.google.com/#en/fi/";

    public FragmentDictionaryHard() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        dictPref = SharedPrefManager
                .getInstance(this.getContext())
                .loadFromPref(Dictionary.class, CONSTANT.DICTIONARY_PREF, CONSTANT.FIN_TO_EN_KEY);

        if ( dictPref != null) {

            dict = SharedPrefManager
                    .getInstance(this.getContext())
                    .loadFromPref(Dictionary.class, CONSTANT.DICTIONARY_PREF, CONSTANT.FIN_TO_EN_KEY);

            wordKeys = dict
                    .getKeys()
                    .toArray(new String[dict.getWordCount()]);

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, wordKeys);

            setListAdapter(adapter);
            getListView().setOnItemClickListener(this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        /*if (dict != null) {
            wordKeys = dict
                    .getKeys()
                    .toArray(new String[dict.getWordCount()]);
            adapter.addAll(wordKeys);
            adapter.notifyDataSetChanged();
        }*/

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if ( wordKeys != null) {
            String wordSearch = wordKeys[position];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleTranslateURL + wordSearch));
            startActivity(intent);
        }

    }
}

