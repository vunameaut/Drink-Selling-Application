package com.pro.shopfee.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.shopfee.R;
import com.pro.shopfee.model.Topping;
import com.pro.shopfee.utils.Constant;

import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {

    private final List<Topping> listTopping;
    private final IClickToppingListener iClickToppingListener;

    public interface IClickToppingListener {
        void onClickToppingItem(Topping topping);
    }

    public ToppingAdapter(List<Topping> list, IClickToppingListener listener) {
        this.listTopping = list;
        this.iClickToppingListener = listener;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topping, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = listTopping.get(position);
        if (topping == null) return;

        holder.tvName.setText(topping.getName());
        String strPrice = "+" + topping.getPrice() + Constant.CURRENCY;
        holder.tvPrice.setText(strPrice);
        if (topping.isSelected()) {
            holder.imgSelected.setBackgroundResource(R.drawable.ic_item_selected);
        } else {
            holder.imgSelected.setBackgroundResource(R.drawable.ic_item_unselect);
        }

        holder.layoutItem.setOnClickListener(v -> iClickToppingListener.onClickToppingItem(topping));
    }

    @Override
    public int getItemCount() {
        if (listTopping != null) {
            return listTopping.size();
        }
        return 0;
    }

    public static class ToppingViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvPrice;
        private final ImageView imgSelected;
        private final RelativeLayout layoutItem;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imgSelected = itemView.findViewById(R.id.img_selected);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
}
