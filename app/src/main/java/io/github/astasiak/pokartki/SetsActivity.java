package io.github.astasiak.pokartki;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import io.github.astasiak.pokartki.dao.FlashcardSet;
import io.github.astasiak.pokartki.dao.FlashcardSetsDao;


public class SetsActivity extends AppCompatActivity {

    private ListView setsListView;
    private FlashcardSetsDao flashcardSetsDao;
    private SetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.flashcardSetsDao = new FlashcardSetsDao(getApplicationContext());
        List<FlashcardSet> list = flashcardSetsDao.list();

        setContentView(R.layout.activity_sets);
        adapter = new SetAdapter(list, getApplicationContext(), this.flashcardSetsDao);

        setsListView = (ListView) findViewById(R.id.setsListView);
        setsListView.setAdapter(adapter);
    }
}


class SetAdapter extends ArrayAdapter<FlashcardSet> {

    private List<FlashcardSet> list;
    private Context context;
    private FlashcardSetsDao setsDao;

    public SetAdapter(List<FlashcardSet> list, Context context, FlashcardSetsDao setsDao) {
        super(context, R.layout.set_item, list);
        this.list = new ArrayList<>(list);
        this.context = context;
        this.setsDao = setsDao;
    }

    private static class SetHolder {
        public TextView name;
        public TextView description;
        public CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SetHolder holder = null;

        final FlashcardSet set = list.get(position);
        if(convertView==null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.set_item, null);

            holder = new SetHolder();
            holder.name = (TextView) convertView.findViewById(R.id.set_name);
            holder.description = (TextView) convertView.findViewById(R.id.set_info);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.set_checkbox);
            convertView.setTag(holder);

            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setsDao.setActive(set.getId(), isChecked);
                }
            });
        } else {
            holder = (SetHolder) convertView.getTag();
        }

        holder.name.setText(set.getName());
        holder.description.setText(set.getId().toString());
        holder.checkbox.setChecked(set.isActive());
        holder.checkbox.setTag(set);
        return convertView;
    }
}