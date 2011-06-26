package moticon.powersheet;

public class Attribute {
	private int value;
	private BonusManager bManager;
	private powerSheetDbAdapter db;
	
	public Attribute(powerSheetDbAdapter db1){
		value = 0;
		db = db1;
		bManager = new BonusManager(db);
		updateValue();
	}
	
	public void updateValue(){
		value = bManager.getValue();
	}
	
	public void clearBonuses(){
		bManager.clearBonuses();
	}
	
	public void addBonus(Bonus b) throws InvalidBonusTypeException{
			bManager.addBonus(b);
		updateValue();
	}
	
	public void removeBonus(Bonus b) throws InvalidBonusTypeException, BonusNotFoundException{
		bManager.removeBonus(b);
		updateValue();
	}
	
	public int getValue(){
		return value;
	}
}
