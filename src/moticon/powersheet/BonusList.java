package moticon.powersheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.lang.Override;

public class BonusList extends ArrayList<Bonus>{
	public static final long serialVersionUID = 1L;
	private String type;
	private int value;
	private int stackable;
	
	//Constructor
	public BonusList(String name1, int stackable1){
		super();
		type = new String(name1);
		value = 0;
		stackable = stackable1;
	}
	
	//Updates the value of list
	private void updateValue(){
		int result = 0;
		int temp = 0;
		ListIterator<Bonus> iterator = this.listIterator();
		
		while (iterator.hasNext()){
			temp = iterator.next().getValue();
			if(stackable == 1){
				result += temp;
			}
			else{
				if(temp > result){
					result = temp;
				}
			}
		}
		value = result;
	}
	
	public int getValue(){
		return value;
	}
	
	public String getType(){
		return type;
	}
	/*Overrides to auto-calculate as items are
	 * added or removed.
	 */
	@Override public boolean add(Bonus b){
		super.add(b);
		updateValue();
		return true;
	}
	
	@Override public boolean addAll(Collection <? extends Bonus> collection){
		super.addAll(collection);
		updateValue();
		return true;
	}
	
	@Override public boolean remove(Object o){
		boolean result;
		result = super.remove(o);
		updateValue();
		return result;
	}
	
	@Override public boolean removeAll(Collection <?> collection){
		boolean result;
		result = super.removeAll(collection);
		updateValue();
		return result;
	}
	
	@Override public void clear(){
		super.clear();
		value = 0;
	}
}
