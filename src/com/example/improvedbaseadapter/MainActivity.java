package com.example.improvedbaseadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	private class ViewHolder implements UnMixable {

		ListView multiViewType;
		ListView simpleViewType;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewHolder holder = new ViewHolder();
		ViewUtil.injectByFieldAnnotate(holder, getWindow().getDecorView());

		// generate multiViewType datas
		List<Card> datas = new ArrayList<Card>();
		Random r = new Random();
		Card temp;
		for (int i = 0, count = 10; i < count; i++) {
			temp = new Card();
			temp.type = r.nextInt(3);
			datas.add(temp);
		}

		holder.multiViewType.setAdapter(new MultiViewTypeAdapter(this, datas));
		holder.simpleViewType.setAdapter(new SimpleAdapterWithoutViewType(this,
				datas));

	}

}
