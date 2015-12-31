package mobi.jzcx.android.chongmi.ui.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Mostafa Gazar on 11/2/13.
 */
public class RectangleImageView extends BaseImageView {

    public RectangleImageView(Context context) {
        super(context);
    }

    public RectangleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        canvas.drawRect(new RectF(0.0f, 0.0f, width, height), paint);
        return bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        //return getBitmap(getWidth(), getHeight());
    	 return getRoundCornerImage(getBitmap(getWidth(), getHeight()),20);
    }
    
    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels)  
    {  
        //创建一个和原始图片一样大小位图  
        Bitmap roundConcerImage = Bitmap.createBitmap(bitmap.getWidth(),  
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);  
        //创建带有位图roundConcerImage的画布  
        Canvas canvas = new Canvas(roundConcerImage);  
        //创建画笔  
        Paint paint = new Paint();  
        //创建一个和原始图片一样大小的矩形  
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        RectF rectF = new RectF(rect);  
        // 去锯齿   
        paint.setAntiAlias(true);  
        //画一个和原始图片一样大小的圆角矩形  
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);  
        //设置相交模式  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        //把图片画到矩形去  
        canvas.drawBitmap(bitmap, null, rect, paint);  
        return roundConcerImage;  
    } 
}
