package com.huiyoumall.uu.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.huiyoumall.uu.common.PayResult;
import com.huiyoumall.uu.util.SignUtils;

public class Alipay {

	public Alipay(Activity context) {
		this.mContext = context;
	}

	private Activity mContext;
	private String product;
	private double price;
	private String order;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	// �̻�PID
	public static final String PARTNER = "2088911930334151";
	// �̻��տ��˺�
	public static final String SELLER = "danney.wu@huiyoumall.com";
	// �̻�˽Կ��pkcs8��ʽ
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMo8j9Jo10rNvjOh"
			+ "EXGdIb/ebCUWXbRU6w+guLqkvcCjlHcwxLLC3z3t7NwMTpPqICQw5jq/BbyuyobO"
			+ "/V2q+FMYdVEu7HYXRz2p5Ov3hQJ/U5xXLtdb05xo6VyHb2cE0tHRxiHe/PWY09A0"
			+ "a4G2BySWFVfP3Qp0/Kw1CpVGQXs/AgMBAAECgYEAikt/zlMDZpPrTUMHurnJKnvf"
			+ "WD1DNt4bIES4Dmo5mFJEZH1lBRldr+vT3WZNFbk04YrO5b1bHvwoYqeJe1Q/Gtw9"
			+ "3XTP2eQEfxRYL67SjBV4OzaJ5q5yCZbHhwTs+lIEjqnS/UNCdWj2QoUXiopMdBM9"
			+ "GSqsRlMI1SCfJz/49QECQQDu7FUUNNh9OkFL08azIfLp++G4i2rSJOlJeFP346Zl"
			+ "INr33qzrQdBvOQ2I3P1ge3WqBalURTmxZ1TwvWOKpNz3AkEA2LD19y37E+QF0nbB"
			+ "PW2Zumy/A4Kg84983+o8rToMfPicA0wLFHPOi5sPeCJ9Mdx8kJE1y7dXZObpLA6i"
			+ "bdUp+QJAVus/YGSBRb2Ft7JJnS2Ck8EAswR1ThIDlKjj01DMXgn/3yDxQ3zj+TKG"
			+ "S/4pZ4pCmiIJIhE1FecDrbkYl322rQJALocQyVpeJw83ENHe/EmkUJs5CI/r72BH"
			+ "9+xbyvZD6DGZjjutxV54YxGQaMM/Do3BUWlcm7lqUogiWQtrZRn4QQJAD6RV4nfe"
			+ "wi/Ss5Sa+X/Rt1hbT4i+RnIRwbv6sa8y8w41lx1/+cMQhSSmYLiLxTpF/AYWsxVN"
			+ "zUBemmbELcgubA==";
	// ֧������Կ
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKPI/SaNdKzb4zoRFxnSG/3mwlFl20VOsPoLi6pL3Ao5R3MMSywt897ezcDE6T6iAkMOY6vwW8rsqGzv1dqvhTGHVRLux2F0c9qeTr94UCf1OcVy7XW9OcaOlch29nBNLR0cYh3vz1mNPQNGuBtgcklhVXz90KdPysNQqVRkF7PwIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(mContext, "֧���ɹ�", Toast.LENGTH_SHORT).show();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mContext, "֧�����ȷ����", Toast.LENGTH_SHORT)
								.show();

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						Toast.makeText(mContext, "֧��ʧ��", Toast.LENGTH_SHORT)
								.show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(mContext, "�����Ϊ��" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. ����SDK֧��
	 * 
	 */
	public void pay(String orderInfo) {
		// ����
		// String orderInfo2 = getOrderInfo("�Բ�", "0.01" + "");

		// �Զ�����RSA ǩ��
		String sign = sign(orderInfo);
		try {
			// �����sign ��URL����
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// �����ķ���֧���������淶�Ķ�����Ϣ
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(mContext);
				// ����֧���ӿڣ���ȡ֧�����
				System.out.println("---֧�����" + payInfo);
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	public void payTask(final String payInfo) {
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(mContext);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * ��ѯ�ն��豸�Ƿ����֧������֤�˻�
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask payTask = new PayTask(mContext);
				// ���ò�ѯ�ӿڣ���ȡ��ѯ���
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. ��ȡSDK�汾��
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(mContext);
		String version = payTask.getVersion();
		Toast.makeText(mContext, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. ����������Ϣ
	 */
	public String getOrderInfo(String subject, String price) {
		// ǩԼ���������ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// ǩԼ����֧�����˺�
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// �̻���վΨһ������
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// // ��Ʒ����
		// orderInfo += "&body=" + "\"" + body + "\"";

		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + "0.01" + "\"";

		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// ����ӿ����ƣ� �̶�ֵ
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// ֧�����ͣ� �̶�ֵ
		orderInfo += "&payment_type=\"1\"";

		// �������룬 �̶�ֵ
		orderInfo += "&_input_charset=\"utf-8\"";

		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		orderInfo += "&return_url=\"m.alipay.com\"";

		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE.replace(" ", "").toString());
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
