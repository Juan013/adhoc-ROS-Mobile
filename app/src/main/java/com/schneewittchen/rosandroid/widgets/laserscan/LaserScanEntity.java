package com.schneewittchen.rosandroid.widgets.laserscan;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;

import java.util.List;

import sensor_msgs.LaserScan;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 14.05.21
 */
public class LaserScanEntity extends SubscriberLayerEntity {

    public int pointsColor;
    public int areaColor;
    public int pointSize;
    public boolean showFreeSpace;


    public LaserScanEntity() {
        this.topic = new Topic("/scan", LaserScan._TYPE);
        this.pointsColor = ROSColor.fromHexAndAlpha("377dfa", 0.6f).toInt();
        this.areaColor = ROSColor.fromHexAndAlpha("377dfa", 0.2f).toInt();
        this.pointSize = 10;
        this.showFreeSpace = true;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    protected void bindEntity(BaseEntity entity) {

    }

    @Override
    protected void updateEntity(BaseEntity entity) {

    }

    @Override
    public List<String> getTopicTypes() {
        return null;
    }
}
