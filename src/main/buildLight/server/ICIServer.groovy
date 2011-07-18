package buildLight.server

import buildLight.BuildConstants.BuildStatus

public interface ICIServer {

    void setServerUrl(String serverUrl);

    void setCredentials(String login, String password);

    BuildStatus getLastBuildStatus();

    class CIServerNotFound extends Exception {}

}