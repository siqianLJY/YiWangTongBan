package com.example.litianci.yiwangtongban.banshi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BanshiChooseTypeActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.tv_theme)
    TextView tvTheme;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.iv_zjtxbd)
    ImageView ivZjtxbd;
    @Bind(R.id.iv_dytxbd)
    ImageView ivDytxbd;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banshi_choose_type);
        ButterKnife.bind(this);
        tvTheme.setText(getIntent().getStringExtra("theme"));
        ivZjtxbd.setOnClickListener(this);
        ivDytxbd.setOnClickListener(this);
        ivHome.setOnClickListener(this);
        ivLast.setOnClickListener(this);
        Log.i("ID", getIntent().getStringExtra("ID"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_zjtxbd: {
                Intent intent = new Intent(BanshiChooseTypeActivity.this, BanshiTXBDActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("theme",getIntent().getStringExtra("theme"));
                startActivity(intent);
            }
            break;
            case R.id.iv_dytxbd: {
                Intent intent = new Intent(BanshiChooseTypeActivity.this, BanshiZjdyFormActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("theme",getIntent().getStringExtra("theme"));
                startActivity(intent);
            }
            break;
            case R.id.iv_home: {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(BanshiChooseTypeActivity.this, MainActivity.class);
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
}
