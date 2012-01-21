package net.x4a42.volksempfaenger.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.x4a42.volksempfaenger.data.Columns.Episode;
import net.x4a42.volksempfaenger.data.Columns.Podcast;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteQueryBuilder;

public class QueryHelper {

	private static final String PODCAST_TABLE = DatabaseHelper.TABLE_PODCAST;
	private static final String EPISODE_TABLE = DatabaseHelper.TABLE_EPISODE;
	private static final String PODCAST_WHERE_ID = PODCAST_TABLE + "."
			+ Podcast._ID + "=?";
	private static final String EPISODE_WHERE_ID = EPISODE_TABLE + "."
			+ Episode._ID + "=?";
	private static final String EPISODE_JOIN_PODCAST = "INNER JOIN podcast ON episode.podcast_id = podcast._id";
	private static final String EPISODE_JOIN_ENCLOSURE = "LEFT OUTER JOIN enclosure ON episode.enclosure_id = enclosure._id";
	private static final Map<String, String> podcastColumnMap;
	private static final Map<String, String> episodeColumnMap;
	private static final Map<String, String> enclosureColumnMap;

	static {
		Map<String, String> temp;

		// podcastColumnMap
		temp = new HashMap<String, String>();
		temp.put(Podcast._ID, "_id AS " + Podcast._ID);
		temp.put(Podcast.TITLE, "title AS " + Podcast.TITLE);
		temp.put(Podcast.DESCRIPTION, "description AS " + Podcast.DESCRIPTION);
		temp.put(Podcast.FEED, "feed AS " + Podcast.FEED);
		temp.put(Podcast.WEBSITE, "website AS " + Podcast.WEBSITE);
		temp.put(Podcast.LAST_UPDATE, "last_update AS " + Podcast.LAST_UPDATE);
		temp.put(Podcast.NEW_EPISODES,
				"(SELECT COUNT(*) FROM episode WHERE episode.podcast_id = podcast._id "
						+ "AND status IN (0,1,2)) AS " + Podcast.NEW_EPISODES);
		podcastColumnMap = Collections.unmodifiableMap(temp);

		// episodeColumnMap
		temp = new HashMap<String, String>();
		temp.put(Episode._ID, "episode._id AS " + Episode._ID);
		temp.put(Episode.DATE, "episode.date AS " + Episode.DATE);
		temp.put(Episode.DESCRIPTION, "episode.description AS "
				+ Episode.DESCRIPTION);
		temp.put(Episode.DOWNLOAD_ID, "episode.download_id AS "
				+ Episode.DOWNLOAD_ID);
		temp.put(Episode.DURATION_LISTENED, "episode.duration_listened AS "
				+ Episode.DURATION_LISTENED);
		temp.put(Episode.DURATION_TOTAL, "episode.duration_total AS "
				+ Episode.DURATION_TOTAL);
		temp.put(Episode.ENCLOSURE_ID, "episode.encosure_id AS "
				+ Episode.ENCLOSURE_ID);
		temp.put(Episode.ENCLOSURE_MIME, "enclosure.mime AS "
				+ Episode.ENCLOSURE_MIME);
		temp.put(Episode.ENCLOSURE_SIZE, "enclosure.size AS "
				+ Episode.ENCLOSURE_SIZE);
		temp.put(Episode.ENCLOSURE_TITLE, "enclosure.title AS "
				+ Episode.ENCLOSURE_TITLE);
		temp.put(Episode.ENCLOSURE_URL, "enclosure.url AS "
				+ Episode.ENCLOSURE_URL);
		temp.put(Episode.FEED_ITEM_ID, "episode.feed_item_id AS "
				+ Episode.FEED_ITEM_ID);
		temp.put(Episode.PODCAST_DESCRIPTION, "podcast.description AS "
				+ Episode.PODCAST_DESCRIPTION);
		temp.put(Episode.PODCAST_FEED, "podcast.feed AS "
				+ Episode.PODCAST_FEED);
		temp.put(Episode.PODCAST_ID, "episode.podcast_id AS "
				+ Episode.PODCAST_ID);
		temp.put(Episode.PODCAST_TITLE, "podcast.title AS "
				+ Episode.PODCAST_TITLE);
		temp.put(Episode.PODCAST_WEBSITE, "podcast.website AS "
				+ Episode.PODCAST_WEBSITE);
		temp.put(Episode.STATUS, "episode.status AS " + Episode.STATUS);
		temp.put(Episode.TITLE, "episode.title AS " + Episode.TITLE);
		temp.put(Episode.URL, "episode.url AS " + Episode.URL);
		// temp.put(Episode.DOWNLOAD_DONE, value);
		// temp.put(Episode.DOWNLOAD_FILE, value);
		// temp.put(Episode.DOWNLOAD_PROGRESS, value);
		// temp.put(Episode.DOWNLOAD_STATUS, value);
		// temp.put(Episode.DOWNLOAD_TOTAL, value);
		episodeColumnMap = Collections.unmodifiableMap(temp);

		// enclosureColumnMap
		temp = new HashMap<String, String>();
		// TODO
		enclosureColumnMap = Collections.unmodifiableMap(temp);
	}

	private DatabaseHelper dbHelper;
	private DownloadManager dlManager;
	private SQLiteQueryBuilder podcastQueryBuilder;
	private SQLiteQueryBuilder episodeQueryBuilder;
	private SQLiteQueryBuilder enclosureQueryBuilder;

	protected QueryHelper(DatabaseHelper dbHelper, DownloadManager dlManager) {
		this.dbHelper = dbHelper;
		this.dlManager = dlManager;

		podcastQueryBuilder = new SQLiteQueryBuilder();
		podcastQueryBuilder.setTables(DatabaseHelper.TABLE_PODCAST);
		podcastQueryBuilder.setProjectionMap(podcastColumnMap);

		episodeQueryBuilder = new SQLiteQueryBuilder();
		episodeQueryBuilder.setProjectionMap(episodeColumnMap);
	}

	public Cursor queryPodcastDir(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return podcastQueryBuilder.query(dbHelper.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
	}

	public Cursor queryPodcastItem(long podcastId, String[] projection) {
		return queryPodcastDir(projection, PODCAST_WHERE_ID,
				new String[] { String.valueOf(podcastId) }, null);
	}

	public Cursor queryEpisodeDir(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		boolean joinPodcast = false;
		boolean joinEnclosure = false;
		boolean joinDownload = false;

		int removeCols = 0;
		for (String col : projection) {
			if (!joinPodcast
					&& (Episode.PODCAST_DESCRIPTION.equals(col)
							|| Episode.PODCAST_FEED.equals(col)
							|| Episode.PODCAST_TITLE.equals(col) || Episode.PODCAST_WEBSITE
								.equals(col))) {
				joinPodcast = true;
			} else if (!joinEnclosure
					&& (Episode.ENCLOSURE_MIME.equals(col)
							|| Episode.ENCLOSURE_SIZE.equals(col)
							|| Episode.ENCLOSURE_TITLE.equals(col) || Episode.ENCLOSURE_URL
								.equals(col))) {
				joinEnclosure = true;
			} else if (Episode.DOWNLOAD_DONE.equals(col)
					|| Episode.DOWNLOAD_FILE.equals(col)
					|| Episode.DOWNLOAD_STATUS.equals(col)
					|| Episode.DOWNLOAD_TOTAL.equals(col)) {
				joinDownload = true;
				removeCols++;
			}
		}
		// remove all DOWNLOAD_* colums except DOWNLOAD_ID because we cannot
		// query them from the database
		if (removeCols > 0) {
			String[] temp = new String[projection.length - removeCols];
			int i = 0;
			for (String col : projection) {
				if (!(Episode.DOWNLOAD_DONE.equals(col)
						|| Episode.DOWNLOAD_FILE.equals(col)
						|| Episode.DOWNLOAD_STATUS.equals(col) || Episode.DOWNLOAD_TOTAL
							.equals(col))) {
					temp[i++] = col;
				}
			}
			projection = temp;
		}

		Cursor cursor;

		// TODO: do this without synchronization
		synchronized (episodeQueryBuilder) {
			if (joinPodcast && joinEnclosure) {
				episodeQueryBuilder.setTables(DatabaseHelper.TABLE_EPISODE
						+ " " + EPISODE_JOIN_PODCAST + " "
						+ EPISODE_JOIN_ENCLOSURE);
			} else if (joinPodcast) {
				episodeQueryBuilder.setTables(DatabaseHelper.TABLE_EPISODE
						+ " " + EPISODE_JOIN_PODCAST);
			} else if (joinEnclosure) {
				episodeQueryBuilder.setTables(DatabaseHelper.TABLE_EPISODE
						+ " " + EPISODE_JOIN_ENCLOSURE);
			} else {
				episodeQueryBuilder.setTables(DatabaseHelper.TABLE_EPISODE);
			}

			episodeQueryBuilder.buildQuery(projection, selection,
					selectionArgs, null, null, sortOrder, null);

			cursor = episodeQueryBuilder
					.query(dbHelper.getReadableDatabase(), projection,
							selection, selectionArgs, null, null, sortOrder);
		}

		if (joinDownload) {
			Cursor dbCursor = cursor;
			Cursor dlCursor;
			cursor = null;
			Query query = new DownloadManager.Query();
			long[] ids = new long[dbCursor.getCount()];
			int downloadIdColumn = dbCursor.getColumnIndex(Episode.DOWNLOAD_ID);
			while (dbCursor.moveToNext()) {
				ids[dbCursor.getPosition()] = dbCursor
						.getLong(downloadIdColumn);
			}
			query.setFilterById(ids);
			dlCursor = dlManager.query(query);
			cursor = new EpisodeWithDownloadCursor(dbCursor, dlCursor);
		}

		return cursor;
	}

	public Cursor queryEpisodeItem(long episodeId, String[] projection) {
		return queryEpisodeDir(projection, EPISODE_WHERE_ID,
				new String[] { String.valueOf(episodeId) }, null);
	}

}
