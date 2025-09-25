package net.litetex.capes.provider;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;


public class SkinMCProvider implements CapeProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(SkinMCProvider.class);
	
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
		return "https://skinmc.net/api/v1/skinmcCape/" + profile.getId().toString(); // https://skinmc.net/api/v1/skinmcCape/<uuid>
	}
	
	@Override
	public boolean hasChangeCapeUrl()
	{
		return true;
	}
	
	@Override
	public String changeCapeUrl(final MinecraftClient client)
	{
		return "https://skinmc.net/account/capes";
	}
	
	@Override
	public String homepageUrl()
	{
		return "https://skinmc.net/capes";
	}
}
