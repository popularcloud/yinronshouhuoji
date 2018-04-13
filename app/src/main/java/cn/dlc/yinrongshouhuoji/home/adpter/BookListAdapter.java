package cn.dlc.yinrongshouhuoji.home.adpter;

import android.content.Context;
import android.widget.TextView;
import cn.dlc.commonlibrary.ui.adapter.BaseRecyclerAdapter;
import cn.dlc.yinrongshouhuoji.R;
import cn.dlc.yinrongshouhuoji.home.bean.BookListBean;
import java.text.SimpleDateFormat;

/**
 * Created by liuwenzhuo on 2018/3/13.
 */

public class BookListAdapter extends BaseRecyclerAdapter<BookListBean> {

    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;

    public BookListAdapter(Context mContext) {
        this.mContext = mContext;
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_book_list;
    }

    @Override
    public void onBindViewHolder(CommonHolder holder, int position) {
        BookListBean mBookListBean = getItem(position);
        TextView mTvGoods = holder.getView(R.id.tv_goods);
        TextView mTvDate = holder.getView(R.id.tv_date);
        mTvGoods.setText(mContext.getString(R.string._fen, mBookListBean.getGoodsName(),
            mBookListBean.getGoodsCount()));
        mTvDate.setText(mSimpleDateFormat.format(mBookListBean.getDate() * 1000L));
    }
}
