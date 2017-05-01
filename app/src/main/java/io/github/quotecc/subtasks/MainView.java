package io.github.quotecc.subtasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by cCorliss on 4/24/17.
 */

public class MainView extends Fragment {
    Context actMain;
    ListView lv;
    String[] because = {"TEST1", "TEST2", "TEST3"};

    @Override
    public void onAttach(Context act){
        super.onAttach(act);
        actMain = act;

    }
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.main_view, container, false); //the null stands in for the layout file

        lv = (ListView) view.findViewById(R.id.mainContent);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(actMain, android.R.layout.simple_list_item_1, because);
        lv.setAdapter(adapt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //handles short click --> opens full view of task

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //handles long click, the true means that it won't trigger both the short and long click events
                //  --> allows for renaming of task

                return true;
            }
        });

        return view;

    }

}
