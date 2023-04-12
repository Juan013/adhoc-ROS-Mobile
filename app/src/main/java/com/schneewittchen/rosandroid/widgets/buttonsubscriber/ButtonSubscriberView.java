package com.schneewittchen.rosandroid.widgets.buttonsubscriber;

/**
 * @author jbravo@uma.es (Juan Bravo-Arrabal)
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberWidgetView;
import com.schneewittchen.rosandroid.widgets.camera.CameraData;
import com.schneewittchen.rosandroid.widgets.gps.GpsData;

import org.ros.internal.message.Message;

import sensor_msgs.CompressedImage;
import sensor_msgs.Image;
import sensor_msgs.NavSatFix;
import std_msgs.Int8;

public class ButtonSubscriberView extends SubscriberWidgetView{
    public static final String TAG = ButtonSubscriberView.class.getSimpleName();

    Paint buttonPaint;
    TextPaint textPaint;
    StaticLayout staticLayout;
    private float rotation;
    private CharSequence text;
    public byte data; // era private

    /*
    private Button btn;
    btn = (Button)findViewById(R.id.button2);
    btn.performClick();
    */

    public ButtonSubscriberView(Context context) {
        super(context);
        init();
    }


    public ButtonSubscriberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        buttonPaint = new Paint();
        buttonPaint.setColor(getResources().getColor(R.color.colorPrimary));
        buttonPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26 * getResources().getDisplayMetrics().density);
    }

    private void changeState(boolean pressed) {
        //this.publishViewData(new ButtonData(pressed));
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.editMode) {
            return super.onTouchEvent(event);
        }
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_UP:
                // Si el valor de la fuerza en Z es seguro:
                int value = this.data;

                Log.d("value", String.valueOf(value));
                if(value == 0){
                    changeState(false);
                    buttonPaint.setColor(getResources().getColor(R.color.ok_green));
                    Log.d("value", "VERDE");
                    changeState(true);


                }
                else if(value == 1){
                    changeState(true);
                    buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                    Log.d("value", "ORANGE");
                    changeState(false);

                    //buttonPaint.performClick();
                }
                else if(value == 2){
                    changeState(true);
                    buttonPaint.setColor(getResources().getColor(R.color.delete_red));
                    Log.d("value", "RED");
                    changeState(false);
                }
                changeState(false);

                // Y si el valor de la z está en un rango de valores peligroso:
                //buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                // Y si el valor de z es dañino para el cuerpo:
                //
                break;

            case MotionEvent.ACTION_DOWN:
                //buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                /*
                value = this.data;

                if(value == 0){
                    buttonPaint.setColor(getResources().getColor(R.color.ok_green));
                    Log.d("value", "VERDE");
                }
                else if(value == 1){
                    buttonPaint.setColor(getResources().getColor(R.color.color_attention));
                    Log.d("value", "ORANGE");
                }
                else if(value == 2){
                    buttonPaint.setColor(getResources().getColor(R.color.delete_red));
                    Log.d("value", "RED");
                }
                */

                changeState(true);
                break;

            default:
                return false;
        }

        return true;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int value = this.data;

        float width2 = getWidth();
        float height2 = getHeight();
        float textLayoutWidth2 = width2;

        ButtonSubscriberEntity entity = (ButtonSubscriberEntity) widgetEntity;


        if (entity.rotation2 == 90 || entity.rotation2 == 270) {
            textLayoutWidth2 = height2;
        }

        canvas.drawRect(new Rect(0, 0, (int) width2, (int) height2), buttonPaint);

        if(value == 0) {

            staticLayout = new StaticLayout(entity.text2,
                    textPaint,
                    (int) textLayoutWidth2,
                    Layout.Alignment.ALIGN_CENTER,
                    1.0f,
                    0,
                    false);
            buttonPaint.setColor(getResources().getColor(R.color.ok_green));
        }

        else if(value == 1){

            staticLayout = new StaticLayout(entity.text3,
                    textPaint,
                    (int) textLayoutWidth2,
                    Layout.Alignment.ALIGN_CENTER,
                    1.0f,
                    0,
                    false);
            buttonPaint.setColor(getResources().getColor(R.color.color_attention));
        }

        else if(value == 2){
            staticLayout = new StaticLayout(entity.text3,
                    textPaint,
                    (int) textLayoutWidth2,
                    Layout.Alignment.ALIGN_CENTER,
                    1.0f,
                    0,
                    false);
            buttonPaint.setColor(getResources().getColor(R.color.delete_red));
        }

        canvas.save();
        canvas.rotate(entity.rotation2, width2 / 2, height2 / 2);
        canvas.translate(((width2 / 2) - staticLayout.getWidth() / 2), height2 / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public void onNewMessage(Message message) {
       //his.data =/ verde al principio
        if(!(message instanceof std_msgs.UInt8)) return;
        this.data = ((std_msgs.UInt8)message).getData();
        this.invalidate();

    }

}
