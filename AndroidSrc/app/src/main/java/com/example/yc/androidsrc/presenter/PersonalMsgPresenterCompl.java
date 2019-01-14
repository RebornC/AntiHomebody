package com.example.yc.androidsrc.presenter;

import android.content.Context;

import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * 用户个人界面的数据访问
 *
 * @author RebornC
 * Created on 2019/1/10.
 */

public class PersonalMsgPresenterCompl implements IPersonalMsgPresenter {

    private IPersonalMsgView iPersonalMsgView;
    private static final int UPLOAD_HEADPORTRAIT_CODE = 1; // 上传头像
    private static final int UPLOAD_MESSAGE_CODE = 2; // 上传其他用户信息
    private static final int SYNC_CODE = 3; // 同步云端

    public PersonalMsgPresenterCompl(IPersonalMsgView iPersonalMsgView) {
        this.iPersonalMsgView = iPersonalMsgView;
    }

    /**
     * 注销当前用户
     */
    @Override
    public void logOut() {
        BmobUser.logOut();
    }

    /**
     * 将用户新更换的头像上传到后台
     * @param uri
     */
    @Override
    public void uploadHeadPortrait(String uri) {
        final BmobFile file = new BmobFile(new File(uri));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    _User current_user = BmobUser.getCurrentUser(_User.class);
                    current_user.setHeadPortrait(file);
                    current_user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                iPersonalMsgView.onUpdateData(true, UPLOAD_HEADPORTRAIT_CODE, "头像更换成功");
                            } else {
                                iPersonalMsgView.onUpdateData(false, UPLOAD_HEADPORTRAIT_CODE, "头像更换失败："+e.getMessage());
                            }
                        }
                    });
                } else {
                    iPersonalMsgView.onUpdateData(false, UPLOAD_HEADPORTRAIT_CODE, "头像上传失败："+e.getMessage());
                }
            }
        });
    }

    /**
     * 修改用户名字
     * @param newName
     */
    @Override
    public void modifyUserNickname(String newName) {
        _User current_user = BmobUser.getCurrentUser(_User.class);
        current_user.setNickName(newName);
        current_user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null) {
                    iPersonalMsgView.onUpdateData(true, UPLOAD_MESSAGE_CODE, "修改成功");
                } else {
                    iPersonalMsgView.onUpdateData(false, UPLOAD_MESSAGE_CODE, "修改失败："+e.getMessage());
                }
            }
        });
    }

    /**
     * 修改用户签名
     * @param newSignature
     */
    @Override
    public void modifyUserSignature(String newSignature) {
        _User current_user = BmobUser.getCurrentUser(_User.class);
        current_user.setSignature(newSignature);
        current_user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    iPersonalMsgView.onUpdateData(true, UPLOAD_MESSAGE_CODE, "修改成功");
                } else {
                    iPersonalMsgView.onUpdateData(false, UPLOAD_MESSAGE_CODE, "修改失败："+e.getMessage());
                }
            }
        });
    }

    /**
     * 同步云端
     * 有时候会因为网络问题导致云端数据与本地数据不一致
     * 比如本地数据更新了，云端数据却因为没网而无法更新
     * 所以增加这个功能，防止这个情况
     */
    @Override
    public void syncBackend(Context context) {
        _User curUser = BmobUser.getCurrentUser(_User.class);
        String objectId = curUser.getObjectId();
        UserDataDao userDataDao = new UserDataDao(context);
        _User sqlUser = userDataDao.getUserDataById(objectId);
        curUser.setCurLevel(sqlUser.getCurLevel());
        curUser.setNumerator(sqlUser.getNumerator());
        curUser.setDenominator(sqlUser.getDenominator());
        curUser.setCurEnergy(sqlUser.getCurEnergy());
        curUser.setTotalEnergy(sqlUser.getTotalEnergy());
        curUser.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    iPersonalMsgView.onUpdateData(true, SYNC_CODE, "同步成功");
                } else {
                    iPersonalMsgView.onUpdateData(false, SYNC_CODE, "同步失败");
                }
            }
        });
    }
}
