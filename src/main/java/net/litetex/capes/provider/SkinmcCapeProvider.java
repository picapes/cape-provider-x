package net.litetex.capes.provider;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.authlib.GameProfile;

import net.litetex.capes.provider.antifeature.AntiFeature;
import net.litetex.capes.provider.antifeature.AntiFeatures;
import net.minecraft.client.Minecraft;


public class SkinmcCapeProvider implements CapeProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(SkinmcCapeProvider.class);
	
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
	public String changeCapeUrl(final Minecraft client)
	{
		return "https://skinmc.net/capes/";
	}
	
	@Override
	public String homepageUrl()
	{
		return "https://skinmc.net/";
	}
}
