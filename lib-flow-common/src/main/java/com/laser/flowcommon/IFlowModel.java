package com.laser.flowcommon;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @created 13-六月-2017 17:54:24
 */
public interface IFlowModel
{
    List<IBaseBean> getInfoList();
    List<IBaseBean> refresh();
    List<IBaseBean> getCacheList();
}