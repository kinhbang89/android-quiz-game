package fi.metropolia.translatorskeleton.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import fi.metropolia.translatorskeleton.R;
import fi.metropolia.translatorskeleton.model.Dictionary;
import fi.metropolia.translatorskeleton.model.Quiz;
import fi.metropolia.translatorskeleton.model.QuizItem;
import fi.metropolia.translatorskeleton.model.RandomQuiz;
import fi.metropolia.translatorskeleton.model.Setting;
import fi.metropolia.translatorskeleton.model.TimeOutObserver;
import fi.metropolia.translatorskeleton.model.TimeOutQuestion;
import fi.metropolia.translatorskeleton.model.TrackRecord;
import fi.metropolia.translatorskeleton.model.User;
import fi.metropolia.translatorskeleton.model.UserData;
import fi.metropolia.translatorskeleton.utils.CONSTANT;
import fi.metropolia.translatorskeleton.utils.FlickrManager;
import fi.metropolia.translatorskeleton.utils.Mapper;
import fi.metropolia.translatorskeleton.utils.SharedPrefManager;
import fi.metropolia.translatorskeleton.utils.Utils;

/**
 * Created by Bang on 25/04/16.
 */

// quiz activity for handling all logic to play quiz
public class QuizzActivity extends AppCompatActivity {

    private int currentScore = 0;
    private int highScore;
    private  int scoredPoint = 0;
    private Button submit_btn;

    private TextView questionTv;
    private EditText answerET;

    private TextView currentScoreTv;
    private TextView timer;

    private Setting setting;
    private Quiz quizz;
    Dictionary dict;
    private int currentQuizPostion = 0;
    private ImageView imageView;

    private TrackRecord trackRecord;
    private TimeOutQuestion timeOutQuestion;
    private TimeOutObserver timeOutObserver;

    private String quizzType;
    private int quizzLength;

    private int timeout;
    private User user;
    private UserData userData;
    private FlickrManager flickrManager;
    TimeoutTask timeOutTask;

    private CounterClass countTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        fetchDataFromPref();
        //Default values for variables that are going to loaded from shared prefs
        int type = getIntent().getExtras().getInt("type");
        dict = new Dictionary("fin", "eng");


        fetchDataFromPref();

        Log.d("quizz type", quizzType);
        Gson gson = new Gson();
        trackRecord = new TrackRecord();


        flickrManager = new FlickrManager(this.getApplicationContext());

        questionTv = (TextView) findViewById(R.id.question_textView);
        answerET = (EditText) findViewById(R.id.answer_editText);
        submit_btn = (Button) findViewById(R.id.btnSubmit);
        imageView = (ImageView) findViewById(R.id.imageView);
        currentScoreTv = (TextView) findViewById(R.id.score);
        timer = (TextView) findViewById(R.id.timers);


        String title = quizzType.equals("random") ? "RANDOM" : "HARD";

        System.out.println("dict is here"+ dict);
        try {
            quizz = new RandomQuiz(quizzLength, dict);
        } catch (NullPointerException e) {
            System.out.print("random quiz for playing" + e);
        }

        if (timeout <= 10)
            scoredPoint = 5;
        else if (10 < timeout && timeout < 20)
            scoredPoint = 10;
        else
            scoredPoint = 15;

        currentScoreTv.setText("Score: " + Integer.toString(currentScore));

        // A timer of 60 seconds to play for, with an interval of 1 second (1000 milliseconds)
        countTimer = new CounterClass(timeout * 1000, 1000);
        countTimer.start();

        String firstQuestion = quizz.getItem(currentQuizPostion).getQuestion();

        questionTv.setText(firstQuestion);

        System.out.println("first question"+ firstQuestion);
        flickrManager.placeImage((String) Mapper.getInstance().getMap().get(firstQuestion), imageView);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer(quizz);
            }
        });

        setupThreadingTasks();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPrefManager
                .saveToPref(highScore, CONSTANT.DEFAULT, CONSTANT.HIGH_SCORE_KEY);

        SharedPrefManager
                .saveToPref(userData, CONSTANT.USER, CONSTANT.USER_KEY);

        System.out.println("new user data" + userData);

        timeOutQuestion.removeTimeOutObserver(timeOutObserver);

        timeOutTask.cancel(true);

    }

    // assign pref saved data to value in game
    private void fetchDataFromPref() {
        int type = getIntent().getExtras().getInt("type");
        if ( Utils.loadSettingFromSp(this) != null ) {
            setting = Utils.loadSettingFromSp(this);
        } else {
            setting = new Setting(CONSTANT.DEFAULT_LENGTH, CONSTANT.RANDOM, CONSTANT.DEFAULT_TIMEOUT);
        }
        quizzLength = setting.getLength();
        timeout = setting.getTime_out();
        quizzType = setting.getType();

        Object
                dictFromPref = SharedPrefManager
                .loadFromPref(Dictionary.class, CONSTANT.DICTIONARY_PREF, CONSTANT.FIN_TO_EN_KEY);

        Object
                hightScorePref = SharedPrefManager
                .loadFromPref(Integer.class, CONSTANT.DEFAULT, CONSTANT.HIGH_SCORE_KEY);

//        Object
//                userPref = SharedPrefManager
//                .loadFromPref(UserData.class, CONSTANT.USER, CONSTANT.USER_KEY);
//
//
//        if (userPref == null) {
//            // USER data for user
//            userData = MyModelRoot.getInstance().getUserData();
//            userData.setUser(new User("Bang"));
//            userData.addDictionary(CONSTANT.EN_TO_FIN_KEY, dict);
//        } else {
//            userData = (UserData) userPref;
//        }
        if (dictFromPref != null) {
            dict = (Dictionary) dictFromPref;
        }
        if (hightScorePref != null) {
            highScore = (int) hightScorePref;
        }

    }

    //HANDLE LOGIC FOR GAME
    private void submitAnswer(Quiz quiz) {
        String ans = answerET.getText().toString();
        System.out.println("check answer" + quiz);
        // stop the quiz if it passed quiz length
        if (quiz.checkAnswer(currentQuizPostion, ans)) {
            if (currentQuizPostion >= quizzLength - 1) {
                currentQuizPostion = 0;
            } else {
                ++currentQuizPostion;
            }
            handleSuccess(quiz);
        } else {
            handleFailQuiz();
        }
    }

    private void writeScore() {
        currentScoreTv.setText("Score: " + Integer.toString(currentScore));
    }

    private void handleSuccess(Quiz quiz) {
        currentScore += scoredPoint;
        QuizItem item = quiz.getItem(currentQuizPostion);
        questionTv.setText(item.getQuestion());
        answerET.setText("");
        currentScoreTv.setText("Score: " + Integer.toString(currentScore));
        if (currentScore > highScore) {
            highScore = currentScore;
        }
        // win the game
        if (currentQuizPostion == 0) {
            AlertDialog alert = alertDialogBuilder("Congratz, Current high score: " + highScore, "Restart ?").create();
            alert.show();
            return;
        }
        String id = Mapper.getInstance().getMap().get(item.getQuestion());
        flickrManager.placeImage(id, imageView);

        restartTasks();
    }

    private void stopTimer() {
        // reset timeer here
        timeOutTask.cancel(true);
        countTimer.cancel();
    }
    private void handleFailQuiz() {
        AlertDialog alert = alertDialogBuilder("GAME OVER", "Restart ?").create();
        alert.show();
        stopTimer();
    }

    // THREAD TASK FOR GAME TO STOOP T
    private void setupThreadingTasks() {
        timeOutObserver = new TimeOutObserver() {
            @Override
            public void timeout() {
                //Log.d("timeout done", "tick tick tick");

            }
        };
        timeOutQuestion = new TimeOutQuestion(timeout * 1000);
        timeOutQuestion.registerTimeOutObserver(timeOutObserver);

        timeOutTask = new TimeoutTask();
        /*
        * Run 2 tasks on a thread pool
        * So that they can execute in parallel
        * */
        timeOutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeOutQuestion);
    }

    private void restartTasks() {
        timeOutTask.cancel(true);
        timeOutTask = new TimeoutTask();
        timeOutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, timeOutQuestion);

        countTimer.cancel();
        countTimer = new CounterClass(timeout * 1000, 1000);
        countTimer.start();
    }


    private void resetScene() {
        currentQuizPostion = 0;
        currentScore = 0;
        quizz = new RandomQuiz(quizzLength, dict);
        String initialQuestion = quizz.getItem(currentQuizPostion).getQuestion();
        questionTv.setText(initialQuestion);
        answerET.setText("");
        writeScore();
        flickrManager.placeImage((String) Mapper.getInstance().getMap().get(initialQuestion), imageView);
        restartTasks();
    }


    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void onFinish() {
            timer.setText("Time is up");
        }
        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(millis)));
            timer.setText(hms);
        }
    }

    private class TimeoutTask extends AsyncTask<TimeOutQuestion, Void, Void> {
        private volatile boolean running;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            running = true;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog alert = alertDialogBuilder("Game Over", "Try again?").create();
            alert.show();
        }

        @Override
        protected Void doInBackground(TimeOutQuestion... params) {
            TimeOutQuestion quest = params[0];
            if (isCancelled())
                return null;
            quest.run();
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            running = false;
        }
    }

    private AlertDialog.Builder alertDialogBuilder(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetScene();
                dialog.cancel();
            }
        });
        return builder;
    }
}





