package ru.maximoff.namegen;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	public Settings(Context ctx) {
		this.preferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
		this.editor = preferences.edit();
	}

	public void setb(String name, boolean value) {
		editor.putBoolean(name, value);
		editor.commit();
	}

	public boolean getb(String name, boolean defaultValue) {
		return preferences.getBoolean(name, defaultValue);
	}

	public void seti(String name, int value) {
		editor.putInt(name, value);
		editor.commit();
	}

	public int geti(String name, int defaultValue) {
		return preferences.getInt(name, defaultValue);
	}
}
