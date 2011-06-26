package moticon.powersheet;

/*This class is a storage container for bonus types
 * such as "Enhancement", etc. The name field
 * is self explanatory and the stackable field indicates
 * whether the bonus stacks or only uses the max [0 or 1].
 */
public class BonusType {
	private String name;
	private int stackable;
	
	public BonusType(String name1, int stackable1){
		name = new String(name1);
		stackable = stackable1;
	}
	
	public BonusType(BonusType copy){
		name = new String(copy.getName());
		stackable = copy.getStackable();
	}
	
	public String getName(){
		return name;
	}
	
	public int getStackable(){
		return stackable;
	}
}
