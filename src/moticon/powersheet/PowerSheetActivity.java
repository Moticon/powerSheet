package moticon.powersheet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class PowerSheetActivity extends Activity {
	BonusManager bManager; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView test = (TextView) findViewById(R.id.testDisplay);
        Button testButton = (Button) findViewById(R.id.showTestButton);
        
        testButton.setOnClickListener(onTest);
        
        ArrayList<BonusType> testList = new ArrayList<BonusType>();
        testList.add(new BonusType("Dodge",1));
        testList.add(new BonusType("Competence",0));
        bManager = new BonusManager(testList);
        
        try{
        	bManager.addBonus(new Bonus("Dodge", 2, "Strength", "Dodge Feat", 1, false));
        	bManager.addBonus(new Bonus("Dodge", 1, "Strength", "Dodge Feat", 2, false));
        	bManager.removeBonus(new Bonus("Dodge", 1, "Strength", "Dodge Feat", 2, false));
        }catch(InvalidBonusTypeException e){
        	test.setText(e.getMessage());
        }catch(BonusNotFoundException e){
        	test.setText(e.getMessage());
        }
        
        
    }
    private View.OnClickListener onTest = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Context ctext = getApplicationContext();
			CharSequence text = Integer.toString(bManager.getValue());
			int duration = Toast.LENGTH_SHORT;
			
			Toast toast = Toast.makeText(ctext, text, duration);
			toast.show();
		}
	};
}