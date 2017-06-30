package com.laser.flowmanager.fake1;

import com.laser.flowcommon.IAdModel;
import com.laser.flowcommon.IBaseBean;

/**
 * Created by 易剑锋 on 2017/6/13.
 */

public class FakeAdModel1 implements IAdModel
{
    @Override
    public IBaseBean getAd()
    {
        return new FakeAdDataBean1();
    }
}
