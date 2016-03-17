package io.github.astasiak.pokartki.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import io.github.astasiak.pokartki.R;
import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardSet;
import io.github.astasiak.pokartki.dao.FlashcardSetsDao;
import io.github.astasiak.pokartki.dao.FlashcardsDao;
import io.github.astasiak.pokartki.engine.AdaptingQuestionStore;
import io.github.astasiak.pokartki.engine.Answer;
import io.github.astasiak.pokartki.engine.RandomQuestionStore;
import io.github.astasiak.pokartki.engine.Question;
import io.github.astasiak.pokartki.engine.QuestionDirection;
import io.github.astasiak.pokartki.engine.QuestionSource;


public class MainActivity extends AppCompatActivity {

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
        insertSampleData(flashcardsDao, flashcardSetsDao);

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
            Intent intent = new Intent(MainActivity.this, SetsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_directions) {
            Intent intent = new Intent(MainActivity.this, DirectionsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_options) {
            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureDirections(SharedPreferences settings) {
        boolean randomQuestions = settings.getBoolean("randomQuestions", false);
        if(randomQuestions) {
            this.questionSource = new RandomQuestionStore(flashcardsDao, flashcardSetsDao);
        } else {
            this.questionSource = new AdaptingQuestionStore(flashcardsDao, flashcardSetsDao);
        }
        boolean fromEnglish = settings.getBoolean("fromEnglish", true);
        boolean fromChinese = settings.getBoolean("fromChinese", true);
        boolean fromPinyin = settings.getBoolean("fromPinyin", true);
        Set<QuestionDirection> directions = new HashSet<>();
        if(fromEnglish) directions.add(QuestionDirection.FROM_ENGLISH);
        if(fromChinese) directions.add(QuestionDirection.FROM_CHINESE);
        if(fromPinyin) directions.add(QuestionDirection.FROM_PINYIN);
        questionSource.configureDirections(directions);
    }

    private void insertSampleData(FlashcardsDao flashcardsDao, FlashcardSetsDao flashcardSetsDao) {
        flashcardsDao.clear();
        flashcardSetsDao.clear();
        XmlResourceParser xml = getResources().getXml(R.xml.data);
        try {
            XmlDataParser.Result data = XmlDataParser.parse(xml);
            for (FlashcardSet set : data.getSets()) {
                flashcardSetsDao.insert(set);
            }
            flashcardsDao.clear();
            for (Flashcard card : data.getCards()) {
                flashcardsDao.insert(card);
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
