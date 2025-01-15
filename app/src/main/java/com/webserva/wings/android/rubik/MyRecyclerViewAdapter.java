package com.webserva.wings.android.rubik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<MyDataModel> dataList;
    private Context context;

    public MyRecyclerViewAdapter(Context context, List<MyDataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyDataModel data = dataList.get(position);
        holder.imageView.setImageResource(data.getImageId());
        holder.correctCount.setText(String.valueOf(data.getCorrectCount()));
        holder.incorrectCount.setText(String.valueOf(data.getIncorrectCount()));
        holder.successRate.setText(String.format("%.2f%%", data.getSuccessRate()));
        holder.meanTime.setText(String.valueOf(data.getMeanTime()));
        holder.solutionView.setText(String.valueOf(data.getSolution()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // この部分をMyRecyclerViewAdapterクラスの内部に移動
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView correctCount;
        TextView incorrectCount;
        TextView successRate;
        TextView meanTime;
        TextView solutionView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            correctCount = itemView.findViewById(R.id.correctCount);
            incorrectCount = itemView.findViewById(R.id.incorrectCount);
            successRate = itemView.findViewById(R.id.successRate);
            solutionView = itemView.findViewById(R.id.solutionView);
            meanTime = itemView.findViewById(R.id.meanTime);
        }
    }
}
