package io.github.quotecc.subtasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by cCorliss on 4/24/17.
 */

public class PagerThing extends FragmentPagerAdapter {
    Context context;
    int curId;

    public PagerThing(FragmentManager fm, Context context, int curId){
        super(fm);
        this.context = context;
        this.curId = curId;
    }

    @Override
    public int getCount(){ //3 fragments
        return 3;
    }

    @Override
    public Fragment getItem(int index){

        Bundle args = new Bundle();
        args.putInt("page_position", index +1);
        args.putInt("id", curId);


        switch (index){

            case 0: //full task view LISTVIEW
                MainView mv = new MainView();
                mv.setArguments(args);
                return mv;
            case 1: //View by date LISTVIEW
                MainView mv1 = new MainView();
                mv1.setArguments(args);
                return mv1;
            case 2: //Notes TEXTVIEW (but has onclick to hide textview and show edit text)
                MainView mv2 = new MainView();
                mv2.setArguments(args);
                return mv2;

        }

        return null;

    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Main 1";
            case 1:
                return "Main 2";
            case 2:
                return "Main 3";
        }
        return null;
    }


}

