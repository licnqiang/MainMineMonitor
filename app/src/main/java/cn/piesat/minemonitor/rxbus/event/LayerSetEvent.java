package cn.piesat.minemonitor.rxbus.event;

import java.util.List;

import pie.core.LayerSet;

/**
 * Created by admin on 2017/6/7.
 */

public class LayerSetEvent {

    public List<LayerSet> layerSets;

    public LayerSetEvent(List<LayerSet> layerSets) {
        this.layerSets = layerSets;
    }
}
