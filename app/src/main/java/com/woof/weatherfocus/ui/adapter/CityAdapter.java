package com.woof.weatherfocus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Woof on 2/25/2017.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private Context mContext;
    private ArrayList<String> mDataList;
    private OnRecyclerViewItemListerner mOnRecyclerViewItemListerner = null;

    public CityAdapter(Context context, ArrayList<String> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    /**
     * 填充城市选择布局 city_item
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(mContext).inflate((R.layout.city_item), parent, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, final int position) {
        // 数据列表和Holder进行绑定
        holder.bind(mDataList.get(position));
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecyclerViewItemListerner.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnRecyclerViewItemListerner(OnRecyclerViewItemListerner listerner){
        this.mOnRecyclerViewItemListerner = listerner;
    }

    public class CityViewHolder extends BaseViewHolder<String> {

        @BindView(R.id.item_city)
        TextView mCityItem;
        @BindView(R.id.cardView)
        CardView mCardView;
        public CityViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(String s) {
            mCityItem.setText(s);
        }
    }
}
