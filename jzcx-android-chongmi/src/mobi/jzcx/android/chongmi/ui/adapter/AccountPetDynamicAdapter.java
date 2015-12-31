package mobi.jzcx.android.chongmi.ui.adapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mobi.jzcx.android.chongmi.R;
import mobi.jzcx.android.chongmi.biz.vo.DynamicObject;
import mobi.jzcx.android.chongmi.utils.CommonTextUtils;
import mobi.jzcx.android.chongmi.utils.CommonUtils;
import mobi.jzcx.android.chongmi.utils.FileUtils;
import mobi.jzcx.android.chongmi.utils.ImageLoaderHelper;
import mobi.jzcx.android.chongmi.utils.MD5;
import mobi.jzcx.android.core.mvc.utils.LogUtils;
import mobi.jzcx.android.core.utils.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountPetDynamicAdapter extends BaseAdapter {
	private ArrayList<DynamicObject> mList;
	Context mContext;

	public AccountPetDynamicAdapter(Context context) {
		this.mContext = context;
		mList = new ArrayList<DynamicObject>();
	}

	public void updateList(ArrayList<DynamicObject> data) {
		if (mList != null) {
			mList.clear();
			mList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public int getCount() {
		return mList.size();
	}

	public DynamicObject getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_account_pet_dynamic, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DynamicObject dynamicObj = getItem(position);
		if (dynamicObj != null) {
			if (dynamicObj.getPetPhotos() != null && dynamicObj.getPetPhotos().size() != 0) {
				if (dynamicObj.getPetPhotos().size() == 1) {
					ImageLoaderHelper.displayImage(dynamicObj.getPetPhotos().get(0), holder.dynamicImage, R.drawable.image_default_image, false);
				} else {
					// getAllBitmapByURL(dynamicObj.getPetPhotos());
					// int width = holder.dynamicImage.getMeasuredWidth();
					// int width
					// =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
					// int height
					// =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
					// holder.dynamicImage.measure(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					int measurewidth = holder.dynamicImage.getWidth();
					LogUtils.i("LayerDrawable MeasuredWidth", measurewidth + "");
					holder.dynamicImage.setImageDrawable(getDrawable(dynamicObj.getPetPhotos(), measurewidth));
				}
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getText())) {
				holder.tvContent.setText(dynamicObj.getText().trim());
			}
			if (!CommonTextUtils.isEmpty(dynamicObj.getCreateTime())) {
				holder.tvTime.setText(CommonUtils.getTimeSamp(mContext, dynamicObj.getCreateTime()));
			}
		}
		return convertView;
	}

	private Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE
					? Bitmap.Config.ARGB_8888
					: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	@SuppressWarnings({"static-access", "deprecation"})
	private Drawable getDrawable(ArrayList<String> urls, int width) {
		int count = urls.size();
		if (count > 4) {
			count = 4;
		}
		int screenWiddth = CommonUtils.getScreenWidth(mContext);
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();

		width = (int) (width / dm.density + 0.5f);// width/2;//66;//screenWiddth
													// * 958 / 10000;
		// width=(int) (width *dm.density/160 )/2;
		Drawable[] array = new Drawable[count];
		LogUtils.i("LayerDrawable count", count + "");
		LogUtils.i("LayerDrawable width", width + "");
		for (int i = 0; i < count; i++) {
			Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.COVER + MD5.md5(urls.get(i)) + FileUtils.JPG);
			// if (count == 2) {
			// array[i] = new BitmapDrawable(resizeImage(bitmap, 4.0f));
			// } else if (count == 3) {
			// if (i == 1) {
			// array[i] = new BitmapDrawable(resizeImage(bitmap, 2.0f));
			// } else {
			// array[i] = new BitmapDrawable(bitmap);
			// }
			// } else if (count == 4) {
			//
			// }
			array[i] = new BitmapDrawable(bitmap);
		}
		LayerDrawable la = new LayerDrawable(array);
		// 其中第一个参数为层的索引号，后面的四个参数分别为left、top、right和bottom

		if (count == 2) {
			la.setLayerInset(0, 0, 0, width, 0);
			la.setLayerInset(1, width, 0, 0, 0);
		} else if (count == 3) {
			la.setLayerInset(0, 0, 0, width, 0);
			la.setLayerInset(1, width, 0, 0, width);
			la.setLayerInset(2, width, width, 0, 0);
		} else if (count == 4) {
			la.setLayerInset(0, 0, 0, width, width);
			la.setLayerInset(1, width, 0, 0, width);
			la.setLayerInset(2, 0, width, width, 0);
			la.setLayerInset(3, width, width, 0, 0);
		}

		return la;
	}

	public Bitmap resizeImage(Bitmap bitmap, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	class ViewHolder {
		ImageView dynamicImage;
		TextView tvContent;
		TextView tvTime;

		public ViewHolder(View view) {
			this.dynamicImage = (ImageView) view.findViewById(R.id.account_dynamic_img);
			this.tvContent = (TextView) view.findViewById(R.id.account_dynamic_text);
			this.tvTime = (TextView) view.findViewById(R.id.account_dynamic_time);
		}
	}

}