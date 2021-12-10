package org.oewntk.xml.in;

public class CompatException extends Exception
{
	private static final long serialVersionUID = 1091637245877106012L;

	public CompatException(String message)
	{
		super(message);
	}

	public CompatException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CompatException(Throwable cause)
	{
		super(cause);
	}
}
