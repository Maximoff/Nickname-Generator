package ru.maximoff.namegen;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ru.maximoff.namegen.R;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Adapter;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final String[] values = new String[11];
		for (int i = 0; i <= 10; i++) {
			values[i] = String.valueOf(i + 5);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, values);
		final Spinner range = findViewById(R.id.mainSpinner1);
		range.setAdapter(adapter);
		range.setSelection(2);
		final TextView text = findViewById(R.id.mainTextView1);
		final Button button = findViewById(R.id.mainButton1);
		final NameGenerator generator = new NameGenerator();
		generator.setLength(7);
		range.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					generator.setLength(p3 + 5);
					text.setText(generator.getName());
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
				}
			});
		button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					generator.setLength(range.getSelectedItemPosition() + 5);
					text.setText(generator.getName());
				}
			});
		text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					setClipboard(generator.getLast());
				}
			});
		text.setText(generator.getName());
    }

	private void setClipboard(String text) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("Copied Text", text);
		clipboard.setPrimaryClip(clip);
		Toast.makeText(this, getString(R.string.copied, text), Toast.LENGTH_SHORT).show();
	}
}
