package io.github.quotecc.subtasks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
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
        SharedPreferences s = getContext().getSharedPreferences("index", Context.MODE_PRIVATE);
        int indx = s.getInt("curId", 0);

        if (curId != 0){
            tds.open();
            Task cur = tds.searchTask(curId);
            tds.close();
            getActivity().setTitle(cur.getContent());
        }
        else {
            getActivity().setTitle("Main Tasks");
        }
        items.add(new Task(addT, indx, curId)); //blank task with just the text add a task for use as button



        lv = (ListView) view.findViewById(R.id.mainContent);
        lv.setItemsCanFocus(true);
        //ArrayAdapter<String> adapt = new ArrayAdapter<String>(actMain, android.R.layout.simple_list_item_1, items);
        custAdapt adapt = new custAdapt(items, getContext(), curId);
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

    public class custAdapt extends ArrayAdapter<Task>{
        private LayoutInflater layInf;
        ArrayList<Task> currTasks;
        final Context context;
        Calendar c = Calendar.getInstance();
        int currPos = 0;
        int timeId = 0;
        int parId = 0;

        class ViewHold{
            EditText txtEd;
            TextView txtVw;
            TextView dataStor;
            Button bttn;
            @Override
            public String toString(){
                String s = txtVw.getText().toString() + "     " + dataStor.getText().toString();
                return s;

            }
        }





        public custAdapt(ArrayList<Task> tasks, Context context, int par){
            super(context, R.layout.lv_custom, tasks);
            this.context = context;
            parId = par;
            layInf = LayoutInflater.from(context);
            //layInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currTasks = tasks;
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return currTasks.size();
        }
        @Override
        public Task getItem(int pos){
            return currTasks.get(pos);
        }
        @Override
        public long getItemId(int pos){
            return currTasks.get(pos).getId();
        }
        @Override
        public View getView( int pos, View convertView, ViewGroup parent){
            //Log.d("ITEM ERROR", currTasks.toString() + " \n" + pos);
            Task t = currTasks.get(pos);

            if (convertView == null){
                final ViewHold holder = new ViewHold();
                convertView = layInf.inflate(R.layout.lv_custom,null);
                holder.txtEd = (EditText) convertView.findViewById(R.id.noteEdit);
                holder.dataStor = (TextView) convertView.findViewById(R.id.store); //This is always gone, but stores the id of the current item
                holder.dataStor.setText(currTasks.get(pos).getId() + "");
                holder.txtVw = (TextView) convertView.findViewById(R.id.noteText);
                holder.txtVw.setText(currTasks.get(pos).getContent());



                holder.bttn = (Button) convertView.findViewById(R.id.duePick);

                holder.bttn.setText(currTasks.get(pos).getDueS());
                //Log.d("ITEM ERROR BEFORE", holder.toString());
                setTVListeners(holder);
                setButtListeners(holder);
                setETListeners(holder);


                holder.txtVw.setVisibility(View.VISIBLE);
                if (!currTasks.get(pos).getContent().equals("Add a Task")){
                    if (currTasks.get(pos).getDue() != null){
                        holder.bttn.setText(currTasks.get(pos).getDueS());
                    }
                    else{
                        holder.bttn.setText("Set Due?");
                    }
                }
                else{
                    holder.bttn.setVisibility(View.INVISIBLE);
                }



                convertView.setTag(holder);
            }
            else{
                ViewHold holder = (ViewHold) convertView.getTag();
            }



            return convertView;
        }

        public void setTVListeners(final ViewHold hold){
            //Log.d("ITEM ERROR AFT", hold.toString());
            hold.txtVw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("EVENT", "Short Click");
                    String s = hold.dataStor.getText().toString(); //dataStor has the ID of the current item
                    int id = Integer.parseInt(s);
                    int pos = currTasks.indexOf(findTaskById(id));
                    if (pos == currTasks.size()-1){
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
                        getContext().startActivity(i);

                    }


                }
            });
            hold.txtVw.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String s = hold.dataStor.getText().toString(); //dataStor has the ID of the current item
                    int id = Integer.parseInt(s);
                    int pos = currTasks.indexOf(findTaskById(id));
                    if (pos == currTasks.size()-1){
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
            hold.txtEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String s = hold.dataStor.getText().toString();
                    int id = Integer.parseInt(s);
                    Log.d("TASKS", currTasks.toString());
                    Log.d("IDSSSS", id+"");
                    Task t = findTaskById(id);
                    int pos = currTasks.indexOf(t);
                    String conts = hold.txtEd.getText().toString();

                    if (!conts.equals(t.getContent()) && !conts.equals("")){
                        tds.open();

                        currTasks.get(pos).setContent(conts);
                        hold.txtVw.setText(conts);
                        hold.bttn.setVisibility(View.VISIBLE);
                        if (pos == currTasks.size()-1){

                            SharedPreferences shr  = getContext().getSharedPreferences("index", Context.MODE_PRIVATE);
                            int got = shr.getInt("curId", 0);
                            SharedPreferences.Editor shrE = shr.edit();
                            shrE.putInt("curId", got+1);
                            shrE.apply();

                            Log.d("ParId Standard", parId+"");
                            currTasks.add(new Task(addT, got+1, parId));
                            long l = tds.insertTask(currTasks.get(pos));
                            Log.d("Insert ID", l + "");
                            tds.close();
                        }
                        else { //if it already exists then update, if new created, then insert
                            tds.updateTask(currTasks.get(pos));
                            tds.close();
                        }

                        hold.txtEd.setVisibility(View.GONE);
                        hold.txtVw.setVisibility(View.VISIBLE);


                        notifyDataSetChanged();

                    }


                    return false;
                }
            });
            /*hold.txtEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String s = hold.dataStor.getText().toString();
                    int id = Integer.parseInt(s);
                    int pos = currTasks.indexOf(findTaskById(id));
                    if (!hasFocus){
                        Log.d("FOCUS PROBS", "focus changing instantly");
                        tds.open();
                        String curr = hold.txtEd.getText().toString();
                        if (!curr.equals("")){
                            currTasks.get(pos).setContent(curr);
                            hold.txtVw.setText(curr);
                            hold.bttn.setVisibility(View.VISIBLE);
                            if (pos == currTasks.size()-1){

                                SharedPreferences shr  = getContext().getSharedPreferences("index", Context.MODE_PRIVATE);
                                int got = shr.getInt("curId", 0);
                                SharedPreferences.Editor shrE = shr.edit();
                                shrE.putInt("curId", got+1);
                                shrE.apply();

                                currTasks.add(new Task(addT, got+1));
                                tds.insertTask(currTasks.get(pos));
                                tds.close();
                            }
                            else { //if it already exists then update, if new created, then insert
                                tds.updateTask(currTasks.get(pos));
                                tds.close();
                            }
                        }
                        hold.txtEd.setVisibility(View.GONE);
                        hold.txtVw.setVisibility(View.VISIBLE);


                        notifyDataSetChanged();

                    }
                }
            });*/
        }

        public void setButtListeners(final ViewHold hold){
            String s = hold.dataStor.getText().toString();
            final int id = Integer.parseInt(s);
            int pos = currTasks.indexOf(findTaskById(id));
            final TimePickerDialog.OnTimeSetListener ts = new TimePickerDialog.OnTimeSetListener(){
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);
                    findTaskById(id).setDue(c.getTime());
                    Task t = findTaskById(id);
                    tds.open();
                    tds.updateTask(t);
                    tds.close();
                    hold.bttn.setText(t.getDueS());

                    notifyDataSetChanged();
                }
            };

            final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    new TimePickerDialog(getContext(), ts, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
                }
            };
            hold.bttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = hold.dataStor.getText().toString();
                    int id = Integer.parseInt(s);
                    int pos = currTasks.indexOf(findTaskById(id));

                    new DatePickerDialog(getContext(), d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            hold.bttn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    String s = hold.dataStor.getText().toString();
                    int id = Integer.parseInt(s);
                    Task t = findTaskById(id);
                    Log.d("TaskDelete", t.toString() + "   " + id);
                    int pos = currTasks.indexOf(t);
                    if (pos != currTasks.size()-1) {
                        Log.d("CurTasksB", currTasks.toString());
                        currTasks.remove(pos);
                        Log.d("CurTasksA", currTasks.toString());
                        tds.open();
                        tds.deleteTask(t);
                        tds.close();
                        notifyDataSetChanged();
                    }
                    return true;
                }
            });
        }

        public Task findTaskById(int id){
            for (Task t: currTasks){
                if (t.getId() == id){
                    return t;
                }
            }
            return new Task(addT, -1, parId);

        }




    }


}
