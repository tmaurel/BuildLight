package buildLight.server.hudson

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import org.apache.http.HttpRequest
import org.apache.http.HttpRequestInterceptor
import org.apache.http.protocol.HttpContext
import buildLight.constants.BuildStatus
import buildLight.server.ICIServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class HudsonServer implements ICIServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HudsonServer.class)

    final static String STATUS_SUCCESS = "SUCCESS"
    final static String STATUS_FAILURE = "FAILURE"
    final static String STATUS_UNSTABLE = "UNSTABLE"


    final static String JSON_API_PATH = "lastBuild/api/json"

    HTTPBuilder builder

    public void setServerUrl(String serverUrl) {
        String server = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/"
        this.builder = new HTTPBuilder(server)
    }

    public void setCredentials(String login, String password) {
        this.builder?.client?.addRequestInterceptor(new HttpRequestInterceptor() {
            void process(HttpRequest httpRequest, HttpContext httpContext) {
                httpRequest.addHeader('Authorization', "Basic " + "$login:$password".getBytes('iso-8859-1').encodeBase64().toString())
            }
        })
    }

    public BuildStatus getLastBuildStatus() {
        BuildStatus status = null;
        int retries = 3;
        int i = 0;

        while(i < retries && status == null) {
            LOGGER.info("Trying to retrieve status from Hudson (try # {} on {}}", [i+1, retries].toArray())
            try {
                if (this.builder) {
                    this.builder.get(
                            path: JSON_API_PATH,
                            contentType: ContentType.JSON
                    ) { resp, json ->
                        if (resp.statusLine.statusCode == 200) {
                            status = parseInputStreamForStatus(json)
                        }
                        else {
                            LOGGER.error("Wrong status for Hudson response : {}", [resp.statusLine].toArray())
                        }
                    }
                }
            }
            catch (IOException e) {
                LOGGER.error("IOException while trying to retrieve status from Hudson", e)
            }
            catch (IllegalStateException e) {
                LOGGER.error("IllegalStateException while trying to retrieve status from Hudson", e)
            }
            ++i
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
