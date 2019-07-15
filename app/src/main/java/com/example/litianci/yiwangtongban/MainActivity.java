package com.example.litianci.yiwangtongban;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.litianci.yiwangtongban.banshi.JixuBanli1Activity;
import com.example.litianci.yiwangtongban.jindu.JinduSearchActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements View.OnClickListener {


    @Bind(R.id.iv_wybs)
    ImageView ivWybs;
    @Bind(R.id.iv_bszn)
    ImageView ivBszn;
    @Bind(R.id.iv_jdcx)
    ImageView ivJdcx;
    @Bind(R.id.iv_print)
    ImageView ivPrint;
    @Bind(R.id.btn_jxbl)
    Button btnJxbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setOnClick();

    }

    /**
     * 控件绑定监听器
     */
    public void setOnClick() {
        ivWybs.setOnClickListener(this);
        ivBszn.setOnClickListener(this);
        ivJdcx.setOnClickListener(this);
        ivPrint.setOnClickListener(this);
        btnJxbl.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_wybs: {
                Intent intent = new Intent(MainActivity.this, BanliShixiangActivity.class);
                intent.putExtra("type", "banshi");
                startActivity(intent);
            }
            break;
            case R.id.iv_bszn: {
                Intent intent = new Intent(MainActivity.this, BanliShixiangActivity.class);
                intent.putExtra("type", "zhinan");
                startActivity(intent);
            }
            break;
            case R.id.iv_jdcx: {
                Intent intent = new Intent(MainActivity.this, JinduSearchActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.iv_print: {
                Intent intent = new Intent(MainActivity.this, PrintActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.btn_jxbl: {
                Intent intent = new Intent(MainActivity.this, JixuBanli1Activity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}
