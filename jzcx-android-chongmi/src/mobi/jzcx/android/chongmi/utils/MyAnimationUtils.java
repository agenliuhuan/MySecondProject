package mobi.jzcx.android.chongmi.utils;

import mobi.jzcx.android.chongmi.R;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class MyAnimationUtils {

	public static void showTopLayout(final View rootview, final View v, final Context context) {
		Animation top2bottomanim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_top_in);
		top2bottomanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				rootview.setVisibility(View.VISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {

			}
		});
		rootview.startAnimation(top2bottomanim);
	}

	public static void hideTopLayout(final View rootview, final View v, final Context context) {
		Animation bottom2topanim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_top_out);
		bottom2topanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				rootview.setVisibility(View.INVISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {

			}
		});
		rootview.startAnimation(bottom2topanim);
	}

	public static void showMessageTopLayout(final View rootview, final View v, final Context context) {
		Animation top2bottomanim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_top_in);
		top2bottomanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				rootview.setVisibility(View.VISIBLE);
			}
		});
		v.startAnimation(top2bottomanim);
	}

	public static void hideMessageTopLayout(final View rootview, final View v, final Context context) {
		Animation bottom2topanim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_bottom_in);
		bottom2topanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				rootview.setVisibility(View.INVISIBLE);
			}
		});
		v.startAnimation(bottom2topanim);
	}

	public static void hideBottomView(final View rootview, final View v, final Context context) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_bottom_out);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
				Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.view_alpha_out);
				anim2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						rootview.setVisibility(View.GONE);

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				rootview.startAnimation(anim2);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

		v.startAnimation(anim);

	}

	public static void showBottomView(final View view, final Context context) {
		Animation top2bottomanim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_bottom_in);
		top2bottomanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}
		});
		view.startAnimation(top2bottomanim);
	}

	public static void hideBottomView(final View view, final Context context) {
		Animation top2bottomanim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_bottom_out);
		top2bottomanim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.INVISIBLE);
			}
		});
		view.startAnimation(top2bottomanim);
	}

	public static void showBottomView(final View rootview, final View v, Context context) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_bottom_in);
		v.startAnimation(anim);
		v.setVisibility(View.VISIBLE);

		Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.view_alpha_in);
		rootview.startAnimation(anim2);
		rootview.setVisibility(View.VISIBLE);

	}

	public static void hideRightView(final View rootview, final View v, Context context) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_right_out);
		v.startAnimation(anim);
		v.setVisibility(View.GONE);

		Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.view_alpha_out);
		rootview.startAnimation(anim2);
		rootview.setVisibility(View.GONE);
	}

	public static void showRightView(final View rootview, final View v, Context context) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_between_interface_right_in);
		v.startAnimation(anim);
		v.setVisibility(View.VISIBLE);

		Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.view_alpha_in);
		rootview.startAnimation(anim2);
		rootview.setVisibility(View.VISIBLE);

	}

}
