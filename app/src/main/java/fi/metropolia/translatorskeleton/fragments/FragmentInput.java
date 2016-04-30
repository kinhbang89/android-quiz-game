package fi.metropolia.translatorskeleton.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fi.metropolia.translatorskeleton.R;
import fi.metropolia.translatorskeleton.utils.Utils;

// input words to the server
public class FragmentInput extends Fragment {
    private  EditText finEditText;
    private  EditText engEditText;
    private  Button submitBtn;
    private String API_URL;

    public FragmentInput() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_input, container, false);
        finEditText = (EditText)view.findViewById(R.id.input_finnish);
        engEditText = (EditText)view.findViewById(R.id.input_english);
        submitBtn = (Button)view.findViewById(R.id.btnSubmit);
        API_URL = this.getResources().getString(R.string.API_URL);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    uploadWordToServer();
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void uploadWordToServer() throws JSONException {

        String finWord = finEditText.getText().toString();
        String enWord = engEditText.getText().toString();
        JSONObject obj = new JSONObject();
        obj.put("fin", finWord);
        obj.put("en", enWord);


        if (finWord.length()<2 || enWord.length()<2){
            AlertDialog alert = Utils.alertDialogBuilder("Fail ","Invalid", getContext()).create();
            alert.show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest newRequest = new JsonArrayRequest(Request.Method.POST, API_URL + "/word", obj, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                AlertDialog alert = Utils.alertDialogBuilder("Success","Updated", getContext()).create();
                alert.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlertDialog alert = Utils.alertDialogBuilder("Fail","Existing or error in updating", getContext()).create();
                alert.show();
            }
        });

        queue.add(newRequest);

        finEditText.setText("");
        engEditText.setText("");
    }
}
