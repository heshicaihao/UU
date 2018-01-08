package com.huiyoumall.uu.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyoumall.uu.GlobalParams;
import com.huiyoumall.uu.HelperUi;
import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.common.ActionSheetDialog;
import com.huiyoumall.uu.common.ActionSheetDialog.OnSheetItemClickListener;
import com.huiyoumall.uu.common.ActionSheetDialog.SheetItemColor;
import com.huiyoumall.uu.common.WheelMain;
import com.huiyoumall.uu.util.DateUtil;
import com.huiyoumall.uu.widget.MyAlertDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * �༭������Ϣ ����
 * 
 * @author ASUS
 * 
 */
public class EditPersonalInfoActivity extends BaseActivity implements
		OnClickListener {

	private static final int PHOTO_REQUEST_CAMERA = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���

	public Bitmap bitmap;
	public String sexString;
	public int ageInt;

	/* ͷ������ */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;

	private TextView vTitle;
	private TextView vRight_btn;

	private ImageView vHead_portrait_image;
	private TextView vNick_name_text;
	private TextView vSex_text;
	private TextView vAge_text;
	private TextView vCity_text;
	private TextView vPerson_signature_text;

	private LinearLayout vHead_portrait;
	private LinearLayout vNick_name;
	private LinearLayout vSex;
	private LinearLayout vAge;
	private LinearLayout vCity;
	private LinearLayout vPerson_signature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_personal_info);

	}

	@Override
	public void findViewById() {
		vTitle = (TextView) findViewById(R.id.title);
		vRight_btn = (TextView) findViewById(R.id.right_btn);

		vHead_portrait = (LinearLayout) findViewById(R.id.head_portrait_container);
		vHead_portrait_image = (ImageView) findViewById(R.id.head_portrait_image);

		vNick_name = (LinearLayout) findViewById(R.id.nick_name_container);
		vNick_name_text = (TextView) findViewById(R.id.nick_name_text);

		vSex = (LinearLayout) findViewById(R.id.sex_container);
		vSex_text = (TextView) findViewById(R.id.sex_text);

		vAge = (LinearLayout) findViewById(R.id.age_container);
		vAge_text = (TextView) findViewById(R.id.age_text);

		vCity = (LinearLayout) findViewById(R.id.city_container);
		vCity_text = (TextView) findViewById(R.id.city_text);

		vPerson_signature = (LinearLayout) findViewById(R.id.person_signature_container);
		vPerson_signature_text = (TextView) findViewById(R.id.person_signature_text);

		vHead_portrait_image.setImageBitmap(bitmap);
	}

	@Override
	public void initView() {
		vTitle.setText("�༭����");
		vRight_btn.setText("����");

		vHead_portrait.setOnClickListener(this);
		vNick_name.setOnClickListener(this);
		vSex.setOnClickListener(this);
		vAge.setOnClickListener(this);
		vCity.setOnClickListener(this);
		vPerson_signature.setOnClickListener(this);

	}

	@Override
	public void activitybackview(View view) {
		thisfinish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = getSharedPreferences(
				"Person_Info", this.MODE_PRIVATE);
		String NickName = sharedPreferences.getString("NickName", "");
		String Person_signature = sharedPreferences.getString(
				"Person_signature", "");

		vNick_name_text.setText(NickName);
		vPerson_signature_text.setText(Person_signature);
		// vAge_text.setText(ageInt + "��");
		// vSex_text.setText(sexString);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			thisfinish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void thisfinish() {
		Intent intent = new Intent();
		intent.putExtra("bitmap", bitmap);
		this.setResult(Activity.RESULT_OK, intent);
		this.finish();
	}

	private WheelMain wheelMain;

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.head_portrait_container:
			showPictureDialog();
			break;
		case R.id.nick_name_container:
			HelperUi.startActivity(this, NickNameActivity.class);
			break;
		case R.id.sex_container:
			showSixDialog();
			break;
		case R.id.age_container:
			showAgeDialog();
			break;
		case R.id.city_container:
			HelperUi.startActivity(this, ChoiceSiteActivity.class);
			break;
		case R.id.person_signature_container:
			HelperUi.startActivity(this, PersonSignatureActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * ��������Ի���
	 * 
	 */
	public void showAgeDialog() {
		LayoutInflater inflater1 = LayoutInflater.from(this);
		final View timepickerview1 = inflater1.inflate(
				R.layout.view_timepicker, null);
		wheelMain = new WheelMain(timepickerview1, false);
		wheelMain.screenheight = GlobalParams.WIN_HEIGHT;
		Calendar calendar1 = Calendar.getInstance();
		int year1 = calendar1.get(Calendar.YEAR);
		int month1 = calendar1.get(Calendar.MONTH);
		int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
		int hour2 = calendar1.get(Calendar.HOUR_OF_DAY);
		int min2 = calendar1.get(Calendar.MINUTE);
		wheelMain.initDateTimePicker(year1, month1, day1, hour2, min2);
		final MyAlertDialog dialog = new MyAlertDialog(this).builder()
				.setTitle("�������").setView(timepickerview1)
				.setNegativeButton("ȡ��", new OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
		dialog.setPositiveButton("����", new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ageInt = DateUtil.getAgeByBirthday(new SimpleDateFormat(
							"yyyy-MM-dd").parse(wheelMain.getTime()));
					if (ageInt >= 0) {
						vAge_text.setText(ageInt + "��");
					} else {
						vAge_text.setText("����ѧ����û����ѽ��");
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
		dialog.show();
	}

	/**
	 * ѡ���Ա�ĶԻ���
	 */
	public void showSixDialog() {
		new ActionSheetDialog(this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("��", SheetItemColor.BLACK,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								sexString = "��";
								vSex_text.setText(sexString);
							}
						})
				.addSheetItem("Ů", SheetItemColor.BLACK,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								sexString = "Ů";
								vSex_text.setText(sexString);
							}
						}).show();

	}

	/**
	 * ѡ��ͷ��Ի���
	 */
	public void showPictureDialog() {
		new ActionSheetDialog(this)
				.builder()
				// .setTitle("��ѡ�����")
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem("�����ϴ�", SheetItemColor.BLACK,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent = new Intent(
										"android.media.action.IMAGE_CAPTURE");
								// �жϴ洢���Ƿ�����ã����ý��д洢
								if (hasSdcard()) {
									intent.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(
													Environment
															.getExternalStorageDirectory(),
													PHOTO_FILE_NAME)));
								}
								startActivityForResult(intent,
										PHOTO_REQUEST_CAMERA);
							}
						})
				.addSheetItem("ѡ�����", SheetItemColor.BLACK,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								Intent intent = new Intent(Intent.ACTION_PICK);
								intent.setType("image/*");
								startActivityForResult(intent,
										PHOTO_REQUEST_GALLERY);
							}
						}).show();
	}

	// �жϴ洢���Ƿ������
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// �õ�ͼƬ��ȫ·��
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(EditPersonalInfoActivity.this, "δ�ҵ��洢�����޷��洢��Ƭ��",
						0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				this.vHead_portrait_image.setImageBitmap(bitmap);
				// boolean delete = tempFile.delete();
				// System.out.println("delete = " + delete);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����ͼƬ
	 * 
	 */
	private void crop(Uri uri) {
		// �ü�ͼƬ��ͼ
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// �ü���ı�����1��1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// �ü������ͼƬ�ĳߴ��С
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// ͼƬ��ʽ
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
		intent.putExtra("return-data", true);// true:������uri��false������uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/*
	 * �ϴ�ͼƬ
	 */
	public void upload(View view) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			byte[] buffer = out.toByteArray();

			byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
			String photo = new String(encode);

			RequestParams params = new RequestParams();
			params.put("photo", photo);
			String url = "http://192.168.1.133/huiyoumall/data/upload/";

			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (statusCode == 200) {

							Toast.makeText(EditPersonalInfoActivity.this,
									"ͷ���ϴ��ɹ�!", 0).show();
						} else {
							Toast.makeText(EditPersonalInfoActivity.this,
									"��������쳣�������룺" + statusCode, 0).show();

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					Toast.makeText(EditPersonalInfoActivity.this,
							"��������쳣��������  > " + statusCode, 0).show();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
