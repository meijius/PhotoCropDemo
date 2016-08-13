package com.mmeijiu.photo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mmeijiu.photo.utils.PhotoImageUtils;
import com.mmeijiu.photo.utils.PhotoUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.button)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.button:
                PhotoUtils.showPhotoPickDialog(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            // 拍照和本地选择获取图片
            case PhotoImageUtils.IMAGE_CODE:
                PhotoImageUtils.cropPicture(data, MainActivity.this); // 剪裁
//                imageView.setImageURI(PhotoImageUtils.imageUriFromCamera); // 拍照不剪裁
//                imageView.setImageURI(PhotoImageUtils.cropImageUri); // 本地获取不剪裁
                break;
            // 裁剪图片后结果
            case PhotoImageUtils.REQUEST_CropPictureActivity:
                if (PhotoImageUtils.cropImageUri != null) {
                    imageView.setImageURI(null);
                    imageView.setImageURI(PhotoImageUtils.cropImageUri);
                }
                break;
            default:
                break;
        }
    }

}
