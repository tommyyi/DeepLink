package com.laser.flowmanager.ui;

import java.util.List;

import com.laser.flowcommon.IAdModel;
import com.laser.flowcommon.IBaseBean;

/**
 * This class (a) defines behaviour for components having children, (b) stores
 * child components, and (c) implements child-related operations in the Component
 * interface.
 * @version 1.0
 */
public class RealAdModelGroup implements IAdModel
{
	private int mIndex=0;
    /*广告模块列表*/
    private List<IAdModel> mAdModelList;
    
    public RealAdModelGroup(List<IAdModel> adModelList)
    {
		mAdModelList=adModelList;
    }

    public IBaseBean getAd()
    {
		IBaseBean ad=null;
		while (ad==null)
		{
			if(mIndex<mAdModelList.size())
            {
                ad = mAdModelList.get(mIndex).getAd();
                mIndex++;
            }
            else
            {
                ad = mAdModelList.get(0).getAd();
				mIndex=1;
            }
		}
		return ad;
    }
}