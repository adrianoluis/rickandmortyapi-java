package com.rickandmortyapi;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.rickandmortyapi.util.Jsons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class RestClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

	private HttpsURLConnection httpClient;

	private String method;

	private String url;

	private Map<String, Object> parameters;

	private String getUserAgent() {
		final Package pkg = getClass().getPackage();

		String title = pkg.getImplementationTitle();
		if (Strings.isNullOrEmpty(title)) {
			title = "rickandmortyapi-java";
		}

		String version = pkg.getImplementationVersion();
		if (Strings.isNullOrEmpty(version)) {
			version = "DEV";
		}

		return String.format("%s/%s (%s; %s/%s)", title, version,
				System.getProperty("java.vm.vendor", "Generic"),
				System.getProperty("java.vm.name", "Java"),
				System.getProperty("java.version", "1.0"));
	}

	public RestClient(final String method, final String url) throws ApiException {
		this(method, url, null, null);
	}

	public RestClient(final String method, final String url, Map<String, Object> parameters) throws ApiException {
		this(method, url, parameters, null);
	}

	@SuppressWarnings("unchecked")
	public RestClient(final String method, final String url, Map<String, Object> parameters,
					  Map<String, String> headers) throws ApiException {
		this.method = method.toUpperCase();
		this.url = url;
		this.parameters = parameters;

		if (null == headers) {
			headers = new HashMap<>();
		}

		if (null == this.parameters) {
			this.parameters = new HashMap<>();
		}

		headers.put("User-Agent", getUserAgent());
		headers.put("Accept", "application/json");

		if (Strings.isNullOrEmpty(url)) {
			throw new ApiException("You must set the URL to make a request.");
		}

		if (!Strings.isNullOrEmpty(method)) {

			try {
				final UriBuilder builder = UriBuilder.fromPath(this.url);

				if (method.equalsIgnoreCase(HttpMethod.GET)) {

					for (Map.Entry<String, Object> entry : this.parameters.entrySet()) {
						builder.queryParam(entry.getKey(), entry.getValue());
					}

				}

				httpClient = (HttpsURLConnection) builder
						.build(this)
						.toURL()
						.openConnection();
				httpClient.setRequestMethod(this.method);
				httpClient.setDoInput(true);
				httpClient.setDoOutput(false);

				if (headers.size() > 0) {

					for (Map.Entry<String, String> entry : headers.entrySet()) {
						httpClient.addRequestProperty(entry.getKey(), entry.getValue());
					}

				}

			} catch (Exception e) {
				throw ApiException.buildWithError(e);
			}
		}
	}

	public ApiResponse execute() throws ApiException {
		int responseCode = -1;
		String response = null;

		try {
			LOGGER.trace("{} {}", httpClient.getRequestMethod(), httpClient.getURL().toString());

			if (method.equalsIgnoreCase(HttpMethod.POST) ||
					method.equalsIgnoreCase(HttpMethod.PUT) ||
					method.equalsIgnoreCase(HttpMethod.DELETE)) {
				httpClient.setDoOutput(true);

				if (parameters.size() > 1) {
					final String payload = Jsons.provider().toJson(parameters);
					final byte[] rawPayload = payload.getBytes();
					httpClient.addRequestProperty("Content-Type", "application/json");

					traceRequest(payload);

					final OutputStream os = httpClient.getOutputStream();
					os.write(rawPayload);
					os.flush();
				}

			} else {
				traceRequest();
			}

			InputStream is;
			try {
				is = httpClient.getInputStream();
				responseCode = httpClient.getResponseCode();
			} catch (IOException e) {
				is = httpClient.getErrorStream();
				responseCode = httpClient.getResponseCode();
			}

			// @see http://web.archive.org/web/20140531042945/https://weblogs.java
			// .net/blog/pat/archive/2004/10/stupid_scanner_1.html
			final Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
			response = s.hasNext() ? s.next() : "";

			traceResponse(response);

			httpClient.disconnect();

			return new ApiResponse(responseCode, Jsons.provider().fromJson(response, JsonElement.class));
		} catch (Exception e) {
			if (e instanceof JsonSyntaxException) {
				throw new ApiException(responseCode, url, method, response);
			}
			throw ApiException.buildWithError(e);
		}
	}

	private void traceRequest() {
		traceRequest(null);
	}

	private void traceRequest(String payload) {
		final StringBuilder traceLog = new StringBuilder("\nRequest Headers:\n");

		extractHeaders(traceLog, httpClient.getRequestProperties().entrySet().iterator());

		if (!Strings.isNullOrEmpty(payload))
			traceLog.append("Payload:\n  ")
					.append(payload).append("\n");
		LOGGER.trace(traceLog.toString());
	}

	private void extractHeaders(StringBuilder traceLog, Iterator<Map.Entry<String, List<String>>> i) {
		while (i.hasNext()) {
			final Map.Entry<String, List<String>> entry = i.next();
			traceLog.append("  ");

			if (!Strings.isNullOrEmpty(entry.getKey()))
				traceLog.append(entry.getKey())
						.append(": ");

			if (entry.getValue().size() == 1) {
				traceLog.append(entry.getValue().get(0)).append("\n");
			} else {
				traceLog.append(entry.getValue()).append("\n");
			}
		}
	}

	private void traceResponse(String body) {
		final StringBuilder traceLog = new StringBuilder("\nResponse Headers:\n");

		extractHeaders(traceLog, httpClient.getHeaderFields().entrySet().iterator());

		if (!Strings.isNullOrEmpty(body))
			traceLog.append("Body:\n  ")
					.append(body).append("\n");
		LOGGER.trace(traceLog.toString());
	}
}
