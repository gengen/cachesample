package com.example.cachesample;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileDataUtil {
public static Bitmap loadBitmap(String fileName) throws IOException {
	      Bitmap result = null;

	      FileInputStream fileInput = null;
	      BufferedInputStream bufInput = null;
	      try {
	         fileInput = new FileInputStream(fileName);
	         bufInput = new BufferedInputStream(fileInput);
	         result = BitmapFactory.decodeStream(bufInput);
	      } finally {
	         if (fileInput != null) {
	            try {
	               fileInput.close();
	            } catch (IOException e) {
	            }
	         }
	         if (bufInput != null) {
	            try {
	               bufInput.close();
	            } catch (IOException e) {
	            }
	         }
	      }
	      return result;
	   }
	   //--�����܂� ---------------------------------------------------------

	   /**
	    * �A�v���P�[�V�����ۑ��f�B���N�g���ɑ��݂���摜�t�@�C�����擾����B
	    * @return �摜�t�@�C�����X�g (���݂��Ȃ��ꍇ�͋�̃��X�g)
	    */
	   public static List<File> getApplicationBitmapFileList(Context context)
	                                                                  throws IOException {
	      List<File> result = new ArrayList<File>();;

	      File sdcardRoot = getSdCardRootDirectory();
	      File loadDir = new File(sdcardRoot, /*context.getPackageName()*/"ContShooting");
	      Log.d("GallerySample", loadDir.getPath());
	      if (!loadDir.exists()) {
	         throw new IOException(
	            String.format("�A�v���P�[�V�����̕ۑ��悪���݂��܂���(%s)", loadDir));
	      }
	      if (!loadDir.canRead()) {
	         throw new IOException(
	            String.format("�t�@�C����ǂݏo���ł��܂���(%s)", loadDir));
	      }

	      // �A�v���P�[�V�����f�B���N�g���̉摜�t�@�C�����擾
	      File[] files = loadDir.listFiles(new FilenameFilter() {

	         // �摜�t�@�C���݂̂�ΏۂƂ���(*.PNG(*.png) or *.JPEG(*.jpeg))
	         public boolean accept(File loadDir, String fileName) {
	            // CompressFormat
	            //   JPEG = new CompressFormat("JPEG", 0);
	            //   PNG = new CompressFormat("PNG", 1);
	            return fileName.matches(".+\\.(PNG|png|JPEG|jpeg|JPG|jpg)$");
	         }
	      });
	      // ���C�����ň����₷���悤�Ƀ��X�g�ɕϊ����Ă��܂�
	      if (files != null && files.length > 0) {
	         for (File item : files) {
	            result.add(item);
	         }
	      }
	      return result;
	   }

	   /**
	    * �O�����f�B�A�f�B���N�g�����擾����B
	    * @return �O�����f�B�A�f�B���N�g�� (/sdcard/)
	    * @throws IOException ���f�B�A�G���[
	    */
	   public static File getSdCardRootDirectory() throws IOException {
	      // SD�J�[�h���}�E���g����Ă��邩
	      if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
	         throw new IOException(String.format("���f�B�A���Z�b�g����Ă��܂���(%s)",
	               Environment.getExternalStorageState()));
	      }
	      File root = Environment.getExternalStorageDirectory();
	      if (root == null) {
	         throw new IOException("���f�B�A�����p�ł��܂���");
	      }
	      return root;
	   }
}
