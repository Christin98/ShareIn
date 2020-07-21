package com.project.christinkcdev.share.sharein.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.genonbeta.android.framework.widget.PowerfulActionMode;
import com.google.android.material.tabs.TabLayout;
import com.project.christinkcdev.share.sharein.R;
import com.project.christinkcdev.share.sharein.adapter.SmartFragmentPagerAdapter;
import com.project.christinkcdev.share.sharein.app.Activity;
import com.project.christinkcdev.share.sharein.app.EditableListFragment;
import com.project.christinkcdev.share.sharein.app.EditableListFragmentImpl;
import com.project.christinkcdev.share.sharein.fragment.ApplicationListFragment;
import com.project.christinkcdev.share.sharein.fragment.FileExplorerFragment;
import com.project.christinkcdev.share.sharein.fragment.ImageListFragment;
import com.project.christinkcdev.share.sharein.fragment.MusicListFragment;
import com.project.christinkcdev.share.sharein.fragment.VideoListFragment;
import com.project.christinkcdev.share.sharein.ui.callback.SharingActionModeCallback;

public class ContentSharingActivity extends Activity {

    public static final String TAG = ContentSharingActivity.class.getSimpleName();

    private PowerfulActionMode mMode;
    private SharingActionModeCallback mSelectionCallback;
    private Activity.OnBackPressedListener mBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_sharing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        mMode = findViewById(R.id.activity_content_sharing_action_mode);
        final TabLayout tabLayout = findViewById(R.id.activity_content_sharing_tab_layout);
        final ViewPager viewPager = findViewById(R.id.activity_content_sharing_view_pager);

        mSelectionCallback = new SharingActionModeCallback(null);
        final PowerfulActionMode.SelectorConnection selectorConnection = new PowerfulActionMode.SelectorConnection(mMode, mSelectionCallback);

        final SmartFragmentPagerAdapter pagerAdapter = new SmartFragmentPagerAdapter(this, getSupportFragmentManager())
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
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            fragment.getAdapterImpl().notifyAllSelectionChanges();
                        }
                    }, 200);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressedListener == null || !mBackPressedListener.onBackPressed()) {
            if (mMode.hasActive(mSelectionCallback))
                mMode.finish(mSelectionCallback);
            else
                super.onBackPressed();
        }
    }

    public void attachListeners(EditableListFragmentImpl fragment)
    {
        mSelectionCallback.updateProvider(fragment);
        mBackPressedListener = fragment instanceof Activity.OnBackPressedListener
                ? (OnBackPressedListener) fragment
                : null;
    }

}