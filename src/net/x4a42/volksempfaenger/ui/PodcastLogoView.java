package net.x4a42.volksempfaenger.ui;

import java.util.HashMap;

import net.x4a42.volksempfaenger.R;
import net.x4a42.volksempfaenger.Utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PodcastLogoView extends ImageView {

	private long podcastId;

	// TODO: there must be a better way to do this...
	// suggestion: weak references to bitmaps
	private static HashMap<Long, Bitmap> logoCache = new HashMap<Long, Bitmap>();

	public PodcastLogoView(Context context) {
		super(context);
	}

	public PodcastLogoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PodcastLogoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setPodcastId(long id) {
		if (id == podcastId) {
			// don't reload the logo
			return;
		}
		podcastId = id;

		Bitmap podcastLogoBitmap = Utils.getPodcastLogoBitmap(getContext(),
				podcastId, logoCache);
		if (podcastLogoBitmap == null) {
			setImageResource(R.drawable.default_logo);
		} else {
			setImageBitmap(podcastLogoBitmap);
		}
	}

	public long getPodcastId() {
		return podcastId;
	}

}
