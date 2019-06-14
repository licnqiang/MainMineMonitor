package cn.piesat.minemonitor.rxbus.event;

import pie.core.Layer;

/**
 * Created by admin on 2017/6/7.
 */

public class ThemeLayerEvent {

    public Layer m_layer;

    public ThemeLayerEvent(Layer layer) {
        this.m_layer = layer;
    }
}
