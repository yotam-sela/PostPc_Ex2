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

public class TodoListDisplayAdapter extends ArrayAdapter<ITodoItem> {

	public TodoListDisplayAdapter(
			TodoListManagerActivity activity, List<ITodoItem> todoList) {
		super(activity, android.R.layout.activity_list_item, todoList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ITodoItem todoTask = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);

		TextView todoName = (TextView)view.findViewById(R.id.txtTodoTitle);
		todoName.setText(todoTask.getTitle());

		TextView todoDate = (TextView)view.findViewById(
				R.id.txtTodoDueDate);
		if(todoTask.getDueDate() == null)
		{
			todoDate.setText("No due date");
		}
		else
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			todoDate.setText(df.format(todoTask.getDueDate()));

			Date currentDate = new Date();
			if(currentDate.after(todoTask.getDueDate()))
			{
				todoDate.setTextColor(Color.RED);
				todoName.setTextColor(Color.RED);
			}
		}
		return view;
	}

}
