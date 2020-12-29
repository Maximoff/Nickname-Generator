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
import android.widget.CheckBox;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final NameGenerator generator = new NameGenerator();
		final String[] lettArray = new String[letters.length() + 1];
		lettArray[0] = "-";
		for (int i = 0; i < letters.length(); i++) {
			lettArray[i + 1] = String.valueOf(letters.charAt(i));
		}
		ArrayAdapter<String> lettAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lettArray);
		final Spinner firstLett = findViewById(R.id.mainSpinner2);
		firstLett.setAdapter(lettAdapter);
		int size = generator.max() - generator.min();
		final String[] values = new String[size + 1];
		for (int i = 0; i <= size; i++) {
			values[i] = String.valueOf(i + generator.min());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, values);
		final Spinner range = findViewById(R.id.mainSpinner1);
		range.setAdapter(adapter);
		range.setSelection(generator.def() - generator.min());
		final CheckBox firstCap = findViewById(R.id.mainCheckBox1);
		final CheckBox allCap = findViewById(R.id.mainCheckBox2);
		final CheckBox doubleVow = findViewById(R.id.mainCheckBox3);
		final TextView text = findViewById(R.id.mainTextView1);
		final Button button = findViewById(R.id.mainButton1);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View p1) {
				generator.setFirstChar(lettArray[firstLett.getSelectedItemPosition()]);
				generator.firstToUpper(firstCap.isChecked());
				generator.allToUpper(allCap.isChecked());
				generator.setDouble(doubleVow.isChecked());
				generator.setLength(range.getSelectedItemPosition() + generator.min());
				text.setText(generator.getName());
			}
		};
		firstLett.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					generator.setFirstChar(lettArray[p3]);
					text.setText(generator.getName());
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
				}
			});
		range.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					generator.setLength(p3 + generator.min());
					text.setText(generator.getName());
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
				}
			});
		button.setOnClickListener(listener);
		allCap.setOnClickListener(listener);
		firstCap.setOnClickListener(listener);
		doubleVow.setOnClickListener(listener);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
