package com.adam.annuairechiali.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adam.annuairechiali.Activity.ContactDetailActivity;
import com.adam.annuairechiali.Manager.API_Manager;
import com.adam.annuairechiali.Manager.RealmManager;
import com.adam.annuairechiali.Model.Constant;
import com.adam.annuairechiali.Model.Contact;
import com.adam.annuairechiali.Model.ImageV;
import com.adam.annuairechiali.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.adam.annuairechiali.Manager.PictureDecodeManager.decodeSampleBitmap;

public class ContactCAdapter extends RecyclerView.Adapter<ContactCAdapter.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {
    private Context mContext ;
    private List<Contact> mData ;
    private Activity activity;
    private ArrayList<Contact> saveData ;
    private HashMap<String, ViewHolder> holderHashMap;
    private Handler handler;

    public ContactCAdapter(Context mContext, List<Contact> mData, Activity activity) throws UnsupportedEncodingException {
        handler = new HandlerContact();
        holderHashMap = new HashMap<>();
        this.mContext = mContext;
        this.activity = activity;
        this.mData = mData;
        this.saveData = new ArrayList<>(mData);
        ArrayList<String> ids = new ArrayList<>();
        for( Contact c : mData){
            String pic =  c.getPictureC();
            if (pic == null) pic = "null";
            if(pic .equals("none")){
              //  holder.imageView.setImageResource(R.drawable.user);
               // Log.d("NONE", "onBindViewHolder: " + mData.get(position).getName() );
            }
            else {


                if (!pic.equals("null")) {
                    //Bitmap bitmap = decodeSampleBitmap(Base64.decode(pic, Base64.DEFAULT), 60, 60);
                  //  holder.imageView.setImageBitmap(bitmap);
                } else {
                    ids.add(c.getId());
                }
            }
        }
       // API_Manager.getPicsByIds(ids, mContext, handler, Constant.CONTACT);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0){
            return R.layout.list_item2;
        }
        else
        return R.layout.list_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holderHashMap.put( holder.toString().substring(11, 18), holder);
        holder.name.setText(mData.get(position).getName());
        String job = mData.get(position).getDescription();
        if(job.equals("null"))
            job ="";
        holder.job.setText(job);
        holder.company.setVisibility(View.VISIBLE);
        holder.company.setText(mData.get(position).getCompany());
        /*
        if(mData.get(position).isBoss()) {
            holder.star.setImageResource(R.drawable.star3);
            holder.star.setVisibility(View.VISIBLE);
           // holder.name.setTextColor(mContext.getResources().getColor(R.color.white));
           // holder.job.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {

            holder.star.setVisibility(View.GONE);
          //  holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
            //holder.name.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            //holder.job.setTextColor(mContext.getResources().getColor(R.color.gray3));
        }*/
        holder.id = mData.get(position).getId();

        Log.d("HOLDER", "onBindViewHolder: " + holder.toString().substring(11, 18) + holder.toString());
        //holder.imageView.setImageResource(R.drawable.user);
        // byte[] b = s.getBytes();
       // Log.d("PIC", "onBindViewHolder: " + mData.get(position).getName() + "     LL:" + );

        String pic =  mData.get(position).getPictureC();

        if (pic == null) pic = "null";
        if(pic .equals("none")){
            holder.imageView.setImageResource(R.drawable.user);
            Log.d("NONE", "onBindViewHolder: " + mData.get(position).getName());
        }
        else {


            if (!pic.equals("null")) {
                Message m = new Message();
                m.what = Constant.IMAGE;
                m.obj = new ImageV(holder.imageView, pic);
                handler.sendMessage(m);
                //holder.imageView.setImageBitmap(decodeSampleBitmap(Base64.decode(pic, Base64.DEFAULT), 60, 60));
            } else {
                Log.d("DKHALE", "onBindViewHolder: " + pic);
                holder.imageView.setImageResource(R.drawable.user);
                holderHashMap.put( holder.toString().substring(11, 18), holder);
                try {
                    API_Manager.getPicById(mData.get(position).getId(), mContext, holder.imageView, (mData.get(position)), holder.toString().substring(11, 18));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                //intent.putExtra("contact", mData.get(position));
                intent.putExtra("id", mData.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //mContext.startActivity(intent);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
            }
        });
        //holder.job.setImageResource(mData.get(position).getImage());



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return mData.get(position).getName().substring(0,1);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, job, company;
        public ImageView imageView;
        public CardView cardView;
        public String id;
        public ImageView star;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
            company = itemView.findViewById(R.id.company);
            job = itemView.findViewById(R.id.job);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardview2);
            star = itemView.findViewById(R.id.star);
        }
    }
    public void filter(String charText) {

        charText = charText.toLowerCase();
        mData = null;
        mData = new ArrayList<>();
        if (charText.length() == 0) {
            mData = new ArrayList<>(saveData);
        } else {
            for (Contact contact : saveData) {
                if (contact.getName().toLowerCase()
                        .contains(charText)) {
                    mData.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class HandlerContact extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.CONTACT:
                    for(ViewHolder viewHolder: holderHashMap.values()) {

                        String pic = RealmManager.getContactbyId(viewHolder.id).getPictureC();
                        if (pic == null) pic = "null";
                        if(pic .equals("none")){
                            viewHolder.imageView.setImageResource(R.drawable.user);
                        //    Log.d("NONE", "onBindViewHolder: " + mData.get(position).getName() );
                        }
                        else {


                            if (!pic.equals("null")) {
                                Bitmap bitmap = decodeSampleBitmap(Base64.decode(pic, Base64.DEFAULT), 60, 60);
                                viewHolder.imageView.setImageBitmap(bitmap);
                            } else {

                /*try {
                    API_Manager.getPicById(mData.get(position).getId(), mContext, holder.imageView, (mData.get(position)), holder.toString().substring(11, 18));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                            }
                        }

                    }
                    break;


                case Constant.IMAGE:
                    ImageV i = (ImageV) msg.obj;
                    i.getImageView().setImageBitmap(decodeSampleBitmap(Base64.decode(i.getPic(), Base64.DEFAULT), 30, 30));
                    break;
            }
        }}

}