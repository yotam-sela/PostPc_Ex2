package il.ac.huji.todolist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListDisplayAdapter extends ArrayAdapter<TodoHolder> {

	public TodoListDisplayAdapter(
			TodoListManagerActivity activity, List<TodoHolder> todoList) {
		super(activity, android.R.layout.activity_list_item, todoList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoHolder todoTask = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);

		TextView todoName = (TextView)view.findViewById(R.id.txtTodoTitle);
		todoName.setText(todoTask.title);

		TextView todoDate = (TextView)view.findViewById(
				R.id.txtTodoDueDate);
		if(todoTask.dueDate == null)
		{
			todoDate.setText("No due date");
		}
		else
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			todoDate.setText(df.format(todoTask.dueDate));

			Date currentDate = new Date();
			if(currentDate.after(todoTask.dueDate))
			{
				todoDate.setTextColor(Color.RED);
				todoName.setTextColor(Color.RED);
			}
		}
		return view;
	}

}
