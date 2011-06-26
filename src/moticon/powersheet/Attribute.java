package moticon.powersheet;

public class Attribute {
	private int value;
	private BonusManager bManager;
	private powerSheetDbAdapter db;
	
	public Attribute(powerSheetDbAdapter db1){
		value = 0;
		db = db1;
	}
}
