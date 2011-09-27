package jp.moo.snsk;

import jp.moo.snsk.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class WariCaLocActivity extends Activity {
    /** Called when the activity is first created. */

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*イベントハンドラの設定パターン1 コードの中でListenerを定義するパターン*/
        //final Button button1 = (Button)findViewById(R.id.Button01);
        final CheckBox checkBox1 = (CheckBox)findViewById(R.id.CheckBox01);
        final EditText editText4 = (EditText)findViewById(R.id.EditText04);
        final EditText editText5 = (EditText)findViewById(R.id.EditText05);
        final Button button1 = (Button)findViewById(R.id.Button01);
        
        button1.setOnClickListener(
        	new OnClickListener(){
        		public void onClick(View v){
        			//「計算！」を押したときの挙動。resultViewActivityにIntent経由でデータを飛ばす
        			Intent intent = new Intent(v.getContext(),jp.moo.snsk.ResultViewActivity.class);
        			try {
						calc();
					} catch (WariCaLocException e) {
						// TODO Auto-generated catch block
						Toast.makeText(WariCaLocActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
						return;
					}
        			
        			intent.putExtra("HEADCOUNT",(hCnt - oHCnt));
        			intent.putExtra("OFFED_HEADCOUNT",oHCnt);
        			intent.putExtra("ANSWER",answer);
        			intent.putExtra("OFFED_ANSWER",(int)actuallyOffPrice);
        			intent.putExtra("SHORTAGE",(int)shortage);
        			intent.putExtra("ANSWER_MOD",answer_mod);
    				intent.putExtra("ISCHECKED", checkBox1.isChecked());
        			startActivityForResult(intent,0);
        		}
        	}
        );
        
        checkBox1.setOnClickListener(
        	new OnClickListener() {
				public void onClick(View v) {
					if(checkBox1.isChecked() != false){
						editText4.setEnabled(true);
						editText4.setInputType(2);
						editText5.setEnabled(true);
						editText5.setInputType(2);
					}else{
						editText4.setEnabled(false);
						editText4.setInputType(0);
						editText5.setEnabled(false);
						editText5.setInputType(0);
					}
				}
			}
        );
    }
    
///*イベントハンドラの設定パターン2 main.xml で クリック時のhandler を定義するパターン SDK1.6以上*/
//   public void button1ClickHandler(View v){
//	   Intent intent = new Intent(this,jp.moo.snsk.resultViewActivity.class);
//	   startActivity(intent);
//   }
    
    private void calc() throws WariCaLocException{

    	EditText totalBill = (EditText)findViewById(id.EditText01);
    	EditText headCount = (EditText)findViewById(id.EditText02);
    	EditText prePayed  = (EditText)findViewById(id.EditText03);
        EditText offHeadCount = (EditText)findViewById(R.id.EditText04);
        EditText offPercent = (EditText)findViewById(R.id.EditText05);
    	CheckBox checkBox1 = (CheckBox)findViewById(R.id.CheckBox01);

		//error handling
		if(totalBill.getText().toString().equals("") || headCount.getText().toString().equals("")
				|| prePayed.getText().toString().equals("")){
			throw new WariCaLocException("空の入力欄があります");
		}
		if(headCount.getText().toString().equals("0")){
			throw new WariCaLocException("0人では割れません");
		}
		
    	//EditTextからデータを読み込む
		tBills = Integer.parseInt(totalBill.getText().toString());
		hCnt =  Integer.parseInt(headCount.getText().toString());
		pPayed = Integer.parseInt(prePayed.getText().toString());
				
    	if(checkBox1.isChecked() != false){//割引パターン
    		if(offPercent.getText().toString().equals("") || offHeadCount.getText().toString().equals("")){
    			throw new WariCaLocException("空の入力欄があります");
    		}
    		oPer = Integer.parseInt(offPercent.getText().toString());
    		oHCnt = Integer.parseInt(offHeadCount.getText().toString());
    		
    		answer = (tBills - pPayed)/hCnt; //通常の頭割り額算出
    		actuallyOffPrice = answer * ((100 - oPer)*0.01); //通常の頭割り額から割引額を引いた額の算出
    			//Log.v("actuallyOffPrice", String.valueOf(actuallyOffPrice)+ " * " + String.valueOf(oHCnt));
    		answer = (int) (((tBills - pPayed) - (actuallyOffPrice * oHCnt)) / (hCnt - oHCnt)); //割引を踏まえて通常額を再計算
				//Log.v("answer", String.valueOf(answer) + " * " + String.valueOf(hCnt - oHCnt));
			shortage = tBills - ((answer * (hCnt - oHCnt)) + (actuallyOffPrice * oHCnt)); 
    	}else{//割引なしパターン
        	answer = (tBills - pPayed)/hCnt;
        	answer_mod = (tBills - pPayed)%hCnt;
    	}
    }
    
    private int tBills;
	private int hCnt;
	private int pPayed;	
	private int oHCnt;
	private int oPer;
	private int answer;
	private int answer_mod;	
    private double actuallyOffPrice;
    private double shortage;
}

class WariCaLocException extends Exception{
	public WariCaLocException(String msg){
		super(msg);
	};
	private static final long serialVersionUID = 1L;
}
