package javastrava.api.v3.auth.model;

import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.rest.API;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <p>
 * The TokenResponse is returned by authorisation services; it contains user details and the access token which is then used for authentication purposes for all
 * other Strava API access
 * </p>
 *
 * @author Dan Shannon
 */
public class TokenResponse {
	/**
	 * The value of the access token
	 */
	private String accessToken;

	/**
	 * The type of token (usually "Bearer" - is used to create the authentication request header - see {@link API#instance(Class, Token)}
	 */
	private String tokenType;

	/**
	 * Strava returns details of the athlete along with the access token
	 */
	private StravaAthlete athlete;

	private String refreshToken;

	private long expiresAt;

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TokenResponse)) {
			return false;
		}
		final TokenResponse other = (TokenResponse) obj;
		if (this.accessToken == null) {
			if (other.accessToken != null) {
				return false;
			}
		} else if (!this.accessToken.equals(other.accessToken)) {
			return false;
		}
		if (this.refreshToken == null) {
			if (other.refreshToken!= null) {
				return false;
			}
		} else if (!this.refreshToken.equals(other.refreshToken)) {
			return false;
		}
		if (this.athlete == null) {
			if (other.athlete != null) {
				return false;
			}
		} else if (!this.athlete.equals(other.athlete)) {
			return false;
		}
		if (this.tokenType == null) {
			if (other.tokenType != null) {
				return false;
			}
		} else if (!this.tokenType.equals(other.tokenType)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return this.accessToken;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return this.refreshToken;
	}

	/**
	 * @return the athlete
	 */
	public StravaAthlete getAthlete() {
		return this.athlete;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return this.tokenType;
	}

	/**
	 * @return seconds since the Epoch when the provided access token will expire
	 */
	public long getExpiresAt() {
		return this.expiresAt;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.accessToken == null) ? 0 : this.accessToken.hashCode());
		result = (prime * result) + ((this.refreshToken == null) ? 0 : this.refreshToken.hashCode());
		result = (prime * result) + ((this.athlete == null) ? 0 : this.athlete.hashCode());
		result = (prime * result) + ((this.tokenType == null) ? 0 : this.tokenType.hashCode());
		return result;
	}

	/**
	 * @param accessToken
	 *            the accessToken to set
	 */
	public void setAccessToken(final String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @param refreshToken
	 *            the refresh token to set
	 */
	public void setRefreshToken(final String refreshToken) {
		this.refreshToken= refreshToken;
	}

	/**
	 * @param athlete
	 *            the athlete to set
	 */
	public void setAthlete(final StravaAthlete athlete) {
		this.athlete = athlete;
	}

	/**
	 * @param tokenType
	 *            the tokenType to set
	 */
	public void setTokenType(final String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @param expiresAt
	 *            seconds since the Epoch when the provided access token will expire.
	 */
	public void setExpiresAt(final long expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TokenResponse [accessToken=" + this.accessToken + ", refreshToken=" + this.refreshToken + ", expires=" +
				LocalDateTime.ofEpochSecond(this.expiresAt, 0, ZoneOffset.UTC) +
				", tokenType=" + this.tokenType + ", athlete=" + this.athlete + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}
