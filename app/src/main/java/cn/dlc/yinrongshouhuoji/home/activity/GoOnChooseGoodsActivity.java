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
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.adpter.ChooseGoodsListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.ChooseGoodsListBean;
import cn.dlc.yinrongshouhuoji.home.utlis.ChooseGoodsManager;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TextViewDialog;
import cn.dlc.yinrongshouhuoji.home.widget.dialog.TwoTextViewDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class GoOnChooseGoodsActivity extends BaseActivity {
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
    @BindView(R.id.tv_remainder_count)
    TextView mTvRemainderCount;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;

    private static final String EXTRA_INT = "extra_int";

    private int remainderCount;
    private ChooseGoodsListAdapter mChooseGoodsListAdapter;
    private List<ChooseGoodsListBean> mFakeList;

    public static Intent newIntent(Context mContext, int remainderCount) {
        Intent mIntent = new Intent(mContext, GoOnChooseGoodsActivity.class);
        mIntent.putExtra(EXTRA_INT, remainderCount);
        return mIntent;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_go_on_choose_goods;
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
        remainderCount = getIntent().getIntExtra(EXTRA_INT, 0);
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
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "番茄炒蛋",
//            18.00f, 0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "牛肉炒蛋",
//            18.00f, 0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "辣子鸡", 18.00f,
//            0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "扬州炒饭",
//            18.00f, 0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "牛鞭", 18.00f,
//            0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "苦瓜炒蛋",
//            18.00f, 0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "酸菜鱼", 18.00f,
//            0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "炸排骨", 18.00f,
//            0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "土豆丝", 18.00f,
//            0));
//        mFakeList.add(new ChooseGoodsListBean(
//            "http://img0.imgtn.bdimg.com/it/u=3023628788,1024846702&fm=200&gp=0.jpg", "拉面", 18.00f,
//            0));

        mTvRemainderCount.setText(
            Html.fromHtml(getString(R.string.fangrushuliang_, remainderCount)));
    }

    private void initRecyclerView() {
        mChooseGoodsListAdapter = new ChooseGoodsListAdapter(this, remainderCount);
        mRvGoods.setLayoutManager(
            new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvGoods.setAdapter(mChooseGoodsListAdapter);
        mChooseGoodsListAdapter.setNewData(mFakeList);
    }

    @OnClick({ R.id.img_exit, R.id.img_search, R.id.btn_confirm, R.id.btn_cancel })
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
            case R.id.btn_cancel:
                showSuccessDialog();
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

    private void confirm() {
        ChooseGoodsListBean mSelectedItem = mChooseGoodsListAdapter.getSelectedItem();
        if (mSelectedItem == null) {
            showOneToast(R.string.qingxuanzeshangpin);
            return;
        }
        if (mSelectedItem.getGoodsCount() == 0) {
            showOneToast(R.string.qingxuanzeshuliang);
            return;
        }
        ChooseGoodsManager.getInstance().addData(mSelectedItem);
        showOpenTipsDialog(mSelectedItem.getGoodsCount());
    }

    private void showOpenTipsDialog(final int goodsCount) {//仓门打开
        TextViewDialog mOpenTipsDialog = new TextViewDialog(this);
        mOpenTipsDialog.setTitle(R.string.tishi);
        mOpenTipsDialog.setContent(getString(R.string._cangmendakai, "101、102、103"));
        mOpenTipsDialog.setOnDismissListener(new TextViewDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                //PM说硬件检测是否还有放置格子
                if (remainderCount - goodsCount > 0) {
                    startActivity(GoOnChooseGoodsActivity.newIntent(GoOnChooseGoodsActivity.this,
                        remainderCount - goodsCount));
                    finish();
                } else if (remainderCount - goodsCount == 0) {
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
                Intent mIntent = new Intent(GoOnChooseGoodsActivity.this, SetTakeOutActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                finish();
            }

            @Override
            public void onClickCancel() {
                Intent mIntent = new Intent(GoOnChooseGoodsActivity.this, MainActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                finish();
            }
        });
        mSuccessDialog.show();
    }
}
