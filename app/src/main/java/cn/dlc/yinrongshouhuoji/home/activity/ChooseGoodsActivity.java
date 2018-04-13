package cn.dlc.yinrongshouhuoji.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.adpter.ChooseGoodsListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.ChooseGoodsListBean;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.home.bean.setWaiMaiListBean;
import cn.dlc.yinrongshouhuoji.home.utlis.ChooseGoodsManager;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TextViewDialog;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TwoTextViewDialog;
import cn.dlc.yinrongshouhuoji.https.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class ChooseGoodsActivity extends BaseActivity {
    @BindView(R.id.img_exit)
    ImageView mImgExit;
    @BindView(R.id.edit_key_word)
    EditText mEditKeyWord;
    @BindView(R.id.img_search)
    ImageView mImgSearch;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    private int page = 1;
    private int pageSize = 10;
    private String api_name = "";


    private static final String EXTRA_INT = "extra_int";
    private static final String EXTRA_INT_CUP = "extra_int_cup";

    private int max;
    private int cupboard_id;
    private ChooseGoodsListAdapter mChooseGoodsListAdapter;
    private List<ChooseGoodsListBean> mFakeList;//需要维护一个原来的数据集合，搜索关键字为空时，显示原来的数据集合

    public static Intent newIntent(Context mContext, int max,int cupboard_id) {
        Intent mIntent = new Intent(mContext, ChooseGoodsActivity.class);
        mIntent.putExtra(EXTRA_INT, max);
        mIntent.putExtra(EXTRA_INT_CUP,cupboard_id);
        return mIntent;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_choose_goods;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveIntent();
        initSearch();
        initFakeData();
        initRecyclerView();
    }

    private void resolveIntent() {
        max = getIntent().getIntExtra(EXTRA_INT, 0);
        cupboard_id = getIntent().getIntExtra(EXTRA_INT_CUP,0);
    }

    private void initSearch() {
        mEditKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
    }

    private void initFakeData() {
        mFakeList = new ArrayList<>();
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "goodsListForPut");
        api_name = "goodsListForPut";
        dataMap.put("title", "");
        dataMap.put("token", Config.token);
        dataMap.put("cupboardid",cupboard_id+"");
        dataMap.put("put_log", max + "");
        dataMap.put("page", page + "");
        dataMap.put("psize", pageSize + "");
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    private void initRecyclerView() {
        mChooseGoodsListAdapter = new ChooseGoodsListAdapter(this, max);
        mRvGoods.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvGoods.setAdapter(mChooseGoodsListAdapter);
        mChooseGoodsListAdapter.setNewData(mFakeList);
    }

    @OnClick({R.id.img_exit, R.id.img_search, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_exit:
                finish();
                break;
            case R.id.img_search:
                search();
                break;
            case R.id.btn_confirm:
                confirm();
                break;
        }
    }

    private void search() {
        String keyWord = mEditKeyWord.getText().toString();
        if (TextUtils.isEmpty(keyWord)) {
            mChooseGoodsListAdapter.setNewData(mFakeList);
        } else {
            List<ChooseGoodsListBean> mList = new ArrayList<>();
            for (ChooseGoodsListBean mChooseGoodsListBean : mFakeList) {
                if (mChooseGoodsListBean.getGoodsName().contains(keyWord)) {
                    mList.add(mChooseGoodsListBean);
                }
            }
            mChooseGoodsListAdapter.setNewData(mList);
        }
    }

    ChooseGoodsListBean mSelectedItem;
    private void confirm() {
        mSelectedItem = mChooseGoodsListAdapter.getSelectedItem();
        if (mSelectedItem == null) {
            showOneToast(R.string.qingxuanzeshangpin);
            return;
        }
        if (mSelectedItem.getGoodsCount() == 0) {
            showOneToast(R.string.qingxuanzeshuliang);
            return;
        }
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "submitGoodsForPut");
        dataMap.put("goods_id", mSelectedItem.getGoods_id()+"");
        dataMap.put("token", Config.token);
        dataMap.put("put_log", max + "");
        dataMap.put("num", mSelectedItem.getGoodsCount() + "");
        api_name = "submitGoodsForPut";
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    private void showOpenTipsDialog(final int goodsCount,String box_number) {//仓门打开
        TextViewDialog mOpenTipsDialog = new TextViewDialog(this);
        mOpenTipsDialog.setTitle(R.string.tishi);
        mOpenTipsDialog.setContent(getString(R.string._cangmendakai, box_number));
        mOpenTipsDialog.setOnDismissListener(new TextViewDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                //PM说硬件检测是否还有放置格子
                if (max - goodsCount > 0) {
                    startActivity(GoOnChooseGoodsActivity.newIntent(ChooseGoodsActivity.this,
                            max - goodsCount));
                    finish();
                } else if (max - goodsCount == 0) {
                    showSuccessDialog();
                }
            }
        });
        mOpenTipsDialog.show();
    }

    private void showSuccessDialog() {
        TwoTextViewDialog mSuccessDialog = new TwoTextViewDialog(this);
        mSuccessDialog.setTitle(R.string.tishi);
        StringBuilder sb = new StringBuilder();
        sb.append(ChooseGoodsManager.getInstance().getListStr(this));
        sb.append(Html.fromHtml(getString(R.string.keyongcangkou_ge, 15)));
        mSuccessDialog.setContent(sb.toString());
        mSuccessDialog.setBtnConfirm(R.string.jixufangzhi);
        mSuccessDialog.setBtnCancel(R.string.fanhuishouye);
        mSuccessDialog.setOnClickButtonListener(new TwoTextViewDialog.OnClickButtonListener() {
            @Override
            public void onClickConfirm() {
                Intent mIntent = new Intent(ChooseGoodsActivity.this, SetTakeOutActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                finish();
            }

            @Override
            public void onClickCancel() {
                Intent mIntent = new Intent(ChooseGoodsActivity.this, MainActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                finish();
            }
        });
        mSuccessDialog.show();
    }

    @Override
    public void onNetJSONArray(JSONArray jsonArray, String trxcode) {
        super.onNetJSONArray(jsonArray, trxcode);

    }

    @Override
    public void onNetJSONObject(JSONObject jsonObject, String trxcode) {
        super.onNetJSONObject(jsonObject, trxcode);
        setWaiMaiListBean listBean = new Gson().fromJson(jsonObject.toString(), new TypeToken<setWaiMaiListBean>() {
        }.getType());
        if (!api_name.equals("submitGoodsForPut")){
            if (listBean != null) {
                for (int i = 0; i < listBean.getGoods().size(); i++) {
                    mFakeList.add(new ChooseGoodsListBean(
                            listBean.getGoods().get(i).getCover_url(),
                            listBean.getGoods().get(i).getTitle(),
                            Float.parseFloat(listBean.getGoods().get(i).getPrice()),
                            0,
                            listBean.getGoods().get(i).getGoods_id()));
                }
                initRecyclerView();
            }
        }else {
            ChooseGoodsManager.getInstance().addData(mSelectedItem);
            showOpenTipsDialog(mSelectedItem.getGoodsCount(),listBean.getBox_number());
        }

    }
}
