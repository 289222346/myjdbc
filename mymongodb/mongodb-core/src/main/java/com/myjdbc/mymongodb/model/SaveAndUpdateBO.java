package com.myjdbc.mymongodb.model;

import org.bson.Document;

import java.io.Serializable;

/**
 * 保存并更新业务实体
 * 用于数据检查且传递
 *
 * @Author 陈文
 * @Date 2020/4/23  16:20
 */
public class SaveAndUpdateBO {

    /**
     * 操作结果
     */
    private Integer code;
    /**
     * 操作ID
     */
    private Serializable id;
    /**
     * 操作对象
     */
    private Document document;
    /**
     * 操作表名
     */
    private String collectionName;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
