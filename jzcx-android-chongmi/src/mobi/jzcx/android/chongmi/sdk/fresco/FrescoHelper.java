package mobi.jzcx.android.chongmi.sdk.fresco;

import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

public class FrescoHelper {
	/**
	 * http://fresco-cn.org/docs/index.html
	 */
	/**
	 * 图像选项类
	 * 
	 * @param resources
	 *            Resources
	 * @param isRound
	 *            是否圆角
	 * @param radius
	 *            圆角角度
	 * @return
	 */
	public static GenericDraweeHierarchy getImageViewHierarchy(Resources resources, int defaultid, boolean isRound,
			float radius) {
		GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(resources);
		builder.setFadeDuration(300);
		if (isRound) {
			RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);
			builder.setRoundingParams(roundingParams);
		}
		if (defaultid != 0) {
//			builder.setPlaceholderImage(resources.getDrawable(defaultid));
			builder.setBackground(resources.getDrawable(defaultid));
		}

		return builder.build();
	}

	/**
	 * 图像选项类
	 * 
	 * @param resources
	 *            Resources
	 * @param isCircle
	 *            是否圆圈
	 * @return
	 */
	public static GenericDraweeHierarchy getImageViewHierarchy(Resources resources, int defaultid, boolean isCircle) {
		GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(resources);
		builder.setFadeDuration(300);
		if (isCircle) {
			RoundingParams circleParams = RoundingParams.asCircle();
			builder.setRoundingParams(circleParams);
		}
		if (defaultid != 0) {
			builder.setPlaceholderImage(resources.getDrawable(defaultid));
		}
		return builder.build();
	}

	/**
	 * 图像选项类
	 * 
	 * @param uri
	 *            图片路径
	 * @param oldController
	 *            DraweeView.getoldcontroller
	 * @param controllerListener
	 *            监听
	 * @return
	 */
	public static DraweeController getImageViewController(String uri, DraweeController oldController,
			ControllerListener<ImageInfo> controllerListener) {
		PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
		if (!TextUtils.isEmpty(uri)) {
			builder.setUri(Uri.parse(uri));
		}
		if (oldController != null) {
			builder.setOldController(oldController);
		}
		if (controllerListener != null) {
			builder.setControllerListener(controllerListener);
		}
		return builder.build();
	}

	/**
	 * 图像选项类
	 * 
	 * @param SimpleDraweeView
	 *            draweeView
	 * @param uri
	 *            图片路径
	 * @param controllerListener
	 *            监听
	 * @return
	 */

	public static void displayImageview(SimpleDraweeView draweeView, String uri, int defaultid,
			ControllerListener<ImageInfo> controllerListener, Resources resources, boolean isRound, float radius) {
		draweeView.setHierarchy(getImageViewHierarchy(resources, defaultid, isRound, radius));
		draweeView.setController(getImageViewController(uri, draweeView.getController(), controllerListener));
	}

	/**
	 * 图像选项类
	 * 
	 * @param SimpleDraweeView
	 *            draweeView
	 * @param uri
	 *            图片路径
	 * @param isCircle
	 *            圆形
	 * @return
	 */

	public static void displayImageview(SimpleDraweeView draweeView, String uri, int defaultid, Resources resources,
			boolean isCircle) {
		draweeView.setHierarchy(getImageViewHierarchy(resources, defaultid, isCircle));
		draweeView.setController(getImageViewController(uri, draweeView.getController(), null));
	}

	/**
	 * 图像选项类
	 * 
	 * @param SimpleDraweeView
	 *            draweeView
	 * @param uri
	 *            图片路径
	 * @return
	 */

	public static void displayImageview(SimpleDraweeView draweeView, String uri, int defaultid, Resources resources) {
		displayImageview(draweeView, uri, defaultid, null, resources, false, 0);
	}

	public static void displayImageview(SimpleDraweeView draweeView, String uri, Postprocessor postProcessor) {
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)).setPostprocessor(postProcessor)
				.build();
		PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
				.setImageRequest(request).setOldController(draweeView.getController()).build();
		draweeView.setController(controller);
	}

}
