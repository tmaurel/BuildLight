package buildLight

import buildLight.server.ICIServer
import buildLight.BuildConstants.ServerType
import buildLight.server.CIServerFactory

class CIServerService {

    ICIServer server

    def initServer(ServerType server, String serverUrl) {
        this.server = CIServerFactory.newServer server
        this.server.serverUrl = serverUrl
    }

    def setCredentials(String login, String password) {
        this.server.setCredentials(login, password)
    }

    def getLastBuildStatus() {
        this.server.lastBuildStatus
    }
}