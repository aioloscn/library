package com.aiolos.library.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class Action implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 行为编号
     */
    @Column(name = "action_code")
    private String actionCode;

    /**
     * 行为名称
     */
    @Column(name = "action_name")
    private String actionName;

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
     * 获取行为编号
     *
     * @return action_code - 行为编号
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * 设置行为编号
     *
     * @param actionCode 行为编号
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode == null ? null : actionCode.trim();
    }

    /**
     * 获取行为名称
     *
     * @return action_name - 行为名称
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * 设置行为名称
     *
     * @param actionName 行为名称
     */
    public void setActionName(String actionName) {
        this.actionName = actionName == null ? null : actionName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", actionCode=").append(actionCode);
        sb.append(", actionName=").append(actionName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}