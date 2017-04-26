package io.github.quotecc.subtasks;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by cCorliss on 4/24/17.
 */

public class PagerThing extends FragmentPagerAdapter {

    public PagerThing(FragmentManager fm){
        super(fm);
    }

    @Override
    public int getCount(){ //3 fragments
        return 3;
    }

    @Override
    public Fragment getItem(int index){

        switch (index){
            case 0: //full task view LISTVIEW
                return new MainView();
            case 1: //View by date LISTVIEW
                return new MainView();
            case 2: //Notes TEXTVIEW (but has onclick to hide textview and show edit text)
                return new MainView();

        }

        return null;

    }


}

