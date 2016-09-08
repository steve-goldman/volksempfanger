package net.x4a42.volksempfaenger.data;

import net.x4a42.volksempfaenger.data.Columns.Podcast;
import net.x4a42.volksempfaenger.net.CacheInformation;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

/**
 * CursorWrapper for Cursors of the podcast table with advanced methods for
 * getting specific columns.
 */
public class PodcastCursor extends ExtendedCursorWrapper {

	private int COLUMN_ID;
	private int COLUMN_DESCRIPTION;
	private int COLUMN_FEED;
	private int COLUMN_LAST_UPDATE;
	private int COLUMN_NEW_EPISODES;
	private int COLUMN_LISTENING_EPISODES;
	private int COLUMN_TITLE;
	private int COLUMN_WEBSITE;
	private int COLUMN_HTTP_EXPIRES;
	private int COLUMN_HTTP_LAST_MODIFIED;
	private int COLUMN_HTTP_ETAG;

	/**
	 * Creates a PodcastCursor.
	 * 
	 * @param cursor
	 *            The underlying cursor to wrap.
	 */
	public PodcastCursor(Cursor cursor) {
		super(cursor);
		COLUMN_ID = getColumnIndex(Podcast._ID);
		COLUMN_DESCRIPTION = getColumnIndex(Podcast.DESCRIPTION);
		COLUMN_FEED = getColumnIndex(Podcast.FEED);
		COLUMN_LAST_UPDATE = getColumnIndex(Podcast.LAST_UPDATE);
		COLUMN_NEW_EPISODES = getColumnIndex(Podcast.NEW_EPISODES);
		COLUMN_LISTENING_EPISODES = getColumnIndex(Podcast.LISTENING_EPISODES);
		COLUMN_TITLE = getColumnIndex(Podcast.TITLE);
		COLUMN_WEBSITE = getColumnIndex(Podcast.WEBSITE);
		COLUMN_HTTP_EXPIRES = getColumnIndex(Podcast.HTTP_EXPIRES);
		COLUMN_HTTP_LAST_MODIFIED = getColumnIndex(Podcast.HTTP_LAST_MODIFIED);
		COLUMN_HTTP_ETAG = getColumnIndex(Podcast.HTTP_ETAG);
	}

	/**
	 * @return Podcast Uri.
	 */
	public Uri getUri() {
		return ContentUris.withAppendedId(
				VolksempfaengerContentProvider.PODCAST_URI, getId());
	}

	/**
	 * @return Podcast ID.
	 */
	public long getId() {
		return getLong(COLUMN_ID);
	}

	/**
	 * @return Podcast description.
	 */
	public String getDescription() {
		return getString(COLUMN_DESCRIPTION);
	}

	/**
	 * @return Podcast feed URL as String.
	 */
	public String getFeed() {
		return getString(COLUMN_FEED);
	}

	/**
	 * @return Last time the podcast was updated.
	 */
	public long getLastUpdate() {
		return getLong(COLUMN_LAST_UPDATE);
	}

	/**
	 * @return Number of new episodes.
	 */
	public int getNewEpisodes() {
		return getInt(COLUMN_NEW_EPISODES);
	}

	/**
	 * @return Number of episodes in the 'listening' state.
	 */
	public int getListeningEpisodes() {
		return getInt(COLUMN_LISTENING_EPISODES);
	}

	/**
	 * @return Podcast title.
	 */
	public String getTitle() {
		return getString(COLUMN_TITLE);
	}

	/**
	 * @return true if title is null.
	 */
	public boolean titleIsNull() {
		return isNull(COLUMN_TITLE);
	}

	/**
	 * @return Podcast website URL.
	 */
	public String getWebsite() {
		return getString(COLUMN_WEBSITE);
	}

	/**
	 * @return Podcast website as an Uri object.
	 */
	public Uri getWebsiteUri() {
		return Uri.parse(getWebsite());
	}

	public long getHttpExpires() {
		return getLong(COLUMN_HTTP_EXPIRES);
	}

	public long getHttpLastModified() {
		return getLong(COLUMN_HTTP_LAST_MODIFIED);
	}

	public String getHttpEtag() {
		return getString(COLUMN_HTTP_ETAG);
	}

	public CacheInformation getCacheInformation() {
		CacheInformation cacheInfo = new CacheInformation();
		cacheInfo.expires = getHttpExpires();
		cacheInfo.lastModified = getHttpLastModified();
		cacheInfo.eTag = getHttpEtag();
		return cacheInfo;
	}

}