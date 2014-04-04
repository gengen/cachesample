package com.example.cachesample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * {@link GridView} の {@link ArrayAdapter}.
 */
public class ImageAdapter extends ArrayAdapter<ImageItem> {
    private LayoutInflater mInflater;

    /**
     * コンストラクタ.
     * @param context {@link Context}
     */
    public ImageAdapter(Context context) {
        super(context, 0);

        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public ImageAdapter(Context context, int layout){
    	super(context, layout);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageItem item = getItem(position);
        
        //ここを有効にすると画像が表示されずに欠けることがあるためコメントアウトする
        //if (convertView == null) {
        //convertView = mInflater.inflate(R.layout.grid_item, null);
        //convertView.setLayoutParams(new AbsListView.LayoutParams(100, 100));
        //ImageView view = (ImageView)convertView.findViewById(R.id.imageItem);
        //} else {
        //view = (ImageView) convertView;
        //}
        
        //view = new ImageView(getContext());

        ImageView view = new ImageView(getContext());
        view.setLayoutParams(new AbsListView.LayoutParams(100, 100));
        view.setImageBitmap(item.bitmap);
        view.setTag(item);

        return view;
        //return convertView;
    }

}
