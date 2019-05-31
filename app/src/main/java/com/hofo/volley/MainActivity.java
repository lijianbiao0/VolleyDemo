package com.hofo.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView mImg;
    private NetworkImageView mNetworkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg = (ImageView) findViewById(R.id.imageView);
        mNetworkImageView = findViewById(R.id.niv);
    }


    public void post(View view) {
// 1 创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);
// 2 创建一个请求
        String url = "http://192.168.3.9:8080/GetAndPostTest/MianTest";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 正确接收数据回调
            @Override
            public void onResponse(String s) {
                Log.e("AppDebug", "onResponse: " + s);
            }
        }, new Response.ErrorListener() {
            // 发生异常后的监听回调
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("AppDebug", "onErrorResponse: " + volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("name", "param1");
                return map;
            }
        };

// 3 将创建的请求添加到请求队列中
        requestQueue.add(stringRequest);
    }

    public void get(View view) {
        // 1 创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // 2 创建一个请求
        String url = "http://192.168.3.9:8080/GetAndPostTest/MianTest";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            // 正确接收数据回调
            @Override
            public void onResponse(String s) {
                Log.e("AppDebug", "onResponse: " + s);
            }
        }, new Response.ErrorListener() {
            // 发生异常后的监听回调
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("AppDebug", "onErrorResponse: " + volleyError);
            }
        });

        // 3 将创建的请求添加到请求队列中
        requestQueue.add(stringRequest);
    }

    public void Json(View view) {
// 1 创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);

// 2 创建一个请求
        String url = "http://192.168.3.9:8080/GetAndPostTest/MianTest";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("AppDebug", "onResponse: " + jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("AppDebug", "onErrorResponse: " + volleyError);
            }
        });

// 3 将创建的请求添加到请求队列中
        requestQueue.add(jsonObjectRequest);
    }

    public void getImg(View view) {
        mImg.setImageResource(android.R.color.white);
// 1 创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);
// 2 创建一个图片的请求
        String url = "http://img3.duitang.com/uploads/item/201510/11/20151011101817_fZ2hJ.thumb.700_0.jpeg";
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                // 正确接收到图片
                mImg.setVisibility(View.VISIBLE);
                mImg.setImageBitmap(bitmap);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //错误处理
            }
        });
// 3 将请求添加到请求队列中
        requestQueue.add(imageRequest);
    }

    public void imageloader(View view) {
// 创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);
// 创建一个imageloader
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
// 加载图片
        String url = "http://img3.duitang.com/uploads/item/201510/11/20151011101817_fZ2hJ.thumb.700_0.jpeg";
        mImg.setVisibility(View.VISIBLE);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(mImg, android.R.color.transparent, android.R.color.black);
        imageLoader.get(url, imageListener);
    }

    public void showNIV(View view) {
// 让控件显示
        mNetworkImageView.setVisibility(View.VISIBLE);
// 默认图片和异常图片设置
        mNetworkImageView.setDefaultImageResId(android.R.color.transparent);
        mNetworkImageView.setErrorImageResId(android.R.color.black);
// 创建一个请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
// 创建一个Imageloader
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
// 加载图片
        String url = "http://img3.duitang.com/uploads/item/201510/11/20151011101817_fZ2hJ.thumb.700_0.jpeg";
        mNetworkImageView.setImageUrl(url, imageLoader);
    }

    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;// 10m
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }
}
