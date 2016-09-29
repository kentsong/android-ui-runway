package com.android.ui.kent.demo.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ui.kent.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.android.ui.kent.demo.recyclerview.ItemAdapter.VIEW_TYPE.ITEM_TYPE_1;
import static com.android.ui.kent.demo.recyclerview.ItemAdapter.VIEW_TYPE.ITEM_TYPE_2;

/**
 * Created by Kent on 2016/9/27.
 */

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum VIEW_TYPE {
        ITEM_TYPE_1,
        ITEM_TYPE_2
    }

    private Context context;
    private List<String> list;
    private int count = 10;

    public ItemAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();

        for(int i=0;i<count;i++){
            list.add("項目("+i+")");
        }
    }

    public void addItem(){
        list.add("項目("+ list.size()+1 +")");
    }

    public void removeItem(){
        list.remove(list.size() -1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //依viewType決定使用item_layout
        View rootView;
        if(viewType == ITEM_TYPE_1.ordinal()){
            rootView =  LayoutInflater.from(context).inflate(R.layout.view_item_recycler_type_1, null, false);
            rootView.setLayoutParams(lp);
            return new ViewHolder1(rootView);
        }
        else{
            rootView = LayoutInflater.from(context).inflate(R.layout.view_item_recycler_type_2, null, false);
            rootView.setLayoutParams(lp);
            return new ViewHolder2(rootView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        //可以彈性Bind兩種以上viewHolder
        if(viewHolder instanceof ViewHolder1){
            ViewHolder1 holder = (ViewHolder1)viewHolder;
            holder.mainText.setText("特賣會( " + position + " )");
            holder.subText.setText("買5件送5件，快來買");
        } else{
            ViewHolder2 holder = (ViewHolder2)viewHolder;
            holder.mainText.setText("大拍賣( " + position + " )");
            holder.subText.setText("買3件送3件，快來買，快來買，快來買");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3 == 0 ? ITEM_TYPE_1.ordinal() : ITEM_TYPE_2.ordinal();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {

        @Bind(R.id.text_1)
        TextView mainText;
        @Bind(R.id.text_2)
        TextView subText;

        public ViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        @Bind(R.id.text_1)
        TextView mainText;
        @Bind(R.id.text_2)
        TextView subText;

        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
