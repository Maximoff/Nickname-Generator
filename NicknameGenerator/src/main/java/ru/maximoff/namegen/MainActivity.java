package ru.maximoff.namegen;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.URLSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

public class MainActivity extends Activity {
	private boolean initSpin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final Settings set = new Settings(this);
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
		firstLett.setSelection(set.geti("letter", 0));
		int size = generator.max() - generator.min();
		int prgrs = set.geti("size", generator.def() - generator.min());
		final TextView length = findViewById(R.id.mainTextView3);
		final SeekBar range = findViewById(R.id.mainSeekBar1);
		range.setMax(size);
		range.setProgress(prgrs);
		length.setText(getString(R.string.length, prgrs + generator.min()));
		final CheckBox firstCap = findViewById(R.id.mainCheckBox1);
		firstCap.setChecked(set.getb("first_cap", true));
		final CheckBox allCap = findViewById(R.id.mainCheckBox2);
		allCap.setChecked(set.getb("all_cap", false));
		final CheckBox doubleVow = findViewById(R.id.mainCheckBox3);
		doubleVow.setChecked(set.getb("double_vow", true));
		final TextView text = findViewById(R.id.mainTextView1);
		final Button button = findViewById(R.id.mainButton1);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View p1) {
				switch (p1.getId()) {
					case R.id.mainCheckBox1:
						set.setb("first_cap", ((CheckBox) p1).isChecked());
						break;

					case R.id.mainCheckBox2:
						boolean checked = ((CheckBox) p1).isChecked();
						firstCap.setEnabled(!checked);
						set.setb("all_cap", checked);
						break;

					case R.id.mainCheckBox3:
						set.setb("double_vow", ((CheckBox) p1).isChecked());
						break;

					default:
						break;
				}
				generator.setFirstChar(lettArray[firstLett.getSelectedItemPosition()]);
				generator.firstToUpper(firstCap.isChecked());
				generator.allToUpper(allCap.isChecked());
				generator.setDouble(doubleVow.isChecked());
				generator.setLength(range.getProgress() + generator.min());
				String nick = generator.getName();
				text.setText(nick);
				set.sets("last", nick);
			}
		};
		firstLett.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
					if (initSpin) {
						set.seti("letter", p3);
						generator.setFirstChar(lettArray[p3]);
						generator.firstToUpper(firstCap.isChecked());
						generator.allToUpper(allCap.isChecked());
						generator.setDouble(doubleVow.isChecked());
						generator.setLength(range.getProgress() + generator.min());
						String nick = generator.getName();
						text.setText(nick);
						set.sets("last", nick);
					} else {
						initSpin = true;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> p1) {
				}
			});
		range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				private boolean touch = false;

				@Override
				public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
					if (!touch) {
						return;
					}
					set.seti("size", p2);
					generator.setFirstChar(lettArray[firstLett.getSelectedItemPosition()]);
					generator.firstToUpper(firstCap.isChecked());
					generator.allToUpper(allCap.isChecked());
					generator.setDouble(doubleVow.isChecked());
					generator.setLength(p2 + generator.min());
					length.setText(getString(R.string.length, p2 + generator.min()));
					String nick = generator.getName();
					text.setText(nick);
					set.sets("last", nick);
				}

				@Override
				public void onStartTrackingTouch(SeekBar p1) {
					touch = true;
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1) {
					touch = false;
				}
			});
		button.setOnClickListener(listener);
		allCap.setOnClickListener(listener);
		firstCap.setOnClickListener(listener);
		doubleVow.setOnClickListener(listener);
		text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					setClipboard(text.getText().toString());
				}
			});
		String year = DateFormat.format("yyyy", new Date()).toString();
		if (!year.equals("2020")) {
			year = "2020 - " + year;
		}
		final TextView copyright = findViewById(R.id.mainTextView2);
		copyright.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					Intent open = new Intent(Intent.ACTION_VIEW);
					open.setData(Uri.parse("https://maximoff.su/about/?from=soft"));
					startActivity(open);
				}
			});
		setHyperlinkText(copyright, "© Maximoff, " + year);
		String nick = set.gets("last", null);
		if (nick == null) {
			button.performClick();
		} else {
			text.setText(nick);
		}
    }

	private void setHyperlinkText(final TextView tv, final String text) {
		final SpannableString spannableString = new SpannableString(text);
		spannableString.setSpan(new URLSpan(""), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(spannableString, TextView.BufferType.SPANNABLE);
		tv.setClickable(true);
	}

	private void setClipboard(String text) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(getText(R.string.app_name), text);
		clipboard.setPrimaryClip(clip);
		Toast.makeText(this, getString(R.string.copied, text), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		if (Build.VERSION.SDK_INT < 21) {
			finish();
		} else {
			finishAndRemoveTask();
		}
	}
}
