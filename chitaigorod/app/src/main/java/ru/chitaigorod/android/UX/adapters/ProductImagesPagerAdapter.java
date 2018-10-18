package ru.chitaigorod.android.UX.adapters;


import android.content.*;
import android.support.annotation.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.*;
import java.util.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.custom_view.*;


/**
 * Simple images pager adapter. Uses {@link bf.io.openshop.views.TouchImageView} for zooming single images.
 */
public class ProductImagesPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> images;

    /**
     * Creates an adapter for viewing product images.
     *
     * @param context activity context.
     * @param images  list of product images.
     */
    public ProductImagesPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TouchImageView imgDisplay;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.fullscreen_image);

        Picasso.get().load(images.get(position))
			.fit().centerInside()
			.placeholder(R.drawable.placeholder_loading)
			.error(R.drawable.placeholder_error)
			.into(imgDisplay);

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
	}
	
