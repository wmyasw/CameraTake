package com.wmy.main.entity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20/020.
 */

public class UplaodEntity implements Serializable {

    /**
     * id : 1
     * user_id : 1
     * name : 人民日报
     * update_time : 2018-04-12
     * images : [{"file_stream":"xxxxxx","name":"A1"}]
     */

    private int id;
    private int user_id;
    private String name;
    private String update_time;
    private List<ImagesBean> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements Serializable{
        /**
         * file_stream : xxxxxx
         * name : A1
         */

        private File file_stream;
        private String name;

        public File getFile_stream() {
            return file_stream;
        }

        public void setFile_stream(File file_stream) {
            this.file_stream = file_stream;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ImagesBean{" +
                    "file_stream='" + file_stream + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UplaodEntity{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", update_time='" + update_time + '\'' +
                ", images=" + images +
                '}';
    }
}
