package il.ac.huji.todolist;

import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListDisplayAdapter extends ArrayAdapter<String> {
	
	public TodoListDisplayAdapter(
			TodoListManagerActivity activity, List<String> todoList) {
		super(activity, android.R.layout.activity_list_item, todoList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String todoTask = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView todoTaskName = (TextView)view.findViewById(R.id.todoTaskName);
		if(position % 2 == 0){
			todoTaskName.setTextColor(Color.RED);		
		}else{
			todoTaskName.setTextColor(Color.BLUE);
		}
		todoTaskName.setText(todoTask);
		return view;
	}

}
