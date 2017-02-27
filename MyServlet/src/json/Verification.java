package json;

public class Verification {
	private String cookie;
	private String verifyCode;
	
	public Verification(String cookie,String verifyCode)
	{
		this.cookie=cookie;
		this.verifyCode=verifyCode;
	}
	
	public void setCookie(String cookie)
	{
		this.cookie=cookie;
	}
	
	public String getCookie()
	{
		return this.cookie;
	}
	
	public void setVerifyCode(String verifyCode)
	{
		this.verifyCode=verifyCode;
	}
	
	public String getVerifyCode()
	{
		return this.verifyCode;
	}
	
}
