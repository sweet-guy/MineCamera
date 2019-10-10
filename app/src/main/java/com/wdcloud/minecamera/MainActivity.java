package com.wdcloud.minecamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private int cameraid=0;
    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        surfaceView = findViewById(R.id.surface_view);
//        Button takephoto=findViewById(R.id.take_photo);
        Button nicephoto=findViewById(R.id.nice_photo);
        Button chage=findViewById(R.id.chage);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
        chage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopPreview();
                camera.release();
                if(cameraid==0)
                {
                    cameraid=1;
                    onChange();
                }
                else
                {
                    cameraid=0;
                    onChange();
                }
            }
        });
//        takephoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                camera.takePicture(new Camera.ShutterCallback() {
//                    @Override
//                    public void onShutter() {
//
//                    }
//                }, new Camera.PictureCallback() {
//                    @Override
//                    public void onPictureTaken(byte[] data, Camera camera) {
//
//                    }
//                }, new Camera.PictureCallback() {
//                    @Override
//                    public void onPictureTaken(byte[] data, Camera camera) {
//                        String path = Environment.getExternalStorageDirectory() +"/test.jpg";
//                        try {
//                            data2file(data,path);
//                        }catch (Exception e) {
//                            Log.d("aabccdd","进入catch");
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
        nicephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(null);

            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open(0);
        setCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(camera!= null) {
            //停止浏览
            camera.stopPreview();
            camera.release();
        }
    }
    public Camera.Size parameters(Camera camera) {
        //照片支持的分辨率列表
        List<Camera.Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();
        //预览画面的分辨率列表
        //List<Camera.Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();
        Camera.Size psize;
        Camera.Size maxpsize=null;
        for (int i = 0;i < pictureSizes.size();i++) {
            psize = pictureSizes.get(i);
            Log.i("pictureSize","aaaaa"+psize.width+" x "+psize.height);
            if(maxpsize==null||maxpsize.width<psize.width){
                maxpsize=psize;
            }
        }
//        for (int i = 0; i < previewSizes.size(); i++) {
//            psize = previewSizes.get(i);
//            Log.i("previewSize","bbbbb"+psize.width+" x "+psize.height);
//        }
        return maxpsize;
    }
    private void data2file(byte[] w,String fileName) throws Exception {//将二进制数据转换为文件的函数
        FileOutputStream out =null;
        try {
            out =new FileOutputStream(fileName);
            out.write(w);
            out.close();
        }catch (Exception e) {
            Log.d("aabccdd","进入catch222");
            if (out !=null)
                out.close();
            throw e;
        }
    }
    public void onChange() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
            camera = Camera.open(cameraid);
            setCamera(surfaceView.getHolder());

    }
    private void setCamera(SurfaceHolder holder){
        try {
            //获取camera参数（参数集合）
            Camera.Parameters parameters =camera.getParameters();
            //判断手机横向或纵向（手机竖着时图像是反的，需要旋转图像）
            if(this.getResources().getConfiguration().orientation
                    != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation","portrait");
                camera.setDisplayOrientation(90);
                parameters.setRotation(90);
            }else {
                parameters.set("orientation","landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            //获取图片分辨率（拍照的图片的分辨率是需要设置的，而且不能随便设置，不同的摄像头支持的分辨率不同，随便设置会报错）
            Camera.Size psize=parameters(camera);
            if(psize!=null)
                //设置照片的分辨率（不设置照片可能会模糊）
                parameters.setPictureSize(psize.width,psize.height);
            //将参数设置给camera对象
            camera.setParameters(parameters);
            //设置预览监听
            camera.setPreviewDisplay(holder);
            //启动摄像头预览
            camera.startPreview();
        }catch (IOException e) {
            e.printStackTrace();
            camera.release();
        }
    }
}
