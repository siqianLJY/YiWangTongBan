package com.example.litianci.yiwangtongban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
*android 王能适配器（适配普通的ListView）
*@author ZJP
*created at 2016/3/14 14:59
*/
public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected Context mContext;
	protected List<T> mDatas = new ArrayList<>();
	protected LayoutInflater mInflater;
	private int layoutId;
//	public ArrayList<String> strings = new ArrayList<>();
	public int add;
	public CommonAdapter(Context context, List<T> datas, int layoutId)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}



	public void removeData2( int p) {
		this.mDatas.remove(p);
	}
	public void removeData( ) {
		this.mDatas.clear();
	}
	public void setData( List<T> datas) {
		this.mDatas= datas;
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}


	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// current menu type
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);

		convert(holder, getItem(position));

		return holder.getConvertView();
	}

	public void seti(int s){
		add=s;
	}
	public int geti(){
		return add;
	}

	public abstract void convert(ViewHolder holder, T t);

}
