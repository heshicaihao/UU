package com.huiyoumall.uu.activity;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyoumall.uu.AppConfig;
import com.huiyoumall.uu.util.MD5;
import com.huiyoumall.uu.util.Util;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WinXinPay {

	private Context mContext;

	public WinXinPay(Context context) {
		this.mContext = context;
		msgApi = WXAPIFactory.createWXAPI(mContext, null);
		req = new PayReq();
		sb = new StringBuffer();
		msgApi.registerApp(AppConfig.APP_ID);
		show = new TextView(mContext);
	}

	public void wxpay() {
		if (checkPaySupport()) {
			// 第一步 app生成预支付订单
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();
		} else {
			Toast.makeText(mContext, "微信不支持支付", Toast.LENGTH_SHORT).show();
		}

		// String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
		// .toUpperCase();
	}

	private static final String TAG = "PayActivity";

	private PayReq req;
	private IWXAPI msgApi = null;
	private TextView show;
	private Map<String, String> resultunifiedorder;
	private StringBuffer sb;

	// 检查版本是否支持支付
	private boolean checkPaySupport() {
		return msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
	}

	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(AppConfig.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(AppConfig.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "提示", "正在获取预支付订单...");
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			show.setText(sb.toString());

			resultunifiedorder = result;
			genPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	// 随机字符串
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	// 时间戳
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	// 获取商户订单号
	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	//
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", AppConfig.APP_ID));
			// 商品或支付单简要描述
			packageParams.add(new BasicNameValuePair("body", "UU微信支付"));
			packageParams
					.add(new BasicNameValuePair("mch_id", AppConfig.MCH_ID));
			// 随机字符串
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			// 接收微信支付异步通知回调地址
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://121.40.35.3/test"));//
			// 商户订单号
			packageParams.add(new BasicNameValuePair("out_trade_no",
					genOutTradNo()));
			// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			// 总金额
			packageParams.add(new BasicNameValuePair("total_fee", "1"));
			// 交易类型
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

	}

	// 第二步 生成app微信支付参数
	private void genPayReq() {

		req.appId = AppConfig.APP_ID;
		req.partnerId = AppConfig.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		show.setText(sb.toString());

		Log.e("orion", signParams.toString());

		sendPayReq();

	}

	// 第三步 调起微信支付
	private void sendPayReq() {

		// msgApi.registerApp(AppConfig.APP_ID);
		msgApi.sendReq(req);
	}
}
