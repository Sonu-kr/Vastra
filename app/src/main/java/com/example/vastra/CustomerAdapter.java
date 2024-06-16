package com.example.vastra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastra.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customerList = new ArrayList<>();

    public void setCustomers(List<Customer> customers) {
        this.customerList = customers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvCustomerName.setText(customer.getName());

        int totalItemCount = 0;
        double totalPrice = 0;

        for (Item item : customer.getItems().values()) {
            totalItemCount += item.getCount();
            totalPrice += item.getCount() * item.getPrice();
        }

        holder.tvTotalItemCount.setText(String.valueOf(totalItemCount));
        holder.tvTotalPrice.setText(String.valueOf(totalPrice));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName;
        TextView tvTotalItemCount;
        TextView tvTotalPrice;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvTotalItemCount = itemView.findViewById(R.id.tvTotalItemCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
