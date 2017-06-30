package com.laser.events;

import com.laser.flowcommon.IBaseBean;

/**
 * @author Administrator
 * @description
 * @data 2017/6/16
 */

public class ItemRemoveEvent {

    private IBaseBean mIBaseBean;

    public ItemRemoveEvent(IBaseBean IBaseBean) {
        mIBaseBean = IBaseBean;
    }

    public IBaseBean getIBaseBean() {
        return mIBaseBean;
    }
}
