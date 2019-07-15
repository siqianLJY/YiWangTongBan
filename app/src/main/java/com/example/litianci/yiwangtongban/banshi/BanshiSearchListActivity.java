package com.example.litianci.yiwangtongban.banshi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.litianci.yiwangtongban.been.BanshiSearchResult;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;
import com.example.litianci.yiwangtongban.views.ListViewForScrollView;
import com.example.litianci.yiwangtongban.zhinan.BanshiZhinanActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanshiSearchListActivity extends Activity implements AdapterView.OnItemClickListener {

    @Bind(R.id.listview)
    ListViewForScrollView listview;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;
    @Bind(R.id.btn_theme)
    Button btnTheme;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.text)
    TextView text;
    private CommonAdapter<BanshiResult> adapter1;
    private ArrayList<BanshiResult> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banshi2);
        ButterKnife.bind(this);
        getList(this, getIntent().getStringExtra("key"));
        btnTheme.setText("搜索结果");
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BanshiSearchListActivity.this, MainActivity.class);
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


        listview.setOnItemClickListener(this);
    }

    public void getList(final Context context, String key) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                BanshiSearchResult s3 = GsonUtils.json2bean(response, BanshiSearchResult.class);
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

                    adapter1 = new CommonAdapter<BanshiResult>(BanshiSearchListActivity.this, items, R.layout.item_banshi_list) {
                        @Override
                        public void convert(ViewHolder holder, final BanshiResult o) {

                            holder.setText(R.id.tv_theme, o.getTheme());

                            if (getIntent().getStringExtra("type").equals("zhinan")) {
                                Button btn = holder.getView(R.id.btn_banli);
                                btn.setVisibility(View.GONE);
                            }
                            holder.setOnClickListener(R.id.btn_zhinan, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(BanshiSearchListActivity.this, BanshiZhinanActivity.class);
                                    intent.putExtra("theme", "");
                                    Log.i("传Id了没", o.getId());
                                    intent.putExtra("id", o.getId());
                                    startActivity(intent);
                                }
                            });
                            holder.setOnClickListener(R.id.btn_banli, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(BanshiSearchListActivity.this, BanshiShenfenActivity.class);
                                    intent.putExtra("theme", "");
                                    intent.putExtra("id", o.getId());
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    listview.setAdapter(adapter1);

                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "searchlist?key=" + key, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
