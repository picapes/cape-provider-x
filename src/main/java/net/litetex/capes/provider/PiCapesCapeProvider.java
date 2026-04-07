package net.litetex.capes.provider;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;

import net.litetex.capes.handler.textures.AnimatedSpriteTextureResolver;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;


public class PiCapesCapeProvider extends CacheableCapeProvider
{
	private static final Logger LOG = LoggerFactory.getLogger(PiCapesCapeProvider.class);
	
	static
	{
		initializeServerHost();
	}
	
	public static final String ID = "picapesmod";
	
	private static String serverHost = "";
	private static final String modNameDefault = "PiCapes";
	private static String modName = "PiCapes";
	private static boolean initialized = false;
	
	// (PiCapes) API server fetching
	public static void initializeServerHost()
	{
		if(initialized)
		{
			return;
		}
		
		initialized = true;
		
		try
		{
			final HttpClient client = HttpClient.newHttpClient();
			final HttpRequest request = HttpRequest.newBuilder()
				.uri(java.net.URI.create("https://picapes.github.io/api/server.json"))
				.GET()
				.build();
			final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			if(response.statusCode() / 100 == 2)
			{
				final Map<?, ?> json = new Gson().fromJson(response.body(), Map.class);
				
				final Object ip = json.get("serverHost");
				if(ip != null)
				{
					serverHost = ip.toString();
					LOG.info("[PiCapes] Server host found: {}", serverHost);
				}
				else
				{
					LOG.warn("[PiCapes] Server host not found in response JSON.");
				}
				
				final Object nameObj = json.get("name");
				if(nameObj != null && !nameObj.toString().isEmpty())
				{
					modName = nameObj.toString();
					LOG.info("[PiCapes] Mod name set from API: {}", modName);
				}
				else
				{
					LOG.warn("[PiCapes] Mod name not found in response JSON, using fallback.");
				}
			}
			else
			{
				LOG.warn("[PiCapes] Server host API returned status code: {}", response.statusCode());
			}
		}
		catch(final Exception ex)
		{
			LOG.error("[PiCapes] Failed to fetch server host from API.", ex);
			serverHost = null;
			modName = modNameDefault;
		}
	}
	
	@Override
	public String id()
	{
		return ID;
	}
	
	@Override
	public String name()
	{
		return modName != null && !modName.isEmpty() ? modName : modNameDefault;
	}
	
	@Override
	public String getBaseUrl(final GameProfile profile)
	{
		if(serverHost == null || serverHost.isEmpty())
		{
			// Try to initialize again if not set
			initializeServerHost();
			if(serverHost == null || serverHost.isEmpty())
			{
				return null;
			}
		}
		// serverHost contains protocol (http/https), e.g. https://capeserver.picapes.syanic.org
		return serverHost + "/profile/" + profile.name();
	}
	
	@Override
	public ResolvedTextureInfo resolveTexture(
		final HttpClient.Builder clientBuilder,
		final HttpRequest.Builder requestBuilder,
		final GameProfile profile) throws IOException, InterruptedException
	{
		requestBuilder
			.setHeader("User-Agent", "picapes-mod/" + SharedConstants.getCurrentVersion().name());
		
		try(final HttpClient client = clientBuilder.build())
		{
			final HttpResponse<String> response =
				client.send(requestBuilder.GET().build(), HttpResponse.BodyHandlers.ofString());
			
			if(response.statusCode() / 100 != 2)
			{
				return null;
			}
			
			record ResponseData(
				Boolean animatedCape,
				String textureURL
			)
			{
			}
			
			final ResponseData responseData = new Gson().fromJson(response.body(), ResponseData.class);
			if(responseData.textureURL() == null || responseData.textureURL().isEmpty())
			{
				return null;
			}
			
			return this.resolveCacheableTexture(
				responseData.textureURL(),
				clientBuilder,
				requestBuilder,
				responseData.animatedCape() ? AnimatedSpriteTextureResolver.ID : null
			);
		}
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
	public double rateLimitedReqPerSec()
	{
		return 10;
	}
	
	@Override
	public boolean hasChangeCapeUrl()
	{
		return true;
	}
	
	@Override
	public String changeCapeUrl(final Minecraft client)
	{
		return "https://picapes.syanic.org/changeCape";
	}
	
	@Override
	public String homepageUrl()
	{
		return "https://picapes.syanic.org/";
	}
}
