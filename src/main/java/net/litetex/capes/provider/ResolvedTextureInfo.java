package net.litetex.capes.provider;

import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.Nullable;


public interface ResolvedTextureInfo {
	byte[] imageBytes();

	@Nullable
	String textureResolverId();

	record ByteArrayTextureInfo(byte[] imageBytes, String textureResolverId) implements ResolvedTextureInfo {
		public ByteArrayTextureInfo(final byte[] imageBytes) {
			this(imageBytes, null);
		}
	}

	record Base64TextureInfo(String base64Texture, String textureResolverId) implements ResolvedTextureInfo {
		public Base64TextureInfo(final String base64Texture) {
			this(base64Texture, null);
		}

		@Override
		public byte[] imageBytes() {
			if (this.base64Texture == null || this.base64Texture.isEmpty()) {
				return null;
			}
			try {
				return Base64.decodeBase64(this.base64Texture);
			} catch (final Exception ex) {
				return null;
			}
		}
	}

	// (PiCapes) New record for direct URL textures
	record UrlTextureInfo(String textureURL, String textureResolverId) implements ResolvedTextureInfo {
		public UrlTextureInfo(final String textureURL) {
			this(textureURL, null);
		}

		@Override
		public byte[] imageBytes() {
			// Not used for URL textures, should be handled elsewhere
			return null;
		}
	}
}
