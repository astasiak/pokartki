package io.github.astasiak.pokartki;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import io.github.astasiak.pokartki.engine.Answer;
import io.github.astasiak.pokartki.engine.Question;
import io.github.astasiak.pokartki.engine.QuestionDirection;
import io.github.astasiak.pokartki.engine.QuestionSource;
import io.github.astasiak.pokartki.engine.mock.MockQuestionSource;


public class MainActivity extends ActionBarActivity {

    private QuestionSource questionSource;

    private Button englishButton;
    private Button chineseButton;
    private Button pinyinButton;
    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.questionSource = new MockQuestionSource();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        configureDirections(settings);
        settings.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                configureDirections(sharedPreferences);
            }
        });

        englishButton = (Button) findViewById(R.id.englishButton);
        chineseButton = (Button) findViewById(R.id.chineseButton);
        pinyinButton = (Button) findViewById(R.id.pinyinButton);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        englishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                englishButton.setText(questionSource.getCurrentQuestion().getEnglish());
            }
        });
        chineseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chineseButton.setText(questionSource.getCurrentQuestion().getChinese());
            }
        });
        pinyinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pinyinButton.setText(questionSource.getCurrentQuestion().getPinyin());
            }
        });

        class OnClickGradeListener implements View.OnClickListener {
            private Answer answer;
            OnClickGradeListener(Answer answer) {
                this.answer = answer;
            }
            public void onClick(View v) {
                questionSource.setAnswer(this.answer);
                presentQuestion(questionSource.getCurrentQuestion());
            }
        }
        button1.setOnClickListener(new OnClickGradeListener(Answer.ANSWER1));
        button2.setOnClickListener(new OnClickGradeListener(Answer.ANSWER2));
        button3.setOnClickListener(new OnClickGradeListener(Answer.ANSWER2));

        Question question = questionSource.getCurrentQuestion();
        presentQuestion(question);
    }

    private void presentQuestion(Question question) {
        String englishText = question.getDirection() == QuestionDirection.FROM_ENGLISH ?
                question.getEnglish() : "?";
        String chineseText = question.getDirection() == QuestionDirection.FROM_CHINESE ?
                question.getChinese() : "?";
        String pinyinText = question.getDirection() == QuestionDirection.FROM_PINYIN ?
                question.getPinyin() : "?";
        englishButton.setText(englishText);
        chineseButton.setText(chineseText);
        pinyinButton.setText(pinyinText);
    }

    @Override
    protected void onResume() {
        //showDialog("We're back!", "onRestore invoked");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sets) {
            showDialog("Hello world", "How are you?");
            return true;
        }
        if (id == R.id.action_directions) {
            Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureDirections(SharedPreferences settings) {
        boolean fromEnglish = settings.getBoolean("fromEnglish", true);
        boolean fromChinese = settings.getBoolean("fromChinese", true);
        boolean fromPinyin = settings.getBoolean("fromPinyin", true);
        Set<QuestionDirection> directions = new HashSet<>();
        if(fromEnglish) directions.add(QuestionDirection.FROM_ENGLISH);
        if(fromChinese) directions.add(QuestionDirection.FROM_CHINESE);
        if(fromPinyin) directions.add(QuestionDirection.FROM_PINYIN);
        questionSource.configureDirections(directions);
    }

    private void showDialog(String title, String text) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
                .create();
        dialog.show();
    }

}
