package cn.dlc.yinrongshouhuoji.home.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.commonlibrary.ui.widget.EmptyView;
import cn.dlc.commonlibrary.utils.ToastUtil;
import cn.dlc.commonlibrary.utils.rv_tool.EmptyRecyclerView;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.base.activity.BaseActivity;
import cn.dlc.yinrongshouhuoji.home.adpter.AddressListAdapter;
import cn.dlc.yinrongshouhuoji.home.bean.AddressListBean;
import cn.dlc.yinrongshouhuoji.home.bean.SaleMachineListBean;
import cn.dlc.yinrongshouhuoji.https.Config;
import cn.dlc.yinrongshouhuoji.util.SPUtils;

/**
 * Created by liuwenzhuo on 2018/3/14.
 */

public class SetAddressActivity extends BaseActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener,
        Inputtips.InputtipsListener,
        TextWatcher, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.img_exit)
    ImageView mImgExit;
    @BindView(R.id.edit_key_word)
    EditText mEditKeyWord;
    @BindView(R.id.img_search)
    ImageView mImgSearch;
    @BindView(R.id.tv_current_address)
    TextView mTvCurrentAddress;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    @BindView(R.id.rv_address)
    RecyclerView mRvAddress;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout mRefreshLayout;

    private int page;
    private AddressListAdapter mAddressListAdapter;
    private Map<Integer, List<AddressListBean>> mFakeMap;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private final int REFRESH_LOCALTION = 1;//获取到定位
    private final int LOCALTION_FAILT = 2;//获取定位失败
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private AMapLocation mapLocation;
    private SaleMachineListBean.DataBean mDeviceListBean;
    private AMapLocation aMapLocation;
    private boolean LocalRequest = false;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_set_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeviceListBean = (SaleMachineListBean.DataBean) getIntent().getSerializableExtra("data");
        initRecyclerView();
        getAddress();
        showWaitDialog("正在定位..");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            ToastUtil.show(this, aMapLocation.getAddress());
            if (!"".equals(aMapLocation.getAddress())) {
                mapLocation = aMapLocation;
                locationClient.stopLocation();
                Message message = Message.obtain();
                message.what = REFRESH_LOCALTION;
                message.obj = aMapLocation;
                handler.sendMessage(message);
            } else {
                handler.sendEmptyMessage(LOCALTION_FAILT);
            }
        }
    }


    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        mFakeMap = new HashMap<>();
        List<AddressListBean> addlist = new ArrayList<>();
        for (int index = 0; index < list.size(); index++) {
            String title = list.get(i).getName();
            String snippet = list.get(i).getAddress();
            AddressListBean bean = new AddressListBean(title, snippet);
            addlist.add(bean);
            mFakeMap.put(page, addlist);
        }
        if (page == 1) {
            List<AddressListBean> mList = mFakeMap.get(page);
            if (mList != null && mList.size() != 0) {
                page++;
                mAddressListAdapter.setNewData(mList);
                mRefreshLayout.finishRefreshing();
            }
        } else {
            List<AddressListBean> mList = mFakeMap.get(page);
            if (mList != null && mList.size() != 0) {
                page++;
                mAddressListAdapter.appendData(mList);
            } else {
                showOneToast(R.string.meiyougengduoshuju);
            }
            mRefreshLayout.finishLoadmore();
        }
        mAddressListAdapter.notifyDataSetChanged();
//        showPopWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_search:
                page = 1;
                mFakeMap.clear();
                InputtipsQuery inputquery = new InputtipsQuery(mEditKeyWord.toString(), mapLocation.getCity());
                inputquery.setCityLimit(true);//限制在当前城市
                Inputtips inputTips = new Inputtips(SetAddressActivity.this, inputquery);
                inputTips.setInputtipsListener(this);
                inputTips.requestInputtipsAsyn();
                break;
            case R.id.tv_current_address:
                setAddressRequest();
                break;
        }
    }
    //设置地址
    private void  setAddressRequest(){
        if (LocalRequest){
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("api_name", "setCupboardAddress");
            dataMap.put("token", Config.token);
            dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
            dataMap.put("lat", aMapLocation.getLatitude()+ "");
            dataMap.put("lng", aMapLocation.getLongitude() + "");
            dataMap.put("address", aMapLocation.getAddress() + "");
            doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
        }else {
            ToastUtil.show(this,"定位失败！无法设置地址！");
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        System.out.print(s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        InputtipsQuery inputquery = new InputtipsQuery(s.toString(), mapLocation.getCity());
        inputquery.setCityLimit(true);//限制在当前城市
        Inputtips inputTips = new Inputtips(SetAddressActivity.this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {
        System.out.print(s.toString());
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int ip) {
        mFakeMap = new HashMap<>();
        List<AddressListBean> list = new ArrayList<>();
        for (int i = 0; i < poiResult.getPois().size(); i++) {
            String title = poiResult.getPois().get(i).getTitle();
            String snippet = poiResult.getPois().get(i).getSnippet();
            AddressListBean bean = new AddressListBean(title, snippet);
            list.add(bean);
            mFakeMap.put(page, list);
        }
        if (page == 1) {
            List<AddressListBean> mList = mFakeMap.get(page);
            if (mList != null && mList.size() != 0) {
                page++;
                mAddressListAdapter.setNewData(mList);
                mRefreshLayout.finishRefreshing();
            }
        } else {
            List<AddressListBean> mList = mFakeMap.get(page);
            if (mList != null && mList.size() != 0) {
                page++;
                mAddressListAdapter.appendData(mList);
            } else {
                showOneToast(R.string.meiyougengduoshuju);
            }
            mRefreshLayout.finishLoadmore();
        }
        mRefreshLayout.finishRefreshing();
        Set<Integer> set=mFakeMap.keySet();
        for(Integer key:set){
            List<AddressListBean> value=mFakeMap.get(key);
            listBeans.addAll(value);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        ToastUtil.show(this, poiItem.getAdName());
    }


    int ii = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_LOCALTION:
                    LocalRequest = true;
                    aMapLocation = (AMapLocation) msg.obj;
                    getAroundAddr(aMapLocation, "");
                    mTvCurrentAddress.setText(aMapLocation.getAddress());
                    dissMissWaitDialog();
                    break;
                case LOCALTION_FAILT:
                    LocalRequest = false;
                    ii++;
                    if (ii >= 12) {//默认定位次数为12次
                        locationClient.startLocation();
                    }
                    mTvCurrentAddress.setText("获取定位失败！");
                    break;
            }
        }
    };


    private void getAddress() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();

    }

    private void getAroundAddr(AMapLocation aMapLocation, String keyWord) {
        query = new PoiSearch.Query(keyWord, "", aMapLocation.getCityCode());
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码
        poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(aMapLocation.getLatitude(),
                aMapLocation.getLongitude()), 1000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }


    private void initRecyclerView() {
        mImgSearch.setOnClickListener(this);
        mTvCurrentAddress.setOnClickListener(this);
        mAddressListAdapter = new AddressListAdapter();
        mRvAddress.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRvAddress.setAdapter(mAddressListAdapter);
        EmptyRecyclerView.bind(mRvAddress, mEmptyView);
        initRefresh();
        mEditKeyWord.addTextChangedListener(this);
        mAddressListAdapter.setOnItemClickListener(this);
    }

    List<AddressListBean> listBeans  = new ArrayList<>();
    @Override
    public void onItemClick(ViewGroup parent, BaseRecyclerAdapter.CommonHolder holder, int position) {
        String addr = listBeans.get(position).getAddressDetail();
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("api_name", "setCupboardAddress");
        dataMap.put("token", Config.token);
        dataMap.put("cupboard_id", mDeviceListBean.getCupboard_id() + "");
        dataMap.put("lat", aMapLocation.getLatitude()+ "");
        dataMap.put("lng", aMapLocation.getLongitude() + "");
        dataMap.put("address", addr+ "");
        doApiPost(Config.INTERFACE_MAIN_LIST, dataMap);
    }

    private void initRefresh() {
        ProgressLayout mProgressLayout = new ProgressLayout(getActivity());
        mProgressLayout.setColorSchemeResources(R.color.color_ff9557);
        mRefreshLayout.setHeaderView(mProgressLayout);
        mRefreshLayout.setFloatRefresh(true);
        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page = 1;
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                query.setPageNum(page);
                poiSearch.searchPOIAsyn();
            }
        });
        mRefreshLayout.startRefresh();
    }


    @OnClick({R.id.img_exit, R.id.img_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_exit:
                finish();
                break;
            case R.id.img_search:
                search();
                break;
        }
    }

    private void search() {
        //具体地图搜索不知道是不是用第三方搜索还是本地搜索，本地搜索参考ChooseGoodsActivity的initSearch
    }

    /**
     * 如果AMapLocationClient是在当前Activity实例化的，
     * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    private PopupWindow popupWindow = null;

    private void showPopWindow(ArrayList<String> strings) {
        // 加载popupWindow的布局文件
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(infServie);
        View contentView = layoutInflater.inflate(R.layout.dropdownlist_popupwindow, null, false);
        ListView listView = (ListView) contentView.findViewById(R.id.listView);

        listView.setAdapter(new XCDropDownListAdapter(SetAddressActivity.this, strings));
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(mEditKeyWord);
    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
        popupWindow = null;
    }


    /**
     * 数据适配器
     *
     * @author caizhiming
     */
    class XCDropDownListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<String> mData;
        LayoutInflater inflater;

        public XCDropDownListAdapter(Context ctx, ArrayList<String> data) {
            mContext = ctx;
            mData = data;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            // 自定义视图
            ListItemView listItemView = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.dropdown_list_item, null);
                listItemView = new ListItemView();
                listItemView.tv = (TextView) convertView
                        .findViewById(R.id.tv);
                listItemView.layout = (LinearLayout) convertView.findViewById(R.id.layout_container);
                convertView.setTag(listItemView);
            } else {
                listItemView = (ListItemView) convertView.getTag();
            }
            listItemView.tv.setText(mData.get(position).toString());
            final String text = mData.get(position).toString();
            listItemView.layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mEditKeyWord.setText(text);
                    closePopWindow();
                }
            });
            return convertView;
        }

    }

    private static class ListItemView {
        TextView tv;
        LinearLayout layout;
    }
}
