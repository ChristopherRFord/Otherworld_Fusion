package com.fusion.err;

public class EntityLoadingException extends Exception
{
	public EntityLoadingException(String entityLocation, String type)
	{
		super("ERROR LOADING COMPONENT " + type + " AT " + entityLocation);
	}
}
