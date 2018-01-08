package com.huiyoumall.uu.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class WebImageCache {
	private static final String DISK_CACHE_PATH = "/UU_image_cache/";

	private static ConcurrentHashMap<String, SoftReference<Bitmap>> memoryCache;
	private static String diskCachePath;
	private boolean diskCacheEnabled = false;
	private ExecutorService writeThread;

	public WebImageCache(Context context) {
		// Set up in-memory cache store
		memoryCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

		// Set up disk cache store
		Context appContext = context.getApplicationContext();
		diskCachePath = appContext.getCacheDir().getAbsolutePath()
				+ DISK_CACHE_PATH;

		File outFile = new File(diskCachePath);
		outFile.mkdirs();

		diskCacheEnabled = outFile.exists();

		// Set up threadpool for image fetching tasks
		writeThread = Executors.newSingleThreadExecutor();
	}

	public Bitmap get(final String url) {
		Bitmap bitmap = null;

		// Check for image in memory
		bitmap = getBitmapFromMemory(url);

		// Check for image on disk cache
		if (bitmap == null) {
			bitmap = getBitmapFromDisk(url);

			// Write bitmap back into memory cache
			if (bitmap != null) {
				cacheBitmapToMemory(url, bitmap);
			}
		}

		return bitmap;
	}

	public void put(String url, Bitmap bitmap) {
		cacheBitmapToMemory(url, bitmap);
		cacheBitmapToDisk(url, bitmap);
	}

	public void remove(String url) {
		if (url == null) {
			return;
		}

		// Remove from memory cache
		memoryCache.remove(getCacheKey(url));

		// Remove from file cache
		File f = new File(diskCachePath, getCacheKey(url));
		if (f.exists() && f.isFile()) {
			f.delete();
		}
	}

	public static void clear() {
		// Remove everything from memory cache
		memoryCache.clear();

		// Remove everything from file cache
		File cachedFileDir = new File(diskCachePath);
		if (cachedFileDir.exists() && cachedFileDir.isDirectory()) {
			File[] cachedFiles = cachedFileDir.listFiles();
			for (File f : cachedFiles) {
				if (f.exists() && f.isFile()) {
					f.delete();
				}
			}
		}
	}

	private void cacheBitmapToMemory(final String url, final Bitmap bitmap) {
		memoryCache.put(getCacheKey(url), new SoftReference<Bitmap>(bitmap));
	}

	private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
		writeThread.execute(new Runnable() {
			@Override
			public void run() {
				if (diskCacheEnabled) {
					BufferedOutputStream ostream = null;
					try {
						ostream = new BufferedOutputStream(
								new FileOutputStream(new File(diskCachePath,
										getCacheKey(url))), 2 * 1024);
						bitmap.compress(CompressFormat.PNG, 100, ostream);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							if (ostream != null) {
								ostream.flush();
								ostream.close();
							}
						} catch (IOException e) {
						}
					}
				}
			}
		});
	}

	private Bitmap getBitmapFromMemory(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softRef = memoryCache.get(getCacheKey(url));
		if (softRef != null) {
			bitmap = softRef.get();
		}

		return bitmap;
	}

	private Bitmap getBitmapFromDisk(String url) {
		Bitmap bitmap = null;
		if (diskCacheEnabled) {
			String filePath = getFilePath(url);
			File file = new File(filePath);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(filePath);
			}
		}
		return bitmap;
	}

	private String getFilePath(String url) {
		return diskCachePath + getCacheKey(url);
	}

	private String getCacheKey(String url) {
		if (url == null) {
			throw new RuntimeException("Null url passed in");
		} else {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
	}

	// 得到缓存
	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		return getFormatSize(cacheSize);
	}

	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0KB";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}
}
