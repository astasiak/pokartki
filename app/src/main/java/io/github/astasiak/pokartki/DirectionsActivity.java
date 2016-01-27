package io.github.astasiak.pokartki;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Andrzej on 26.01.2016.
 */
public class DirectionsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.directions);
    }
}
