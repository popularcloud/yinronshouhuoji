package cn.dlc.yinrongshouhuoji.personal.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.MenuBean;
import cn.dlc.yinrongshouhuoji.personal.bean.AllBean;

/**
 * 页面:李旭康  on  2018/3/14.
 * 对接口:
 * 作用:
 */

public class AllGreensAdapter extends BaseRecyclerAdapter<AllBean> {
    
    private Context mContext;
    private RecyclerView mRecyclerview;
    private AllBean mBean;
    private  ClickListener mClickListener;
    private int mType;

    public AllGreensAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_allgreens;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder,  final int position) {
        mBean = getItem(position);
        if (mBean.getGreesType()==1){
            holder.setText(R.id.adapter_all_type_tv,"已派送");
        }else {
            holder.setText(R.id.adapter_all_type_tv,"未派送");
        }
        holder.setText(R.id.adapter_all_tv, Html.fromHtml(mContext.getString(R.string.gongduos,mBean.getList().size())));
        mRecyclerview = holder.getView(R.id.adapter_recyclerview);
        mRecyclerview.setNestedScrollingEnabled(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(position);
            }
        });
        
        initRecyclerView(position);
    }
    private void initRecyclerView(final int type) {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        AllItemAdapter adapter = new AllItemAdapter(mContext);
        mRecyclerview.setAdapter(adapter);
        ArrayList<MenuBean> list = mBean.getList();
        adapter.setNewData(list);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, CommonHolder holder, int position) {
               mClickListener.onClick(type);
            }
        });
    }
    
    public  void setClickListener(ClickListener listener){
        this.mClickListener=listener;
    }
    
    public  interface ClickListener{
        
        void onClick(int position);
    }
}
