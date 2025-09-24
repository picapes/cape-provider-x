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



public class PiCapesCapeProvider implements CapeProvider {
	static {
		initializeServerIP();
	}

	public static final String ID = "picapesmod";
	private static String serverIP = "";
	private static boolean initialized = false;

	// (PiCapes) Called on mod initialization
	public static void initializeServerIP() {
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
				Object ip = json.get("serverIP");
				if (ip != null) serverIP = ip.toString();
			}
		} catch (Exception e) {
			// fallback or log error
			serverIP = ""; //put smthing here
		}
	}

	@Override
	public String id() {
		return ID;
	}

	@Override
	public String name() {
		return "PiCapes BETA";
	}

	@Override
	public String getBaseUrl(final GameProfile profile) {
		if (serverIP == null || serverIP.isEmpty()) {
			return ""; //put smthing here
		}
		return "http://" + serverIP + "/profile/" + profile.getName();
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
	public boolean hasChangeCapeUrl() {
		return true;
	}

	@Override
	public String changeCapeUrl(final MinecraftClient client) {
		return this.homepageUrl();
	}

	@Override
	public String homepageUrl() {
		return "https://picapes.github.io/";
	}
}
