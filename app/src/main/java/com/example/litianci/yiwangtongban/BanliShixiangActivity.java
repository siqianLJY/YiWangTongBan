package com.example.litianci.yiwangtongban;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.adapter.CommonAdapter;
import com.example.litianci.yiwangtongban.adapter.ViewHolder;
import com.example.litianci.yiwangtongban.banshi.Banshi2Activity;
import com.example.litianci.yiwangtongban.banshi.BanshiSearchListActivity;
import com.example.litianci.yiwangtongban.been.BanshibeenResult;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;
import com.example.litianci.yiwangtongban.views.GridViewForScrollView;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanliShixiangActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.btn_zhuti)
    Button btnZhuti;
    @Bind(R.id.btn_bumen)
    Button btnBumen;
    @Bind(R.id.btn_zhouqi)
    Button btnZhouqi;
    @Bind(R.id.btn_tddx)
    Button btnTddx;
    @Bind(R.id.gridview)
    GridViewForScrollView gridview;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.webview)
    WebView webview;
    private int biaoji = 0;
    private ArrayList<Map<String, Object>> items;
    private CommonAdapter adapter1;
    List<BanshibeenResult.DataBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banli_shixiang);
        ButterKnife.bind(this);
        Globals.context = getApplicationContext();
        initImageConfigure();
        setOnClick();
        getList(this, "1");
//        addGridView();
    }

    /**
     * 初始化ImageLoader 设置了内存已经磁盘缓存 可以通过uri获取缓存图片
     */
    public static ImageLoaderConfiguration getImageLoaderConfiguration() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.hj_zhanwei) // resource or
                // drawable
                .showImageForEmptyUri(R.mipmap.hj_zhanwei) // resource or
                // drawable
                // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.EXACTLY)
                // .preProcessor(...)
                // .postProcessor(...)
                // .extraForDownloader(...)
                .considerExifParams(false) // default
                // .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //
                // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                // .decodingOptions(...)
                // .displayer(new SimpleBitmapDisplayer()) // default
                // .handler(new Handler()) // default
                .build();

        return new ImageLoaderConfiguration.Builder(Globals.context)
                // .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // .diskCacheExtraOptions(480, 800, null)
                // .taskExecutor(...)
                // .taskExecutorForCachedImages(...)
                .threadPoolSize(2)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 2)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                // default
                // .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                // .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(Globals.context)) // default
                // .imageDecoder(new BaseImageDecoder()) // default
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .defaultDisplayImageOptions(options)//
                .writeDebugLogs().build();
    }

    public static void initImageConfigure() {
        ImageLoader.getInstance().init(getImageLoaderConfiguration());
    }

    public void setOnClick() {
        btnSearch.setOnClickListener(this);
        btnZhuti.setOnClickListener(this);
        btnBumen.setOnClickListener(this);
        btnZhouqi.setOnClickListener(this);
        btnTddx.setOnClickListener(this);
        gridview.setOnItemClickListener(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
    }

    public void getList(final Context context, String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                BanshibeenResult s3 = GsonUtils.json2bean(response, BanshibeenResult.class);
                Log.i("MainActivity", response);

                if (s3 == null || s3.getCode() != 0) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else {
                    list = s3.getData();
                    items = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < s3.getData().size(); i++) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("imageItem", s3.getData().get(i).getIcons());//添加图像资源的ID
                        item.put("textItem", s3.getData().get(i).getName());//按序号添加ItemText
                        items.add(item);
                    }
                    adapter1 = new CommonAdapter<Map<String, Object>>(BanliShixiangActivity.this, items, R.layout.grid_item) {
                        @Override
                        public void convert(ViewHolder holder, Map<String, Object> o) {

                            holder.setText(R.id.grid_text, (String) o.get("textItem"));
                            ImageView img = holder.getView(R.id.grid_iamge);
                            ImageLoader.getInstance().displayImage(Globals.ICONpath + (String) o.get("imageItem"), img);
                        }
                    };
                    gridview.setAdapter(adapter1);

                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "gettypelist?type=" + type, null);
    }

    private void addGridView() {

        //适配显示数据
//        政务中心
        items = new ArrayList<Map<String, Object>>();

        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.sysy);//添加图像资源的ID
            item.put("textItem", "生育收养");//按序号添加ItemText
            items.add(item);

        }

        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shbz);//添加图像资源的ID
            item.put("textItem", "社会保障");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.ylws);//添加图像资源的ID
            item.put("textItem", "医疗卫生");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.jyky);//添加图像资源的ID
            item.put("textItem", "教育科研");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.zfbz);//添加图像资源的ID
            item.put("textItem", "住房保障");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.zhengjianbanli);//添加图像资源的ID
            item.put("textItem", "证件办理");//按序号添加ItemText
            items.add(item);
        }


        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shehuijiuzhu);//添加图像资源的ID
            item.put("textItem", "社会救助");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.lizhituixiu);//添加图像资源的ID
            item.put("textItem", "离职退休");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shiyejiuye);//添加图像资源的ID
            item.put("textItem", "失业就业");//按序号添加ItemText
            items.add(item);
        }
        if (true) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.shipinyaopin);//添加图像资源的ID
            item.put("textItem", "食品药品");//按序号添加ItemText
            items.add(item);
        }
        adapter1 = new CommonAdapter<Map<String, Object>>(BanliShixiangActivity.this, items, R.layout.grid_item) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> o) {

                holder.setImageResource(R.id.grid_iamge, (Integer) o.get("imageItem")).setText(R.id.grid_text, (String) o.get("textItem"));
            }
        };
        gridview.setAdapter(adapter1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search: {
                Intent intent = new Intent(BanliShixiangActivity.this, BanshiSearchListActivity.class);
                intent.putExtra("type", getIntent().getStringExtra("type"));
                intent.putExtra("key", etSearch.getText().toString().trim());

                startActivity(intent);


            }
            break;
            case R.id.btn_zhuti: {
                biaoji = 0;
                btnZhuti.setTextColor(this.getResources().getColor(R.color.colorwhite));
                btnBumen.setTextColor(this.getResources().getColor(R.color.color333));
                btnZhouqi.setTextColor(this.getResources().getColor(R.color.color333));
                btnTddx.setTextColor(this.getResources().getColor(R.color.color333));
                btnZhuti.setBackgroundColor(this.getResources().getColor(R.color.colorbanshi));
                btnBumen.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnZhouqi.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnTddx.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                getList(this, "1");


            }
            break;
            case R.id.btn_bumen: {
                biaoji = 1;
                btnZhuti.setTextColor(this.getResources().getColor(R.color.color333));
                btnBumen.setTextColor(this.getResources().getColor(R.color.colorwhite));
                btnZhouqi.setTextColor(this.getResources().getColor(R.color.color333));
                btnTddx.setTextColor(this.getResources().getColor(R.color.color333));
                btnZhuti.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnBumen.setBackgroundColor(this.getResources().getColor(R.color.colorbanshi));
                btnZhouqi.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnTddx.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                getList(this, "2");

            }
            break;
            case R.id.btn_zhouqi: {
                biaoji = 2;
                btnZhuti.setTextColor(this.getResources().getColor(R.color.color333));
                btnBumen.setTextColor(this.getResources().getColor(R.color.color333));
                btnZhouqi.setTextColor(this.getResources().getColor(R.color.colorwhite));
                btnTddx.setTextColor(this.getResources().getColor(R.color.color333));
                btnZhuti.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnBumen.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnZhouqi.setBackgroundColor(this.getResources().getColor(R.color.colorbanshi));
                btnTddx.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                getList(this, "3");

            }
            break;
            case R.id.btn_tddx: {
                biaoji = 3;
                btnZhuti.setTextColor(this.getResources().getColor(R.color.color333));
                btnBumen.setTextColor(this.getResources().getColor(R.color.color333));
                btnZhouqi.setTextColor(this.getResources().getColor(R.color.color333));
                btnTddx.setTextColor(this.getResources().getColor(R.color.colorwhite));
                btnZhuti.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnBumen.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnZhouqi.setBackgroundColor(this.getResources().getColor(R.color.colorwhite));
                btnTddx.setBackgroundColor(this.getResources().getColor(R.color.colorbanshi));
                getList(this, "4");
            }
            break;
            case R.id.iv_home: {

                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(BanliShixiangActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                adb.create().show();


            }
            break;
            case R.id.iv_last: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认返回上一页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                adb.create().show();


            }
            break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(BanliShixiangActivity.this, Banshi2Activity.class);
        intent.putExtra("type", getIntent().getStringExtra("type"));
        intent.putExtra("theme", list.get(i).getName());
        intent.putExtra("type2", list.get(i).getType() + "");
        intent.putExtra("id", list.get(i).getId() + "");
        startActivity(intent);
    }
}
