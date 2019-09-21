package com.example.syouk.Agricowture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<List_item> {

    private int mResource;
    private List<List_item> mItems;
    private LayoutInflater mInflater;

    ListAdapter(Context context, int resource, List<List_item> items){
        super(context, resource, items);

        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view;
        if(convertView != null){
            view = convertView;
        } else {
            view = mInflater.inflate(mResource, null);
        }

        //ListViewに表示する要素の取得
        List_item item = mItems.get(position);

        //サムネイルの設定
        ImageView thumbnail = view.findViewById(R.id.thumbnail);
        thumbnail.setImageBitmap(item.getThumbnail());

        //タイトル設定
        TextView title = view.findViewById(R.id.title);
        title.setText(item.getTitle());

        return view;
    }
}
