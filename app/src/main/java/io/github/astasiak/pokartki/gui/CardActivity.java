package io.github.astasiak.pokartki.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.github.astasiak.pokartki.R;
import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardsDao;


public class CardActivity extends AppCompatActivity {

    private FlashcardsDao flashcardsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        this.flashcardsDao = new FlashcardsDao(getApplicationContext());

        Long cardId = getIntent().getExtras().getLong("cardId");
        System.out.println("Card id: "+cardId);
        Flashcard card = flashcardsDao.get(cardId);
        TextView english = (TextView) findViewById(R.id.carddet_english);
        TextView chinese = (TextView) findViewById(R.id.carddet_chinese);
        TextView pinyin = (TextView) findViewById(R.id.carddet_pinyin);
        TextView details = (TextView) findViewById(R.id.carddet_details);
        english.setText(card.getEnglish().getWord());
        chinese.setText(card.getChinese().getWord());
        pinyin.setText(card.getPinyin().getWord());
        String detailsText =
                printParameters("English", card.getEnglish())+"\n"
                +printParameters("Chinese", card.getChinese())+"\n"
                +printParameters("Pinyin", card.getPinyin());
        details.setText(detailsText);

    }

    private String printParameters(String name, Flashcard.Parameters params) {
        return String.format("%s: %.2f %.2f", name, params.getPosition(), params.getInterval());
    }
}
