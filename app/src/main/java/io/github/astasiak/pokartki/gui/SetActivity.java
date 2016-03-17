package io.github.astasiak.pokartki.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Collections.sort(list, new Comparator<Flashcard>() {
            public int compare(Flashcard f1, Flashcard f2) {
                return Double.compare(f1.getAverageInterval(), f2.getAverageInterval());
            }
        });

        setContentView(R.layout.activity_sets);
        adapter = new CardAdapter(list, getApplicationContext(), this);

        setsListView = (ListView) findViewById(R.id.setsListView);
        setsListView.setAdapter(adapter);
    }
}


class CardAdapter extends ArrayAdapter<Flashcard> {

    private List<Flashcard> list;
    private Context context;
    private SetActivity activity;

    public CardAdapter(List<Flashcard> list, Context context, SetActivity activity) {
        super(context, R.layout.card_item, list);
        this.list = new ArrayList<>(list);
        this.context = context;
        this.activity = activity;
    }

    private static class CardHolder {
        public TextView english;
        public TextView chinese;
        public TextView parameters;
        public LinearLayout layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardHolder holder = null;

        final Flashcard card = list.get(position);
        if(convertView==null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.card_item, null);

            holder = new CardHolder();
            holder.chinese = (TextView) convertView.findViewById(R.id.card_chinese);
            holder.english = (TextView) convertView.findViewById(R.id.card_english);
            holder.parameters = (TextView) convertView.findViewById(R.id.card_params);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.card_item_layout);
            convertView.setTag(holder);
        } else {
            holder = (CardHolder) convertView.getTag();
        }

        holder.english.setText(card.getEnglish().getWord());
        holder.chinese.setText(card.getChinese().getWord());
        DecimalFormat format = new DecimalFormat("0.00");
        holder.parameters.setText(format.format(card.getAverageInterval()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CardActivity.class);
                intent.putExtra("cardId", card.getId());
                activity.startActivity(intent);
            }
        });
        return convertView;
    }
}