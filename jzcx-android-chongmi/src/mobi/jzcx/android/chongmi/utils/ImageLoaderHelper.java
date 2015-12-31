package mobi.jzcx.android.chongmi.utils;

import mobi.jzcx.android.chongmi.R;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class ImageLoaderHelper {
	public static final int HEADIMAGE_CORNER_RADIUS = 0;

	/**
	 * 图像选项类
	 * 
	 * @param defResId
	 *            默认图片id
	 * @param round
	 *            是否圆角
	 * @return
	 */
	public static DisplayImageOptions imageOptionWithCircle(int defResId, boolean round, int cornerRadius) {
		DisplayImageOptions.Builder displayOptions = new DisplayImageOptions.Builder();

		displayOptions.cacheInMemory(true);
		displayOptions.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		displayOptions.bitmapConfig(Bitmap.Config.RGB_565);
		// 防止内存溢出的，图片太多就这这个。还有其他设置
		// 如Bitmap.Config.ARGB_8888
		if (defResId != 0) {
			displayOptions.showImageOnLoading(defResId); // 默认图片
			displayOptions.showImageForEmptyUri(defResId); // url爲空會显示该图片，自己放在drawable里面的
			displayOptions.showImageOnFail(defResId); // 加载失败显示的图片
		}
		displayOptions.cacheOnDisk(true);
		if (round) {
			displayOptions.displayer(new CircleBitmapDisplayer(cornerRadius)); // 圆角
		}

		return displayOptions.build();
	}

	/**
	 * 图像选项类
	 * 
	 * @param defResId
	 *            默认图片id
	 * @param round
	 *            是否圆角
	 * @return
	 */
	public static DisplayImageOptions imageOptionWithRound(int defResId, boolean round, int cornerRadius) {
		DisplayImageOptions.Builder displayOptions = new DisplayImageOptions.Builder();
		displayOptions.cacheInMemory(true);
		displayOptions.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		displayOptions.bitmapConfig(Bitmap.Config.RGB_565);
		// 防止内存溢出的，图片太多就这这个。还有其他设置
		// 如Bitmap.Config.ARGB_8888
		if (defResId != 0) {
			displayOptions.showImageOnLoading(defResId); // 默认图片
			displayOptions.showImageForEmptyUri(defResId); // url爲空會显示该图片，自己放在drawable里面的
			displayOptions.showImageOnFail(defResId); // 加载失败显示的图片
		}
		displayOptions.cacheOnDisk(true);
		if (round) {
			if (cornerRadius != 0) {
				displayOptions.displayer(new RoundedBitmapDisplayer(cornerRadius)); // 圆角
			} else {
				displayOptions.displayer(new CircleBitmapDisplayer(0)); // 圆
			}
		}

		return displayOptions.build();
	}

	public static DisplayImageOptions imageOptionWithScale(int defResId, boolean round, int cornerRadius) {
		DisplayImageOptions.Builder displayOptions = new DisplayImageOptions.Builder();
		displayOptions.cacheInMemory(true);
		displayOptions.imageScaleType(ImageScaleType.NONE_SAFE);
		displayOptions.bitmapConfig(Bitmap.Config.RGB_565);
		// 防止内存溢出的，图片太多就这这个。还有其他设置
		// 如Bitmap.Config.ARGB_8888
		if (defResId != 0) {
			displayOptions.showImageOnLoading(defResId); // 默认图片
			displayOptions.showImageForEmptyUri(defResId); // url爲空會显示该图片，自己放在drawable里面的
			displayOptions.showImageOnFail(defResId); // 加载失败显示的图片
		}
		displayOptions.cacheOnDisk(true);
		if (round) {
			if (cornerRadius != 0) {
				displayOptions.displayer(new RoundedBitmapDisplayer(cornerRadius)); // 圆角
			} else {
				displayOptions.displayer(new CircleBitmapDisplayer(0)); // 圆
			}
		}

		return displayOptions.build();
	}

	public static DisplayImageOptions imageOptionWithRound(int defResId, boolean round, BitmapProcessor post) {
		DisplayImageOptions.Builder displayOptions = new DisplayImageOptions.Builder();
		displayOptions.cacheInMemory(true);
		displayOptions.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		displayOptions.bitmapConfig(Bitmap.Config.RGB_565);// 防止内存溢出的，图片太多就这这个。还有其他设置
		// 如Bitmap.Config.ARGB_8888
		if (defResId != 0) {
			displayOptions.showImageOnLoading(defResId); // 默认图片
			displayOptions.showImageForEmptyUri(defResId); // url爲空會显示该图片，自己放在drawable里面的
			displayOptions.showImageOnFail(defResId);// 加载失败显示的图片
		}
		displayOptions.cacheOnDisk(true);
		displayOptions.postProcessor(post);
		if (round) {
			displayOptions.displayer(new RoundedBitmapDisplayer(HEADIMAGE_CORNER_RADIUS)); // 圆角
		}

		return displayOptions.build();
	}

	public static DisplayImageOptions imageOptionWithRound(int defResId, BitmapProcessor post, int cornerRadius) {
		DisplayImageOptions.Builder displayOptions = new DisplayImageOptions.Builder();
		displayOptions.cacheInMemory(true);
		displayOptions.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		displayOptions.bitmapConfig(Bitmap.Config.RGB_565);// 防止内存溢出的，图片太多就这这个。还有其他设置
		// 如Bitmap.Config.ARGB_8888
		if (defResId != 0) {
			displayOptions.showImageOnLoading(defResId); // 默认图片
			displayOptions.showImageForEmptyUri(defResId); // url爲空會显示该图片，自己放在drawable里面的
			displayOptions.showImageOnFail(defResId);// 加载失败显示的图片
		}
		displayOptions.cacheOnDisk(true);
		displayOptions.postProcessor(post);
		displayOptions.displayer(new RoundedBitmapDisplayer(cornerRadius)); // 圆角

		return displayOptions.build();
	}

	/**
	 * 显示用户头像 (默认图片是用户头像)
	 * 
	 * @param uri
	 * @param imageView
	 */
	public static void displayAvatar(String uri, ImageView imageView) {
		if (TextUtils.isEmpty(uri) || null == imageView) {
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView,
				imageOptionWithCircle(R.drawable.avatar_default_image, true, HEADIMAGE_CORNER_RADIUS));

	}

	/**
	 * 显示用户头像 (默认图片是用户头像)
	 * 
	 * @param uri
	 * @param imageView
	 * @param defResId
	 *            默认图片id
	 */
	public static void displayAvatar(String uri, ImageView imageView, int defResId) {
		if (TextUtils.isEmpty(uri) || null == imageView) {
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView,
				imageOptionWithCircle(defResId, true, HEADIMAGE_CORNER_RADIUS));

	}

	/**
	 * 显示图片
	 * 
	 * @param uri
	 * @param imageView
	 * @param defResId
	 *            默认图片id
	 * @param round
	 *            是否圆角
	 */
	public static void displayImage(String uri, ImageView imageView, int defResId, boolean round) {
		if (null == imageView) {
			return;
		}

		if (TextUtils.isEmpty(uri)) {
			imageView.setImageResource(defResId);
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView,
				imageOptionWithRound(defResId, round, HEADIMAGE_CORNER_RADIUS));
	}

	public static void displayImage(String uri, ImageView imageView, int defResId, boolean round,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		if (null == imageView) {
			return;
		}

		if (TextUtils.isEmpty(uri)) {
			imageView.setImageResource(defResId);
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView,
				imageOptionWithRound(defResId, round, HEADIMAGE_CORNER_RADIUS), listener, progressListener);
	}

	public static void displayImage(String uri, ImageView imageView, int defResId, BitmapProcessor post, boolean round) {
		if (null == imageView) {
			return;
		}

		if (TextUtils.isEmpty(uri)) {
			imageView.setImageResource(defResId);
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView, imageOptionWithRound(defResId, round, post));
	}

	public static void displayImage(String uri, ImageView imageView, int defResId, boolean round, int cornerRadius) {
		if (null == imageView) {
			return;
		}

		if (TextUtils.isEmpty(uri)) {
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView, imageOptionWithRound(defResId, round, cornerRadius));
	}

	public static void displayActivityImage(String uri, ImageView imageView, int defResId, boolean round,
			int cornerRadius) {
		if (null == imageView) {
			return;
		}

		if (TextUtils.isEmpty(uri)) {
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView, imageOptionWithScale(defResId, round, cornerRadius));
	}

	public static void displayImage(String uri, ImageView imageView, int defResId, BitmapProcessor post, boolean round,
			int cornerRadius) {
		if (null == imageView) {
			return;
		}

		if (TextUtils.isEmpty(uri)) {
			return;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(uri), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			uri = "file:/" + filepath;
		} else {
			if (FileUtils.exists(uri)) {
				uri = "file:/" + uri;
			}
		}

		ImageLoader.getInstance().displayImage(uri, imageView, imageOptionWithRound(defResId, post, cornerRadius));
	}

	public static void displayImage(String url, ImageView img) {
		ImageLoader.getInstance().displayImage(url, img);
	}

	public static Bitmap loadImageByURL(String url, int width, int height) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}

		// 本地如果存在，则从本地读取
		String filepath = FileUtils.getFilePath(FileUtils.COVER, MD5.encoderForString(url), FileUtils.JPG);
		if (FileUtils.exists(filepath)) {
			url = "file:/" + filepath;
		} else {
			if (FileUtils.exists(url)) {
				url = "file:/" + url;
			}
		}
		// ImageSize imagesize = new ImageSize(50, 50);
		ImageSize imagesize = new ImageSize(width, height);
		return ImageLoader.getInstance().loadImageSync(url, imagesize);
	}
}
