package com.leecode1988.accountingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leecode1988.accountingapp.R;
import com.leecode1988.accountingapp.bean.RecordBean;
import com.leecode1988.accountingapp.util.GlobalUtil;
import com.leecode1988.accountingapp.bean.CategoryResBean;

import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/3/4 10:33
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    private static final String TAG = "CategoryRecyclerAdapter";
    private LayoutInflater mInflater;
    public Context mContext;
    private LinkedList<CategoryResBean> cellList = GlobalUtil.getInstance().costRes;

    public String getSelected() {
        return selected;
    }

    private String selected = "";

    private OnCategoryClickListener onCategoryClickListener;

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public CategoryRecyclerAdapter(Context context, String category) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

        if (findFromCellList(category) == -1) {
            changedType(RecordBean.RecordType.RECORD_TYPE_INCOME);
        }
        Log.d(TAG, "-->" + category);
        selected = category;
    }

    /**
     * 查找category是否在默认的cellList中
     *
     * @param category
     * @return 返回-1则表示不在,需要切换list.
     */
    private int findFromCellList(String category) {
        for (int i = 0; i < cellList.size(); i++) {
            if (category.equals(cellList.get(i).title)) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_category, parent, false);

        CategoryViewHolder myViewHolder = new CategoryViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoryResBean res = cellList.get(position);

        holder.imageView.setImageResource(res.resWhite);
        holder.textView.setText(res.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = res.title;
                notifyDataSetChanged();

                if (onCategoryClickListener != null) {
                    onCategoryClickListener.onClick(res.title);
                }
            }
        });

        if (holder.textView.getText().toString().equals(selected)) {
            holder.background.setBackgroundResource(R.drawable.bg_edit_text);
        } else {
            holder.background.setBackgroundResource(R.color.colorPrimary);
        }

        Log.d(TAG, "-->" + getItemCount());

    }

    public void changedType(RecordBean.RecordType type) {
        if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
            cellList = GlobalUtil.getInstance().costRes;
        } else {

            cellList = GlobalUtil.getInstance().earnRes;
        }

        selected = cellList.get(0).title;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return cellList == null ? 0 : cellList.size();
    }


    public interface OnCategoryClickListener {
        void onClick(String category);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout background;
        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.cell_background);
            imageView = itemView.findViewById(R.id.imageView_category);
            textView = itemView.findViewById(R.id.textView_category);
        }
    }

}

