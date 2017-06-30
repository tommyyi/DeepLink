package com.laser.flowmanager.fake3;

import com.laser.flowcommon.IAdModel;
import com.laser.flowcommon.IBaseBean;

/**
 * Created by 易剑锋 on 2017/6/13.
 */

public class FakeAdModel3 implements IAdModel
{
    @Override
    public IBaseBean getAd()
    {
        return new FakeAdDataBean3();
    }
}
