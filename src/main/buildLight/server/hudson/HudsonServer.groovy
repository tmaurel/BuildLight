package buildLight.server.hudson

import groovyx.net.http.HTTPBuilder
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext
import buildLight.constants.BuildStatus
import buildLight.server.ICIServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler
import java.util.concurrent.TimeUnit

public class HudsonServer implements ICIServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HudsonServer.class)

    final static String STATUS_SUCCESS = "SUCCESS"
    final static String STATUS_FAILURE = "FAILURE"
    final static String STATUS_UNSTABLE = "UNSTABLE"


    final static String JSON_API_PATH = "lastBuild/api/json"

    HTTPBuilder builder

    public void setServerUrl(String serverUrl) {
        String server = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/"
        this.builder = new RESTClient(server)
    }

    public void setCredentials(String login, String password) {
        this.builder?.client?.addRequestInterceptor(new HttpRequestInterceptor() {
            void process(HttpRequest httpRequest, HttpContext httpContext) {
                httpRequest.addHeader('Authorization', "Basic " + "$login:$password".getBytes('iso-8859-1').encodeBase64().toString())
            }
        })
    }

    public BuildStatus getLastBuildStatus(int retries) {
        BuildStatus status = null;

        LOGGER.info("Trying to retrieve status from Hudson")
        try {
            if (this.builder) {
                this.builder.client.httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(retries, false)

                def resp = this.builder.get(path: JSON_API_PATH)
                if (resp.status == 200) {
                    status = parseInputStreamForStatus(resp.data)
                }
                else {
                    LOGGER.error("Wrong status for Hudson response : {}", [resp.statusLine].toArray())
                }

                this.builder.client?.connectionManager?.closeIdleConnections(1, TimeUnit.MILLISECONDS)
                this.builder.client?.connectionManager?.closeExpiredConnections()
            }
        } catch (HttpResponseException e) {
            LOGGER.error("HttpResponseException when trying to retrieve status from Hudson : {}", [e.response.data]);
        }
        catch (IOException e) {
            LOGGER.error("IOException while trying to retrieve status from Hudson", e)
        }
        catch (IllegalStateException e) {
            LOGGER.error("IllegalStateException while trying to retrieve status from Hudson", e)
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
