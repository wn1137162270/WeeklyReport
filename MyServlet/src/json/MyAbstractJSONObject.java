package json;

import java.util.Date;

public abstract class MyAbstractJSONObject {
	private String code;
	private String msg;
	private long time;
	
	public void setCode(String code)
	{
		this.code=code;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public void setMsg(String msg)
	{
		this.msg=msg;
	}
	
	public String getMsg()
	{
		return this.msg;
	}
	
	public void setTime()
	{
		this.time=new Date().getTime();
	}
	
	public long getTime()
	{
		return this.time;
	}
	
}
