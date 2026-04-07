package net.litetex.capes.provider;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;


public class SkinMCProvider extends CacheableCapeProvider
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
	public ResolvedTextureInfo resolveTexture(
		final HttpClient.Builder clientBuilder,
		final HttpRequest.Builder requestBuilder,
		final GameProfile profile) throws IOException, InterruptedException
	{
		return this.resolveCacheableTexture(
			this.getBaseUrl(profile),
			clientBuilder,
			requestBuilder,
			this.textureResolverId());
	}
	
	@Override
	protected ResolvedTextureInfo.ByteArrayTextureInfo fetchTexture(
		final HttpClient.Builder clientBuilder,
		final HttpRequest.Builder requestBuilder,
		final String textureResolverId) throws IOException, InterruptedException
	{
		return CapeProvider.resolveTextureDefault(clientBuilder, requestBuilder, textureResolverId);
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
