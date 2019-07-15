package com.example.litianci.yiwangtongban.banshi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.adapter.CommonAdapter;
import com.example.litianci.yiwangtongban.adapter.ViewHolder;
import com.example.litianci.yiwangtongban.been.BanshiResult;
import com.example.litianci.yiwangtongban.been.PauseResult;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;
import com.example.litianci.yiwangtongban.views.ListViewForScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JixuBanli3Activity extends Activity implements AdapterView.OnItemClickListener {

    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.btn_theme)
    Button btnTheme;
    @Bind(R.id.listview)
    ListViewForScrollView listview;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    private CommonAdapter<BanshiResult> adapter1;
    private ArrayList<BanshiResult> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jixu_banli3);
        ButterKnife.bind(this);
        getList(this, getIntent().getStringExtra("id"));
        listview.setOnItemClickListener(this);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JixuBanli3Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getList(final Context context, String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                PauseResult s3 = GsonUtils.json2bean(response, PauseResult.class);
                Log.i("MainActivity", response);
                if (s3 == null || s3.getCode() != 0) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else if (s3.getData().size() == 0) {
                    listview.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                } else {
                    items = new ArrayList<BanshiResult>();
                    for (int i = 0; i < s3.getData().size(); i++) {
                        BanshiResult item = new BanshiResult();
                        item.setTheme(s3.getData().get(i).getName());//按序号添加ItemText
                        item.setId(s3.getData().get(i).getId());//按序号添加ItemText
                        items.add(item);
                    }

                    adapter1 = new CommonAdapter<BanshiResult>(JixuBanli3Activity.this, items, R.layout.item_banshi_list) {
                        @Override
                        public void convert(ViewHolder holder, final BanshiResult o) {

                            holder.setText(R.id.tv_theme, o.getTheme());

                            Button btn = holder.getView(R.id.btn_banli);
                            Button btn2 = holder.getView(R.id.btn_zhinan);
                            btn.setVisibility(View.GONE);
                            btn2.setVisibility(View.GONE);
                        }
                    };
                    listview.setAdapter(adapter1);

                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "getpause?mid=" + key, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(JixuBanli3Activity.this, BanshiZjdyFormActivity.class);
        intent.putExtra("theme", items.get(i).getTheme());
        Log.i("传Id了没", items.get(i).getId());
        intent.putExtra("ID", items.get(i).getId());
        startActivity(intent);
    }


}
