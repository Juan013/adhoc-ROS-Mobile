package com.schneewittchen.rosandroid.widgets.buttonsubscriber;
import android.view.View;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.ui.views.details.SubscriberWidgetViewHolder;
import java.util.Collections;
import java.util.List;
import std_msgs.UInt8;


/**
 * @author jbravo@uma.es (Juan Bravo-Arrabal)
 */

public class ButtonSubscriberDetailVH extends SubscriberWidgetViewHolder {


    @Override
    protected void initView(View parentView) {

    }

    @Override
    protected void bindEntity(BaseEntity entity) {

    }

    @Override
    protected void updateEntity(BaseEntity entity) {

    }

    @Override
    public List<String> getTopicTypes() {
        return Collections.singletonList(UInt8._TYPE);
    }

}
