package com.example.cachesample;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * GridView を表示する {@link Activity}.
 */
public class MainActivity extends FragmentActivity {

    /** ログ出力用のタグ. */
    private static final String TAG = MainActivity.class.getSimpleName();

    /** メモリキャッシュクラス. */
    private LruCache<String, Bitmap> mLruCache;
    /** {@link GridView}. */
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGridView = new GridView(this);
        mGridView.setNumColumns(2);
        setContentView(mGridView);

        // LruCache のインスタンス化
        int maxSize = 5 * 1024 * 1024;
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        // Adapter の作成とアイテムの追加
		List<File> imageFileList = null;
		try {
			imageFileList = FileDataUtil.getApplicationBitmapFileList(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageAdapter adapter = new ImageAdapter(this);
        mGridView.setAdapter(adapter);
        //TODO NullPointer
        Log.d("cachesample", "size = " + imageFileList.size());
        for (int i = 0; i < imageFileList.size(); i++) {
            ImageItem item = new ImageItem();
            //item.key = "item" + String.valueOf(i);
            item.id = i;
            adapter.add(item);
        }
        
        /*
        MyAdapter mapAdapter = new MyAdapter(this);
		mGridView.setAdapter(mapAdapter);

		// アプリで保存した画像を画像リストアダプターにロードする
		loadMapImage(mapAdapter);

		// 画像ロードによりデータが変更されたことを通知する
		//  ※これをしないとギャラリーが表示されない
		mapAdapter.notifyDataSetChanged();
		*/
		
        // onScrollListener の実装
        mGridView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    // スクロールが止まったときに読み込む
                    loadBitmap();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        loadBitmap();
    }

    /**
     * 画像を読み込む.
     */
    private void loadBitmap() {
        // 現在の表示されているアイテムのみリクエストする
        ImageAdapter adapter = (ImageAdapter) mGridView.getAdapter();
        int first = mGridView.getFirstVisiblePosition();
        int count = mGridView.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageItem item = adapter.getItem(i + first);
            // キャッシュの存在確認
            Bitmap bitmap = mLruCache.get("" + item.id);
            if (bitmap != null) {
                // キャッシュに存在
                Log.i(TAG, "キャッシュあり=" + item.id);
                setBitmap(item);
                mGridView.invalidateViews();
            } else {
                // キャッシュになし
                Log.i(TAG, "キャッシュなし=" + item.id);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                getSupportLoaderManager().initLoader(i, bundle, callbacks);
            }
        }
    }

    /**
     * アイテムの View に Bitmap をセットする.
     * @param item
     */
    private void setBitmap(ImageItem item) {
        ImageView view = (ImageView) mGridView.findViewWithTag(item);
        if (view != null) {
            view.setImageBitmap(item.bitmap);
            mGridView.invalidateViews();
        }
    }

    /**
     * ImageLoader のコールバック.
     */
    private LoaderCallbacks<Bitmap> callbacks = new LoaderCallbacks<Bitmap>() {
        @Override
        public Loader<Bitmap> onCreateLoader(int i, Bundle bundle) {
            ImageItem item = (ImageItem) bundle.getSerializable("item");
            ImageLoader loader = new ImageLoader(getApplicationContext(), item);
            loader.forceLoad();
            return loader;
        }
        @Override
        public void onLoadFinished(Loader<Bitmap> loader, Bitmap bitmap) {
            int id = loader.getId();
            getSupportLoaderManager().destroyLoader(id);
            // メモリキャッシュに登録する
            ImageItem item = ((ImageLoader) loader).item;
            Log.i(TAG, "キャッシュに登録=" + item.id);
            item.bitmap = bitmap;
            mLruCache.put("" + item.id, bitmap);
            setBitmap(item);
        }
        @Override
        public void onLoaderReset(Loader<Bitmap> loader) {
        }
    };
}
