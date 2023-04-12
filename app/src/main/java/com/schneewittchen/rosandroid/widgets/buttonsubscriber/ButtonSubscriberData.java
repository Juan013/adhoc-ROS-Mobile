package com.schneewittchen.rosandroid.widgets.buttonsubscriber;

import org.ros.internal.message.Message;

import java.util.ArrayList;

import std_msgs.Int8;


/**
 * @author jbravo@uma.es (Juan Bravo-Arrabal)
 */

public class ButtonSubscriberData {

    public boolean pressed;
    public  ButtonSubscriberData(boolean pressed) {
        this.pressed = pressed;
    }
/*
    public ButtonSubscriberData(Message message) {
       // Int8 zValue = new Int8();
        msgToString(message, 0);

    }

    private void msgToString(Message message, int i) {
    }


 */
}