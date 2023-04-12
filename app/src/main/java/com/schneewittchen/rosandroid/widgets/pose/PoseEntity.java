package com.schneewittchen.rosandroid.widgets.pose;

import android.view.View;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.SubscriberLayerEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;

import java.util.List;

import geometry_msgs.PoseWithCovarianceStamped;


/**
 * Pose entity represents a widget which subscribes
 * to a topic with message type "geometry_msgs.PoseStamped".
 * Usable in 2D widgets.
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 10.03.21
 */
public class PoseEntity extends SubscriberLayerEntity {


    public PoseEntity() {
        this.topic = new Topic("/amcl_pose", PoseWithCovarianceStamped._TYPE);
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
