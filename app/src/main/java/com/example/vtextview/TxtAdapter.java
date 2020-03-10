package com.example.vtextview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;



/**
 * Created by kxyu on 2020-02-20
 */
public class TxtAdapter extends BaseAdapter{

	private List<String> mList;
	private LayoutInflater inflater;
	private Context mContext;
	public TxtAdapter(Context context){
		mContext = context;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setList(List<String> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String model = mList.get(position);
		MyHolderView holderView;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_txt, null);
			holderView = new MyHolderView(convertView);
		}else{
			holderView = (MyHolderView) convertView.getTag();
		}
		holderView.model = model;
		holderView.index = position;
		holderView.update();
		return convertView;
	}
	private class MyHolderView extends RecyclerView.ViewHolder {
		String model;
		int index = 0;
		public VerticalTextView vt;
		public MyHolderView( View itemView) {
			super(itemView);
			itemView.setTag(this);
			vt = itemView.findViewById(R.id.pw);
		}
		public void update(){
			vt.setText(model);
		}
	}

}
