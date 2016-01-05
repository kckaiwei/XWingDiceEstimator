package com.dice.kevin.x_wingdiceestimator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DiceRoller extends AppCompatActivity {
    int AttackDieInt,DefenseDieInt;
    double hitChance;
    double hitMod,evadeMod;
    double currentHitProb, currentMissProb, currentDodgeProb, currentFailProb;
    double a0dp,a1dp,a2dp,a3dp,a4dp,a5dp,a6dp;
    double e0dp,e1dp,e2dp,e3dp,e4dp,e5dp,e6dp;
    TextView AttackDieText, DefenseDieText;
    TextView Attack0Prob,Attack1Prob,Attack2Prob,Attack3Prob,Attack4Prob,Attack5Prob,Attack6Prob;
    TextView Attack0Cumu,Attack1Cumu,Attack2Cumu,Attack3Cumu,Attack4Cumu,Attack5Cumu,Attack6Cumu;
    TextView DefDie0Cumu,DefDie1Cumu, DefDie2Cumu,DefDie3Cumu, DefDie4Cumu, DefDie5Cumu, DefDie6Cumu;
    TextView Defense0Prob,Defense1Prob,Defense2Prob,Defense3Prob,Defense4Prob,Defense5Prob,Defense6Prob;
    Boolean EvadeFocus,AttackFocus,CritOnly, TargetLock;
    CheckBox EvadeFocusCheck,AttackFocusCheck, CritOnlyCheck, TargetLockCheck;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roller);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        EvadeFocusCheck = (CheckBox) findViewById(R.id.EvadeFocusCheckBox);
        AttackFocusCheck = (CheckBox) findViewById(R.id.AttackFocusCheckBox);
        CritOnlyCheck = (CheckBox) findViewById(R.id.AttackCritOnly);
        TargetLockCheck = (CheckBox) findViewById(R.id.targetLockCheckBox);

        AttackDieText = (TextView) findViewById(R.id.NumAttackDieText);
        DefenseDieText = (TextView) findViewById(R.id.NumDefDieText);

        Attack0Prob = (TextView) findViewById(R.id.Attack0Prob);
        Attack1Prob = (TextView) findViewById(R.id.Attack1Prob);
        Attack2Prob = (TextView) findViewById(R.id.Attack2Prob);
        Attack3Prob = (TextView) findViewById(R.id.Attack3Prob);
        Attack4Prob = (TextView) findViewById(R.id.Attack4Prob);
        Attack5Prob = (TextView) findViewById(R.id.Attack5Prob);
        Attack6Prob = (TextView) findViewById(R.id.Attack6Prob);

        Attack0Cumu = (TextView) findViewById(R.id.Attack0Cumu);
        Attack1Cumu = (TextView) findViewById(R.id.Attack1Cumu);
        Attack2Cumu = (TextView) findViewById(R.id.Attack2Cumu);
        Attack3Cumu = (TextView) findViewById(R.id.Attack3Cumu);
        Attack4Cumu = (TextView) findViewById(R.id.Attack4Cumu);
        Attack5Cumu = (TextView) findViewById(R.id.Attack5Cumu);
        Attack6Cumu = (TextView) findViewById(R.id.Attack6Cumu);

        DefDie0Cumu = (TextView) findViewById(R.id.DefDie0Cumu);
        DefDie1Cumu = (TextView) findViewById(R.id.DefDie1Cumu);
        DefDie2Cumu = (TextView) findViewById(R.id.DefDie2Cumu);
        DefDie3Cumu = (TextView) findViewById(R.id.DefDie3Cumu);
        DefDie4Cumu = (TextView) findViewById(R.id.DefDie4Cumu);
        DefDie5Cumu = (TextView) findViewById(R.id.DefDie5Cumu);
        DefDie6Cumu = (TextView) findViewById(R.id.DefDie6Cumu);




        Defense0Prob = (TextView) findViewById(R.id.DefDie0Prob);
        Defense1Prob = (TextView) findViewById(R.id.DefDie1Prob);
        Defense2Prob = (TextView) findViewById(R.id.DefDie2Prob);
        Defense3Prob = (TextView) findViewById(R.id.DefDie3Prob);
        Defense4Prob = (TextView) findViewById(R.id.DefDie4Prob);
        Defense5Prob = (TextView) findViewById(R.id.DefDie5Prob);
        Defense6Prob = (TextView) findViewById(R.id.DefDie6Prob);


        AttackDieInt = 0;
        DefenseDieInt = 0;
    }

    public void onTickBox(View view){
        if (view.getId()== R.id.AttackCritOnly){
            AttackFocusCheck.setChecked(Boolean.FALSE);
        }


        compute();
    }

    public void onClickAttackIncrease(View view){
        if (AttackDieInt !=6) {
            AttackDieInt++;
        }
        AttackDieText.setText(String.valueOf(AttackDieInt));
        compute();

    }
    public void onClickDefenseIncrease(View view){
        if (DefenseDieInt !=6) {
            DefenseDieInt++;
        }
        DefenseDieText.setText(String.valueOf(DefenseDieInt));
        compute();



    }
    public void onClickAttackDecrease(View view){
        if (AttackDieInt != 0) {
            AttackDieInt--;
        }
        AttackDieText.setText(String.valueOf(AttackDieInt));
        compute();

    }

    public void onClickDefenseDecrease(View view){
        if (DefenseDieInt != 0) {
            DefenseDieInt--;
        }
        DefenseDieText.setText(String.valueOf(DefenseDieInt));
        compute();



    }

    public void compute(){


        if (AttackFocusCheck.isChecked()){
            AttackFocus = Boolean.TRUE;

            //Can't Have Both
            CritOnly = Boolean.FALSE;
            CritOnlyCheck.setChecked(Boolean.FALSE);
        } else {
            AttackFocus = Boolean.FALSE;
        }
        if (EvadeFocusCheck.isChecked()){
            EvadeFocus = Boolean.TRUE;
        } else {
            EvadeFocus = Boolean.FALSE;
        }

        if (CritOnlyCheck.isChecked()) {
            CritOnly = Boolean.TRUE;

            //Can't Have Both
            AttackFocus = Boolean.FALSE;
            AttackFocusCheck.setChecked(Boolean.FALSE);

        }else {
            CritOnly = Boolean.FALSE;
        }

        if (TargetLockCheck.isChecked()){
            TargetLock = Boolean.TRUE;
        } else {
            TargetLock = Boolean.FALSE;
        }


        resetAttackandDefCalc();


        if (AttackFocus == Boolean.FALSE) {
            hitMod = 4; //default value
        }else{
            hitMod = 6;
        }
        if (CritOnly == Boolean.TRUE){
            hitMod = 1;
        }
        if (EvadeFocus == Boolean.FALSE) {
            evadeMod = 3;
        }else {
            evadeMod =5;
        }
        chanceMultiplication();
        switch (AttackDieInt) {
            case 0: Attack0Calc();
                break;
            case 1: Attack1Calc();
                break;
            case 2: Attack2Calc();
                break;
            case 3: Attack3Calc();
                break;
            case 4: Attack4Calc();
                break;
            case 5: Attack5Calc();
                break;
            case 6: Attack6Calc();
                break;

        }
        switch (DefenseDieInt) {
            case 0: Defense0Calc();
                break;
            case 1: Defense1Calc();
                break;
            case 2: Defense2Calc();
                break;
            case 3: Defense3Calc();
                break;
            case 4: Defense4Calc();
                break;
            case 5: Defense5Calc();
                break;
            case 6: Defense6Calc();
                break;

        }
        setAttackandDefText();


    }

    public void resetAttackandDefCalc(){
        a0dp = 0;
        a1dp = 0;
        a2dp = 0;
        a3dp = 0;
        a4dp = 0;
        a5dp = 0;
        a6dp = 0;

        e0dp = 0;
        e1dp = 0;
        e2dp = 0;
        e3dp = 0;
        e4dp = 0;
        e5dp = 0;
        e6dp = 0;

        Attack0Cumu.setText(String.valueOf(0));
        Attack1Cumu.setText(String.valueOf(0));
        Attack2Cumu.setText(String.valueOf(0));
        Attack3Cumu.setText(String.valueOf(0));
        Attack4Cumu.setText(String.valueOf(0));
        Attack5Cumu.setText(String.valueOf(0));
        Attack6Cumu.setText(String.valueOf(0));


        DefDie0Cumu.setText(String.valueOf(0));
        DefDie1Cumu.setText(String.valueOf(0));
        DefDie2Cumu.setText(String.valueOf(0));
        DefDie3Cumu.setText(String.valueOf(0));
        DefDie4Cumu.setText(String.valueOf(0));
        DefDie5Cumu.setText(String.valueOf(0));
        DefDie6Cumu.setText(String.valueOf(0));

        //reset probability values
    }


    public void setAttackandDefText(){

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
/*
        Attack0Prob.setText(df.format(a0dp*AttackDieInt));
        Attack1Prob.setText(df.format(a1dp*AttackDieInt));
        Attack2Prob.setText(df.format(a2dp*AttackDieInt));
        Attack3Prob.setText(df.format(a3dp*AttackDieInt));
        Attack4Prob.setText(df.format(a4dp*AttackDieInt));
        Attack5Prob.setText(df.format(a5dp * AttackDieInt));
        Attack6Prob.setText(df.format(a6dp * AttackDieInt));
        */

        Attack0Prob.setText(df.format(a0dp));
        Attack1Prob.setText(df.format(a1dp));
        Attack2Prob.setText(df.format(a2dp));
        Attack3Prob.setText(df.format(a3dp));
        Attack4Prob.setText(df.format(a4dp));
        Attack5Prob.setText(df.format(a5dp));
        Attack6Prob.setText(df.format(a6dp));

        /*
        Attack0Cumu.setText(df.format(a0dp*AttackDieInt));
        if (AttackDieInt >= 1) {
            Attack1Cumu.setText(df.format((a0dp * AttackDieInt) + (a1dp * AttackDieInt)));
        }
        if (AttackDieInt >=2) {
            Attack2Cumu.setText(df.format((a2dp * AttackDieInt) + (a0dp * AttackDieInt) + (a1dp * AttackDieInt)));
        }
        if (AttackDieInt >=3) {
            Attack3Cumu.setText(df.format((a3dp * AttackDieInt) + (a2dp * AttackDieInt) + (a0dp * AttackDieInt) + (a1dp * AttackDieInt)));
        }
        if (AttackDieInt >=4) {
            Attack4Cumu.setText(df.format((a3dp * AttackDieInt) + (a2dp * AttackDieInt) + (a0dp * AttackDieInt) + (a1dp * AttackDieInt) + (a4dp * AttackDieInt)));
        }
        if (AttackDieInt >=5) {
            Attack5Cumu.setText(df.format((a3dp * AttackDieInt) + (a2dp * AttackDieInt) + (a0dp * AttackDieInt) + (a1dp * AttackDieInt) + (a4dp * AttackDieInt) + (a5dp * AttackDieInt)));
        }
        if (AttackDieInt >=6) {
            Attack6Cumu.setText(df.format((a3dp * AttackDieInt) + (a2dp * AttackDieInt) + (a0dp * AttackDieInt) + (a1dp * AttackDieInt) + (a4dp * AttackDieInt) + (a5dp * AttackDieInt) + (a6dp * AttackDieInt)));
        }
        */

        Attack0Cumu.setText(df.format(a0dp));
        if (AttackDieInt >= 1) {
            Attack1Cumu.setText(df.format((a0dp ) + (a1dp )));
        }
        if (AttackDieInt >=2) {
            Attack2Cumu.setText(df.format((a2dp ) + (a0dp ) + (a1dp )));
        }
        if (AttackDieInt >=3) {
            Attack3Cumu.setText(df.format((a3dp ) + (a2dp) + (a0dp ) + (a1dp )));
        }
        if (AttackDieInt >=4) {
            Attack4Cumu.setText(df.format((a3dp ) + (a2dp ) + (a0dp ) + (a1dp ) + (a4dp )));
        }
        if (AttackDieInt >=5) {
            Attack5Cumu.setText(df.format((a3dp ) + (a2dp ) + (a0dp ) + (a1dp ) + (a4dp ) + (a5dp )));
        }
        if (AttackDieInt >=6) {
            Attack6Cumu.setText(df.format((a3dp ) + (a2dp ) + (a0dp ) + (a1dp ) + (a4dp ) + (a5dp ) + (a6dp )));
        }

        /*
        Defense0Prob.setText(df.format(e0dp*DefenseDieInt));
        Defense1Prob.setText(df.format(e1dp*DefenseDieInt));
        Defense2Prob.setText(df.format(e2dp*DefenseDieInt));
        Defense3Prob.setText(df.format(e3dp*DefenseDieInt));
        Defense4Prob.setText(df.format(e4dp*DefenseDieInt));
        Defense5Prob.setText(df.format(e5dp*DefenseDieInt));
        Defense6Prob.setText(df.format(e6dp*DefenseDieInt));
        */

        Defense0Prob.setText(df.format(e0dp));
        Defense1Prob.setText(df.format(e1dp));
        Defense2Prob.setText(df.format(e2dp));
        Defense3Prob.setText(df.format(e3dp));
        Defense4Prob.setText(df.format(e4dp));
        Defense5Prob.setText(df.format(e5dp));
        Defense6Prob.setText(df.format(e6dp));
        /*
        DefDie0Cumu.setText(df.format(e0dp));
        if (DefenseDieInt >=1) {
            DefDie1Cumu.setText(df.format(e1dp * DefenseDieInt));
        }
        if (DefenseDieInt >=2) {
            DefDie2Cumu.setText(df.format((e2dp * DefenseDieInt) + (e1dp * DefenseDieInt)));
        }
        if (DefenseDieInt >=3) {
            DefDie3Cumu.setText(df.format((e2dp * DefenseDieInt) + (e1dp * DefenseDieInt)+(e3dp * DefenseDieInt)));
        }
        if (DefenseDieInt >=4) {
            DefDie4Cumu.setText(df.format((((e2dp * DefenseDieInt) + (e1dp * DefenseDieInt)) + (e3dp * DefenseDieInt)) + (e4dp * DefenseDieInt)));
        }
        if (DefenseDieInt >=5) {
            DefDie5Cumu.setText(df.format((((e2dp * DefenseDieInt) + (e1dp * DefenseDieInt)) + (e3dp * DefenseDieInt)) + (e4dp * DefenseDieInt) + (e5dp * DefenseDieInt)));
        }
        if (DefenseDieInt >=6) {
            DefDie6Cumu.setText(df.format((((e2dp * DefenseDieInt) + (e1dp * DefenseDieInt)) + (e3dp * DefenseDieInt)) + (e4dp * DefenseDieInt) + (e5dp * DefenseDieInt) + (e6dp * DefenseDieInt)));
        }
        */
        DefDie0Cumu.setText(df.format(e0dp));
        if (DefenseDieInt >=1) {
            DefDie1Cumu.setText(df.format(e1dp ));
        }
        if (DefenseDieInt >=2) {
            DefDie2Cumu.setText(df.format((e2dp ) + (e1dp )));
        }
        if (DefenseDieInt >=3) {
            DefDie3Cumu.setText(df.format((e2dp ) + (e1dp )+(e3dp )));
        }
        if (DefenseDieInt >=4) {
            DefDie4Cumu.setText(df.format((((e2dp ) + (e1dp )) + (e3dp )) + (e4dp )));
        }
        if (DefenseDieInt >=5) {
            DefDie5Cumu.setText(df.format((((e2dp ) + (e1dp )) + (e3dp )) + (e4dp ) + (e5dp )));
        }
        if (DefenseDieInt >=6) {
            DefDie6Cumu.setText(df.format((((e2dp ) + (e1dp )) + (e3dp )) + (e4dp ) + (e5dp ) + (e6dp )));
        }


    }

    public void chanceMultiplication() {
        hitChance = (0.125);
        currentHitProb = hitMod*hitChance;
        currentDodgeProb = evadeMod*hitChance;
        currentFailProb = (1-currentDodgeProb);
        currentMissProb = (1 - currentHitProb);

        if (TargetLock) {
            currentHitProb += currentMissProb*currentHitProb;
            currentMissProb -= currentMissProb*currentMissProb;
        }
    }

    public void Attack0Calc() {

    }

    public void Attack1Calc(){

        a0dp = currentMissProb;
        a1dp = currentHitProb*1;


         // Attack0Prob.setText(String.valueOf((currentMissProb) * 1));
         // Attack1Prob.setText(String.valueOf((currentHitProb) * 1));

    }

    public void Attack2Calc(){
        /*
            Attack0Prob.setText(String.valueOf((Math.pow(currentMissProb,2.0))));
            Attack1Prob.setText(String.valueOf((currentHitProb*currentMissProb) +(currentHitProb*currentMissProb)));
            Attack2Prob.setText(String.valueOf(currentHitProb* currentHitProb));
    */

        a0dp = Math.pow(currentMissProb,2.0);
        a1dp = (currentHitProb*currentMissProb) *2;
        a2dp = Math.pow(currentHitProb,2.0);
    }


    public void Attack3Calc() {
        a0dp = Math.pow(currentMissProb,3.0);
        a1dp = (currentMissProb*currentMissProb*currentHitProb)*3;
        a2dp = (currentHitProb* currentHitProb* currentMissProb)*3;
        a3dp = Math.pow(currentHitProb,3.0);

        /*
        Attack0Prob.setText(String.valueOf(Math.pow(currentMissProb, 3.0)));
        Attack1Prob.setText(String.valueOf((currentMissProb * currentMissProb * currentHitProb) * 3));
        Attack2Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentMissProb) * 3));
        Attack3Prob.setText(String.valueOf(Math.pow(currentHitProb, 3.0)));
*/
    }

    public void Attack4Calc(){
        /*
        Attack0Prob.setText(String.valueOf(Math.pow(currentMissProb, 4.0)));
        Attack1Prob.setText(String.valueOf((currentMissProb * currentMissProb * currentHitProb * currentMissProb) * 4));
        Attack2Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentMissProb * currentMissProb) * 6));
        Attack3Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentHitProb * currentMissProb) * 4));
        Attack4Prob.setText(String.valueOf(Math.pow(currentHitProb, 4.0)));

    */
        a0dp = Math.pow(currentMissProb,4.0);
        a1dp = (currentHitProb * currentMissProb *currentMissProb * currentMissProb)*4;
        a2dp = (currentHitProb * currentHitProb *currentMissProb * currentMissProb)*6;
        a3dp = (currentHitProb * currentHitProb *currentHitProb * currentMissProb)*4;
        a4dp = Math.pow(currentHitProb, 4.0);


    }
    public void Attack5Calc(){
        /*
        Attack0Prob.setText(String.valueOf(Math.pow(currentMissProb, 5.0)));
        Attack1Prob.setText(String.valueOf((currentMissProb * currentMissProb * currentHitProb * currentMissProb * currentMissProb) * 5));
        Attack2Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentMissProb * currentMissProb *currentMissProb) * 10));
        Attack3Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentHitProb * currentMissProb *currentMissProb) * 10));
        Attack4Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentHitProb * currentHitProb *currentMissProb) * 5));
        Attack5Prob.setText(String.valueOf(Math.pow(currentHitProb, 5.0)));
    */

        a0dp = Math.pow(currentMissProb,5.0);
        a1dp = (currentHitProb * currentMissProb *currentMissProb * currentMissProb * currentMissProb)*5;
        a2dp = (currentHitProb * currentHitProb *currentMissProb * currentMissProb * currentMissProb)*10;
        a3dp = (currentHitProb * currentHitProb *currentHitProb * currentMissProb * currentMissProb)*10;
        a4dp = (currentHitProb* currentHitProb* currentHitProb* currentHitProb * currentMissProb)*5;
        a5dp = Math.pow(currentHitProb, 5.0);
    }
    public void Attack6Calc(){
        /*
        Attack0Prob.setText(String.valueOf(Math.pow(currentMissProb, 6.0)));
        Attack1Prob.setText(String.valueOf((currentMissProb * currentMissProb * currentHitProb * currentMissProb * currentMissProb* currentMissProb) * 6));
        Attack2Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentMissProb * currentMissProb *currentMissProb *currentMissProb) * 15));
        Attack3Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentHitProb * currentMissProb *currentMissProb *currentMissProb) * 20));
        Attack4Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentHitProb * currentHitProb *currentMissProb *currentMissProb) * 15));
        Attack5Prob.setText(String.valueOf((currentHitProb * currentHitProb * currentHitProb * currentHitProb *currentHitProb *currentMissProb) * 6));
        Attack6Prob.setText(String.valueOf(Math.pow(currentHitProb, 6.0)));
*/
        a0dp = Math.pow(currentMissProb,6.0);
        a1dp = (currentHitProb * currentMissProb *currentMissProb * currentMissProb * currentMissProb * currentMissProb)*6;
        a2dp = (currentHitProb * currentHitProb *currentMissProb * currentMissProb * currentMissProb * currentMissProb)*15;
        a3dp = (currentHitProb * currentHitProb *currentHitProb * currentMissProb * currentMissProb * currentMissProb)*20;
        a4dp = (currentHitProb* currentHitProb* currentHitProb* currentHitProb * currentMissProb * currentMissProb)*15;
        a5dp = (currentHitProb* currentHitProb* currentHitProb *currentHitProb *currentHitProb * currentMissProb)*6;
        a6dp = Math.pow(currentHitProb, 6.0);
    }


    //DEFENSE CALCULATIONS
    //
    //
    //LALALALALALA

    public void Defense0Calc(){

    }

    public void Defense1Calc(){

        e0dp = currentFailProb;
        e1dp = currentDodgeProb*1;



    }

    public void Defense2Calc(){

        e0dp = Math.pow(currentFailProb,2.0);
        e1dp = (currentDodgeProb*currentFailProb) *2;
        e2dp = Math.pow(currentDodgeProb,2.0);
    }


    public void Defense3Calc() {
        e0dp = Math.pow(currentFailProb,3.0);
        e1dp = (currentFailProb*currentFailProb*currentDodgeProb)*3;
        e2dp = (currentDodgeProb* currentDodgeProb* currentFailProb)*3;
        e3dp = Math.pow(currentDodgeProb,3.0);

    }

    public void Defense4Calc(){

        e0dp = Math.pow(currentFailProb,4.0);
        e1dp = (currentDodgeProb * currentFailProb *currentFailProb * currentFailProb)*4;
        e2dp = (currentDodgeProb * currentDodgeProb *currentFailProb * currentFailProb)*6;
        e3dp = (currentDodgeProb * currentDodgeProb *currentDodgeProb * currentFailProb)*4;
        e4dp = Math.pow(currentDodgeProb, 4.0);


    }
    public void Defense5Calc(){


        e0dp = Math.pow(currentFailProb,5.0);
        e1dp = (currentDodgeProb * currentFailProb *currentFailProb * currentFailProb * currentFailProb)*5;
        e2dp = (currentDodgeProb * currentDodgeProb *currentFailProb * currentFailProb * currentFailProb)*10;
        e3dp = (currentDodgeProb * currentDodgeProb *currentDodgeProb * currentFailProb * currentFailProb)*10;
        e4dp = (currentDodgeProb* currentDodgeProb* currentDodgeProb* currentDodgeProb * currentFailProb)*5;
        e5dp = Math.pow(currentDodgeProb, 5.0);
    }
    public void Defense6Calc(){

        e0dp = Math.pow(currentFailProb,6.0);
        e1dp = (currentDodgeProb * currentFailProb *currentFailProb * currentFailProb * currentFailProb * currentFailProb)*6;
        e2dp = (currentDodgeProb * currentDodgeProb *currentFailProb * currentFailProb * currentFailProb * currentFailProb)*15;
        e3dp = (currentDodgeProb * currentDodgeProb *currentDodgeProb * currentFailProb * currentFailProb * currentFailProb)*20;
        e4dp = (currentDodgeProb* currentDodgeProb* currentDodgeProb* currentDodgeProb * currentFailProb * currentFailProb)*15;
        e5dp = (currentDodgeProb* currentDodgeProb* currentDodgeProb *currentDodgeProb *currentDodgeProb * currentFailProb)*6;
        e6dp = Math.pow(currentDodgeProb, 6.0);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dice_roller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
