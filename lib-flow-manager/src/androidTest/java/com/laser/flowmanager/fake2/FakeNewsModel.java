package com.laser.flowmanager.fake2;

import com.laser.flowcommon.IAdModel;
import com.laser.flowcommon.IBaseBean;
import com.laser.flowcommon.IFlowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 易剑锋 on 2017/6/13.
 */

public class FakeNewsModel implements IFlowModel
{
    @Override
    public List<IBaseBean> getInfoList()
    {
        return getCacheList();
    }

    @Override
    public List<IBaseBean> refresh()
    {
        return getCacheList();
    }

    @Override
    public List<IBaseBean> getCacheList()
    {
        FakeNewsDataBean fakeNewsDataBean1=new FakeNewsDataBean();
        FakeNewsDataBean fakeNewsDataBean2=new FakeNewsDataBean();
        FakeNewsDataBean fakeNewsDataBean3=new FakeNewsDataBean();
        List<IBaseBean> list=new ArrayList<>();
        list.add(fakeNewsDataBean1);
        list.add(fakeNewsDataBean2);
        list.add(fakeNewsDataBean3);
        return list;
    }
}
