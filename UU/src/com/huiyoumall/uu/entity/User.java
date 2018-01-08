package com.huiyoumall.uu.entity;

import java.io.Serializable;
import java.util.List;

import com.huiyoumall.uu.common.GsonTools;
import com.huiyoumall.uu.util.StringUtils;

/**
 * �û� ʵ����
 * 
 * @author ASUS
 * 
 */
public class User implements Serializable {

	private int member_id;// User ID
	private String member_name;// �û���
	private String member_avatar;// ͷ��
	private String member_time;// ע��ʱ��
	private String member_old_login_time;// �����¼
	private String member_truename;// �ǳ�
	private String member_sex;// �Ա�
	private String member_birthday;// ����
	private String member_provinceid;// ʡ
	private String member_cityid;// ��
	private String member_areaid;// ��
	private String member_areainfo;// �ֵ�

	// �ֻ���
	// U�ң�ԭ���Ļ��֣�
	// ����
	// Ⱥ�飨Ȧ�ӣ�
	// �Ż�ȯ
	// �󶨵�΢�ź�
	// �󶨵�QQ
	// �󶨵�΢��
	// ����
	// ����
	// �߼����� ��ͨ ���� ��

	public static List<User> parse(String jsonString) throws Exception {
		List<User> members = null;
		if (!StringUtils.isEmpty(jsonString)) {
			members = GsonTools.getListDate(jsonString, User.class);
		}
		return members;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}

	public String getMember_avatar() {
		return member_avatar;
	}

	public void setMember_avatar(String member_avatar) {
		this.member_avatar = member_avatar;
	}

	public String getMember_time() {
		return member_time;
	}

	public void setMember_time(String member_time) {
		this.member_time = member_time;
	}

	public String getMember_old_login_time() {
		return member_old_login_time;
	}

	public void setMember_old_login_time(String member_old_login_time) {
		this.member_old_login_time = member_old_login_time;
	}

	public String getMember_truename() {
		return member_truename;
	}

	public void setMember_truename(String member_truename) {
		this.member_truename = member_truename;
	}

	public String getMember_sex() {
		return member_sex;
	}

	public void setMember_sex(String member_sex) {
		this.member_sex = member_sex;
	}

	public String getMember_birthday() {
		return member_birthday;
	}

	public void setMember_birthday(String member_birthday) {
		this.member_birthday = member_birthday;
	}

	public String getMember_provinceid() {
		return member_provinceid;
	}

	public void setMember_provinceid(String member_provinceid) {
		this.member_provinceid = member_provinceid;
	}

	public String getMember_cityid() {
		return member_cityid;
	}

	public void setMember_cityid(String member_cityid) {
		this.member_cityid = member_cityid;
	}

	public String getMember_areaid() {
		return member_areaid;
	}

	public void setMember_areaid(String member_areaid) {
		this.member_areaid = member_areaid;
	}

	public String getMember_areainfo() {
		return member_areainfo;
	}

	public void setMember_areainfo(String member_areainfo) {
		this.member_areainfo = member_areainfo;
	}

}
