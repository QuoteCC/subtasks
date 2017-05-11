package io.github.quotecc.subtasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by cCorliss on 4/24/17.
 */

public class MainView extends Fragment {
    Context actMain;

    public ArrayList<Task> items = new ArrayList<Task>();

    ListView lv;
    String addT = "Add a Task";
    int curId;//level of current thing
    TaskDataSource tds;
    String[] because = {"TEST1", "TEST2", "TEST3"}; //old test array

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

        curId = getArguments().getInt("id");

        tds = new TaskDataSource(getContext());
        tds.open();
        items = tds.getSubTasks(curId);
        tds.close();
        items.add(new Task(addT)); //blank task with just the text add a task for use as button



        lv = (ListView) view.findViewById(R.id.mainContent);
        lv.setItemsCanFocus(true);
        //ArrayAdapter<String> adapt = new ArrayAdapter<String>(actMain, android.R.layout.simple_list_item_1, items);
        custAdapt adapt = new custAdapt(items);
        lv.setAdapter(adapt);
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == currTasks.size()-1){ //means that its our dummy task

                }
                else{

                    Intent i = new Intent(getContext(), MainActivity.class);
                    int par = items.get(position).getId();
                    i.putExtra("parent", par);
                }


                //handles short click --> opens full view of task

            }
        });*/
        /*lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //handles long click, the true means that it won't trigger both the short and long click events
                //  --> allows for renaming of task

                return true;
            }
        });*/
        adapt.notifyDataSetChanged();

        return view;

    }

    public class custAdapt extends BaseAdapter{
        private LayoutInflater layInf;

        Calendar c = Calendar.getInstance();
        int currPos = 0;

        TimePickerDialog.OnTimeSetListener ts = new TimePickerDialog.OnTimeSetListener(){
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                items.get(currPos).setDue(c.getTime());
                notifyDataSetChanged();
            }
        };

        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(getContext(), ts, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
            }
        };



        public custAdapt(ArrayList<Task> tasks){
            layInf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            items = tasks;
            notifyDataSetChanged();
        }

        public int getCount(){
            return items.size();
        }

        public Task getItem(int pos){
            return items.get(pos);
        }

        public long getItemId(int pos){
            return items.get(pos).getId();
        }

        public View getView( int pos, View convertView, ViewGroup parent){
            ViewHold holder;
            if (convertView == null){
                holder = new ViewHold();
                convertView = layInf.inflate(R.layout.lv_custom,null);
                holder.txtEd = (EditText) convertView.findViewById(R.id.noteEdit);
                holder.dataStor = (TextView) convertView.findViewById(R.id.store); //This is always gone, but stores the id of the current item
                holder.dataStor.setText(items.get(pos).getId());
                holder.txtVw = (TextView) convertView.findViewById(R.id.lvTxt);
                setTVListeners(holder);

                holder.bttn = (Button) convertView.findViewById(R.id.duePick);
                setButtListeners(holder);
                
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHold) convertView.getTag();
            }
            holder.txtVw.setText(items.get(pos).getContent());
            holder.txtVw.setVisibility(View.VISIBLE);
            if (!items.get(pos).getContent().equals("Add a Task")){
                if (items.get(pos).getDue() != null){
                    holder.bttn.setText(items.get(pos).getDueS());
                }
                else{
                    holder.bttn.setText("Set Due?");
                }
            }
            else{
                holder.bttn.setVisibility(View.INVISIBLE);
            }
            setETListeners(holder);
            return convertView;
        }

        public void setTVListeners(final ViewHold hold){
            hold.txtVw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = hold.dataStor.getText().toString(); //dataStor has the ID of the current item
                    int id = Integer.parseInt(s);
                    int pos = items.indexOf(findTaskById(id));
                    if (id == items.size()-1){
                        hold.txtEd.setVisibility(View.VISIBLE);
                        hold.txtVw.setVisibility(View.GONE);
                        //holder.txtEd.setText(holder.txtVw.getText());
                        hold.txtEd.setText("");
                        if (hold.txtEd.requestFocus()) {
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                    else{

                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.putExtra("parent", id);

                    }


                }
            });
            hold.txtVw.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String s = hold.dataStor.getText().toString(); //dataStor has the ID of the current item
                    int id = Integer.parseInt(s);
                    int pos = items.indexOf(findTaskById(id));
                    if (pos == items.size()-1){
                        return false; //This means that it doesn't consume the onclick event, so would trigger the standard listener
                    }
                    else{
                        hold.txtEd.setVisibility(View.VISIBLE);
                        hold.txtVw.setVisibility(View.GONE);
                        hold.txtEd.setText(hold.txtVw.getText());
                        if (hold.txtEd.requestFocus()) {
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }

                    return true;

                }
            });



        }

        public void setETListeners(final ViewHold hold){
            hold.txtEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String s = hold.dataStor.getText().toString();
                    int id = Integer.parseInt(s);
                    int pos = items.indexOf(findTaskById(id));
                    if (!hasFocus){
                        tds.open();
                        String curr = hold.txtEd.getText().toString();
                        if (curr != ""){
                            items.get(pos).setContent(curr);
                            hold.txtVw.setText(curr);
                            hold.bttn.setVisibility(View.VISIBLE);
                            if (pos == items.size()-1){
                                items.add(new Task(addT));
                                tds.insertTask(items.get(pos));
                                tds.close();
                            }
                            else { //if it already exists then update, if new created, then insert
                                tds.updateTask(items.get(pos));
                                tds.close();
                            }
                        }
                        hold.txtEd.setVisibility(View.GONE);
                        hold.txtVw.setVisibility(View.VISIBLE);


                        notifyDataSetChanged();

                    }
                }
            });
        }

        public void setButtListeners(final ViewHold hold){
            hold.bttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = hold.dataStor.getText().toString();
                    int id = Integer.parseInt(s);
                    int pos = items.indexOf(findTaskById(id));
                    currPos = pos;
                    new DatePickerDialog(getContext(), d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }

        public Task findTaskById(int id){
            for (Task t: items){
                if (t.getId() == id){
                    return t;
                }
            }
            return new Task(addT);

        }




    }
    class ViewHold{
        EditText txtEd;
        TextView txtVw;
        TextView dataStor;
        Button bttn;
    }

}
