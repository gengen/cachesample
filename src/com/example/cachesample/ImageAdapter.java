package com.example.cachesample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * {@link GridView} �� {@link ArrayAdapter}.
 */
public class ImageAdapter extends ArrayAdapter<ImageItem> {

    /**
     * �R���X�g���N�^.
     * @param context {@link Context}
     */
    public ImageAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageItem item = getItem(position);

        ImageView view;
        if (convertView == null) {
            view = new ImageView(getContext());
            view.setLayoutParams(new AbsListView.LayoutParams(100, 100));
            view.setTag(item);
        } else {
            view = (ImageView) convertView;
        }
        
        view.setImageBitmap(item.bitmap);

        return view;
    }

}
