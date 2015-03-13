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
	protected Class<?  > getItemViewHolder(int itemViewType) {

		return	holderTypes[itemViewType];
	}

	private Class<?>[] holderTypes=new Class<?>[]{NumberTypeBinder.class,MemberTypeBinder.class} ;
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
		return holderTypes.length;
	}

	/**
	 * number type view binder
	 * 
	 * @author davidleen29
	 * @创建时间 2013年11月14日
	 */
	@ResId(R.layout.list_item_number_type)
	class NumberTypeBinder implements Bindable<Card> {

		@ResId(R.id.cardType)
		TextView cardType;

		@Override
		public void bindData(AbstractAdapter<Card> adapter,Card data, int position) {
			cardType.setText(data.toString());
		}

	}

	/**
	 * number type view binder
	 * 
	 * @author davidleen29
	 * @创建时间 2013年11月14日
	 */
	@ResId(R.layout.list_item_member_type)
	class MemberTypeBinder implements Bindable<Card> {
		@ResId(R.id.cardType)
		TextView cardType;

		@Override
		public void bindData(AbstractAdapter<Card> adapter,Card data, int position) {
			cardType.setText(data.toString());
		}

	}

}
