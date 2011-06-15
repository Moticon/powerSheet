package moticon.powersheet;

/*
 * This class should be something that you create and it has nothing
 * or one bonus and associated type, and perhaps ceiling in it.
 * when ceiling is 0 then it is not a limit. 
 * 
 * as the bonus evolves you can add or remove bonuses, and get the current 
 * bonus value.
 */

public class Bonus {
	
	private String type;
	private int value;
	private int ceiling;
	
	Bonus (String type, int value, int ceiling){
		setType(type);
		setValue(value);
		setCeiling(ceiling);
	}
	
	Bonus (String type, int value){
		setType(type);
		setValue(value);
		setCeiling(0);
	}
	
	Bonus(String type){
		setType(type);
		setValue(0);
		setCeiling(0);
	}
	
	void setType(String type){
		this.type = type;
	}
	String getType(){
		return(type);
	}
	
	void setValue(int value){
		this.value = value;
	}
	int getValue(){
		return(value);
	}
	
	void setCeiling(int ceiling){
		this.ceiling = ceiling;
	}
    int getCeiling(){
		return(this.ceiling);
	}

}
