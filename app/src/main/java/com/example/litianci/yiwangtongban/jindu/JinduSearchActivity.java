package com.example.litianci.yiwangtongban.jindu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.litianci.yiwangtongban.MainActivity;
import com.example.litianci.yiwangtongban.R;
import com.example.litianci.yiwangtongban.been.NormalResult;
import com.example.litianci.yiwangtongban.utils.GsonUtils;
import com.example.litianci.yiwangtongban.utils.VolleyUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JinduSearchActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.et_num)
    EditText etNum;
    @Bind(R.id.et_mima)
    EditText etMima;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.iv_home)
    ImageView ivHome;
    @Bind(R.id.iv_last)
    ImageView ivLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jindu_search);
        ButterKnife.bind(this);
        
        btnSearch.setOnClickListener(this);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(JinduSearchActivity.this);
                adb.setTitle("确认前往首页？");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(JinduSearchActivity.this, MainActivity.class);
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
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(JinduSearchActivity.this);
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
        });
    }

    @Override
    public void onClick(View view) {
        if (etNum.getText().toString().equals("")) {
            Toast.makeText(JinduSearchActivity.this, "请填写申报号", Toast.LENGTH_SHORT).show();
        } else if (etMima.getText().toString().equals("")) {
            Toast.makeText(JinduSearchActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
        } else {
            JinduSearch(this,"");

        }

    }
    public void JinduSearch(final Context context, String phone) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");

        new VolleyUtil() {

            @Override
            public <T> boolean analysisData(String response) {
                NormalResult s3 = GsonUtils.json2bean(response, NormalResult.class);
                Log.i("MainActivity", response);

                if (s3 == null) {
                    Toast.makeText(context, "数据异常",
                            Toast.LENGTH_SHORT).show();


                } else if (s3.getCode() !=0) {
                    Toast toast = Toast.makeText(JinduSearchActivity.this, "申报号或密码不对，请核对后输入", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else if (s3.getCode() == 0) {
                    Intent intent = new Intent(JinduSearchActivity.this, JinduRusultActivity.class);
                    intent.putExtra("num", etNum.getText().toString());
                    intent.putExtra("pass", etMima.getText().toString());
                    startActivity(intent);

                }
                return false;
            }
        }.volleyStringRequestPost(context, params, "matstatus?num=" + etNum.getText().toString()+"&pass="+etMima.getText().toString(), null);
    }
}
