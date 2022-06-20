package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface ViewPagerInterface {
    Fragment getItem(int position);

    int getCount();

    void addFragment(Fragment fragment, String title);

    @Nullable
    CharSequence getPageTitle(int position);
}
