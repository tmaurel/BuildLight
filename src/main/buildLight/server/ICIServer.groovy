package buildLight.server

import buildLight.constants.BuildStatus

public interface ICIServer {

    void setServerUrl(String serverUrl)

    void setCredentials(String login, String password)

    BuildStatus getLastBuildStatus()

    class CIServerNotFound extends Exception {}

}