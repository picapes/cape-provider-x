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
import net.minecraft.client.MinecraftClient;


public class RumbleCapeProvider implements CapeProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(RumbleCapeProvider.class);
	
	public static final String ID = "rumblecapes";
	
	@Override
	public String id()
	{
		return ID;
	}
	
	@Override
	public String name()
	{
		return "Rumble Capes";
	}
	
	@Override
	public String getBaseUrl(final GameProfile profile)
	{
		return "http://api.rumblecapes.xyz/capes/" + profile.name() + ".png";
	}
	
	@Override
	public boolean hasChangeCapeUrl()
	{
		return true;
	}
	
	@Override
	public String changeCapeUrl(final MinecraftClient client)
	{
	return "http://rumblecapes.xyz/";
	}
	
	@Override
	public String homepageUrl()
	{
		return "http://rumblecapes.xyz/";
	}
}
