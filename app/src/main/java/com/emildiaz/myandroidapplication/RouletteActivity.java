package com.emildiaz.myandroidapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.preferences) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
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
            case 6:
                intent = new Intent("com.unknown.action");
                break;
        }

        if (!isIntentAvailable(this, intent)) {
            Toast.makeText(this, "No application available to handle this action", Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(intent);
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
