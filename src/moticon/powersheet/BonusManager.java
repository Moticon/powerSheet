package moticon.powersheet;

import java.util.ArrayList;
import java.util.ListIterator;

public class BonusManager {
	private int value;
	private ArrayList<BonusList> list;
	
	public BonusManager(ArrayList<BonusType> enchArray){
		ListIterator<BonusType> iterator = enchArray.listIterator();
		BonusType temp;
		list = new ArrayList<BonusList>();
		
		while(iterator.hasNext()){
			temp = new BonusType(iterator.next());
			list.add(new BonusList(temp.getName(),temp.getStackable()));
		}
		value = 0;	
	}
	
	private void updateValue(){
		ListIterator<BonusList> iterator = list.listIterator();
		value = 0;
		while(iterator.hasNext()){
			value += iterator.next().getValue();
		}
	}
	
	public void addBonus(Bonus b) throws InvalidBonusTypeException{
		ListIterator<BonusList> iterator = list.listIterator();
		BonusList temp;
		boolean found = false;
		
		while(iterator.hasNext() && !found){
			temp = iterator.next();
			if(temp.getType().equals(b.getType())){
				temp.add(b);
				found = true;
			}
		}
		if(!found){
			throw new InvalidBonusTypeException("The bonus type was not found in the list");			
		}
		
		updateValue();
	}
	
	public void removeBonus(Bonus b) throws BonusNotFoundException, InvalidBonusTypeException{
		ListIterator<BonusList> iterator = list.listIterator();
		BonusList temp;
		boolean succeeded = false;
		boolean found = false;
		
		while(iterator.hasNext() && !found){
			temp = iterator.next();
			if(temp.getType().equals(b.getType())){
				succeeded = temp.remove(b);
				found = true;
			}
		}
		
		if(!found){
			throw new InvalidBonusTypeException("The bonus type was not found in the list");
		}
		if(!succeeded){
			throw new BonusNotFoundException("The indicated bonus was not found in the list");
		}
		
		updateValue();
	}
	
	public void clearBonuses(){
		ListIterator<BonusList> iterator = list.listIterator();
		
		while(iterator.hasNext()){
			iterator.next().clear();
		}
		
		value = 0;
	}
	
	public int getValue(){
		return value;
	}

} 	
