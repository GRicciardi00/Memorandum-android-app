package com.example.memoapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;


public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    private final MemoList list;
    public String status = "active"; //used for control which memos display in main activity
    public MemoAdapter() {
        this.list = MemoList.getInstance();
    }

    // recyclerView element
    public class ViewHolder extends RecyclerView.ViewHolder {

        // strings that will be use inside intent to switch to activity_detail view
        public static final String MEMO_DESCRIPTION = "com.example.memoapp.MemoAdapter.ViewHolder.DESCRIPTION";
        public static final String MEMO_TITLE = "com.example.memoapp.MemoAdapter.ViewHolder.NAME";
        public static final String MEMO_DATE = "com.example.memoapp.MemoAdapter.ViewHolder.DATE";
        public static final String MEMO_HOUR = "com.example.memoapp.MemoAdapter.ViewHolder.HOUR";
        public static final String LOCATION = "com.example.memoapp.MemoAdapter.ViewHolder.LOCATION";
        private View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;

            // click on single cell opens detail activity
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    int position = getLayoutPosition();
                    // add additional data which will be displayed in intent destination activity
                    intent.putExtra(MEMO_DESCRIPTION, list.memoAtIndex(position).getDescription());
                    intent.putExtra(MEMO_TITLE, list.memoAtIndex(position).getTitle());
                    intent.putExtra(MEMO_DATE, list.memoAtIndex(position).getDay().toString());
                    intent.putExtra(MEMO_HOUR, list.memoAtIndex(position).getHour().toString());
                    intent.putExtra(LOCATION, list.memoAtIndex(position).getLocation());
                    // start new activity
                    view.getContext().startActivity(intent);
                }
            });

            // long click on a cell deletes it
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getLayoutPosition();
                    notifyItemRemoved(position);
                    Memo removed = MemoList.getInstance().memoAtIndex(position);
                    MemoList.getInstance().removeElement(position);
                    // display toast to give the possibility to undo changes
                    Snackbar undo = Snackbar.make(view, "\"" + removed.getTitle() + "\" removed", Snackbar.LENGTH_LONG);
                    undo.setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MemoList.getInstance().addElement(removed);
                            notifyItemInserted(position);
                        }
                    });
                    undo.show();
                    return false;
                }
            });
        }
        public void setText(String text, boolean expired, boolean completed) {
            TextView textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(text);
            // if memo is expired display it in red
            if (expired) {
                textView.setTextColor(0xFFFF0000);
            }
            //if memo is completed display it in green color
            if (completed) {
                textView.setTextColor(Color.rgb(0,177,2));
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }
    //create a new viewHolder for the current memo that have to be displayed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_cell, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // initialize a ViewHolder based on its position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean expired = false;
        Memo c = list.memoAtIndex(position);
        expired = Utils.isExpired(Utils.currentDate(), c.getDay());

        //ViewHolder settings for active memos
        if (status.equals("active") && c.getStatus() !=null ) {
            if(c.getStatus().equals("active") && expired==false)
                holder.setText("● " + list.memoAtIndex(position).getDay()  + " - " + list.memoAtIndex(position).getTitle() , expired, false);
            if(c.getStatus().equals("Completed") || expired == true){
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        //ViewHolder settings for completed memos
        if (status.equals("Completed") && c.getStatus() !=null) {
            if (c.getStatus().equals("Completed"))
                holder.setText("● " + list.memoAtIndex(position).getDay() + " - " + list.memoAtIndex(position).getTitle(), expired, true);
            if (c.getStatus().equals("active")) {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }
        //ViewHolder settings for expired memos
         if (status.equals("expired") && c.getStatus() != null){
            if(c.getStatus().equals("active"))
            {
                if(expired)
                    holder.setText("● " + list.memoAtIndex(position).getDay() + " - " + list.memoAtIndex(position).getTitle(), expired, false);
                else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }
            if (c.getStatus().equals("Completed")){
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //set MemoAdapter status
    public void setStatus (String var) {status = var;}


}
