package com.frame.base.utl.andfix;

import android.app.Application;
import android.os.Environment;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.IOException;

/**
 * Created by YANGQIYUN on 2016/5/11.
 */
public class AndfixManager {

    private Application mApplication;

    /**
     * andfix热修复
     * */
    public PatchManager mPatchManager;
    //apatch路径
    private final String APATCH_PATH = "/out.apatch";

    public AndfixManager(Application mApplication){
        this.mApplication = mApplication;
    }
    /**
     * 网络请求获取patch
     * @param currVersion 当前版本
    * @return null if error occured
    */
    public void requstNewPatch(String currVersion){

    }
    /**
     * 初始化需要修复的文件
     *
     * */
    public void initAndfix(String version){
        mPatchManager = new PatchManager(mApplication);
        mPatchManager.init(version);
        // load patch
        mPatchManager.loadPatch();
        // add patch at runtime
        try {
            // .apatch file path
            String patchFileString = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + APATCH_PATH;
            mPatchManager.addPatch(patchFileString);
        } catch (IOException e) {
        }
    }
}
