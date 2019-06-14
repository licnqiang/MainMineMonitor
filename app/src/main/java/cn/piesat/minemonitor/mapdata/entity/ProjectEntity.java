package cn.piesat.minemonitor.mapdata.entity;

import java.io.Serializable;

/**
 * 作者：wangyi
 * <p>
 * 邮箱：wangyi@piesat.cn
 */
public class ProjectEntity  implements Serializable{
    public String name;
    public String path;

    public ProjectEntity(String name,String path){
        this.name = name;
        this.path = path;
    }
}
