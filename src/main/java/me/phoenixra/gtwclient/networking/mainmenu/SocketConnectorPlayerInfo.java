package me.phoenixra.gtwclient.networking.mainmenu;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.networking.api.SocketServiceConnector;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@SideOnly(value = Side.CLIENT)
public class SocketConnectorPlayerInfo extends SocketServiceConnector {
    public SocketConnectorPlayerInfo(String host, int port) {
        super(host, port);
    }

    @Override
    protected void onResponseReceived(String message, List<String> response) {
        if(response == null) return;
        if(response.size() < 6) return;
        String rank = response.get(0);
        String gang = response.get(1);
        int level = Integer.parseInt(response.get(2));
        double money = Double.parseDouble(response.get(3));
        String kills = response.get(4);
        String deaths = response.get(5);

        ClientProxy.playerData.setRank(rank.replace("&","\u00a7")).setGang(gang)
                .setLevel(level).setMoney(money)
                .putOther("kills", kills).putOther("deaths", deaths).setUpdatedFromServer(true);

    }
}
