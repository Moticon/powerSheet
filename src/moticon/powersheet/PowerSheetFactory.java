package moticon.powersheet;

import android.app.Activity;

/*
 * THe purpose of this class is to have a place where everything is created, connected,
 * and set up for the working application.  
 */
public class PowerSheetFactory {
	powerSheetDbAdapter powerSheetDb;
	PopupManager popupManager;
	
	public PowerSheetFactory(Activity callingActivity){
		powerSheetDb = new powerSheetDbAdapter(callingActivity);
		popupManager = new PopupManager(callingActivity.getBaseContext(), powerSheetDb, callingActivity);
	}
	
	public powerSheetDbAdapter getDbAdapter(){
		return powerSheetDb;
	}
	
	public PopupManager getPopupManager(){
		return popupManager;
	}

}
