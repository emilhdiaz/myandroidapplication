package com.emildiaz.myandroidapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class RouletteActivity extends ActionBarActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.intents, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_roulette, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        int position = spinner.getSelectedItemPosition();
        Intent intent = null;

        switch (position) {
            case 0:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                break;
            case 1:
                intent = new Intent(Intent.ACTION_DIAL);
                break;
            case 2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:40.747686,-73.9857852?z=19"));
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Bryant+Park+NYC"));
                break;
            case 4:
                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                break;
            case 5:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"));
                break;
        }

        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            String result = data.toURI();
            Toast.makeText(this, result, Toast.LENGTH_LONG);
        }
    }
}
