package com.example.improvedbaseadapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

/**
 * a sample adapter with multi viewType
 * 
 * @author davidleen29
 * @创建时间 2013年11月14日
 */
public class MultiViewTypeAdapter extends AbstractAdapter<Card> {

	public MultiViewTypeAdapter(Context context) {
		super(context);

	}
	
	public MultiViewTypeAdapter(Context context,List<Card> cards) {
		super(context,cards);

	}

	@Override
	protected int getItemViewLayout(int itemViewType) {

		switch (itemViewType) {
		case 0:
			return R.layout.list_item_number_type;

		default:
			return R.layout.list_item_member_type;
		}
	}

	@Override
	protected Bindable<Card> getItemViewHolder(int itemViewType) {
		switch (itemViewType) {
		case 0:
			return new NumberTypeBinder();

		default:
			return new MemberTypeBinder();
		}

	}

	/**
	 * get viewType of the position
	 * depend on your bussiness;
	 */
	@Override
	public int getItemViewType(int position) {

		Card card = getItem(position);
		return card.type == Card.NUMBER_TYPE ? 0 : 1;

	}

	/**
	 * view type count here 2 Card.NUMBER_TYPE Card.NUMBER_TYPE
	 */
	@Override
	public int getViewTypeCount() {
		//
		return 2;
	}

	/**
	 * number type view binder
	 * 
	 * @author davidleen29
	 * @创建时间 2013年11月14日
	 */
	class NumberTypeBinder implements Bindable<Card> {
		TextView cardType;

		@Override
		public void bindData(Card data, int position) {
			cardType.setText(data.toString());
		}

	}

	/**
	 * number type view binder
	 * 
	 * @author davidleen29
	 * @创建时间 2013年11月14日
	 */
	class MemberTypeBinder implements Bindable<Card> {
		TextView cardType;

		@Override
		public void bindData(Card data, int position) {
			cardType.setText(data.toString());
		}

	}

}
