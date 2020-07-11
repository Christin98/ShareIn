package com.project.christinkcdev.share.shareindia.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.genonbeta.android.framework.ui.callback.SnackbarSupport;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.christinkcdev.share.shareindia.R;
import com.project.christinkcdev.share.shareindia.adapter.SmartFragmentPagerAdapter;
import com.project.christinkcdev.share.shareindia.app.Activity;
import com.project.christinkcdev.share.shareindia.ui.callback.TitleSupport;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class HomeFragment extends com.genonbeta.android.framework.app.Fragment  implements TitleSupport, SnackbarSupport, com.genonbeta.android.framework.app.FragmentImpl, Activity.OnBackPressedListener {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public HomeFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    private ViewPager mViewPager;
    private SmartFragmentPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        final BottomNavigationView bottomNavigationView = view.findViewById(R.id.layout_home_bottom_navigation_view);
        mViewPager = view.findViewById(R.id.layout_home_view_pager);
        mAdapter = new SmartFragmentPagerAdapter(getContext(), getChildFragmentManager());

        mAdapter.add(new SmartFragmentPagerAdapter.StableItem(0, TransferGroupListFragment.class, null));
        mAdapter.add(new SmartFragmentPagerAdapter.StableItem(1, FileExplorerFragment.class, null));
//        mAdapter.add(new SmartFragmentPagerAdapter.StableItem(2, TextStreamListFragment.class, null));

        mAdapter.createTabs(bottomNavigationView);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
                bottomNavigationView.setSelectedItemId(i);
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                mViewPager.setCurrentItem(menuItem.getOrder());
                return true;
            }
        });

        return view;
    }

    @Override
    public CharSequence getTitle(Context context)
    {
        return context.getString(R.string.text_home);
    }

    @Override
    public boolean onBackPressed()
    {
        Object activeItem = mAdapter.getItem(mViewPager.getCurrentItem());

        if ((activeItem instanceof Activity.OnBackPressedListener
                && ((Activity.OnBackPressedListener) activeItem).onBackPressed()))
            return true;

        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(0, true);
            return true;
        }

        return false;
    }

}