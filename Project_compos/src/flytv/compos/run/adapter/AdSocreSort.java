package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import flytv.compos.run.R;
import flytv.compos.run.bean.ScoreSrotBean;
import flytv.ext.utils.AppUtil;
import flytv.ext.view.RoundedImageView;

public class AdSocreSort extends BaseAdapter {

	private Context context;
	private ArrayList<ScoreSrotBean> items;
	private ViewHolderScore viewHolderScore;
	private BitmapUtils bitmapUtils;

	public AdSocreSort(Context context, ArrayList<ScoreSrotBean> items,
			BitmapUtils bitmapUtils) {
		super();
		this.context = context;
		this.items = items;
		this.bitmapUtils = bitmapUtils;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ScoreSrotBean entity = items.get(position);

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.layout_score_item,
					null);
			viewHolderScore = new ViewHolderScore();
			viewHolderScore.image_sort = (ImageView) convertView
					.findViewById(R.id.image_top);
			viewHolderScore.image_user = (RoundedImageView) convertView
					.findViewById(R.id.imageView_icon);
			viewHolderScore.text_name = (TextView) convertView
					.findViewById(R.id.tx_compos_name);
			viewHolderScore.text_score = (TextView) convertView
					.findViewById(R.id.tx_compos_score);
			viewHolderScore.text_school_name = (TextView) convertView
					.findViewById(R.id.tx_compos_school_name);
			viewHolderScore.text_class = (TextView) convertView
					.findViewById(R.id.tx_compos_class_name);
			viewHolderScore.tx_sort = (TextView) convertView
					.findViewById(R.id.tx_sort);
			
			convertView.setTag(viewHolderScore);
		} else {
			viewHolderScore = (ViewHolderScore) convertView.getTag();
		}
		viewHolderScore.text_school_name.setText(entity.getSchoolName());
		viewHolderScore.image_sort.setVisibility(View.VISIBLE);
		viewHolderScore.tx_sort.setVisibility(View.GONE);
		switch (position) {
		case 0:
			viewHolderScore.image_sort.setImageResource(R.drawable.icon_sort_1);
			break;
		case 1:
			viewHolderScore.image_sort.setImageResource(R.drawable.icon_sort_2);
			break;
		case 2:
			viewHolderScore.image_sort.setImageResource(R.drawable.icon_sort_3);
			break;
		default:
			viewHolderScore.image_sort.setVisibility(View.GONE);
			viewHolderScore.tx_sort.setText(""+(position+1));
			viewHolderScore.tx_sort.setVisibility(View.VISIBLE);
			break;
		}
		viewHolderScore.text_name.setText(entity.getUserName());
		viewHolderScore.text_score.setText(context
				.getString(R.string.main_text_compos_user_score)
				+ entity.getAllScore());
		viewHolderScore.text_class.setText(entity.getBclassName());
		String imageHttp = entity.getMyHeader();
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.default_logo);
		if (!AppUtil.isStrNull(imageHttp)) {
			boolean isShow = imageHttp.contains("http");
			String showImageUrl = null;
			if (!isShow) {
				showImageUrl = AppUtil.UPLOADPATH + "/" + imageHttp;
			} else {
				showImageUrl = imageHttp;
			}
			final String imageUrl = AppUtil.getIPSplit(context, showImageUrl);
			viewHolderScore.image_user.setTag(imageUrl);
			bitmapUtils.configDefaultLoadingImage(bitmap);
			bitmapUtils.display(viewHolderScore.image_user, imageUrl,
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							RoundedImageView roundedImageView = (RoundedImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(arg2);
							}
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							RoundedImageView roundedImageView = (RoundedImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(bitmap);
							}
						}
					});
		} else {
			viewHolderScore.image_user.setImageBitmap(bitmap);
		}
		return convertView;
	}

	class ViewHolderScore {
		TextView text_name, text_score, text_school_name, text_class,tx_sort;
		ImageView image_sort;
		RoundedImageView image_user;
	}

}
