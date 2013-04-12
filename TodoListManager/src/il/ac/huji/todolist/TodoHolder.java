package il.ac.huji.todolist;

import java.util.Date;

public class TodoHolder implements ITodoItem{
	
	private String title;
	private Date dueDate;
	
	public TodoHolder(String title, java.util.Date dueDate) {
		super();
		this.title = title;
		this.dueDate = dueDate;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}
	
	

}
