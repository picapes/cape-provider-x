package net.litetex.capes.provider;

import java.net.URI;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;


public class SkinmcCapeProvider implements CapeProvider
{
	public static final String ID = "skinmc";
	
	@Override
	public String id()
	{
		return ID;
	}
	
	@Override
	public String name()
	{
		return "SkinMC";
	}
	
	@Override
	public String getBaseUrl(final GameProfile profile)
	{
		return "https://skinmc.net/api/v1/skinmcCape/" + profile.id().toString();
	}
	
	@Override
	public boolean hasChangeCapeUrl()
	{
		return true;
	}
	
	@Override
	public URI changeCapeUri(final Minecraft client)
	{
		return URI.create("https://skinmc.net/capes/");
	}
	
	@Override
	public URI homepageUri()
	{
		return URI.create("https://skinmc.net/");
	}
}
