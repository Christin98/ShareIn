package com.project.christinkcdev.share.sharein.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.genonbeta.android.framework.widget.PowerfulActionMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.project.christinkcdev.share.sharein.R;
import com.project.christinkcdev.share.sharein.activity.ConnectionManagerActivity;
import com.project.christinkcdev.share.sharein.adapter.SmartFragmentPagerAdapter;
import com.project.christinkcdev.share.sharein.app.Activity;
import com.project.christinkcdev.share.sharein.app.EditableListFragment;
import com.project.christinkcdev.share.sharein.app.EditableListFragmentImpl;
import com.project.christinkcdev.share.sharein.ui.callback.IconSupport;
import com.project.christinkcdev.share.sharein.ui.callback.SharingActionModeCallback;
import com.project.christinkcdev.share.sharein.ui.callback.TitleSupport;

public class ContentSharingFragment extends com.genonbeta.android.framework.app.Fragment implements com.genonbeta.android.framework.app.FragmentImpl, Activity.OnBackPressedListener, IconSupport, TitleSupport {

    private PowerfulActionMode mMode;
    private SharingActionModeCallback mSelectionCallback;
    private Activity.OnBackPressedListener mBackPressedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_content, container, false);
        mMode = view.findViewById(R.id.content_sharing_action_mode);
        final TabLayout tabLayout = view.findViewById(R.id.content_sharing_tab_layout);
        final ViewPager viewPager = view.findViewById(R.id.content_sharing_view_pager);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);

        mSelectionCallback = new SharingActionModeCallback(null);
        final PowerfulActionMode.SelectorConnection selectorConnection = new PowerfulActionMode.SelectorConnection(mMode, mSelectionCallback);

        final SmartFragmentPagerAdapter pagerAdapter = new SmartFragmentPagerAdapter(getContext(), getChildFragmentManager())
        {
            @Override
            public void onItemInstantiated(StableItem item)
            {
                EditableListFragmentImpl fragmentImpl = (EditableListFragmentImpl) item.getInitiatedItem();
                //EditableListFragmentModelImpl fragmentModelImpl = (EditableListFragmentModelImpl) item.getInitiatedItem();

                fragmentImpl.setSelectionCallback(mSelectionCallback);
                fragmentImpl.setSelectorConnection(selectorConnection);
                //fragmentModelImpl.setLayoutClickListener(groupLayoutClickListener);

                if (viewPager.getCurrentItem() == item.getCurrentPosition())
                    attachListeners(fragmentImpl);
            }
        };

        //mMode.setContainerLayout(findViewById(R.id.activity_content_sharing_action_mode_layout));
        Bundle fileExplorerArgs = new Bundle();
        fileExplorerArgs.putBoolean(FileExplorerFragment.ARG_SELECT_BY_CLICK, true);

        pagerAdapter.add(new SmartFragmentPagerAdapter.StableItem(0, ApplicationListFragment.class, null));
        pagerAdapter.add(new SmartFragmentPagerAdapter.StableItem(1, MusicListFragment.class, null));
        pagerAdapter.add(new SmartFragmentPagerAdapter.StableItem(2, ImageListFragment.class, null));
        pagerAdapter.add(new SmartFragmentPagerAdapter.StableItem(3, VideoListFragment.class, null));
        pagerAdapter.add(new SmartFragmentPagerAdapter.StableItem(4, FileExplorerFragment.class, fileExplorerArgs).setTitle(getString(R.string.text_files)));

        pagerAdapter.createTabs(tabLayout, false, true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());

                final EditableListFragment fragment = (EditableListFragment) pagerAdapter.getItem(tab.getPosition());

                attachListeners(fragment);

                if (fragment.getAdapterImpl() != null)
                    new Handler(Looper.getMainLooper()).postDelayed(() -> fragment.getAdapterImpl().notifyAllSelectionChanges(), 200);

            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getContext(), ConnectionManagerActivity.class)
                .putExtra(ConnectionManagerActivity.EXTRA_ACTIVITY_SUBTITLE, getString(R.string.text_receive))
                .putExtra(ConnectionManagerActivity.EXTRA_REQUEST_TYPE, ConnectionManagerActivity.RequestType.MAKE_ACQUAINTANCE.toString())));

        return view;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        int id = item.getItemId();
//
//        if (id == android.R.id.home)
//            finish();
//        else
//            return super.onOptionsItemSelected(item);
//
//        return true;
//    }

    @Override
    public int getIconRes()
    {
        return R.drawable.ic_share_white_24dp;
    }

    @Override
    public CharSequence getTitle(Context context)
    {
        return context.getString(R.string.butn_share);
    }

    @Override
    public boolean onBackPressed()
    {
        if (mBackPressedListener == null || !mBackPressedListener.onBackPressed()) {
            if (mMode.hasActive(mSelectionCallback))
                mMode.finish(mSelectionCallback);
            else
                return false;
        }
        return false;
    }

    public void attachListeners(EditableListFragmentImpl fragment)
    {
        mSelectionCallback.updateProvider(fragment);
        mBackPressedListener = fragment instanceof Activity.OnBackPressedListener
                ? (Activity.OnBackPressedListener) fragment
                : null;
    }
}
