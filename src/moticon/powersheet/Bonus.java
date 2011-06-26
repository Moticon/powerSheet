package moticon.powersheet;

import java.lang.Override;
/*
 * The bonus class is the fundamental unit for conditions.
 * It is a single modification to a specific attribute.
 */

public class Bonus {
	
	private String type;
	private int value;
	private String attr;
	private String source;
	private int sourceId;		//The sourceId is assigned by the appropriate manager on the fly
	private boolean magic;
	
	
	public Bonus(String type1, int value1, String attr1, String source1, int sourceId1, boolean magic1){
		type = type1;
		value = value1;
		attr = new String(attr1);
		source = new String(source1);
		sourceId = sourceId1;
		magic = magic1;
	}
	
	public String getType(){
		return type;
	}
	
	public int getValue(){
		return value;
	}
	
	public String getSource(){
		return source;
	}
	
	public int getSourceId(){
		return sourceId;
	}
	
	public boolean getMagic(){
		return magic;
	}
	
	public String getAttribute(){
		return attr;
	}
	
	@Override public boolean equals(Object o){
		//If the object is identical, then return true
		if(this == o)
			return true;
		
		// If the object is the wrong type, immediately return false
		if(!(o instanceof Bonus))
			return false;
		
		//Type cast the object and check for equality
		Bonus leftSide = (Bonus) o;
		return (leftSide.getSourceId() == sourceId) && (leftSide.getValue() == value) &&
			(leftSide.getType().equals(type)) && (leftSide.getMagic() == magic) && (leftSide.getAttribute().equals(attr));
	}
	
	@Override public int hashCode() {     
		throw new UnsupportedOperationException();   
	}
	
	@Override public String toString(){
		StringBuilder result = new StringBuilder();
		
		if(value >= 0){
			result.append("+");
		}
		result.append(value).append(" [").append(source).append("]");
		return result.toString();
	}

}
