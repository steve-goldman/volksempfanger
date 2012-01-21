package net.x4a42.volksempfaenger.ui;

import net.x4a42.volksempfaenger.R;
import net.x4a42.volksempfaenger.data.DatabaseHelper;
import net.x4a42.volksempfaenger.data.SubscriptionListAdapter;
import net.x4a42.volksempfaenger.service.UpdateService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SubscriptionGridFragment extends Fragment implements
		OnItemClickListener {

	private static final int CONTEXT_EDIT = 0;
	private static final int CONTEXT_DELETE = 1;

	private DatabaseHelper dbHelper;
	private Cursor cursor;
	private GridView subscriptionList;
	private SubscriptionListAdapter adapter;
	private AdapterView.AdapterContextMenuInfo currentMenuInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.subscription_list, container,
				false);

		dbHelper = DatabaseHelper.getInstance(getActivity());

		subscriptionList = (GridView) view.findViewById(R.id.subscription_list);
		subscriptionList.setEmptyView(view
				.findViewById(R.id.subscription_list_empty));
		subscriptionList.setOnItemClickListener(this);
		subscriptionList.setOnCreateContextMenuListener(this);

		cursor = dbHelper.getReadableDatabase().query(
				DatabaseHelper.ExtendedPodcast._TABLE, null, null, null, null,
				null, DatabaseHelper.ExtendedPodcast.TITLE);
		getActivity().startManagingCursor(cursor);

		adapter = new SubscriptionListAdapter(getActivity(), cursor);
		subscriptionList.setAdapter(adapter);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		cursor.requery();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.subscription_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_add:
			startActivity(new Intent(getActivity(), AddSubscriptionActivity.class));
			return true;
		case R.id.item_update:
			getActivity().startService(new Intent(getActivity(), UpdateService.class));
			Toast.makeText(getActivity(), R.string.message_update_started,
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		TextView podcastTitle = (TextView) info.targetView
				.findViewById(R.id.podcast_title);
		String title = podcastTitle.getText().toString();
		menu.setHeaderTitle(title);

		menu.add(0, CONTEXT_EDIT, 0, R.string.context_edit);
		menu.add(0, CONTEXT_DELETE, 0, R.string.context_delete);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		currentMenuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		Intent intent;
		switch (item.getItemId()) {
		case CONTEXT_EDIT:
			intent = new Intent(getActivity(), EditSubscriptionActivity.class);
			intent.putExtra("id", currentMenuInfo.id);
			startActivity(intent);
			return true;
		case CONTEXT_DELETE:
			intent = new Intent(getActivity(), DeleteSubscriptionActivity.class);
			intent.putExtra("id", currentMenuInfo.id);
			startActivity(intent);
			return true;
		}
		return false;
	}

	public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		Intent intent = new Intent(getActivity(),
				ViewSubscriptionActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}
}