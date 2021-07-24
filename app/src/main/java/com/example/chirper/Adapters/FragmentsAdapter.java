package com.example.chirper.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chirper.Fragments.CallsFragment;
import com.example.chirper.Fragments.ChatsFragment;
import com.example.chirper.Fragments.FriendsFragment;
import com.example.chirper.Fragments.PeopleFragment;

import org.jetbrains.annotations.NotNull;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new ChatsFragment();
            case 1:
                return new FriendsFragment();
            case 2:
                return new PeopleFragment();
            case 3:
                return new CallsFragment();
            default:
                return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;

        if (position == 0) {
            title = "CHATS";
        }
        if (position == 1) {
            title = "FRIENDS";
        }
        if (position == 2) {
            title = "PEOPLE";
        }
        if (position == 3) {
            title = "CALLS";
        }


        return title;
    }
}
