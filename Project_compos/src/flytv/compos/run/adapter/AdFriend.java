package flytv.compos.run.adapter;

import in.srain.cube.image.CubeImageView;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import flytv.compos.run.R;
import flytv.compos.run.bean.Circle;
import flytv.compos.run.bean.CircleComment;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.AutoExtListView;
import flytv.ext.view.MyGridView;
import flytv.ext.view.inter.ItemCommentClickLister;
import flytv.run.bean.TVCodeBean;

public class AdFriend extends BaseAdapter {

	private Context context;
	private ArrayList<Circle> items;
	private ViewHolderStudy viewHolderStudy;
	private BitmapUtils imageLoader;
	private ItemCommentClickLister itemCommentClickLister;

	public ItemCommentClickLister getItemCommentClickLister() {
		return itemCommentClickLister;
	}

	public void setItemCommentClickLister(
			ItemCommentClickLister itemCommentClickLister) {
		this.itemCommentClickLister = itemCommentClickLister;
	}

	public AdFriend(Context context, ArrayList<Circle> items,
			BitmapUtils imageLoader) {
		super();
		this.context = context;
		this.items = items;
		this.imageLoader = imageLoader;
	}

	private boolean isUser;

	public boolean isUser() {
		return isUser;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Circle enCircle = items.get(position);
		if (convertView == null) {
			viewHolderStudy = new ViewHolderStudy();
			convertView = View.inflate(context, R.layout.layout_study_item,
					null);
			viewHolderStudy.tx_name = (TextView) convertView
					.findViewById(R.id.tx_name);
			viewHolderStudy.tx_send_time = (TextView) convertView
					.findViewById(R.id.tx_send_time);
			viewHolderStudy.tx_context = (TextView) convertView
					.findViewById(R.id.tx_context);
			viewHolderStudy.tx_delete = (TextView) convertView
					.findViewById(R.id.tx_delete);
			viewHolderStudy.tx_praise_context = (TextView) convertView
					.findViewById(R.id.tx_praise_context);

			viewHolderStudy.img_praise = (ImageView) convertView
					.findViewById(R.id.img_praise);
			viewHolderStudy.img_comment = (ImageView) convertView
					.findViewById(R.id.img_comment);
			viewHolderStudy.im_comment_lint = (ImageView) convertView
					.findViewById(R.id.im_comment_lint);
			viewHolderStudy.im_lint = (ImageView) convertView
					.findViewById(R.id.im_lint);
			viewHolderStudy.image_bg = (CubeImageView) convertView
					.findViewById(R.id.image_bg);
			viewHolderStudy.myGridView = (MyGridView) convertView
					.findViewById(R.id.gv_image_all);
			viewHolderStudy.autoExtListView = (AutoExtListView) convertView
					.findViewById(R.id.listview_comment);
			viewHolderStudy.layout_friend = (LinearLayout) convertView
					.findViewById(R.id.layout_friend);
			viewHolderStudy.layout_praise = (LinearLayout) convertView
					.findViewById(R.id.layout_praise);
			viewHolderStudy.layout_comment = (LinearLayout) convertView
					.findViewById(R.id.layout_comment);

			convertView.setTag(viewHolderStudy);
		} else {
			viewHolderStudy = (ViewHolderStudy) convertView.getTag();
		}
		viewHolderStudy.layout_comment.setVisibility(View.VISIBLE);
		viewHolderStudy.layout_friend.setVisibility(View.VISIBLE);
		viewHolderStudy.tx_name.setText(enCircle.getUserName());
		viewHolderStudy.tx_send_time.setText(enCircle.getCreateTimeStr());
		viewHolderStudy.tx_context.setText(enCircle.getContent());
		boolean isNull = AppUtil.isStrNull(enCircle.getZanNames());
		if (isNull) {
			viewHolderStudy.layout_praise.setVisibility(View.GONE);
		} else {
			viewHolderStudy.layout_praise.setVisibility(View.VISIBLE);
			viewHolderStudy.tx_praise_context.setText(enCircle.getZanNames());
		}
		if (enCircle.getZanStatus() == 1) {
			viewHolderStudy.img_praise
					.setImageResource(R.drawable.im_praise_pre);
		} else {
			viewHolderStudy.img_praise
					.setImageResource(R.drawable.btn_praise_nor);
		}
		TVCodeBean tvCodeBean = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(context);
		if (tvCodeBean.getUserId().equals(enCircle.getUserId())) {
			viewHolderStudy.tx_delete.setVisibility(View.VISIBLE);
		} else {
			viewHolderStudy.tx_delete.setVisibility(View.INVISIBLE);
		}
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.default_logo);
		viewHolderStudy.image_bg.setImageBitmap(bitmap);

		// 图片
		AdFriendImage adFriendImage = new AdFriendImage(context, imageLoader,
				enCircle.fileList);
		viewHolderStudy.myGridView.setAdapter(adFriendImage);
		viewHolderStudy.myGridView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						AlertHelp.startResource(context, AppUtil
								.getHttpImage(enCircle.fileList.get(arg2)
										.getFileUrl()));
					}
				});
		// 列表
		if (enCircle.circleCommentList.size() > 0) {
			AdFriendComment adFriendComment = new AdFriendComment(context,
					enCircle.circleCommentList);
			viewHolderStudy.autoExtListView.setAdapter(adFriendComment);
			viewHolderStudy.im_lint.setVisibility(View.VISIBLE);
			viewHolderStudy.autoExtListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							TVCodeBean tvCodeBean = (TVCodeBean) UserShareUtils
									.getInstance().getLoginInfo(context);
							CircleComment circleComment = enCircle.circleCommentList
									.get(arg2);
							if (tvCodeBean.getUserId().equals(
									circleComment.getUserId())
									&& !AppUtil.isStrNull(circleComment
											.getReplyUserId())
									|| !tvCodeBean.getUserId().equals(
											circleComment.getUserId())) {
								itemCommentClickLister.onBottomComment(
										position, arg2);
							}
						}
					});
		} else {

			viewHolderStudy.im_lint.setVisibility(View.GONE);
			viewHolderStudy.layout_comment.setVisibility(View.GONE);
			if (isNull) {
				viewHolderStudy.layout_friend.setVisibility(View.GONE);
			}
		}
		viewHolderStudy.img_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemCommentClickLister.onBottomComment(position, -1);
			}
		});
		viewHolderStudy.img_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemCommentClickLister != null) {
					itemCommentClickLister.onBottomPraise(position);
				}
			}
		});
		viewHolderStudy.tx_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemCommentClickLister != null) {
					itemCommentClickLister.onBottomDeleLog(position);
				}
			}
		});
		viewHolderStudy.image_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemCommentClickLister != null) {
					if(!isUser){
						itemCommentClickLister.onBottomEditLog(position);
					}
				}

			}
		});
		viewHolderStudy.tx_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemCommentClickLister != null) {
					if(!isUser){
						itemCommentClickLister.onBottomEditLog(position);
					}
				}

			}
		});
		String imageHttp = enCircle.getMyHeader();
		LogUtils.print(1,"imageUlr="+imageHttp);
		if (!AppUtil.isStrNull(imageHttp)) {
			boolean isShow = imageHttp.contains("http://");
			String showImageUrl = null;
			if(!isShow){
				showImageUrl = AppUtil.UPLOADPATH +"/" +imageHttp;
			}else{
				showImageUrl =  imageHttp;
			}
			final String imageUrl = AppUtil.getIPSplit(context,showImageUrl);
			viewHolderStudy.image_bg.setTag(imageUrl);
			imageLoader.configDefaultLoadingImage(bitmap);
			imageLoader.display(viewHolderStudy.image_bg, imageUrl,
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							CubeImageView roundedImageView = (CubeImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(arg2);
							}
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							CubeImageView roundedImageView = (CubeImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(bitmap);
							}
						}
					});
		} else {
			viewHolderStudy.image_bg.setImageBitmap(bitmap);
		}
		return convertView;
	}

	class ViewHolderStudy {
		TextView tx_name, tx_send_time, tx_context, tx_praise_context,
				tx_delete;
		ImageView img_praise, img_comment, im_comment_lint, im_lint;
		CubeImageView image_bg;
		MyGridView myGridView;
		AutoExtListView autoExtListView;
		LinearLayout layout_friend, layout_praise, layout_comment;// friend 讨论/赞
																	// 布局 praise
																	// 赞
	}

}
