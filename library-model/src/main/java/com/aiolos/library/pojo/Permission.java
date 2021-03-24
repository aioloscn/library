package com.aiolos.library.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class Permission implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 权限
     */
    @Column(name = "permission_name")
    private String permissionName;

    /**
     * 模块id
     */
    @Column(name = "module_id")
    private Long moduleId;

    /**
     * 行为id
     */
    @Column(name = "action_id")
    private Long actionId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取权限
     *
     * @return permission_name - 权限
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * 设置权限
     *
     * @param permissionName 权限
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    /**
     * 获取模块id
     *
     * @return module_id - 模块id
     */
    public Long getModuleId() {
        return moduleId;
    }

    /**
     * 设置模块id
     *
     * @param moduleId 模块id
     */
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 获取行为id
     *
     * @return action_id - 行为id
     */
    public Long getActionId() {
        return actionId;
    }

    /**
     * 设置行为id
     *
     * @param actionId 行为id
     */
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", permissionName=").append(permissionName);
        sb.append(", moduleId=").append(moduleId);
        sb.append(", actionId=").append(actionId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}