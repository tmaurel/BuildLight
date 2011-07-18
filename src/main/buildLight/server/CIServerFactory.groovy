package buildLight.server

import buildLight.BuildConstants.ServerType
import buildLight.server.hudson.HudsonServer

class CIServerFactory {

    def static ICIServer newServer(ServerType type) {
        ICIServer instance = null
        switch(type) {
            case ServerType.HUDSON:
                instance = new HudsonServer();
            break
        }
        instance
    }

}
