package com.zq.dynamicphoto.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zq.dynamicphoto.R;
import com.zq.dynamicphoto.base.BaseActivity;
import com.zq.dynamicphoto.base.BasePresenter;
import com.zq.dynamicphoto.bean.DeviceProperties;
import com.zq.dynamicphoto.bean.DrUtils;
import com.zq.dynamicphoto.bean.DynamicLabel;
import com.zq.dynamicphoto.bean.NetRequestBean;
import com.zq.dynamicphoto.bean.Result;
import com.zq.dynamicphoto.bean.UserInfo;
import com.zq.dynamicphoto.common.Constans;
import com.zq.dynamicphoto.presenter.LabelsPresenter;
import com.zq.dynamicphoto.utils.SharedPreferencesUtils;
import com.zq.dynamicphoto.utils.TitleUtils;
import com.zq.dynamicphoto.view.ILabelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class AddLabelActivity extends BaseActivity<ILabelView,LabelsPresenter<ILabelView>> implements ILabelView{

    @BindView(R.id.layout_back)
    AutoRelativeLayout layoutBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.id_flowlayout)
    FlowLayout flowLayout;
    @BindView(R.id.id_flowlayout_two)
    TagFlowLayout allFlowLayout;

    private List<String> label_list = new ArrayList<>();//上面的标签列表
    private List<String> all_label_List = new ArrayList<>();//所有标签列表
    final List<TextView> labels = new ArrayList<>();//存放标签
    final List<Boolean> labelStates = new ArrayList<>();//存放标签状态
    final Set<Integer> set = new HashSet<>();//存放选中的
    private TagAdapter<String> tagAdapter;//标签适配器
    private LinearLayout.LayoutParams params;
    private EditText editText;
    ArrayList<DynamicLabel> dynamicLabels = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_label;
    }

    @Override
    protected void initView() {
        TitleUtils.setTitleBar(getResources().getString(R.string.add_label), tvTitle,
                layoutBack, ivCamera, tvFinish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        flowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextContent = editText.getText().toString();
                if (TextUtils.isEmpty(editTextContent)) {
                    tagNormal();
                } else {
                    addLabel(editText);
                }
            }
        });
        initEdittext();
        initAllLeblLayout();
    }

    /**
     * 初始化默认的添加标签
     */
    private void initEdittext() {
        editText = new EditText(getApplicationContext());
        editText.setHint("添加标签");
        //设置固定宽度
        editText.setMinEms(4);
        editText.setTextSize(12);
        //设置shape
        editText.setBackgroundResource(R.drawable.add_label);
        editText.setHintTextColor(Color.parseColor("#b4b4b4"));
        editText.setTextColor(Color.parseColor("#000000"));
        editText.setLayoutParams(params);
        //添加到layout中
        flowLayout.addView(editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagNormal();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void allLabelListSet() {
        //初始化适配器
        tagAdapter = new TagAdapter<String>(all_label_List) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.flag_adapter,
                        allFlowLayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
            }
        };

        allFlowLayout.setAdapter(tagAdapter);

        //根据上面标签来判断下面的标签是否含有上面的标签
        for (int i = 0; i < label_list.size(); i++) {
            for (int j = 0; j < all_label_List.size(); j++) {
                if (label_list.get(i).equals(
                        all_label_List.get(j))) {
                    //Log.i(TAG, "选中 = " + i);
                    tagAdapter.setSelectedList(i);//设为选中
                }
            }
        }
        tagAdapter.notifyDataChanged();
    }

    /**
     * 初始化所有标签列表
     */
    private void initAllLeblLayout() {
        allLabelListSet();

        //给下面的标签添加监听
        allFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //Log.i(TAG, "position = " + position);
                if (labels.size() == 0) {
                    editText.setText(all_label_List.get(position));
                    addLabel(editText);
                    return false;
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < labels.size(); i++) {
                    list.add(labels.get(i).getText().toString());
                }
                //如果上面包含点击的标签就删除
                if (list.contains(all_label_List.get(position))) {
                    for (int i = 0; i < list.size(); i++) {
                        if (all_label_List.get(position).equals(list.get(i))) {
                            flowLayout.removeView(labels.get(i));
                            labels.remove(i);
                        }
                    }
                } else {
                    editText.setText(all_label_List.get(position));
                    addLabel(editText);
                }
                return false;
            }
        });

        //已经选中的监听
        allFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                set.clear();
                set.addAll(selectPosSet);
            }
        });

    }

    /**
     * 添加标签
     *
     * @param editText
     * @return
     */
    private boolean addLabel(EditText editText) {
        String editTextContent = editText.getText().toString();
        //判断输入是否为空
        if (editTextContent.equals(""))
            return true;
        //判断是否重复
        for (TextView tag : labels) {
            String tempStr = tag.getText().toString();
            if (tempStr.equals(editTextContent)) {
                editText.setText("");
                editText.requestFocus();
                Toast.makeText(this, getResources().getString(R.string.label_is_have), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        //添加标签
        final TextView temp = getTag(editText.getText().toString());
        labels.add(temp);
        labelStates.add(false);
        //添加点击事件，点击变成选中状态，选中状态下被点击则删除
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curIndex = labels.indexOf(temp);
                if (!labelStates.get(curIndex)) {
                    //显示 ×号删除
                    temp.setText(temp.getText() + " ×");
                    temp.setBackgroundResource(R.drawable.delete_label);
                    temp.setTextColor(Color.parseColor("#ffffff"));
                    //修改选中状态
                    labelStates.set(curIndex, true);
                } else {
                    delByTest(temp.getText().toString());
                    flowLayout.removeView(temp);
                    labels.remove(curIndex);
                    labelStates.remove(curIndex);
                    for (int i = 0; i < label_list.size(); i++) {
                        for (int j = 0; j < labels.size(); j++) {
                            if (label_list.get(i).equals(
                                    labels.get(j).getText())) {
                                tagAdapter.setSelectedList(i);
                            }
                        }
                    }
                    tagAdapter.notifyDataChanged();
                }
            }
        });
        flowLayout.addView(temp);
        //让输入框在最后一个位置上
        editText.bringToFront();
        //清空编辑框
        editText.setText("");
        editText.requestFocus();
        return true;

    }

    /**
     * 根据字符删除标签
     *
     * @param text
     */
    private void delByTest(String text) {
        for (int i = 0; i < all_label_List.size(); i++) {
            String a = all_label_List.get(i) + " ×";
            if (a.equals(text)) {
                set.remove(i);
            }
        }
        tagAdapter.setSelectedList(set);//重置选中的标签
    }

    /**
     * 创建一个正常状态的标签
     *
     * @param label
     * @return
     */
    private TextView getTag(String label) {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(12);
        textView.setBackgroundResource(R.drawable.select_label);
        textView.setTextColor(Color.parseColor("#00FF00"));
        textView.setText(label);
        textView.setLayoutParams(params);
        return textView;
    }


    /**
     * 标签恢复到正常状态
     */
    private void tagNormal() {
        //输入文字时取消已经选中的标签
        for (int i = 0; i < labelStates.size(); i++) {
            if (labelStates.get(i)) {
                TextView tmp = labels.get(i);
                tmp.setText(tmp.getText().toString().replace(" ×", ""));
                labelStates.set(i, false);
                tmp.setBackgroundResource(R.drawable.normal_label);
                tmp.setTextColor(Color.parseColor("#00FF00"));
            }
        }
    }

    @Override
    protected void initData() {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        DynamicLabel dynamicLabel = new DynamicLabel();
        dynamicLabel.setUserId(userId);
        netRequestBean.setDynamicLabel(dynamicLabel);
        if (mPresenter != null) {
            mPresenter.getLabelsList(netRequestBean);
        }
    }

    @Override
    protected LabelsPresenter<ILabelView> createPresenter() {
        return new LabelsPresenter<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_finish, R.id.layout_label_manager})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_finish://完成
                if (labels.size() != 0){
                    ArrayList<String> list = new ArrayList<>();
                    for (TextView tv:labels) {
                        list.add(tv.getText().toString());
                    }
                    createLabel(list);
                }else {
                    finish();
                    //Toast.makeText(this, "请至少选择一个标签", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layout_label_manager:
                break;
        }
    }

    private void createLabel(ArrayList<String> labels) {
        DeviceProperties dr = DrUtils.getInstance();
        SharedPreferences sp = SharedPreferencesUtils.getInstance();
        int userId = sp.getInt(Constans.USERID, 0);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        ArrayList<DynamicLabel> list = new ArrayList<>();
        for (String str:labels) {
            DynamicLabel dynamicLabel = new DynamicLabel();
            dynamicLabel.setLabeltext(str);
            for (DynamicLabel label:dynamicLabels) {
                if (label.getLabeltext().equals(str)){
                    dynamicLabel.setId(label.getId());
                }
            }
            list.add(dynamicLabel);
        }
        userInfo.setDynamicLabels(list);
        NetRequestBean netRequestBean = new NetRequestBean();
        netRequestBean.setDeviceProperties(dr);
        netRequestBean.setUserInfo(userInfo);
        if (mPresenter != null) {
            mPresenter.createLabels(netRequestBean);
        }
    }

    @Override
    public void showGetLabelsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                dealWithGetLabelsResult(result);
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }

    private void dealWithGetLabelsResult(Result result) {
        try {
            JSONObject jsonObject = new JSONObject(result.getData());
            dynamicLabels = new Gson().fromJson(jsonObject.optString("dynamicLabelList"), new TypeToken<List<DynamicLabel>>() {
            }.getType());
            if (dynamicLabels != null){
                if (dynamicLabels.size() != 0){
                    all_label_List.clear();
                    for (DynamicLabel dynamicLabel:dynamicLabels) {
                        all_label_List.add(dynamicLabel.getLabeltext());
                    }
                    tagAdapter.notifyDataChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createLabelsResult(Result result) {
        if (result != null) {
            if (result.getResultCode() == Constans.REQUEST_OK) {
                finish();
            }else {
                showFailed();
            }
        }else {
            showFailed();
        }
    }
}
