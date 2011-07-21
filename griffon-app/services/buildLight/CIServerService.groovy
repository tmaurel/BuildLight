package buildLight

import buildLight.constants.ServerType
import buildLight.server.CIServerFactory
import buildLight.server.ICIServer
import java.awt.event.ActionListener

class CIServerService {

    ICIServer server

    def initServer(ServerType server, String serverUrl) {
        this.server = CIServerFactory.newServer server
        this.server.serverUrl = serverUrl
    }

    def setCredentials(String login, String password) {
        this.server.setCredentials(login, password)
    }

    def getLastBuildStatus(int retries) {
        this.server.getLastBuildStatus(retries)
    }

}