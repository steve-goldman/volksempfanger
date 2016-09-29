package net.x4a42.volksempfaenger.ui.subscriptiongrid;

import android.content.Context;

import net.x4a42.volksempfaenger.ui.episodelist.EpisodeListActivityIntentProvider;
import net.x4a42.volksempfaenger.ui.episodelist.EpisodeListActivityIntentProviderBuilder;

class GridManagerBuilder
{
    public GridManager build(Context context)
    {
        GridViewManager gridViewManager
                = new GridViewManagerBuilder().build(context);

        GridAdapterProxy gridAdapterProxy
                = new GridAdapterProxyBuilder().build(context, gridViewManager);

        EpisodeListActivityIntentProvider intentProvider
                = new EpisodeListActivityIntentProviderBuilder().build(context);

        return new GridManager(context, gridAdapterProxy, gridViewManager, intentProvider);
    }
}