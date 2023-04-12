package com.schneewittchen.rosandroid.model.entities.widgets;

import android.view.View;

import java.util.List;

/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 24.09.20
 */
public abstract class SubscriberLayerEntity
        extends BaseEntity
        implements I2DLayerEntity, ISubscriberEntity{

    public abstract void initView(View view);

    protected abstract void bindEntity(BaseEntity entity);

    protected abstract void updateEntity(BaseEntity entity);

    public abstract List<String> getTopicTypes();
}
