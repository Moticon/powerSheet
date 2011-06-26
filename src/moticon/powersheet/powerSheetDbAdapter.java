/*
 * PowerSheet database class.
 * Date: June 22, 2011
 * This is the class that handles writing and reading from the database
 * 6/22 - this only provides support for one table in the database.. adding multiple tables 
 *    should be challenging!
 */
package moticon.powersheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class powerSheetDbAdapter {
    private static String dbPath = "/data/data/moticon.powersheet/databases/"; 
	private static final String dbName = "powerSheetDb"; //name of the overall database.
	private static int dbVersion = 1;  // update this when you make major changes
	private DatabaseHelper helper;  // this is a private class below, which extends SQLiteOpenHelper class.
	
	/*
	 *  the Condition table is the heart of it all.  It only has values in it, values that
	 *  point to the other tables.  This is done so that you can grab a value (an id number)
	 *  from the condition table and use it to get all associated names and items from 
	 *  the other tables.  
	 *  
	 *  The idea will be that if this is passed a source name you could search for all 
	 *  the conditions that match that source names id and retrieve them in a cursor.
	 */
	private static final String conditionTableName = "conditions";  // name of the table
	public static final String  conditionKeyId = "_id";			    // Key of table 
	public static final String  conditionSourceId = "sourceId";		// the integer value of the source (eg. bless)
	public static final String  conditionAttributeId = "attributeId"; // the integer value of attribute (eg. AC)
	public static final String  conditionTypeId = "typeId";			// the integer value of type (eg. luck bonus)
	public static final String  conditionAffect = "affectValue";	// the value of the affect (eg. +2 or -2)
	
	// Source Table has two columns the id column and the soureName column.
	private static final String sourceTableName = "sources";		// name of the table
	public static final String sourceKeyId = "_id";					// Key of table 
	public static final String sourceColumnName = "sourceName";		// text field containing the source name
	
	// attribute Table is a list of things that can be affected. 
	private static final String attributeTableName = "attributes";	// name of the table
	public static final String attributeKeyId = "_id";				// Key of table 
	public static final String attributeColumnName = "attributeName"; // text field containing the attribute
	
	// type Table is a list of types of bonuses. 
	private static final String typeTableName = "types";			// name of the table
	public static final String typeKeyId = "_id";					// Key of table 
	public static final String typeColumnName = "typeName";			// text field containing the type
	public static final String stackableColumnName = "stackable";	// integer field, 0 if false, 1 if true.
	
	private static final String TAG = "DbHelper: "; // this is a tag for the logging facility.

	private SQLiteDatabase powerSheetDb;			// database object.
	private Context context;					// context, which is passed to us from the caller
	
	/*
	 *  Constructor for the Power Sheet Adapter... 
	 */	
	public powerSheetDbAdapter(Context context) {
		this.context = context;
		
		// create or open the database.  This points to the custom
		//  class below which extends SQLiteOpenHelper
		helper = new DatabaseHelper(context, powerSheetDb);
		
		// create the database (this copies from our static if it's not there.
        try {
        	helper.createDataBase();
	 	} catch (IOException ioe) {
	 		throw new Error("Unable to create database");
	 	}
	 	// open the database
	 	try {
	 		helper.openDataBase();
	 	}catch(SQLException sqle){
	 		throw sqle;
	 	}
			
		// Assign the db object returned by the get database method to our local
		// variable, which is type SQLiteDatabase...
		powerSheetDb = helper.getWritableDatabase();
	}
	
	/*
	 * -------------------  Database Universal Methods  ------------------------
	 */

	/*
	 * the DatabaseHelper class is here to handle opening, creating, upgradeing dbase.
	 * as of 6/22/2011 it only handles all 4 tables in the database.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private final Context helperContext;
		private SQLiteDatabase helperPowerSheetDb;
		
		public DatabaseHelper(Context context, SQLiteDatabase db){
			super(context, dbName, null, dbVersion);
			helperContext = context;
			helperPowerSheetDb = db;
		}
		
		/*
		 * This was taken from: http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
		 *   This method creates the database if it doesn't exist.
		 */
		public void createDataBase() throws IOException{
	    	boolean dbExist = checkDataBase();
	 
	    	if(dbExist){
	    		//do nothing - database already exist
	    	}else{
	    		//By calling this method an empty database will be created into the default system path
	               //of your application so we are going to be able to overwrite that database with our database.
	        	this.getReadableDatabase();
	        	try {
	    			copyDatabase();
	    		} catch (IOException e) {
	    			Log.w(TAG, e.toString());
	        		throw new Error("Error copying database");
	        	}
	    	}
	    }
		
		/*
		 * This was taken from: http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
		 *   This method checks to see if the local app's database exists.
		 */
	    private boolean checkDataBase(){
	    	SQLiteDatabase checkDB = null;
	    	
	    	try{
	    		String myPath = dbPath + dbName;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    	}catch(SQLiteException e){
	    		//database does't exist yet.
	    	}
	    	
	    	if(checkDB != null){
	    		checkDB.close();
	    	}
	    	return checkDB != null ? true : false;
	    }
	    
		 /**
		  *  Also copied from the above web site.. Basically a copy method.
	     * Copies your database from your local assets-folder to the just created empty database in the
	     * system folder, from where it can be accessed and handled.
	     * This is done by transfering bytestream.
	     * */
	    private void copyDatabase() throws IOException{
	    	//Open your local db as the input stream
	    	InputStream myInput = helperContext.getAssets().open(dbName);
	     	
	    	// Path to the just created empty db
	    	String outFileName = dbPath + dbName;
	 
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);

	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	    }

		
	    public void openDataBase() throws SQLException{
	    	//Open the database
	        String myPath = dbPath + dbName;
	    	helperPowerSheetDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    }
		
	    @Override
		public synchronized void close() {
	    	    if(helperPowerSheetDb != null)
	    	    	helperPowerSheetDb.close();
	    	    super.close();
		}
	   		
		/*
		 * this method is called from getWritableDatabase method if the database doesn't exist.
		 */
		@Override
		public void onCreate(SQLiteDatabase db){
/*			// First create the main condition table.  This has all the conditions associated
			//   with a given source (such as haste)
			db.execSQL("create table "  + conditionTableName + 
					" (" + conditionKeyId + " integer primary key autoincrement, " +
					conditionSourceId + " text not null, " +
					conditionAttributeId + " text not null, " +
					conditionTypeId + " text not null, " +
					conditionAffect + " text not null) ");
			
			// now create the source table, which converts conditions to unique id's, or id's to
			//   condition strings.
			db.execSQL("create table " + sourceTableName + 
					" (" + sourceKeyId + " integer primary key autoincrement, " +
					sourceColumnName + "  text not null)");
			
			// now create the attribute table, which converts conditions to unique id's, or id's to
			//   condition strings.
			db.execSQL("create table " + attributeTableName + 
					" (" + attributeKeyId + " integer primary key autoincrement, " +
					attributeColumnName + "  text not null)");
			
			// now create the types table, which converts types of affeccts to unique id's, or id's to
			//   affect strings.  This table also has the stackable column, which tells us if the
			//   type of affect stacks with itself (such as dodge (yes) or enhancment (typically no)
			db.execSQL("create table " + typeTableName + 
					" (" + typeKeyId + " integer primary key autoincrement, " +
					typeColumnName + "  text not null, " +
					stackableColumnName + " text not null)");
	*/	}
		
		/*
		 * brute force.  If the table exists, then we delete it and create a new one with new ver #
		 */		
		@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 /*           Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + conditionTableName);
            db.execSQL("DROP TABLE IF EXISTS " + sourceTableName);
            db.execSQL("DROP TABLE IF EXISTS " + typeTableName);
            db.execSQL("DROP TABLE IF EXISTS " + attributeTableName);
            onCreate(db);
 */       }
	}  // ends helper class

	public void closeDb(){
		helper.close();
	}
	
	/*
	 *  this just calls the onupgrade() method, which drops all tables and then 
	 *  calls the mthod to recreate the tables again... 
	 */
	public void resetDatabase(){
		helper.onUpgrade(powerSheetDb, 1, 1);
	}
	
	/*
	 *  -------------------  Source table Methods  ------------------------
	 */
	
	/*
	 * this method adds a new source to the table "sourceTable". It will be used to populate the database.
	 */
	public void addSource(String sourceString){
		ContentValues cv = new ContentValues();
		cv.put(sourceColumnName, sourceString);
		try {
			powerSheetDb.insert(sourceTableName, null, cv);
		}
		catch(Exception e)
		{
			Log.e(TAG+"1", e.toString());
		}
	}
	
	/*
	 * Here's one to allow us to delete a row. It was in the tutorial and might be handy... 
	 */
	public void deleteSource(long key){
		try {
			powerSheetDb.delete(sourceTableName, sourceKeyId + "=" + key, null);
		}
		catch(Exception e) {
			Log.e(TAG, e.toString());
		}
	}
	
	/* 
	 * this will allow us to change a source name (for example if we see a typo error)
	 */
	public void updateSourceName(long key, String newSourceString){
		ContentValues cv = new ContentValues();
		cv.put(sourceColumnName, newSourceString);
		try {
			powerSheetDb.update(sourceTableName, cv, sourceKeyId + "=" + key, null);
		}
		catch(Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	/*
	 * This method returns the key id when given the source name string. It's so you can use the 
	 * key id for dipping into other tables.. 
	 */
	public long getSourceKeyByName(String sourceNameString){
		long keyId = 0; // initially set to zero. if there's an error we'll return zero instead of key value.
		
		Cursor cursor;
		
		try {
			cursor = powerSheetDb.query(sourceTableName, 
									    new String[] {sourceKeyId, sourceColumnName},
									    sourceColumnName + "='" + sourceNameString +"'",
									    null, null, null, null);
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				keyId = cursor.getLong(0);
			}
		}
		catch (SQLException e) {
			Log.e(TAG, e.toString());
		}
		
		return keyId;
	}
	
	/*
	 * This method returns the key id when given the source name string. It's so you can use the 
	 * key id for dipping into other tables.. 
	 */
	public String getSourceNameByKey(long keyId){
		String sourceName = ""; // initially set to zero. if there's an error we'll return zero instead of key value	
		Cursor cursor;
		
		try {
			cursor = powerSheetDb.query(sourceTableName, 
									    new String[] {sourceKeyId, sourceColumnName},
									    sourceKeyId + "=" + keyId,
									    null, null, null, null);
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				sourceName = cursor.getString(1);  // fields start at 0, column 1 is the source name.
			}
		}
		catch (SQLException e) {
			Log.e(TAG, e.toString());
		}
		return sourceName;
	}
	
	/*
	 * This method returns a cursor containing everything in the sources table.
	 */
	public Cursor getSources(){	
		Cursor cursor=null;
		try {
			cursor = powerSheetDb.query(sourceTableName, 
									    new String[] {sourceKeyId, sourceColumnName},
									    null, null, null, null, null);
		}
		catch (SQLException e) {
			Log.e(TAG, e.toString());
		}
		return cursor;
	}
	
	public ArrayList<BonusList> getAllBonuses(){
	    ArrayList<BonusList> returnBonusList;
	    returnBonusList = new ArrayList<BonusList>();
	    
	    Cursor cursor=null;
	    try {
	        cursor = powerSheetDb.query(typeTableName, 
	                                    new String[] {typeColumnName, stackableColumnName},
	                                    null, null, null, null, null);
	    }
	    catch (SQLException e) {
	        Log.e(TAG, e.toString());
	    }
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()){
	        returnBonusList.add(new BonusList(cursor.getString(0), cursor.getInt(1)));    
	        cursor.moveToNext();
	    }
	    return returnBonusList;
	}

}
