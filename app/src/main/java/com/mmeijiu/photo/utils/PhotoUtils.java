package com.mmeijiu.photo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.mmeijiu.photo.R;

/**
 * Created by Administrator on 2016/8/12.
 */

public class PhotoUtils  {

    private static int photoitems = R.array.pick_photo;
    private static String title = "获取图片方式";

    public static void showPhotoPickDialog(final Activity context) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(photoitems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                PhotoImageUtils.takeFromCamera(context, PhotoImageUtils.IMAGE_CODE, PhotoImageUtils.fileName);
                                break;
                            case 1:
                                PhotoImageUtils.takeFromGallery(context, PhotoImageUtils.IMAGE_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("返回", null)
                .show();
    }

    public static int getPhotoitems() {
        return photoitems;
    }

    public static void setPhotoitems(int photoitems) {
        PhotoUtils.photoitems = photoitems;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        PhotoUtils.title = title;
    }
}
