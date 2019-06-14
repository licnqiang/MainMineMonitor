package cn.piesat.minemonitor.rxbus.event;

import pie.core.Layer;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ThemepostlayerEvent {
    public Layer m_layer;

    public ThemepostlayerEvent(Layer layer) {
        this.m_layer = layer;
    }
}
