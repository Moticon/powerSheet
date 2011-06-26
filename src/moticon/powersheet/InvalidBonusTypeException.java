package moticon.powersheet;

public class InvalidBonusTypeException extends Exception{
	//This exception is used when a bonusType is not found.
	public static final long serialVersionUID = 1L;
	
	public InvalidBonusTypeException(String msg){
		super(msg);
	}
	
	public InvalidBonusTypeException(){
		super();
	}
}
