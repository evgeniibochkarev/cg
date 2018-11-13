package ru.chitaigorod.android.UX.adapters;

import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import org.json.*;
import ru.chitaigorod.android.*;

public class ProductPropRecyclerAdapter extends RecyclerView.Adapter<ProductPropRecyclerAdapter.ViewHolder>
{

	@Override
	public ProductPropRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		// TODO: Implement this method
		if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(p1.getContext());

        View view = layoutInflater.inflate(R.layout.list_prop_info_recycler_card, p1, false);
        return new ViewHolder(view);

	}
	

	@Override
	public void onBindViewHolder(ProductPropRecyclerAdapter.ViewHolder p1, int p2)
	{
		try
		{
			JSONObject item = (JSONObject) prop.get(p2);
			
			p1.productPropTitle.setText(item.getString("title"));
			p1.productPropValue.setText(item.getString("value"));
		}
		catch (JSONException e)
		{}
	}
	
	private JSONArray prop = new JSONArray();
	private LayoutInflater layoutInflater;

    
	public ProductPropRecyclerAdapter(JSONArray prop){
		this.prop = prop;
		notifyDataSetChanged();
	}
	

	@Override
	public int getItemCount()
	{
		// TODO: Implement this method
		return prop.length();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productPropTitle;
		TextView productPropValue;
        int position;

        public ViewHolder(View v) {
            super(v);
            productPropTitle = (TextView) v.findViewById(R.id.list_prop_info_recycler_title);
			productPropValue = (TextView) v.findViewById(R.id.list_prop_info_recycler_value);
			
        }

        public void setPosition(int position) {
            this.position = position;
        }

    }
	
	
}
