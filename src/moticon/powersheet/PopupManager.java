package moticon.powersheet;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PopupManager {
	private Context context;
	
	/*
	 * database popup window widgets
	 */
	EditText newSourceText;
	EditText requestedSourceKey;
	TextView returnedSourceTextView;

	powerSheetDbAdapter powersheetDb;
	Activity parentActivity;
	PopupWindow databasePopupWindow;
	View popupView;
	
	
	public PopupManager(Context c, powerSheetDbAdapter db, Activity activity){
		this.context = c;
		this.powersheetDb = db;
		this.parentActivity = activity;
	}
	
    public void openPopupDatabaseInputView(){
    	// First get a layout inflater object so we can inflate teh popupLayout
    	LayoutInflater popupInflator = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	// now create a view object.  the inflater is given the xml file name (arg1), and the 
    	//    highest level View Object we want as the root of the view (in this case the Linear layout in xml file)
    	View popupLayout = popupInflator.inflate(R.layout.popup_database_window, 
    			                                 (ViewGroup) parentActivity.findViewById(R.id.popupLayoutView));
    	// now that we hvae a layout, we create the popup window, position it, and make it active.
    	databasePopupWindow = new PopupWindow(popupLayout, 900,800 , true);
    	// show the popup window on the screen... 
    	databasePopupWindow.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);
    	// (Thanks to Richard for figuring this one out) Now get the view object of the popup window
    	//   so that we can use it to find the objects in the view. Without this all "findViewById" 
    	//   methods return null
    	popupView = databasePopupWindow.getContentView();
    	
    	/*
    	 * now assign up all the widgets in the popup view to the objects we are creating or 
    	 * declared above. 
    	 */
    	requestedSourceKey = (EditText) popupView.findViewById(R.id.requestedSourceKey);
    	returnedSourceTextView = (TextView) popupView.findViewById(R.id.sourceNameReturned);  
    	newSourceText = (EditText) popupView.findViewById(R.id.newSourceText);
    	// create the buttons
    	Button addSourceButton = (Button) popupView.findViewById(R.id.addSourceButton);
    	Button getSourceByKeyButton = (Button) popupView.findViewById(R.id.getSourceButton);
    	Button resetDatabaseButton = (Button) popupView.findViewById(R.id.resetDatabaseButton);
    	Button closePopupButton = (Button) popupView.findViewById(R.id.databaseInputDoneButton);
    	Button getSourcesButton = (Button) popupView.findViewById(R.id.getSourceListButton);
    	
    	//  Now point the the listeners to the private methods below.  
       	closePopupButton.setOnClickListener(cancelButtonListener);
    	resetDatabaseButton.setOnClickListener(resetButtonListener);
    	addSourceButton.setOnClickListener(addSourceButtonListener); 
	  	getSourceByKeyButton.setOnClickListener(getSourceByKeyButtonListener);
	  	getSourcesButton.setOnClickListener(getSourcesButtonListener);
    }

    // in each of these we simply point it to another method that does the work.
    // I suppose we could put the method directly in the onClick method, but this seems 
    // to be a little more clear to me, especially when I'm looking for a specific method.
    private OnClickListener cancelButtonListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		databasePopupWindow.dismiss();
    	}
    };
    
    private OnClickListener resetButtonListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
        	powersheetDb.resetDatabase();
        	clearTextFields();
    	}
    };
    
    private OnClickListener addSourceButtonListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
        	powersheetDb.addSource(newSourceText.getText().toString());
        	clearTextFields();
    	}
    };
    
    private OnClickListener getSourceByKeyButtonListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
        	long keyId = Long.parseLong(requestedSourceKey.getText().toString());
        	String sourceName = powersheetDb.getSourceNameByKey(keyId);
        	if (sourceName.equals(""))
        		sourceName = "not Found";
        	returnedSourceTextView.setText(sourceName);
        	//clearTextFields();
    	}
    };
    
   /*
    * Whew! This took awhile to get working..   It refers to two different xml files
    *    The first is the popup_database_window file, where you can find the widget named queryResults.
    *        queryResults defines a listView, it's size and attributes, etc.  This is the widget
    *        that all of this code is building an adapter for.
    *    The second xml file is listlayout.xml -- this is where you find the two text fields in a
    *        horizontal linear layout.  The two fields are number_entry and name_entry.  This layout 
    *        basically defines each row.  You've got to get the names right for this to work.
    */
    private OnClickListener getSourcesButtonListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		// call the database helper (through powersheetDb object) to get the cursor returned.
        	Cursor getSourceCursor = powersheetDb.getSources();
        	// this method is depracated. It runs in teh UI, which is frowned upon.  
        	// Until I figure out how to use the cursorLoader we'll go on with it... ;-)
        	// basically this starts up the system to manage the lifecycle of teh cursor object..
        	parentActivity.startManagingCursor(getSourceCursor);
        	
        	// create an array to specify which fields we want to display  Use the column names from the 
        	// table you're working wit.
        	String[] from = new String[]{"_id","sourceName"};
        	// create an array of the display item we want to bind our data to.  use the entries from listlayout.xml
        	//   or your layoutfile.  
        	int[] to = new int[]{R.id.number_entry,R.id.name_entry};
        	// create simple cursor adapter.  Pass it the layoutfile where the "to" fields can be found,
        	//  the cursor, teh columns to copy from, and the xml layout fields defined in "to"
        	SimpleCursorAdapter queryResultsAdapter = 
        	      new SimpleCursorAdapter(context, R.layout.listlayout, getSourceCursor, from, to );
        	//localAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        	// Finally! Get the listview object from the popuplayout xml file (where things go on the screen)
        	ListView queryResultsListView = (ListView) popupView.findViewById( R.id.queryResults);
        	// set the adapter for the listview as the localAdapter we just built! We're done!
        	queryResultsListView.setAdapter(queryResultsAdapter);
    	}
    };

    private void clearTextFields(){
    	newSourceText.setText("");
    	requestedSourceKey.setText("");
    }


}
