package com.example.cachesample;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * �A�C�e���̃f�[�^.
 */
public class ImageItem implements Serializable {
    /** �V���A���o�[�W����. */
    private static final long serialVersionUID = 1L;
    /** {@link Bitmap}. */
    public Bitmap bitmap;
    /** �L�[. */
    //public String key;
    public int id;
}
