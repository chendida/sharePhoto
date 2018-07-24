package com.zq.dynamicphoto.mylive.ui;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.adapter.ConsumptionAdapter;
import com.zq.dynamicphoto.mylive.bean.LiveConsumeRecord;
import com.zq.dynamicphoto.ui.widge.DoubleTimeSelectDialog;

import java.util.ArrayList;
import butterknife.BindView;

/**
 * 消费清单界面
 */
public class ConsumptionListActivity extends AppCompatActivity {
    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.layout_finish)
    AutoRelativeLayout layoutFinish;
    @BindView(R.id.rcl_consumption)
    RecyclerView rclConsumption;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.layout_init)
    AutoRelativeLayout layoutInit;
    @BindView(R.id.et_search)
    EditText etSearch;
    private int pager = 1;
    int pagerCount = 1;//总页码数
    Dialog dialogUtils;
    ArrayList<LiveConsumeRecord> liveConsumeRecords;
    ConsumptionAdapter mAdapter;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
    /**
     * 默认的周开始时间，格式如：yyyy-MM-dd
     **/
    public String defaultWeekBegin;
    /**
     * 默认的周结束时间，格式如：yyyy-MM-dd
     */
    public String defaultWeekEnd;

    private String beginTime = "";
    private String finishTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_list);
    }
}
