package com.fusion.err;

public class AssetLoadingException extends Exception
{
	public AssetLoadingException(String assetLocation, String type)
	{
		super(type + " AT " + assetLocation + " IS NOT A VALID FILE TYPE FOR THIS LOADER");
	}
}
