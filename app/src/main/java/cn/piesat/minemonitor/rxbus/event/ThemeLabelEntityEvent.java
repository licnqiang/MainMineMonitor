package cn.piesat.minemonitor.rxbus.event;

import pie.map.enums.TextAlignmentType;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ThemeLabelEntityEvent {
    public String layerjson;
    public String field;
    public TextAlignmentType type;
    public int forecolor;
    public double fontsize;

    public ThemeLabelEntityEvent() {

    }

    public String getLabelPositionName(TextAlignmentType type)
    {
        String positionname="水平";
        if (type== TextAlignmentType.Horizontal){
            positionname="水平";
        }else if (type== TextAlignmentType.Perpendicular){
            positionname="垂直";
        }else if (type== TextAlignmentType.Parallel){
            positionname="沿线平行";
        }else if (type== TextAlignmentType.Curve){
            positionname="沿线弯曲";
        }
        return positionname;
    }

    public TextAlignmentType getTextAlignmentType(String str) {
        if (str.equals("水平")) {
            return TextAlignmentType.Horizontal;
        } else if (str.equals("垂直")) {
            return TextAlignmentType.Perpendicular;
        } else if (str.equals("沿线平行")) {
            return TextAlignmentType.Parallel;
        } else if (str.equals("沿线弯曲")) {
            return TextAlignmentType.Curve;
        }
        return TextAlignmentType.Horizontal;
    }
}
