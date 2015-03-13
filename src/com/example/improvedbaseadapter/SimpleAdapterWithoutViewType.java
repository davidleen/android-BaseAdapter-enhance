package com.example.improvedbaseadapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

/**
 * a sample adapter without multi viewType
 * just ignore the param itemViewType;
 * 
 * @author davidleen29
 * @创建时间 2013年11月14日
 */
public class SimpleAdapterWithoutViewType extends AbstractAdapter<Card> {

	public SimpleAdapterWithoutViewType(Context context) {
		super(context);

	}

	public SimpleAdapterWithoutViewType(Context context, List<Card> cards) {
		super(context, cards);

	}



	@Override
	protected Class<?  > getItemViewHolder(int itemViewType) {
		return   NumberTypeBinder.class;
	}

	/**
	 * number type view binder
	 * 
	 * @author davidleen29
	 * @创建时间 2013年11月14日
	 */
   @ResId(R.layout.list_item_number_type)
	class NumberTypeBinder implements Bindable<Card> {
		//here  use reflect ,
		//make this memeber name with the same name to the viewId defined in the item layout
		// like:   android:id="@+id/cardType"
		// optional choice: use roboguice do the inject work with its annotation.
		@ResId(R.id.cardType)
		TextView cardType;

		@Override
		public void bindData(AbstractAdapter<Card> adapter,Card data, int position) {
			// here do all your bind obj
			cardType.setText(data.toString());
		}

	}

}
