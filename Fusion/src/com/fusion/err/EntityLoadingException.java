package com.fusion.err;

public class EntityLoadingException extends Exception
{
	public EntityLoadingException(String EntityLocation, String Type)
	{
		super("ERROR LOADING COMPONENT " + Type + " AT " + EntityLocation);
	}
}
