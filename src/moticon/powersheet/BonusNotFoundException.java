package moticon.powersheet;

public class BonusNotFoundException extends Exception{
	public static final long serialVersionUID = 1L;
	
	public BonusNotFoundException(){
		super();
	}
	
	public BonusNotFoundException(String msg){
		super(msg);
	}
}
