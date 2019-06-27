package com.doctor.a247.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.model.Problem;

import java.util.ArrayList;

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Problem> problems;

    public ProblemAdapter(Context context, ArrayList<Problem> problems) {
        this.context = context;
        this.problems = problems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.problem_raw, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            viewHolder.serialTV.setText(String.valueOf(i+1) + " : ");
            viewHolder.problemTV.setText(problems.get(i).getProblemText());

        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure delete this");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        A247.problems(A247.getUserId()).child(problems.get(i).getId()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return problems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serialTV, problemTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serialTV = itemView.findViewById(R.id.serialTV);
            problemTV = itemView.findViewById(R.id.problemTV);
        }
    }
}
