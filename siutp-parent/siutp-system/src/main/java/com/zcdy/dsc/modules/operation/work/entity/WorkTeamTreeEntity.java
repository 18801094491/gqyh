package com.zcdy.dsc.modules.operation.work.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 班组与人员关系
 * @author songguang.jiao
 * @date 2020/7/2 17:47
 */
@Getter
@Setter
public class WorkTeamTreeEntity {

    /**
     * 班组id
     */
    private String teamId;

    /**
     * 班组名称
     */
    private String teamName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;
}
