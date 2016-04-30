package fi.metropolia.translatorskeleton.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fi.metropolia.translatorskeleton.R;
import fi.metropolia.translatorskeleton.activities.QuizzActivity;
import fi.metropolia.translatorskeleton.utils.CONSTANT;

/**
 * Quiz fragment to start the quiz by random or hard
 */
public class FragmentQuiz extends Fragment {

    private Button random_quiz_btn;
    private Button hard_quiz_btn;

    public FragmentQuiz() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_quiz, container, false);
        random_quiz_btn = (Button) v.findViewById(R.id.random_btn);
        random_quiz_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuizzActivity.class);
                intent.putExtra("title", CONSTANT.RANDOM);
                startActivity(intent);
            }
        });


        hard_quiz_btn = (Button) v.findViewById(R.id.hard_btn);
        hard_quiz_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuizzActivity.class);
                intent.putExtra("title", CONSTANT.HARD);
                startActivity(intent);
            }
        });

        return v;
    }
}

