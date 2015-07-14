package org.itrgroup.itr.adapter;

import java.util.List;

import org.itrgroup.itr.R;
import org.itrgroup.itr.entity.CommentPub;
import org.itrgroup.itr.utils.CircleImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter{
	
	private Context context;
	private List<CommentPub> commentList;
	
	public CommentAdapter(Context c,List<CommentPub> l){
		context = c; 
		commentList = l; 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rootView = LayoutInflater.from(context).inflate(
                R.layout.item_comment, null);
		CircleImageView com_user_avatar = (CircleImageView) rootView.findViewById(R.id.user_avatar);
		TextView com_user_name = (TextView) rootView.findViewById(R.id.user_name);
		TextView com_time = (TextView) rootView.findViewById(R.id.com_time);
		TextView com_content = (TextView) rootView.findViewById(R.id.com_content);
		CommentPub temp_comment = (CommentPub) getItem(position);
		com_user_name.setText(temp_comment.getCom_user_name());
		com_time.setText(temp_comment.getCom_time());
		com_content.setText(temp_comment.getCom_content());
		com_user_avatar.setImageDrawable(byteToDrawable(temp_comment.getCom_user_avatar_encoding()));
		return rootView;
	}
	
	private Drawable byteToDrawable(String avatar) {    
        
        byte[] img=Base64.decode(avatar.getBytes(), Base64.DEFAULT);  
        Bitmap bitmap;    
        if (img != null) {
            bitmap = BitmapFactory.decodeByteArray(img,0, img.length);    
            @SuppressWarnings("deprecation")  
            Drawable drawable = new BitmapDrawable(bitmap);    
            return drawable;    
        }    
        return null;    
    
    }  

}
