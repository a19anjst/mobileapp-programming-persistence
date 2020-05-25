package com.example.persistence;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences myPreferenceRef = getPreferences(MODE_PRIVATE);
    SharedPreferences.Editor myPreferenceEditor = myPreferenceRef.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPreferenceRef = getPreferences(MODE_PRIVATE);
        myPreferenceEditor = myPreferenceRef.edit();
    }
    public void savePref(View v){
        // Get the text
        EditText newPrefText1 = findViewById(R.id.editview1);
        EditText newPrefText2 = findViewById(R.id.editview2);
        EditText newPrefText3 = findViewById(R.id.editview3);

        // Store the new preference
        myPreferenceEditor.putString("MyAppPreferenceString", newPrefText1.getText().toString());
        myPreferenceEditor.apply();
        myPreferenceEditor.putString("MyAppPreferenceString", newPrefText2.getText().toString());
        myPreferenceEditor.apply();
        myPreferenceEditor.putString("MyAppPreferenceString", newPrefText3.getText().toString());
        myPreferenceEditor.apply();

        // Display the new preference
        TextView prefTextRef=findViewById(R.id.prefText);

        savePref(prefTextRef);
        prefTextRef.setText(myPreferenceRef.getString("MyAppPreferenceString", "No preference found."));

        // Clear the EditText
        newPrefText1.setText("");
        newPrefText2.setText("");
        newPrefText3.setText("");
    }

}
    private class DatabaseTables {
    static class Gods {

        static final String TABLE_NAME_GODS = "gods";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_LOCATION = "location";
        static final String COLUMN_NAME_GENDER = "gender";

    }

    static final String SQL_CREATE_TABLE_MOUNTAIN =
            // "CREATE TABLE mountain (id INTEGER PRIMARY KEY, name TEXT, height INT)"
            "CREATE TABLE " + Gods.TABLE_NAME_GODS + " (" +
                    Gods.COLUMN_NAME_NAME + " TEXT," +
                    Gods.COLUMN_NAME_LOCATION + " TEXT," +
                    Gods.COLUMN_NAME_GENDER + " INT)";

    static final String SQL_DELETE_TABLE_MOUNTAIN =
            // "DROP TABLE IF EXISTS mountain"
            "DROP TABLE IF EXISTS " + Gods.TABLE_NAME_GODS;

}

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // If this is incremented onUpgrade() will be executed
    private static final String DATABASE_NAME = "Gods.db"; // The file name of our database

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // This method is executed only if there is not already a database in the file `Mountain.db`
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseTables.SQL_CREATE_TABLE_GODS);
    }

    // This method is executed only if the database version has changed, e.g. from 1 to 2
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DatabaseTables.SQL_DELETE_TABLE_GODS);
        onCreate(sqLiteDatabase);
    }

}
public class SqliteActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        // Create
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

    }
    private long addGods(String name, int height) {
        ContentValues values = new ContentValues();
        values.put(DatabaseTables.Gods.COLUMN_NAME_NAME, name);
        values.put(DatabaseTables.Gods.COLUMN_NAME_LOCATION, location);
        return database.insert(DatabaseTables.Gods.TABLE_NAME_GODS, null, values);
    }
    private List<Gods> getGods() {
        Cursor cursor = database.query(DatabaseTables.Gods.TABLE_NAME_GODS, null, null, null, null, null, null);
        List<Gods> mountains = new ArrayList<>();
        while (cursor.moveToNext()) {
            Gods gods = new Gods(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseTables.Gods.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Gods.COLUMN_NAME_LOCATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseTables.Gods.COLUMN_NAME_GENDER))
            );
           gods.add(gods);
        }
        cursor.close();
        return gods;
    }
    private int deleteGods(long id) {
        String selection = DatabaseTables.Gods.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        return database.delete(DatabaseTables.Gods.TABLE_NAME, selection, selectionArgs);
    }
}

