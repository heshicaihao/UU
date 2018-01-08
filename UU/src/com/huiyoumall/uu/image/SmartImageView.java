package com.huiyoumall.uu.image;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SmartImageView extends ImageView {
	private static final int LOADING_THREADS = 4;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);

	private SmartImageTask currentTask;
	private AlphaAnimation alphaOut;
	private AlphaAnimation alphaIn;
	private Context mContext;

	public SmartImageView(Context context) {
		super(context);
		this.mContext = context;
	}

	public SmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	// Helpers to set image by URL
	public void setImageUrl(String url) {
		setImage(new WebImage(url));
	}

	public void setImageUrl(String url,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url), fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, completeListener);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url), fallbackResource, loadingResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(new WebImage(url), fallbackResource, loadingResource,
				completeListener);
	}

	// Helpers to set image by contact address book id
	public void setImageContact(long contactId) {
		setImage(new ContactImage(contactId));
	}

	public void setImageContact(long contactId, final Integer fallbackResource) {
		setImage(new ContactImage(contactId), fallbackResource);
	}

	public void setImageContact(long contactId, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new ContactImage(contactId), fallbackResource,
				fallbackResource);
	}

	// Set image using SmartImage object
	public void setImage(final SmartImage image) {
		setImage(image, null, null, null);
	}

	public void setImage(final SmartImage image,
			final SmartImageTask.OnCompleteListener completeListener) {
		setImage(image, null, null, completeListener);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, fallbackResource, null);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource,
			SmartImageTask.OnCompleteListener completeListener) {
		setImage(image, fallbackResource, fallbackResource, completeListener);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource) {
		setImage(image, fallbackResource, loadingResource, null);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource,
			final SmartImageTask.OnCompleteListener completeListener) {
		// Set a loading resource
		if (loadingResource != null) {
			setImageResource(loadingResource);
		}

		// Cancel any existing tasks for this image view
		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}

		// Set up the new task
		currentTask = new SmartImageTask(getContext(), image);
		currentTask
				.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
					@Override
					public void onComplete(Bitmap bitmap) {
						if (bitmap != null) {
							setImageBitmapAnim(bitmap);
							// setImageBitmap(bitmap);
						} else {
							// Set fallback resource
							if (fallbackResource != null) {
								setImageResource(fallbackResource);
							}
						}

						if (completeListener != null) {
							completeListener.onComplete();
						}
					}
				});

		// Run the task in a threadpool
		threadPool.execute(currentTask);
	}

	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addRoundRect(new RectF(0, 0, w, h), 10.0f, 10.0f,
				Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}

	/** 图片宽和高的比例 */
	private float ratio = 2.43f;

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// 父容器传过来的宽度方向上的模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// 父容器传过来的高度方向上的模式
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		// 父容器传过来的宽度的值
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		// 父容器传过来的高度的值
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
				- getPaddingRight();

		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
			// 判断条件为，宽度模式为Exactly，也就是填充父窗体或者是指定宽度；
			// 且高度模式不是Exaclty，代表设置的既不是fill_parent也不是具体的值，于是需要具体测量
			// 且图片的宽高比已经赋值完毕，不再是0.0f
			// 表示宽度确定，要测量高度
			height = (int) (width / ratio + 0.5f);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else if (widthMode != MeasureSpec.EXACTLY
				&& heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
			// 判断条件跟上面的相反，宽度方向和高度方向的条件互换
			// 表示高度确定，要测量宽度
			width = (int) (height * ratio + 0.5f);

			widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private AlphaAnimation getAlphaOut() {
		if (null != alphaOut) {
			return alphaOut;
		}
		alphaOut = new AlphaAnimation(1.0f, 0f);
		alphaOut.setFillAfter(true);
		alphaOut.setDuration(200);
		return alphaOut;
	}

	private AlphaAnimation getAlphaIn() {
		if (null != alphaIn) {
			return alphaIn;
		}
		alphaIn = new AlphaAnimation(0f, 1.0f);
		alphaIn.setFillAfter(true);
		alphaIn.setDuration(200);
		return alphaIn;
	}

	public void setImageBitmapAnim(final Bitmap bm) {
		// super.setImageBitmap(bm);
		getAlphaOut().setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setImageDrawable(new BitmapDrawable(mContext.getResources(), bm));
				startAnimation(getAlphaIn());
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		this.startAnimation(getAlphaOut());

	}
}