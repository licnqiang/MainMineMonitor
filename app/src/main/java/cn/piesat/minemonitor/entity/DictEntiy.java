package cn.piesat.minemonitor.entity;

/**
 * Created by yjl on 2018/5/25.
 *
 *
 * 2018-2-25新增需求，字典实体类
 */




public class DictEntiy {
    private  String id;
    private  String code_type;
    private  String code_value;
    private  String code_name_en;
    private  String idcode_name_ch;
    private  String belongs_id;
    private  String note;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getCode_value() {
        return code_value;
    }

    public void setCode_value(String code_value) {
        this.code_value = code_value;
    }

    public String getCode_name_en() {
        return code_name_en;
    }

    public void setCode_name_en(String code_name_en) {
        this.code_name_en = code_name_en;
    }

    public String getIdcode_name_ch() {
        return idcode_name_ch;
    }

    public void setIdcode_name_ch(String idcode_name_ch) {
        this.idcode_name_ch = idcode_name_ch;
    }

    public String getBelongs_id() {
        return belongs_id;
    }

    public void setBelongs_id(String belongs_id) {
        this.belongs_id = belongs_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }



}
