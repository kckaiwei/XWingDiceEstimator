package com.dice.kevin.x_wingdiceestimator;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    TextView AttackDieText, DefenseDieText, numAttackDieText, numDefenseDieText;
    TextView Attack0Prob,Attack1Prob,Attack2Prob,Attack3Prob,Attack4Prob,Attack5Prob,Attack6Prob;
    TextView Attack0Cumu,Attack1Cumu,Attack2Cumu,Attack3Cumu,Attack4Cumu,Attack5Cumu,Attack6Cumu;
    TextView DefDie0Cumu,DefDie1Cumu, DefDie2Cumu,DefDie3Cumu, DefDie4Cumu, DefDie5Cumu, DefDie6Cumu;
    TextView Defense0Prob,Defense1Prob,Defense2Prob,Defense3Prob,Defense4Prob,Defense5Prob,Defense6Prob;
    Boolean EvadeFocus,AttackFocus,CritOnly, TargetLock;
    CheckBox EvadeFocusCheck,AttackFocusCheck, CritOnlyCheck, TargetLockCheck;
    int screenWidthpx;
    static final String STATE_ATKDIE = "atkDieInt";
    static final String STATE_DEFDIE = "defDieInt";
    static final String STATE_ATKFOCUSCHECK = "atkFocusCheck";
    static final String STATE_DEFFOCUSCHECK = "defFocusCheck";
    static final String STATE_CRITONLYCHECK = "critOnlyCheck";
    static final String STATE_TARGETLOCKCHECK = "targetLockCheck";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //INITIALIZE THINGS!
        //
        //

        setContentView(R.layout.activity_dice_roller);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E34880D7E997D2304D31F2A9350D6FEF")
                .build();
        mAdView.loadAd(adRequest);


        //Get Screensize
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidthpx = metrics.widthPixels;

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

        numAttackDieText = (TextView) findViewById(R.id.NumAttackDieText);
        numDefenseDieText = (TextView) findViewById(R.id.NumDefDieText);

        initialBarSet();

        //RESUMING STATE! WILL OVERWRITE PREVIOUS
        //
        //

        if (savedInstanceState != null){
            AttackDieInt = savedInstanceState.getInt(STATE_ATKDIE);
            DefenseDieInt = savedInstanceState.getInt(STATE_DEFDIE);

            numAttackDieText.setText(String.valueOf(savedInstanceState.getInt(STATE_ATKDIE)));
            numDefenseDieText.setText(String.valueOf(savedInstanceState.getInt(STATE_DEFDIE)));

            if (savedInstanceState.getBoolean(STATE_ATKFOCUSCHECK)){
                AttackFocus = savedInstanceState.getBoolean(STATE_ATKFOCUSCHECK);
                AttackFocusCheck.setChecked(Boolean.TRUE);
            }
            if (savedInstanceState.getBoolean(STATE_DEFFOCUSCHECK)){
                EvadeFocus = savedInstanceState.getBoolean(STATE_DEFFOCUSCHECK);
                EvadeFocusCheck.setChecked(Boolean.TRUE);
            }
            if (savedInstanceState.getBoolean(STATE_TARGETLOCKCHECK)){
                TargetLock = savedInstanceState.getBoolean(STATE_TARGETLOCKCHECK);
                TargetLockCheck.setChecked(Boolean.TRUE);
            }
            if (savedInstanceState.getBoolean(STATE_CRITONLYCHECK)){
                CritOnly = savedInstanceState.getBoolean(STATE_CRITONLYCHECK);
                CritOnlyCheck.setChecked(Boolean.TRUE);
            }

            compute();


        }
        else {
            AttackDieInt = 0;
            DefenseDieInt = 0;
        }



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_ATKDIE, AttackDieInt);
        savedInstanceState.putInt(STATE_DEFDIE, DefenseDieInt);
        savedInstanceState.putBoolean(STATE_ATKFOCUSCHECK, AttackFocus);
        savedInstanceState.putBoolean(STATE_DEFFOCUSCHECK, EvadeFocus);
        savedInstanceState.putBoolean(STATE_TARGETLOCKCHECK, TargetLock);
        savedInstanceState.putBoolean(STATE_CRITONLYCHECK,CritOnly);

        super.onSaveInstanceState(savedInstanceState);
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

        DecimalFormat dfconvert = new DecimalFormat("#");
        dfconvert.setRoundingMode(RoundingMode.CEILING);

        //Set all Defense to red background default
        Defense0Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        Defense1Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        Defense2Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        Defense3Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        Defense4Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        Defense5Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        Defense6Prob.setBackgroundColor(getResources().getColor(R.color.badRed));




        Attack0Prob.setText(df.format(100*(a0dp)));
        //Color Setting
        //0 is special since it is bad

        setColorCumulativeBoth();

        if (a0dp < e0dp){
            Attack0Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else{
            Attack0Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }

        Attack1Prob.setText(df.format(100*(a1dp)));
        if (a1dp - e1dp >= 0.25){
            Attack1Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else if (a1dp - e1dp >0){
            Attack1Prob.setBackgroundColor(getResources().getColor(R.color.okayYellow));
        }else{
            Attack1Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }
        Attack2Prob.setText(df.format(100*(a2dp)));
        if (a2dp - e2dp >= 0.25){
            Attack2Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else if (a2dp - e2dp >0){
            Attack2Prob.setBackgroundColor(getResources().getColor(R.color.okayYellow));
        }else{
            Attack2Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }
        Attack3Prob.setText(df.format(100*(a3dp)));
        if (a3dp - e3dp >= 0.25){
            Attack3Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else if (a3dp - e3dp >0){
            Attack3Prob.setBackgroundColor(getResources().getColor(R.color.okayYellow));
        }else{
            Attack3Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }
        Attack4Prob.setText(df.format(100*(a4dp)));
        if (a4dp - e4dp >= 0.25){
            Attack4Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else if (a4dp - e4dp >0){
            Attack4Prob.setBackgroundColor(getResources().getColor(R.color.okayYellow));
        }else{
            Attack4Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }
        Attack5Prob.setText(df.format(100*(a5dp)));
        if (a5dp - e5dp >= 0.25){
            Attack5Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else if (a5dp - e5dp >0){
            Attack5Prob.setBackgroundColor(getResources().getColor(R.color.okayYellow));
        }else{
            Attack5Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }
        Attack6Prob.setText(df.format(100*(a6dp)));

        if (a6dp - e6dp >= 0.25){
            Attack6Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }else if (a6dp - e6dp >0){
            Attack6Prob.setBackgroundColor(getResources().getColor(R.color.okayYellow));
        }else{
            Attack6Prob.setBackgroundColor(getResources().getColor(R.color.badRed));
        }


        Attack0Cumu.setText(df.format(100*((a3dp ) + (a2dp ) + (a0dp ) + (a1dp ) + (a4dp ) + (a5dp ) + (a6dp ))));
        if (AttackDieInt >= 1) {
            Attack1Cumu.setText(df.format(100*(a1dp+a2dp+a3dp+a4dp+a5dp+a6dp)));
        }
        if (AttackDieInt >=2) {
            Attack2Cumu.setText(df.format(100*(a2dp+a3dp+a4dp+a5dp+a6dp)));
        }
        if (AttackDieInt >=3) {
            Attack3Cumu.setText(df.format(100*(a3dp+a4dp+a5dp+a6dp)));
        }
        if (AttackDieInt >=4) {
            Attack4Cumu.setText(df.format(100*(a4dp+a5dp+a6dp)));
        }
        if (AttackDieInt >=5) {
            Attack5Cumu.setText(df.format(100*(a5dp+a6dp)));
        }
        if (AttackDieInt >=6) {
            Attack6Cumu.setText(df.format(100*(a6dp)));
        }

        Defense0Prob.setText(df.format(100*(e0dp)));
        Defense1Prob.setText(df.format(100*(e1dp)));
        Defense2Prob.setText(df.format(100*(e2dp)));
        Defense3Prob.setText(df.format(100*(e3dp)));
        Defense4Prob.setText(df.format(100*(e4dp)));
        Defense5Prob.setText(df.format(100*(e5dp)));
        Defense6Prob.setText(df.format(100*(e6dp)));


        //NOT WORKING!!!
        //defenseResetDimensions();
        defenseChangeDimensions();
        attackChangeDimensions();



        DefDie0Cumu.setText(df.format(100*((((e2dp ) + (e1dp )) + (e3dp )) + (e4dp ) + (e5dp ) + (e6dp )+ e0dp)));
        if (DefenseDieInt >=1) {

            DefDie1Cumu.setText(df.format(100*((((e2dp ) + (e1dp )) + (e3dp )) + (e4dp ) + (e5dp ) + (e6dp ))));
        }
        if (DefenseDieInt >=2) {
            DefDie2Cumu.setText(df.format(100*(e6dp+e5dp+e4dp+e3dp+e2dp)));
        }
        if (DefenseDieInt >=3) {
            DefDie3Cumu.setText(df.format(100*(e6dp+e5dp+e4dp+e3dp)));
        }
        if (DefenseDieInt >=4) {
            DefDie4Cumu.setText(df.format(100*((e6dp + e5dp+ e4dp))));
        }
        if (DefenseDieInt >=5) {
            DefDie5Cumu.setText(df.format(100*(e6dp+e5dp)));
        }
        if (DefenseDieInt >=6) {
            DefDie6Cumu.setText(df.format(100*(e6dp)));
        }

        if (e0dp <= a0dp) {
            Defense0Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }

        if (e1dp >= a1dp) {
            Defense1Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }

        if (e2dp >= a2dp) {
            Defense2Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if (e3dp >= a3dp) {
            Defense3Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }

        if (e4dp >= a4dp) {
            Defense4Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if (e5dp >= a5dp) {
            Defense5Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }

        if (e6dp >= a6dp) {
            Defense6Prob.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }



    }

    public void setColorCumulativeBoth(){
        if ((a6dp+a5dp+a4dp+a3dp+a2dp+a1dp+a0dp)> (e6dp+e5dp+e4dp+e3dp+e2dp+e1dp+e0dp)){
            //set Attack to green if higher
            Attack0Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie0Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack0Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie0Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }

        if ((a6dp+a5dp+a4dp+a3dp+a2dp+a1dp)> (e6dp+e5dp+e4dp+e3dp+e2dp+e1dp)){
            //set Attack to green if higher
            Attack1Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie1Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack1Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie1Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }

        if ((a6dp+a5dp+a4dp+a3dp+a2dp)> (e6dp+e5dp+e4dp+e3dp+e2dp)){
            //set Attack to green if higher
            Attack2Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie2Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack2Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie2Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if ((a6dp+a5dp+a4dp+a3dp)> (e6dp+e5dp+e4dp+e3dp)){
            //set Attack to green if higher
            Attack3Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie3Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack3Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie3Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if ((a6dp+a5dp+a4dp)> (e6dp+e5dp+e4dp)){
            //set Attack to green if higher
            Attack4Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie4Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack4Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie4Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if ((a6dp+a5dp+a4dp)> (e6dp+e5dp+e4dp)){
            //set Attack to green if higher
            Attack4Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie4Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack4Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie4Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if ((a6dp+a5dp)> (e6dp+e5dp)){
            //set Attack to green if higher
            Attack5Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie5Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack5Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie5Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
        if ((a6dp)> (e6dp)){
            //set Attack to green if higher
            Attack6Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
            DefDie6Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
        }else {
            Attack6Cumu.setBackgroundColor(getResources().getColor(R.color.badRed));
            DefDie6Cumu.setBackgroundColor(getResources().getColor(R.color.goodGreen));
        }
    }

    //WIP
    //
    //
    //
    public void defenseResetDimensions(){
        //Reset Widths
        Defense0Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Defense1Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Defense2Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Defense3Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Defense4Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Defense5Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        Defense6Prob.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void attackChangeDimensions() {
        Attack0Prob.setWidth((int) ((screenWidthpx/8)+ (a0dp * (screenWidthpx/3))));
        Attack1Prob.setWidth((int) ((screenWidthpx/8)+ (a1dp * (screenWidthpx/3))));
        Attack2Prob.setWidth((int) ((screenWidthpx/8)+ (a2dp * (screenWidthpx/3))));
        Attack3Prob.setWidth((int) ((screenWidthpx/8)+ (a3dp * (screenWidthpx/3))));
        Attack4Prob.setWidth((int) ((screenWidthpx/8)+ (a4dp * (screenWidthpx/3))));
        Attack5Prob.setWidth((int) ((screenWidthpx/8)+ (a5dp * (screenWidthpx/3))));
        Attack6Prob.setWidth((int) ((screenWidthpx/8)+ (a6dp * (screenWidthpx/3))));

        Attack0Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp+a5dp+a4dp+a3dp+a2dp+a1dp+a0dp) * (screenWidthpx/3))));
        Attack1Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp+a5dp+a4dp+a3dp+a2dp+a1dp) * (screenWidthpx/3))));
        Attack2Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp+a5dp+a4dp+a3dp+a2dp) * (screenWidthpx/3))));
        Attack3Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp+a5dp+a4dp+a3dp) * (screenWidthpx/3))));
        Attack4Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp+a5dp+a4dp) * (screenWidthpx/3))));
        Attack5Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp+a5dp) * (screenWidthpx/3))));
        Attack6Cumu.setWidth((int) ((screenWidthpx/8)+ ((a6dp) * (screenWidthpx/3))));
    }

    public void defenseChangeDimensions() {
        Defense0Prob.setWidth((int) ((screenWidthpx/8)+(e0dp * (screenWidthpx / 3))));
        Defense1Prob.setWidth((int) ((screenWidthpx/8)+(e1dp * (screenWidthpx / 3))));
        Defense2Prob.setWidth((int) ((screenWidthpx/8)+(e2dp * (screenWidthpx / 3))));
        Defense3Prob.setWidth((int) ((screenWidthpx/8)+(e3dp * (screenWidthpx / 3))));
        Defense4Prob.setWidth((int) ((screenWidthpx/8)+(e4dp * (screenWidthpx / 3))));
        Defense5Prob.setWidth((int) ((screenWidthpx/8)+(e5dp * (screenWidthpx / 3))));
        Defense6Prob.setWidth((int) ((screenWidthpx/8)+(e6dp * (screenWidthpx / 3))));

        DefDie0Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp+e5dp+e4dp+e3dp+e2dp+e1dp+e0dp) * (screenWidthpx / 3))));
        DefDie1Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp+e5dp+e4dp+e3dp+e2dp+e1dp) * (screenWidthpx / 3))));
        DefDie2Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp+e5dp+e4dp+e3dp+e2dp) * (screenWidthpx / 3))));
        DefDie3Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp+e5dp+e4dp+e3dp) * (screenWidthpx / 3))));
        DefDie4Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp+e5dp+e4dp) * (screenWidthpx / 3))));
        DefDie5Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp+e5dp) * (screenWidthpx / 3))));
        DefDie6Cumu.setWidth((int) ((screenWidthpx/8)+((e6dp) * (screenWidthpx / 3))));

    }

    public void initialBarSet(){
        Defense0Prob.setWidth((int) ((screenWidthpx/8)));
        Defense1Prob.setWidth((int) ((screenWidthpx/8)));
        Defense2Prob.setWidth((int) ((screenWidthpx/8)));
        Defense3Prob.setWidth((int) ((screenWidthpx/8)));
        Defense4Prob.setWidth((int) ((screenWidthpx/8)));
        Defense5Prob.setWidth((int) ((screenWidthpx/8)));
        Defense6Prob.setWidth((int) ((screenWidthpx/8)));

        DefDie0Cumu.setWidth((int) ((screenWidthpx/8)));
        DefDie1Cumu.setWidth((int) ((screenWidthpx/8)));
        DefDie2Cumu.setWidth((int) ((screenWidthpx/8)));
        DefDie3Cumu.setWidth((int) ((screenWidthpx/8)));
        DefDie4Cumu.setWidth((int) ((screenWidthpx/8)));
        DefDie5Cumu.setWidth((int) ((screenWidthpx/8)));
        DefDie6Cumu.setWidth((int) ((screenWidthpx/8)));

        Attack0Prob.setWidth((int) ((screenWidthpx / 8)));
        Attack1Prob.setWidth((int) ((screenWidthpx/8)));
        Attack2Prob.setWidth((int) ((screenWidthpx/8)));
        Attack3Prob.setWidth((int) ((screenWidthpx/8)));
        Attack4Prob.setWidth((int) ((screenWidthpx/8)));
        Attack5Prob.setWidth((int) ((screenWidthpx/8)));
        Attack6Prob.setWidth((int) ((screenWidthpx/8)));

        Attack0Cumu.setWidth((int) ((screenWidthpx/8)));
        Attack1Cumu.setWidth((int) ((screenWidthpx/8)));
        Attack2Cumu.setWidth((int) ((screenWidthpx/8)));
        Attack3Cumu.setWidth((int) ((screenWidthpx/8)));
        Attack4Cumu.setWidth((int) ((screenWidthpx/8)));
        Attack5Cumu.setWidth((int) ((screenWidthpx/8)));
        Attack6Cumu.setWidth((int) ((screenWidthpx/8)));
    }

    public void chanceMultiplication() {
        hitChance = (0.125);
        currentHitProb = hitMod*hitChance;
        currentDodgeProb = evadeMod*hitChance;
        currentFailProb = (1-currentDodgeProb);
        currentMissProb = (1 - currentHitProb);

        if (TargetLock) {
            currentHitProb = currentHitProb+ (currentHitProb*currentMissProb);
            currentMissProb = currentMissProb*currentMissProb;
        }
    }

    public void Attack0Calc() {

    }

    public void Attack1Calc(){

        a0dp = currentMissProb;
        a1dp = currentHitProb*1;


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
