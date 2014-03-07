package com.example.cachesample;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;

/**
 * {@link Bitmap} ��񓯊��œǂݍ��� {@link AsyncTaskLoader}.
 */
public class ImageLoader extends AsyncTaskLoader<Bitmap> {

    /** �Ώۂ̃A�C�e��. */
    public ImageItem item;
    Context mContext;
    List<File> imageFileList;

    /**
     * �R���X�g���N�^.
     * @param context {@link Context}
     * @param item {@link ImageItem}
     */
    public ImageLoader(Context context, ImageItem item) {
        super(context);
        this.mContext = context;
        this.item = item;
        
        init();
    }
    
    private void init(){    	
		try {
			imageFileList = FileDataUtil.getApplicationBitmapFileList(mContext);
		}
		catch (IOException e) {
		}
    }

    @Override
    public Bitmap loadInBackground() {
        //return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.item);
    	return getBitmap(item.id);
    }
    
	private Bitmap getBitmap(int id) {
		File item = imageFileList.get(id);
        Bitmap bitmap = null;
		try {
			bitmap = FileDataUtil.loadBitmap(item.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}
}
