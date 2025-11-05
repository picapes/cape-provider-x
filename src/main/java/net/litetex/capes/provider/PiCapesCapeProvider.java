
package net.litetex.capes.provider;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import net.litetex.capes.handler.textures.AnimatedSpriteTextureResolver;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;

import net.litetex.capes.handler.textures.AnimatedSpriteTextureResolver;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;


public class PiCapesCapeProvider implements CapeProvider {
	private static final Logger LOG = LoggerFactory.getLogger(PiCapesCapeProvider.class);
	static {
		initializeServerHost();
	}

	public static final String ID = "picapesmod";
	private static String serverHost = "";
	private static String modNameDefault = "PiCapes";
	private static String modName = "PiCapes";
	private static boolean initialized = false;

	// (PiCapes) API Server Fetching
	// API Server Fetching kept
	public static void initializeServerHost() {
		if (initialized) return;
		initialized = true;
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
				.uri(java.net.URI.create("https://picapes.github.io/api/server.json"))
				.GET()
				.build();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() / 100 == 2) {
				var json = new Gson().fromJson(response.body(), Map.class);
				Object ip = json.get("serverHost");
				if (ip != null) {
					serverHost = ip.toString();
					LOG.info("[PiCapes] Server host found: {}", serverHost);
				} else {
					LOG.warn("[PiCapes] Server host not found in response JSON.");
				}
				Object nameObj = json.get("name");
				if (nameObj != null && !nameObj.toString().isEmpty()) {
					modName = nameObj.toString();
					LOG.info("[PiCapes] Mod name set from API: {}", modName);
				} else {
					LOG.warn("[PiCapes] Mod name not found in response JSON, using fallback.");
				}
			} else {
				LOG.warn("[PiCapes] Server host API returned status code: {}", response.statusCode());
			}
		} catch (Exception e) {
			LOG.error("[PiCapes] Failed to fetch server host from API.", e);
			serverHost = null;
			modName = modNameDefault;
		}
	}
	@Override
	public String id() {
		return ID;
	}

	@Override
	public String name() {
		return modName != null && !modName.isEmpty() ? modName : modNameDefault;
	}

	@Override
	public String getBaseUrl(final GameProfile profile) {
		if (serverHost == null || serverHost.isEmpty()) {
			// Try to initialize again if not set
			initializeServerHost();
			if (serverHost == null || serverHost.isEmpty()) {
				return null;
			}
		}
		return serverHost + "/profile/" + profile.name(); // serverHost contains the protocol (http/https) (e.g. http://server.picapes.syanic.org) 
	}

	@Override
	public ResolvedTextureInfo resolveTexture(
		final HttpClient.Builder clientBuilder,
		final HttpRequest.Builder requestBuilder,
		final GameProfile profile) throws IOException, InterruptedException {
		requestBuilder
			.setHeader("User-Agent", "picapes-mod/" + SharedConstants.getGameVersion().name());

		try (final HttpClient client = clientBuilder.build()) {
			final HttpResponse<String> response =
				client.send(requestBuilder.GET().build(), HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() / 100 != 2) {
				return null;
			}

			record ResponseData(
				Boolean animatedCape,
				String textureURL
			) {}

			final ResponseData responseData = new Gson().fromJson(response.body(), ResponseData.class);
			if (responseData.textureURL() == null || responseData.textureURL().isEmpty()) {
				return null;
			}
			return new ResolvedTextureInfo.UrlTextureInfo(
				responseData.textureURL(),
				responseData.animatedCape() ? AnimatedSpriteTextureResolver.ID : null
			);
		}
	}

	@Override
	public double rateLimitedReqPerSec()
	{
		return 10;
	}

	@Override
	public boolean hasChangeCapeUrl() {
		return true;
	}

	@Override
	public String changeCapeUrl(final MinecraftClient client) {
		return "https://picapes.syanic.org/changeCape";
	}

	@Override
	public String homepageUrl() {
		return "https://picapes.syanic.org/";
	}
}
