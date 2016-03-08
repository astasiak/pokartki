package io.github.astasiak.pokartki.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.astasiak.pokartki.R;
import io.github.astasiak.pokartki.dao.Flashcard;
import io.github.astasiak.pokartki.dao.FlashcardsDao;


public class SetActivity extends AppCompatActivity {

    private ListView setsListView;
    private FlashcardsDao flashcardsDao;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long setId = getIntent().getExtras().getLong("setId");

        this.flashcardsDao = new FlashcardsDao(getApplicationContext());
        List<Flashcard> list = flashcardsDao.listBySet(setId);

        setContentView(R.layout.activity_sets);
        adapter = new CardAdapter(list, getApplicationContext());

        setsListView = (ListView) findViewById(R.id.setsListView);
        setsListView.setAdapter(adapter);
    }
}


class CardAdapter extends ArrayAdapter<Flashcard> {

    private List<Flashcard> list;
    private Context context;

    public CardAdapter(List<Flashcard> list, Context context) {
        super(context, R.layout.card_item, list);
        this.list = new ArrayList<>(list);
        this.context = context;
    }

    private static class SetHolder {
        public TextView english;
        public TextView chinese;
        public TextView parameters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SetHolder holder = null;

        final Flashcard card = list.get(position);
        if(convertView==null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.card_item, null);

            holder = new SetHolder();
            holder.chinese = (TextView) convertView.findViewById(R.id.card_chinese);
            holder.english = (TextView) convertView.findViewById(R.id.card_english);
            holder.parameters = (TextView) convertView.findViewById(R.id.card_params);
            convertView.setTag(holder);
        } else {
            holder = (SetHolder) convertView.getTag();
        }

        holder.english.setText(card.getEnglish());
        holder.chinese.setText(card.getChinese());
        holder.parameters.setText(""+card.getAverageInterval());
        return convertView;
    }
}