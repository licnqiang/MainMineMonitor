package cn.piesat.minemonitor.mapdata.view;

import java.util.List;

import cn.piesat.minemonitor.mapdata.entity.ProjectEntity;
import pie.core.DataSource;

public interface ProjectDataSource {

    /**
     * 获取工程列表
     * @return 工程集合
     */
	List<ProjectEntity> getProjects();

    /**
     * 获取工程所有的数据源
     * @param entity 工程
     * @return
     */
    List<DataSource> getDataSourcesByProject(ProjectEntity entity);

    /**
     * 获取工程的工作空间路径
     * @param entity 工程
     * @return 工作空间路径
     */
    String getWorkspacePathByProject(ProjectEntity entity);

}
