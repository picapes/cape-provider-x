package net.litetex.capes.provider;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;


public class SkinMCProvider implements CapeProvider
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
		return "SkinMC Cape";
	}
	
	@Override
	public String getBaseUrl(final GameProfile profile)
	{
		return "https://skinmc.net/api/v1/skinmcCape/" + profile.id().toString(); // https://skinmc.net/api/v1/skinmcCape/<uuid>
	}
	
	@Override
	public boolean hasChangeCapeUrl()
	{
		return true;
	}
	
	@Override
	public String changeCapeUrl(final Minecraft client)
	{
		return "https://skinmc.net/account/capes";
	}
	
	@Override
	public String homepageUrl()
	{
		return "https://skinmc.net/capes";
	}
}
