package javastrava.api.v3.auth.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.TokenService;
import javastrava.api.v3.auth.impl.retrofit.TokenServiceImpl;
import javastrava.api.v3.auth.ref.AuthorisationScope;
import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.service.ActivityService;
import javastrava.api.v3.service.AthleteService;
import javastrava.api.v3.service.ClubService;
import javastrava.api.v3.service.GearService;
import javastrava.api.v3.service.SegmentEffortService;
import javastrava.api.v3.service.SegmentService;
import javastrava.api.v3.service.StravaService;
import javastrava.api.v3.service.StreamService;
import javastrava.api.v3.service.UploadService;
import javastrava.api.v3.service.WebhookService;
import javastrava.api.v3.service.impl.ActivityServiceImpl;
import javastrava.api.v3.service.impl.AthleteServiceImpl;
import javastrava.api.v3.service.impl.ClubServiceImpl;
import javastrava.api.v3.service.impl.GearServiceImpl;
import javastrava.api.v3.service.impl.SegmentEffortServiceImpl;
import javastrava.api.v3.service.impl.SegmentServiceImpl;
import javastrava.api.v3.service.impl.StravaServiceImpl;
import javastrava.api.v3.service.impl.StreamServiceImpl;
import javastrava.api.v3.service.impl.UploadServiceImpl;
import javastrava.api.v3.service.impl.WebhookServiceImpl;

/**
 * <p>
 * The token acts as the bearer of authentication within each request to the Strava API.
 * </p>
 *
 * <p>
 * A access token is used to acquire an implementation of each of the service objects that sub-class {@link StravaServiceImpl}
 * </p>
 *
 * <p>
 * Tokens are acquired through the OAuth process; this implementation of the API does not provide a purely programmatic way to acquire a access token as that would
 * kind of destroy the point(!) - although once a user has given their permission to the application via the OAuth process, you can use
 * {@link AuthorisationService#tokenExchange(Integer, String, String, AuthorisationScope...)} to acquire a access token at that point in the process.
 * </p>
 *
 * <p>
 * The application will now be able to make requests on the user’s behalf using the access_token query string parameter (GET) or POST/PUT body, or the
 * Authorization header. This is done auto-magically by javastrava.
 * </p>
 *
 * <p>
 * Applications should check for a 401 Unauthorized response. Access for those tokens has been revoked by the user.
 * </p>
 *
 * @see <a href="http://strava.github.io/api/v3/oauth/">http://strava.github.io/api/v3/oauth/</a>
 *
 * @author Dan Shannon
 *
 */
public class Token {
	/**
	 * The {@link StravaAthlete athlete} to whom this accessToken is assigned
	 */
	private StravaAthlete athlete;

	/**
	 * The value of the access Token, which is used in requests issued via the API
	 */
	private String accessToken;

	/**
	 * List of {@link AuthorisationScope authorisation scopes} granted for this accessToken
	 */
	private List<AuthorisationScope> scopes;

	/**
	 * List of service implementations associated with this accessToken
	 */
	private HashMap<Class<? extends StravaService>, StravaService> services;

	/**
	 * Token type used in the authorisation header of requests to the Strava API - usually set to "Bearer"
	 */
	private String tokenType;

	/**
	 * The value of the refresh Token, which is used in requests issued via the API
	 */
	private String refreshToken;

	private long expiresAt;

	/**
	 * No-args constructor
	 */
	public Token() {
		super();
	}

	/**
	 * <p>
	 * Default constructor is based on the {@link TokenResponse} structure received from
	 * {@link AuthorisationService#tokenExchange(Integer, String, String, AuthorisationScope...)}
	 * </p>
	 *
	 * @param tokenResponse
	 *            The response as received from {@link AuthorisationService#tokenExchange(Integer, String, String, AuthorisationScope...)}
	 * @param scopes
	 *            The list of authorisation scopes to be associated with the accessToken
	 */
	public Token(final TokenResponse tokenResponse, final AuthorisationScope... scopes) {
		this.athlete = tokenResponse.getAthlete();
		this.accessToken = tokenResponse.getAccessToken();
		this.refreshToken = tokenResponse.getRefreshToken();
		this.expiresAt = tokenResponse.getExpiresAt();
		this.tokenType = tokenResponse.getTokenType();
		this.scopes = Arrays.asList(scopes);
		this.services = new HashMap<Class<? extends StravaService>, StravaService>();

		// Get pre-packed instances of all the services
		this.addService(ActivityService.class, ActivityServiceImpl.instance(this));
		this.addService(AthleteService.class, AthleteServiceImpl.instance(this));
		this.addService(ClubService.class, ClubServiceImpl.instance(this));
		this.addService(GearService.class, GearServiceImpl.instance(this));
		this.addService(SegmentEffortService.class, SegmentEffortServiceImpl.instance(this));
		this.addService(SegmentService.class, SegmentServiceImpl.instance(this));
		this.addService(StreamService.class, StreamServiceImpl.instance(this));
		this.addService(TokenService.class, TokenServiceImpl.instance(this));
		this.addService(UploadService.class, UploadServiceImpl.instance(this));
		this.addService(WebhookService.class, WebhookServiceImpl.instance(this));
	}

	/**
	 * <p>
	 * Adds a service implementation into the Token's store
	 * </p>
	 *
	 * @param class1
	 *            The class of the service implementation
	 * @param service
	 *            The service implementation
	 */
	public void addService(final Class<? extends StravaService> class1, final StravaService service) {
		this.services.put(class1, service);
	}

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
		if (!(obj instanceof Token)) {
			return false;
		}
		final Token other = (Token) obj;
		if (this.athlete == null) {
			if (other.athlete != null) {
				return false;
			}
		} else if (!this.athlete.equals(other.athlete)) {
			return false;
		}
		if (this.scopes == null) {
			if (other.scopes != null) {
				return false;
			}
		} else if (!this.scopes.equals(other.scopes)) {
			return false;
		}
		if (this.services == null) {
			if (other.services != null) {
				return false;
			}
		} else if (!this.services.equals(other.services)) {
			return false;
		}
		if (this.accessToken == null) {
			if (other.accessToken != null) {
				return false;
			}
		} else if (!this.accessToken.equals(other.accessToken)) {
			return false;
		}
		if (this.refreshToken == null) {
			if (other.refreshToken != null) {
				return false;
			}
		} else if (!this.refreshToken.equals(other.refreshToken)) {
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
	 * @return the athlete
	 */
	public StravaAthlete getAthlete() {
		return this.athlete;
	}

	/**
	 * @return the scopes
	 */
	public List<AuthorisationScope> getScopes() {
		return this.scopes;
	}

	/**
	 * <p>
	 * Gets the service implementation of the required class from the accessToken
	 * </p>
	 *
	 * @param <T>
	 *            The class being returned
	 * @param class1
	 *            The class to return
	 * @return The implementation of the service required
	 */
	@SuppressWarnings("unchecked")
	public <T extends StravaService> T getService(final Class<T> class1) {
		return (T) this.services.get(class1);
	}

	/**
	 * @return the services
	 */
	public HashMap<Class<? extends StravaService>, StravaService> getServices() {
		return this.services;
	}

	/**
	 * @return the access Token
	 */
	public String getAccessToken() {
		return this.accessToken;
	}

	/**
	 * @return the refresh token
	 */
	public String getRefreshToken() {
		return this.refreshToken;
	}

	/**
	 * @return seconds since the Epoch when the provided access token will expire
	 */
	public long getExpiresAt() {
		return this.expiresAt;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return this.tokenType;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.athlete == null) ? 0 : this.athlete.hashCode());
		result = (prime * result) + ((this.scopes == null) ? 0 : this.scopes.hashCode());
		result = (prime * result) + ((this.services == null) ? 0 : this.services.hashCode());
		result = (prime * result) + ((this.accessToken == null) ? 0 : this.accessToken.hashCode());
		result = (prime * result) + ((this.refreshToken == null) ? 0 : this.refreshToken.hashCode());
		result = (prime * result) + ((this.tokenType == null) ? 0 : this.tokenType.hashCode());
		return result;
	}

	/**
	 * <p>
	 * Validates that the toke has view private access (according to the scopes that it was granted on creation at least; it is quite possible that permissions
	 * have subsequently been revoked by the user)
	 * </p>
	 *
	 * @return <code>true</code> if the accessToken contains the {@link AuthorisationScope#VIEW_PRIVATE}
	 */
	public boolean hasViewPrivate() {
		if ((this.scopes != null) && this.scopes.contains(AuthorisationScope.VIEW_PRIVATE)) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Validates that the accessToken has write access (according to the scopes that it was granted on creation at least; it is quite possible that permissions have
	 * subsequently been revoked by the user)
	 * </p>
	 *
	 * @return <code>true</code> if the accessToken contains the {@link AuthorisationScope#WRITE}
	 */
	public boolean hasWriteAccess() {
		if ((this.scopes != null) && this.scopes.contains(AuthorisationScope.WRITE)) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Removes the service from the Token's store
	 * </p>
	 *
	 * @param class1
	 *            The class of accessToken to be removed
	 */
	public void removeService(final Class<? extends StravaService> class1) {
		this.services.remove(class1);
	}

	/**
	 * @param athlete
	 *            the athlete to set
	 */
	public void setAthlete(final StravaAthlete athlete) {
		this.athlete = athlete;
	}

	/**
	 * @param scopes
	 *            the scopes to set
	 */
	public void setScopes(final List<AuthorisationScope> scopes) {
		this.scopes = scopes;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(final HashMap<Class<? extends StravaService>, StravaService> services) {
		this.services = services;
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
		this.refreshToken = refreshToken;
	}

	/**
	 * @param expiresAt
	 *            seconds since the Epoch when the provided access token will expire.
	 */
	public void setExpiresAt(final long expiresAt) {
		this.expiresAt = expiresAt;
	}

	/**
	 * @param tokenType
	 *            the tokenType to set
	 */
	public void setTokenType(final String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [athlete=" + this.athlete + ", accessToken=" + this.accessToken + ", refreshToken=" + this.refreshToken +
				LocalDateTime.ofEpochSecond(this.expiresAt, 0, ZoneOffset.UTC) +
				", scopes=" + this.scopes + ", services=" + this.services + ", tokenType=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				+ this.tokenType + "]"; //$NON-NLS-1$
	}

}
