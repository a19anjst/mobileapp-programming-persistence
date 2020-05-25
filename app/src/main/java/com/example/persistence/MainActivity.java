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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
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
        Button Read = findViewById(R.id.read);
        Button Write = findViewById(R.id.write);
        Button Delete = findViewById(R.id.delete);
        Read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGods(namn, location, gender);
            }
        });
    }
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
    class DatabaseTables {

        public class GodsTable {

        static final String TABLE_NAME_GODS = "gods";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_LOCATION = "location";
        static final String COLUMN_NAME_GENDER = "gender";

    }

    static final String SQL_CREATE_TABLE_GODS =
            // "CREATE TABLE mountain (id INTEGER PRIMARY KEY, name TEXT, height INT)"
            "CREATE TABLE " + GodsTable.TABLE_NAME_GODS + " (" +
                    GodsTable.COLUMN_NAME_NAME + " TEXT," +
                    GodsTable.COLUMN_NAME_LOCATION + " TEXT," +
                    GodsTable.COLUMN_NAME_GENDER + " TEXT)";

    static final String SQL_DELETE_TABLE_GODS =
            // "DROP TABLE IF EXISTS mountain"
            "DROP TABLE IF EXISTS " + GodsTable.TABLE_NAME_GODS;

}



class DatabaseHelper extends SQLiteOpenHelper {

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
class SqliteActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

    }
        private long addGods (String name, String location, String gender){
            ContentValues values = new ContentValues();
            values.put(DatabaseTables.GodsTable.COLUMN_NAME_NAME, name);
            values.put(DatabaseTables.GodsTable.COLUMN_NAME_LOCATION, location);
            values.put(DatabaseTables.GodsTable.COLUMN_NAME_GENDER, gender);
            return database.insert(DatabaseTables.GodsTable.TABLE_NAME_GODS, null, values);
        }
        private List<Gods> getGods () {
            Cursor cursor = database.query(DatabaseTables.GodsTable.TABLE_NAME_GODS, null, null, null, null, null, null);
            List<Gods> Gods = new ArrayList<>();
            while (cursor.moveToNext()) {
                Gods gods = new Gods(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.GodsTable.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.GodsTable.COLUMN_NAME_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.GodsTable.COLUMN_NAME_GENDER))
                );
                Gods.add(gods);
            }
            cursor.close();
            return Gods;
        }
        private int deleteGods ( long id){
            String selection = DatabaseTables.GodsTable.COLUMN_NAME_NAME + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            return database.delete(DatabaseTables.GodsTable.TABLE_NAME_GODS, selection, selectionArgs);
        }
    }

