package buildLight.server

import org.apache.http.protocol.HttpContext
import org.apache.http.client.HttpRequestRetryHandler
import javax.net.ssl.SSLException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CustomHttpRequestRetryHandler implements HttpRequestRetryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpRequestRetryHandler.class)

    def retries = 3;

    boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount > this.retries) {
            return false
        }
        if (exception instanceof UnknownHostException) {
            return false;
        }
        if (exception instanceof ConnectException) {
            return false;
        }
        if (exception instanceof SSLException) {
            return false;
        }

        LOGGER.info("Retrying request (received {})", [exception.getClass()].toArray())

        return true
    }


}
