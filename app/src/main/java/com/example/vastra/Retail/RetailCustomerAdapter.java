package com.example.vastra.Retail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastra.Customer;
import com.example.vastra.Item;
import com.example.vastra.R;

import java.util.ArrayList;
import java.util.Map;

public class RetailCustomerAdapter extends RecyclerView.Adapter<RetailCustomerAdapter.ViewHolder> {

    private static ArrayList<Customer> customerList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public RetailCustomerAdapter(ArrayList<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RetailCustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    // onBindViewHolder method in RetailCustomerAdapter.java
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvCustomerName.setText(customer.getName());

        // Calculate total item count for the customer
        int totalItemCount = calculateTotalItemCount(customer);
        holder.tvTotalItemCount.setText("Sarees: " + totalItemCount); // Update text for total item count

        // Calculate total price for the customer
        double totalPrice = calculateTotalPrice(customer.getItems());
        holder.tvTotalPrice.setText("Total Price: Rs " + totalPrice); // Set text for total price
    }

    // Updated calculateTotalPrice method in RetailCustomerAdapter.java
    private double calculateTotalPrice(Map<String, Item> items) {
        double total = 0;
        if (items != null) {
            for (Item item : items.values()) {
                total += item.getPrice() * item.getCount();
            }
        }
        return total;
    }

    // Calculate total item count for the customer
    private int calculateTotalItemCount(Customer customer) {
        int totalItemCount = 0;
        if (customer.getItems() != null) {
            for (Item item : customer.getItems().values()) {
                totalItemCount += item.getCount();
            }
        }
        return totalItemCount;
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    // ViewHolder class in RetailCustomerAdapter.java
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName;
        TextView tvTotalItemCount;
        TextView tvTotalPrice; // Add this line

        public ViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvTotalItemCount = itemView.findViewById(R.id.tvTotalItemCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice); // Initialize tvTotalPrice

            // Set click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(customerList.get(position).getId());
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(String customerId);
    }
}
