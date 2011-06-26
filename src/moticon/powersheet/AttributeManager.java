package moticon.powersheet;

import java.util.ArrayList;

public class AttributeManager {
	private ArrayList<Attribute> aList;
	private powerSheetDbAdapter db;
	
	public AttributeManager(powerSheetDbAdapter db1){
		db = db1;
	}
}
