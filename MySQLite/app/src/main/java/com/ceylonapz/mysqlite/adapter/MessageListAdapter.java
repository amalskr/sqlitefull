package com.ceylonapz.mysqlite.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ceylonapz.mysqlite.R;
import com.ceylonapz.mysqlite.db.DatabaseHandler;
import com.ceylonapz.mysqlite.model.Message;

import java.util.List;

/**
 * Created by ceylonApz on 2018-03-14
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {


    private final Activity contextActivity;
    private final List<Message> msgListData;
    private final TextView nodata_tv;
    private DatabaseHandler db;

    public MessageListAdapter(Activity activity, List<Message> msgListArry, TextView nodata_tv) {
        this.contextActivity = activity;
        this.msgListData = msgListArry;
        this.nodata_tv = nodata_tv;
        db = new DatabaseHandler(contextActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Message msg = msgListData.get(position);

        String title = msg.getMsgTitle();
        String msgDate = msg.getMsgBody();
        int isRead = msg.getMsgIsRead();

        holder.title_tv.setText(title);
        holder.date_iv.setText(msgDate);

        System.out.println("View "+ msg.getMsgTitle()+" "+msg.getMsgBody());

        //read un read
        if (isRead == 1) {//read
            holder.title_tv.setTypeface(null, Typeface.NORMAL);
        } else {//unread
            holder.title_tv.setTypeface(null, Typeface.BOLD);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message selectedMsg = msgListData.get(position);

                //update read status
                if (selectedMsg.getMsgIsRead() == 0) {
                    db.updateReadStatus(selectedMsg.getMsgId());
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("SELECTED_MESSAGE", selectedMsg);

                /*Intent nextIntent = new Intent(contextActivity, activity.getClass());
                nextIntent.putExtras(bundle);
                contextActivity.startActivity(nextIntent);
                contextActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);*/
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Message selectedMsg = msgListData.get(position);

                new AlertDialog.Builder(contextActivity)
                        .setTitle("Do you want to delete")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                db.deleteMessage(selectedMsg.getMsgId());

                                msgListData.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, msgListData.size());

                                if (msgListData.size() == 0) {
                                    nodata_tv.setVisibility(View.VISIBLE);
                                    nodata_tv.setText("No messages");
                                } else {
                                    nodata_tv.setVisibility(View.GONE);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                return true;
            }
        });

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return msgListData.size();
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(contextActivity.getApplicationContext(), android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
        //lastPosition = position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv;
        TextView date_iv;

        ViewHolder(View view) {
            super(view);

            title_tv = (TextView) view.findViewById(R.id.tv_msg_title);
            date_iv = (TextView) view.findViewById(R.id.tv_msg_date);
        }
    }

}
