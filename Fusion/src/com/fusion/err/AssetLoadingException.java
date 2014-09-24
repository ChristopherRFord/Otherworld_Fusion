package com.fusion.err;

public class AssetLoadingException extends Exception
{
	public AssetLoadingException(String AssetLocation, String Type)
	{
		super(Type + " AT " + AssetLocation + " IS NOT A VALID FILE TYPE FOR THIS LOADER");
	}
}
