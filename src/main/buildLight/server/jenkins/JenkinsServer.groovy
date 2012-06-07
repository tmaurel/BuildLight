package buildLight.server.jenkins

import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseException
import java.util.concurrent.TimeUnit
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler
import org.apache.http.protocol.HttpContext

import java.util.concurrent.ExecutionException
import org.apache.http.client.HttpRequestRetryHandler

import buildLight.constants.BuildStatus
import buildLight.server.CustomHttpRequestRetryHandler
import buildLight.server.ICIServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class JenkinsServer implements ICIServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsServer.class)

    final static String STATUS_SUCCESS = "SUCCESS"
    final static String STATUS_FAILURE = "FAILURE"
    final static String STATUS_UNSTABLE = "UNSTABLE"


    final static String JSON_API_PATH = "lastBuild/api/json"

    AsyncHTTPBuilder builder

    public void setServerUrl(String serverUrl) {
        String server = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/"
        this.builder = new AsyncHTTPBuilder(
            poolSize : 4,
            uri : server,
            contentType : ContentType.JSON,
            timeout : 5000
        )
    }

    public void setCredentials(String login, String password) {
        this.builder?.client?.addRequestInterceptor(new HttpRequestInterceptor() {
            void process(HttpRequest httpRequest, HttpContext httpContext) {
                httpRequest.addHeader('Authorization', "Basic " + "$login:$password".getBytes('iso-8859-1').encodeBase64().toString())
            }
        })
    }

    public BuildStatus getLastBuildStatus(int retries) {

        BuildStatus status = null
        LOGGER.info("Trying to retrieve status from Hudson")
        try {
            if (this.builder) {
                this.builder.client.httpRequestRetryHandler = new CustomHttpRequestRetryHandler(retries: retries)

                def result = this.builder.get(path: JSON_API_PATH) { resp, json ->
                    if (resp.status == 200) {
                        return parseInputStreamForStatus(json)
                    }
                    else {
                        LOGGER.error("Wrong status for Hudson response : {}", [resp.statusLine].toArray())
                    }
                    return null
                }

                if(result) {
                    while( ! result.done ) {
                        Thread.sleep(500)
                    }
                    status = result.get()
                }
            }
        }
		catch (ExecutionException e) {
            LOGGER.error("ExecutionException when trying to retrieve status from Hudson : {}", [e]);
        }

        if (status == null) {
            serverNotFound()
        }
        status
    }

    private serverNotFound() {
        throw new ICIServer.CIServerNotFound()
    }

    private parseInputStreamForStatus(json) {
        def status = BuildStatus.UNKNOWN
        if (json?.building) {
            status = BuildStatus.BUILDING
        }
        else {
            switch (json?.result) {
                case STATUS_SUCCESS:
                    status = BuildStatus.SUCCESS
                    break
                case STATUS_FAILURE:
                    status = BuildStatus.FAILURE
                    break
                case STATUS_UNSTABLE:
                    status = BuildStatus.UNSTABLE
            }
        }
        status
    }
}
