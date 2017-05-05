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

    ListView lv;
    String addT = "Add a Task";
    int curId;
    TaskDataSource tds;
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

        curId = getArguments().getInt("id");

        tds = new TaskDataSource(getContext());
        tds.open();
        final List<Task> currTasks = tds.getAllTasks(curId);
        currTasks.add(new Task(addT)); //blank task with just the text add a task for use as button



        lv = (ListView) view.findViewById(R.id.mainContent);
        lv.setItemsCanFocus(true);
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(actMain, android.R.layout.simple_list_item_1, because);
        lv.setAdapter(adapt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == currTasks.size()-1){ //means that its our dummy task

                }
                else{

                }


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

    public class custAdapt extends BaseAdapter{
        private LayoutInflater layInf;
        public ArrayList<Task> items = new ArrayList<Task>();
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

        public View getView(final int pos, View convertView, ViewGroup parent){
            final ViewHold holder;

            /*final TimePickerDialog.OnTimeSetListener ts = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);

                }
            };

            final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    new TimePickerDialog(getContext(), ts, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

                }
            };
            */


            if (convertView == null){
                holder = new ViewHold();
                convertView = layInf.inflate(R.layout.lv_custom,null);
                holder.txtEd = (EditText) convertView.findViewById(R.id.noteEdit);
                holder.txtVw = (TextView) convertView.findViewById(R.id.lvTxt);
                holder.bttn = (Button) convertView.findViewById(R.id.duePick);
                holder.bttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        currPos = pos;
                        new DatePickerDialog(getContext(), d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHold) convertView.getTag();
            }
            holder.txtVw.setText(items.get(pos).getContent());
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
            holder.txtEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        final int position = v.getId();
                        final EditText capt = (EditText) v;
                        items.get(position).setContent(capt.getText().toString());
                        holder.bttn.setVisibility(View.VISIBLE);
                        holder.txtVw.setVisibility(View.VISIBLE);
                        holder.txtVw.setText(items.get(position).getContent());
                        holder.txtEd.setVisibility(View.GONE);

                    }
                }
            });





            return convertView;
        }



    }
    class ViewHold{
        EditText txtEd;
        TextView txtVw;
        Button bttn;
    }

}
