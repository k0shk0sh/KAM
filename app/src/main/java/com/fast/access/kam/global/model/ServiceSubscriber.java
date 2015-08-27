package com.fast.access.kam.global.model;

import java.util.Map;

/**
 * Created by Kosh on 8/26/2015. copyrights are reserved
 */
public class ServiceSubscriber {

    public enum OperationType {
        BACKUP, RESTORE, CUSTOM_BACKUP
    }

    private OperationType operationType;

    private Map<Integer, AppsModel> modelMap;

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Map<Integer, AppsModel> getModelMap() {
        return modelMap;
    }

    public void setModelMap(Map<Integer, AppsModel> modelMap) {
        this.modelMap = modelMap;
    }
}
