package com.huiyoumall.uu.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

public class CacheManager {

	// wifi����ʱ��Ϊ5����
	private static long wifi_cache_time = 5 * 60 * 1000;
	// �������绷��Ϊ1Сʱ
	private static long other_cache_time = 60 * 60 * 1000;

	/**
	 * �������
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public static boolean saveObject(Context context, Serializable ser,
			String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ��ȡ����
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Serializable readObject(Context context, String file) {
		if (!isExistDataCache(context, file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// �����л�ʧ�� - ɾ�������ļ�
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * �жϻ����Ƿ����
	 * 
	 * @param cachefile
	 * @return
	 */
	public static boolean isExistDataCache(Context context, String cachefile) {
		if (context == null)
			return false;
		boolean exist = false;
		File data = context.getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}
}
