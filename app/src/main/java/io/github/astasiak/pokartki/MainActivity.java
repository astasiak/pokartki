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

import java.util.HashSet;
import java.util.Set;

import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardSetsDao;
import io.github.astasiak.pokartki.dao.FlashcardsDao;
import io.github.astasiak.pokartki.engine.Answer;
import io.github.astasiak.pokartki.engine.DaoQuestionStore;
import io.github.astasiak.pokartki.engine.Question;
import io.github.astasiak.pokartki.engine.QuestionDirection;
import io.github.astasiak.pokartki.engine.QuestionSource;


public class MainActivity extends ActionBarActivity {

    private QuestionSource questionSource;
    private FlashcardsDao flashcardsDao;
    private FlashcardSetsDao flashcardSetsDao;

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

        this.flashcardsDao = new FlashcardsDao(getApplicationContext());
        this.flashcardSetsDao = new FlashcardSetsDao(getApplicationContext());
        insertSampleData(flashcardsDao);
        this.questionSource = new DaoQuestionStore(flashcardsDao, flashcardSetsDao);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        configureDirections(settings);

        englishButton = (Button) findViewById(R.id.englishButton);
        chineseButton = (Button) findViewById(R.id.chineseButton);
        pinyinButton = (Button) findViewById(R.id.pinyinButton);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);

        englishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Question currentQuestion = questionSource.getCurrentQuestion();
                if(currentQuestion!=null) {
                    englishButton.setText(currentQuestion.getEnglish());
                }
            }
        });
        chineseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Question currentQuestion = questionSource.getCurrentQuestion();
                if(currentQuestion!=null) {
                    chineseButton.setText(currentQuestion.getChinese());
                }
            }
        });
        pinyinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Question currentQuestion = questionSource.getCurrentQuestion();
                if(currentQuestion!=null) {
                    pinyinButton.setText(currentQuestion.getPinyin());
                }
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
        button3.setOnClickListener(new OnClickGradeListener(Answer.ANSWER3));

        Question question = questionSource.getCurrentQuestion();
        presentQuestion(question);
    }

    private void presentQuestion(Question question) {
        String englishText, chineseText, pinyinText;
        if(question == null) {
            englishText = "";
            chineseText = "No cards";
            pinyinText = "";
        } else {
            englishText = question.getDirection() == QuestionDirection.FROM_ENGLISH ?
                    question.getEnglish() : "?";
            chineseText = question.getDirection() == QuestionDirection.FROM_CHINESE ?
                    question.getChinese() : "?";
            pinyinText = question.getDirection() == QuestionDirection.FROM_PINYIN ?
                    question.getPinyin() : "?";
        }
        englishButton.setText(englishText);
        chineseButton.setText(chineseText);
        pinyinButton.setText(pinyinText);
    }

    @Override
    protected void onResume() {
        //showDialog("We're back!", "onRestore invoked");
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        configureDirections(settings);
        presentQuestion(questionSource.getCurrentQuestion());
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

    private void insertSampleData(FlashcardsDao flashcardsDao) {
        flashcardsDao.clear();
        flashcardsDao.insert(sampleFlashcard("a student", "\u5b66\u751f", "xu\u00e9sheng"));
        flashcardsDao.insert(sampleFlashcard("a language", "\u8bed\u8a00", "y\u016dy\u00e1n"));
        flashcardsDao.insert(sampleFlashcard("I'm sorry", "\u5bf9\u4e0d\u8d77", "du\u00ec buq\u012d"));
        flashcardsDao.insert(sampleFlashcard("Good bye", "\u518d\u89c1", "z\u00e0iji\u00e0n"));
        flashcardsDao.insert(sampleFlashcard("this", "\u8FD9", "zh\u00e8"));
    }

    private Flashcard sampleFlashcard(String english, String chinese, String pinyin) {
        Flashcard card = new Flashcard();
        card.setEnglish(english);
        card.setChinese(chinese);
        card.setPinyin(pinyin);
        return card;
    }
}
