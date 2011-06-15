package moticon.powersheet;

public class StrengthValue {
   Bonus enhancement;
   Bonus alchemical;
   Bonus inherent;
   Bonus size;
   Bonus racial;
   
   int baseScore;
   int currentScore;
   
   StrengthValue(int base, int racial){
	   this.baseScore = base;
	   this.racial = new Bonus("racial", racial);
	   this.enhancement = new Bonus("enhancement");
	   this.alchemical = new Bonus("alchemical");
	   this.inherent = new Bonus("inherent");
	   this.size = new Bonus("size");
	   calculateCurrentScore();
   }
   
   void calculateCurrentScore(){
	   currentScore = baseScore + enhancement.getValue()
	                            + alchemical.getValue()
	                            + inherent.getValue()
	                            + size.getValue()
	                            + racial.getValue();
   }
   
   
}
