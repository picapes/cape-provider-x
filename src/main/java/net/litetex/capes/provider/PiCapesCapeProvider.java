package net.litetex.capes.provider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.GameProfile;

import net.litetex.capes.handler.textures.AnimatedSpriteTextureResolver;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;


public class PiCapesCapeProvider extends CacheableCapeProvider
{
	public static final String ID = "picapes";
	private static final String SERVER_INFO_URL = "https://picapes.github.io/api/server.json";
	private static final String DEFAULT_NAME = "PiCapes";
	
	private volatile ServerInfo cachedServerInfo;
	
	@Override
	public String id()
	{
		return ID;
	}
	
	@Override
	public String name()
	{
		final ServerInfo serverInfo = this.getServerInfo();
		if(serverInfo == null || serverInfo.name() == null)
		{
			return DEFAULT_NAME;
		}
		
		return serverInfo.name();
	}
	
	@Override
	public String getBaseUrl(final GameProfile profile)
	{
		final ServerInfo serverInfo = this.getServerInfo();
		if(serverInfo == null || serverInfo.serverHost() == null)
		{
			return null;
		}
		
		return this.joinPath(serverInfo.serverHost(), "/profile/" + profile.name());
	}
	
	@Override
	public ResolvedTextureInfo resolveTexture(
		final HttpClient.Builder clientBuilder,
		final HttpRequest.Builder requestBuilder,
		final GameProfile profile) throws IOException, InterruptedException
	{
		requestBuilder
			.setHeader("User-Agent", "picapes-mod/" + SharedConstants.getCurrentVersion().name());
		
		record ResponseData(
			Boolean animatedCape,
			@SerializedName(value = "textureURL", alternate = "textureUrl")
			String textureURL
		)
		{
		}
		
		final ResponseData responseData;
		try(final HttpClient client = clientBuilder.build())
		{
			final HttpResponse<String> response =
				client.send(
					requestBuilder.copy()
						.setHeader("Accept", "application/json")
						.GET()
						.build(),
					HttpResponse.BodyHandlers.ofString());
			
			if(response.statusCode() < 200 || response.statusCode() >= 300)
			{
				return null;
			}
			
			responseData = new Gson().fromJson(response.body(), ResponseData.class);
		}
		if(responseData == null)
		{
			return null;
		}
		
		final String textureUrl = responseData.textureURL();
		if(textureUrl == null)
		{
			return null;
		}
		
		return this.resolveCacheableTexture(
			textureUrl,
			clientBuilder,
			requestBuilder,
			Boolean.TRUE.equals(responseData.animatedCape()) ? AnimatedSpriteTextureResolver.ID : null);
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
		return "https://catalog.picapes.syanic.org/";
	}
	
	@Override
	public String homepageUrl()
	{
		return "https://picapes.syanic.org/";
	}
	
	private ServerInfo getServerInfo()
	{
		if(this.cachedServerInfo != null)
		{
			return this.cachedServerInfo;
		}
		
		synchronized(this)
		{
			if(this.cachedServerInfo == null)
			{
				this.cachedServerInfo = this.fetchServerInfo();
			}
			return this.cachedServerInfo;
		}
	}
	
	private ServerInfo fetchServerInfo()
	{
		try(final HttpClient client = HttpClient.newHttpClient())
		{
			final HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(SERVER_INFO_URL))
				.setHeader("Accept", "application/json")
				.GET()
				.build();
			
			final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if(response.statusCode() < 200 || response.statusCode() >= 300)
			{
				return null;
			}
			
			return new Gson().fromJson(response.body(), ServerInfo.class);
		}
		catch(final IOException | InterruptedException e)
		{
			return null;
		}
	}
	
	private String joinPath(final String baseUrl, final String path)
	{
		return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) + path : baseUrl + path;
	}
	
	record ServerInfo(
		String name,
		@SerializedName("serverHost")
		String serverHost
	)
	{
	}
}
