package com.schneewittchen.rosandroid.widgets.gridmap;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import java.util.List;

import nav_msgs.OccupancyGrid;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class GridMapEntity extends SubscriberLayerEntity {
    
    public GridMapEntity() {
        this.topic = new Topic("/move_base/local_costmap/costmap", OccupancyGrid._TYPE);
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
