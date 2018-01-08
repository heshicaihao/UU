package com.huiyoumall.uu.remote;

import com.huiyoumall.uu.AppConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UURemoteApi {

	/**
	 * �û���¼ post����
	 * 
	 * @param userName
	 * @param passWord
	 * @param handler
	 */
	public static void login(String userName, String passWord,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("name", userName);
		params.put("pwd", passWord);
		String loginurl = "act=login_interface&op=login";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ֪ͨ���������Ͷ�����֤
	 * 
	 * @param tel
	 * @param handler
	 */
	public static void GetRegisterCode(String tel,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("tel", tel);
		String loginurl = "act=member_interface&op=tel_verify";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ע���û���Ϣ
	 * 
	 * @param tel
	 * @param code
	 * @param handler
	 */
	public static void upLoadRegister(String tel, String pwd, String code,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("tel", tel);
		params.put("pwd", pwd);
		params.put("code", code);
		String loginurl = "act=member_interface&op=tel_confirm";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ��ҳ����
	 * 
	 * @param handler
	 */
	public static void index(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		String loginurl = "act=phoneinterface&op=index";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ���ظ����
	 * 
	 * @param lonlat
	 * @param handler
	 */
	public static void loadNearAct(String lonlat,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("content", lonlat);
		String loginurl = "act=action_interface&op=near_action";
		ApiHttpClient.get(loginurl, params, handler);

	}

	/**
	 * ���س������� get����
	 * 
	 * @param handler
	 */
	public static void loadSports(int sport_id, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("sport_id", sport_id + "");
		params.put("page", page + "");
		String loginurl = "act=phoneinterface&op=sport_list";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ���� get����
	 * 
	 * @param handler
	 */
	public static void searchMerchant(String content, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		params.put("page", page);
		String loginurl = "act=phoneinterface&op=merchant_select";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �ղس���
	 * 
	 * @param mid
	 */
	public static void collectMerchant(String mMid, int member_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mid", mMid);
		params.put("user_id", member_id);
		String loginurl = "act=phoneinterface&op=merchant_collect";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ��ȡ�ҵ��ղ�
	 * 
	 * @param member_id
	 * @param latlon
	 * @param handler
	 */
	public static void loadMyCollect(int member_id, String latlon,
			int currentPagte, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		params.put("latlon", latlon);
		params.put("page", currentPagte);
		String loginurl = "act=member_interface&op=merchant_collect";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ���س�������
	 * 
	 * @param mid
	 */
	public static void loadMerchant(String mid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mid", "32");
		String loginurl = "act=phoneinterface&op=merchant_detail";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ���س���Ԥ����Ϣ
	 * 
	 * @param mid
	 * @param time
	 * @param handler
	 */
	public static void loadReserve(String mid, String time,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mid", mid);
		params.put("time", time);
		String loginurl = "act=merchant_interface&op=merchant_reserve";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ���س����б�
	 * 
	 * @param sport_id
	 * @param page
	 * @param area
	 * @param handler
	 */
	public static void loadSports(String sport_id, String page, String area,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("sport_id", sport_id);
		params.put("page", page);
		params.put("area", area);
		String loginurl = "act=phoneinterface&op=sport_list";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param mid
	 * @param page
	 * @param handler
	 */
	public static void LoadComments(String mid, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("mid", mid);
		params.put("page", page);
		String loginurl = "act=phoneinterface&op=mer_comment_select";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �ύ����Ԥ������
	 * 
	 * @param content
	 * @param handler
	 */
	public static void uploadOrderStr(String content,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("userid", 32);
		params.put("content", content);
		String loginurl = "act=merchant_interface&op=order_add";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ��ȡ�ҵĶ��� δ����
	 * 
	 * @param member_id
	 * @param handler
	 */
	public static void loadMyOrderNotPay(int member_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		String loginurl = "act=member_interface&op=npay_order";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ��ȡ�ҵĶ��� �Ѹ���
	 * 
	 * @param member_id
	 * @param handler
	 */
	public static void loadMyOrderIsPay(int member_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		String loginurl = "act=member_interface&op=pay_order";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ��ȡ�ҵĶ��� ��ȡ��
	 * 
	 * @param member_id
	 * @param handler
	 */
	public static void loadMyOrderCancelPay(int member_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		String loginurl = "act=member_interface&op=cancel_order";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ����֧����֧��
	 * 
	 * @param order
	 * @param handler
	 */
	public static void uploadAilpay(String order,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("order_sn", order);
		// String loginurl = "act=phoneinterface&op=alipay";
		// ApiHttpClient.get(loginurl, params, handler);
		ApiHttpClient.client
				.post("http://192.168.1.112/huiyou/shop/index.php?act=phoneinterface&op=alipay",
						params, handler);
	}

	/**
	 * ��ȡԤ������Ϣ ����
	 * 
	 * @param memberId
	 * @param handler
	 */
	public static void uploadOrderToActivity(String memberId,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", memberId);
		String loginurl = "act=action_interface&op=order_list";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �����
	 * 
	 * @param content
	 * @param handler
	 */
	public static void releaseActivity(String content,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		String loginurl = "act=action_interface&op=action_add";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �һ��ӿ�
	 * 
	 * @param sportid
	 * @param latlon
	 * @param sort
	 * @param handler
	 */
	public static void FindPartner(String sportid, String latlon, String sort,
			String methem, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("sportid", sportid);
		params.put("latlon", latlon);
		params.put("sort", sort);
		params.put("methem", methem);
		params.put("page", page);
		String loginurl = "act=action_interface&op=action_list";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �鿴�����
	 * 
	 * @param memberId
	 * @param act_id
	 * @param handler
	 */
	public static void loadHuoDongDetail(int act_id, int memberId,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("aid", act_id);
		params.put("member_id", memberId);
		String loginurl = "act=action_interface&op=action_detail";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �����μӻ
	 * 
	 * @param act_id
	 * @param memberId
	 * @param handler
	 */
	public static void loadJoinHuodong(int act_id, int memberId,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("actid", act_id);
		params.put("member_id", memberId);
		String loginurl = "act=action_interface&op=action_join";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �ҷ����Ļ
	 * 
	 * @param member_id
	 * @param page
	 * @param handler
	 */
	public static void loadMyPostAct(int member_id, int page, String latlon,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		params.put("page", page);
		params.put("latlon", latlon);
		String loginurl = "act=action_interface&op=mine_action";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �ҲμӵĻ
	 * 
	 * @param member_id
	 * @param page
	 * @param handler
	 */
	public static void loadMyJoinAct(int member_id, int page, String latlon,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		params.put("page", page);
		params.put("latlon", latlon);
		String loginurl = "act=action_interface&op=mine_join";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * ����ɵĻ
	 * 
	 * @param member_id
	 * @param page
	 * @param handler
	 */
	public static void loadMyFinishAct(int member_id, int page, String latlon,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("member_id", member_id);
		params.put("page", page);
		params.put("latlon", latlon);
		String loginurl = "act=action_interface&op=end_action";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �õ��Ҿ���ȥ�ĳ����б�
	 * 
	 * @param memberId
	 * @param lonLat
	 * @param handler
	 */
	public static void loadMyOftenGoMerchant(int memberId, String lonLat,
			AsyncHttpResponseHandler handler) {

		RequestParams params = new RequestParams();
		params.put("memberId", memberId);
		params.put("lonLat", lonLat);
		String loginurl = "act=action_interface&op=action_join";
		ApiHttpClient.get(loginurl, params, handler);
	}

	/**
	 * �޸�
	 * 
	 * @param userName
	 * @param passWord
	 * @param handler
	 */
	public static void update(String userName, String passWord,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("username", userName);
		params.put("password", passWord);
		String loginurl = "";
		ApiHttpClient.post(loginurl, params, handler);
	}

	/**
	 * �õ�δ֧������
	 * 
	 * @param catalog
	 * @param handler
	 */
	public static void getNoPayOrder(int catalog, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("catalog", catalog);
		params.put("pageIndex", page);
		params.put("pageSize", AppConfig.PAGE_SIZE);
		ApiHttpClient.post("action/api/comment_pub", params, handler);
	}

	/**
	 * �õ���֧������
	 * 
	 * @param catalog
	 * @param handler
	 */
	public static void getPayOrder(String content, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		params.put("pageIndex", page);
		params.put("pageSize", AppConfig.PAGE_SIZE);
		ApiHttpClient.post("action/api/comment_pub", params, handler);
	}

	/**
	 * �õ�ȡ������
	 * 
	 * @param catalog
	 * @param handler
	 */
	public static void getCancelPayOrder(String content, int page,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		params.put("pageIndex", page);
		params.put("pageSize", AppConfig.PAGE_SIZE);
		ApiHttpClient.post("action/api/comment_pub", params, handler);
	}

	/**
	 * �����
	 * 
	 * @param object
	 * @param handler
	 */
	// public static void pubTweet(Object object, AsyncHttpResponseHandler
	// handler) {
	// RequestParams params = new RequestParams();
	// params.put("uid", tweet.getAuthorid());
	// params.put("msg", tweet.getBody());
	//
	// if (!TextUtils.isEmpty(tweet.getImageFilePath())) {
	// try {
	// params.put("img", new File(tweet.getImageFilePath()));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	// if (!TextUtils.isEmpty(tweet.getAudioPath())) {
	// try {
	// params.put("amr", new File(tweet.getAudioPath()));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	// ApiHttpClient.post("", params, handler);
	// }
	/**
	 * ����ղ�
	 * 
	 * @param uid
	 *            �û�UID
	 * @param objid
	 * 
	 * @param type
	 */
	public static void addFavorite(int uid, int objid, int type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("objid", objid);
		params.put("type", type);
		ApiHttpClient.post("", params, handler);
	}

	/**
	 * ɾ���ղ�
	 * 
	 * @param uid
	 * @param objid
	 * @param type
	 * @param handler
	 */
	public static void delFavorite(int uid, int objid, int type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("objid", objid);
		params.put("type", type);
		ApiHttpClient.post("", params, handler);
	}

	/**
	 * ��ѯ
	 * 
	 * @param id
	 * @param handler
	 */
	public static void getMessage(int id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams("id", id);
		ApiHttpClient.get("", params, handler);
	}
}
