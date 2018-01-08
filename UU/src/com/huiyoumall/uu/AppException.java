package com.huiyoumall.uu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.HttpException;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.SystemTool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;

/**
 * Ӧ�ó����쳣�����ڲ����쳣����ʾ������Ϣ
 * 
 * @author hsy (hsyemail@126.com)
 * @created 2015��5��25�� ����3:34:05
 * 
 */
@SuppressWarnings("serial")
public class AppException extends Exception implements UncaughtExceptionHandler {

	/** �����쳣���� */
	public final static byte TYPE_NETWORK = 0x01;
	public final static byte TYPE_SOCKET = 0x02;
	public final static byte TYPE_HTTP_CODE = 0x03;
	public final static byte TYPE_HTTP_ERROR = 0x04;
	public final static byte TYPE_XML = 0x05;
	public final static byte TYPE_IO = 0x06;
	public final static byte TYPE_RUN = 0x07;
	public final static byte TYPE_JSON = 0x08;
	public final static byte TYPE_FILENOTFOUND = 0x09;

	private byte type;// �쳣������
	// �쳣��״̬�룬����һ�������������״̬��
	private int code;

	/** ϵͳĬ�ϵ�UncaughtException������ */
	private AppContext mContext;

	private AppException(Context context) {
		this.mContext = (AppContext) context;
	}

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public int getType() {
		return this.type;
	}

	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}

	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0, e);
	}

	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0, e);
	}

	public static AppException file(Exception e) {
		return new AppException(TYPE_FILENOTFOUND, 0, e);
	}

	// io�쳣
	public static AppException io(Exception e) {
		return io(e, 0);
	}

	// io�쳣
	public static AppException io(Exception e, int code) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, code, e);
		} else if (e instanceof IOException) {
			return new AppException(TYPE_IO, code, e);
		}
		return run(e);
	}

	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}

	public static AppException json(Exception e) {
		return new AppException(TYPE_JSON, 0, e);
	}

	// ���������쳣
	public static AppException network(Exception e) {
		if (e instanceof UnknownHostException || e instanceof ConnectException) {
			return new AppException(TYPE_NETWORK, 0, e);
		} else if (e instanceof HttpException) {
			return http(e);
		} else if (e instanceof SocketException) {
			return socket(e);
		}
		return http(e);
	}

	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}

	/**
	 * ��ȡAPP�쳣�����������
	 * 
	 * @param context
	 * @return
	 */
	public static AppException getAppExceptionHandler(Context context) {
		return new AppException(context.getApplicationContext());
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex)) {
			System.exit(0);
		}
	}

	/**
	 * �Զ����쳣����:�ռ�������Ϣ&���ʹ��󱨸�
	 * 
	 * @param ex
	 * @return true:�����˸��쳣��Ϣ;���򷵻�false
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null || mContext == null) {
			return false;
		}
		boolean success = true;
		try {
			success = saveToSDCard(ex);
		} catch (Exception e) {
		} finally {
			if (!success) {
				return false;
			} else {
				final Context context = AppManager.getAppManager()
						.currentActivity();
				// ��ʾ�쳣��Ϣ&���ͱ���
				new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						// �õ�δ������쳣��
						// UIHelper.sendAppCrashReport(context);
						Looper.loop();
					}
				}.start();
			}
		}
		return true;
	}

	private boolean saveToSDCard(Throwable ex) throws Exception {
		boolean append = false;
		File file = FileUtils.getSaveFile("HuiYou", "HYLog.log");
		if (System.currentTimeMillis() - file.lastModified() > 5000) {
			append = true;
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
				file, append)));
		// ���������쳣��ʱ��
		pw.println(SystemTool.getDataTime("yyyy-MM-dd-HH-mm-ss"));
		// �����ֻ���Ϣ
		dumpPhoneInfo(pw);
		pw.println();
		// �����쳣�ĵ���ջ��Ϣ
		ex.printStackTrace(pw);
		pw.println();
		pw.close();
		return append;
	}

	private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
		// Ӧ�õİ汾���ƺͰ汾��
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
				PackageManager.GET_ACTIVITIES);
		pw.print("App Version: ");
		pw.print(pi.versionName);
		pw.print('_');
		pw.println(pi.versionCode);
		pw.println();

		// android�汾��
		pw.print("HY Version: ");
		pw.print(Build.VERSION.RELEASE);
		pw.print("_");
		pw.println(Build.VERSION.SDK_INT);
		pw.println();

		// �ֻ�������
		pw.print("Vendor: ");
		pw.println(Build.MANUFACTURER);
		pw.println();

		// �ֻ��ͺ�
		pw.print("Model: ");
		pw.println(Build.MODEL);
		pw.println();

		// cpu�ܹ�
		pw.print("CPU ABI: ");
		pw.println(Build.CPU_ABI);
		pw.println();
	}
}
