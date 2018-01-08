package com.huiyoumall.uu.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.huiyoumall.uu.AppException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * xml����������
 * 
 * @version ����ʱ�䣺2015��5��25�� ����2:04:19
 * 
 */

public class XmlUtils {

	private final static String TAG = XmlUtils.class.getSimpleName();

	/**
	 * ��һ��xml��ת��Ϊbeanʵ����
	 * 
	 * @param type
	 * @param instance
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(Class<T> type, InputStream is) {
		XStream xmStream = new XStream(new DomDriver("UTF-8"));
		// ���ÿɺ���Ϊ��javabean���ж���Ľ�������
		xmStream.ignoreUnknownElements();
		xmStream.registerConverter(new MyIntCoverter());
		xmStream.registerConverter(new MyLongCoverter());
		xmStream.registerConverter(new MyFloatCoverter());
		xmStream.registerConverter(new MyDoubleCoverter());
		xmStream.processAnnotations(type);
		T obj = null;
		try {
			obj = (T) xmStream.fromXML(is);
		} catch (Exception e) {
			TLog.log(TAG, "����xml�����쳣��" + e.getMessage());
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					TLog.log(TAG, "�ر��������쳣��" + e.getMessage());
				}
			}
		}
		return obj;
	}

	public static <T> T toBean(Class<T> type, byte[] bytes) {
		if (bytes == null)
			return null;
		return toBean(type, new ByteArrayInputStream(bytes));
	}

	private static class MyIntCoverter extends IntConverter {

		@Override
		public Object fromString(String str) {
			int value;
			try {
				value = (Integer) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyLongCoverter extends LongConverter {
		@Override
		public Object fromString(String str) {
			long value;
			try {
				value = (Long) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyFloatCoverter extends FloatConverter {
		@Override
		public Object fromString(String str) {
			float value;
			try {
				value = (Float) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}

	private static class MyDoubleCoverter extends DoubleConverter {
		@Override
		public Object fromString(String str) {
			double value;
			try {
				value = (Double) super.fromString(str);
			} catch (Exception e) {
				value = 0;
			}
			return value;
		}

		@Override
		public String toString(Object obj) {
			return super.toString(obj);
		}
	}
}
