package cn.piesat.minemonitor.mapdata.mode;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cn.piesat.minemonitor.mapdata.entity.ProjectEntity;
import cn.piesat.minemonitor.mapdata.utils.FileUtils;
import cn.piesat.minemonitor.mapdata.utils.SpHelper;
import cn.piesat.minemonitor.mapdata.view.ProjectDataSource;
import cn.piesat.minemonitor.util.ToastUtil;
import pie.core.DataExchange;
import pie.core.DataSource;
import pie.core.Dataset;

/**
 * 工程管理仓库
 */
public class ProjectRepository implements ProjectDataSource {

    private final String TAG = "Project";
    private String m_root;

    /**
     * 工程数据的根目录
     *
     * @param path
     */
    public ProjectRepository(String path) {
        this.m_root = path;
    }


    @Override
    public List<ProjectEntity> getProjects() {
        List<ProjectEntity> projects = new ArrayList<ProjectEntity>();
        //获取指定文件夹下子文件的名称
        List<String> projectNames = FileUtils.getDirectoryNames(m_root);
        for (String name : projectNames) {
            ProjectEntity entity = new ProjectEntity(name, m_root + File.separator + name);
            projects.add(entity);
        }
        return projects;
    }

    @Override
    public ArrayList<DataSource>  getDataSourcesByProject(ProjectEntity entity) {
        ArrayList<DataSource> dataSources = new ArrayList<>();
        //不需要搜索的文件夹名称
        ArrayList<String> except = new ArrayList<>();
        except.add("config");
        except.add("export");
        //要搜索的文件的后缀名
        ArrayList<String> imageFileSuffixs = new ArrayList<>();
        imageFileSuffixs.add(".xml");
        imageFileSuffixs.add(".tif");
        //获取影像数据源路径
        List<File> imgeFiles = FileUtils.hasFileBySuffix(entity.path, except, imageFileSuffixs);
        if (imgeFiles != null && imgeFiles.size() > 0) {
            for (File file : imgeFiles) {
                DataSource ds = new DataSource();
                boolean isSuccess = ds.open(file.getPath());
                if (isSuccess ) {
                    dataSources.add(ds);
                }
            }
        }
        //获取矢量数据源路径
        //要搜索的文件的后缀名
        List<String> shpFileSuffixs = new ArrayList<>();
        shpFileSuffixs.add(".shp");
        List<File> shpFiles = FileUtils.hasFileBySuffix(entity.path, except, shpFileSuffixs);
        if (shpFiles != null && shpFiles.size() > 0) {
            DataSource vectorDs = new DataSource();
            //判断矢量数据源是否存在
            String vectorRootPath = entity.path + "/config/vector";
            String vectorFilePath = vectorRootPath + "/vector.gsf";
            File file = new File(vectorFilePath);
            if (!file.exists() || !file.isFile()) {
                new File(vectorRootPath).mkdirs();
                vectorDs = DataSource.createDataSource(vectorFilePath);
            } else {
                vectorDs.open(vectorFilePath);
            }
            if (vectorDs != null) {
                for (File shpFile : shpFiles) {
                    String fileName = shpFile.getName();
                    String dtName = fileName.substring(0, fileName.lastIndexOf("."));
                    //是否已存在数据源中
                    boolean isExistsInDt = false;
                    int datasetCount = vectorDs.getDatasetCount();
                    for (int i = 0; i < datasetCount; i++) {
                        Dataset dt = vectorDs.getDataset(i);
                        String newDtName = dt.getName();
                        if (newDtName.equals(dtName)) {
                            isExistsInDt = true;
                            break;
                        }
                    }
                    if (!isExistsInDt) {
                        //将shp数据添加至数据源
                        boolean isSuccess = DataExchange.toGsfFromShp(shpFile.getPath(), vectorDs, dtName);
                        Log.v(TAG,isSuccess+"-----");
                    }
                }
                dataSources.add(vectorDs);
            }
        }

        return dataSources;
    }



    public ArrayList<DataSource> getDataSourcesByProject(ProjectEntity entity , String number) {
        ArrayList<DataSource> dataSources = new ArrayList<>();
        //不需要搜索的文件夹名称
        ArrayList<String> except = new ArrayList<>();
        except.add("config");
        except.add("export");
        //要搜索的文件的后缀名
        ArrayList<String> imageFileSuffixs = new ArrayList<>();
        imageFileSuffixs.add(".xml");
        imageFileSuffixs.add(".tif");
        //获取影像数据源路径
        List<File> imgeFiles = FileUtils.hasFileBySuffix(entity.path+"/data/"+SpHelper.getStringValue("FIELD").trim()+"/"+number+"/B01ImageData", except, imageFileSuffixs);
        if (imgeFiles != null && imgeFiles.size() > 0) {
            for (File file : imgeFiles) {
                DataSource ds = new DataSource();
                boolean isSuccess = ds.open(file.getPath());
                if (isSuccess ) {
                    dataSources.add(ds);
                }
            }
        }

        //获取矢量数据源路径
        //要搜索的文件的后缀名
        List<String> shpFileSuffixs = new ArrayList<>();
        shpFileSuffixs.add(".shp");
        List<File> shpFiles = FileUtils.hasFileBySuffix(entity.path+"/data/"+SpHelper.getStringValue("FIELD").trim()+"/"+number, except, shpFileSuffixs);
        if (shpFiles != null && shpFiles.size() > 0) {
            DataSource vectorDs = new DataSource();
            //判断矢量数据源是否存在
            String vectorRootPath = entity.path + "/config/vector";
            String vectorFilePath = vectorRootPath + "/"+number.substring(number.length()-3,number.length())+ "vector.gsf";
            File file = new File(vectorFilePath);
            if (!file.exists() || !file.isFile()) {
                new File(vectorRootPath).mkdirs();
                vectorDs = DataSource.createDataSource(vectorFilePath);
            } else {
                vectorDs.open(vectorFilePath);
            }
            if (vectorDs != null) {
                for (File shpFile : shpFiles) {
                    String fileName = shpFile.getName();
                    String dtName = fileName.substring(0, fileName.lastIndexOf("."));
                    //是否已存在数据源中
                    boolean isExistsInDt = false;
                    int datasetCount = vectorDs.getDatasetCount();
                    for (int i = 0; i < datasetCount; i++) {
                        Dataset dt = vectorDs.getDataset(i);
                        String newDtName = dt.getName();
                        if (newDtName.equals(dtName)) {
                            isExistsInDt = true;
                            break;
                        }
                    }
                    if (!isExistsInDt) {
                        //将shp数据添加至数据源
                        boolean isSuccess = DataExchange.toGsfFromShp(shpFile.getPath(), vectorDs, dtName);
                        Log.v("-----isSuccess--------",isSuccess+"-----");
                    }
                }
                dataSources.add(vectorDs);
            }
        }

        return dataSources;
    }

    @Override
    public String getWorkspacePathByProject(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }
        return entity.path + "/config/workspace.xml";
    }
}
