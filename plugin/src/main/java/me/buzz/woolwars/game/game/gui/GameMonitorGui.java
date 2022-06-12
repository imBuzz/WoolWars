package me.buzz.woolwars.game.game.gui;

import com.hakan.core.item.HItemBuilder;
import com.hakan.core.ui.inventory.HInventory;
import com.hakan.core.ui.inventory.item.ClickableItem;
import com.hakan.core.ui.inventory.pagination.Pagination;
import me.buzz.woolwars.api.game.match.ApiMatch;
import me.buzz.woolwars.api.game.match.player.team.TeamColor;
import me.buzz.woolwars.game.WoolWars;
import me.buzz.woolwars.game.configuration.files.gui.GuiFile;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameMonitorGui extends HInventory {

    public GameMonitorGui() {
        super(UUID.randomUUID().toString().substring(0, 10),
                WoolWars.get().getGUISettings().getProperty(GuiFile.MATCHES_MONITOR),
                6, InventoryType.CHEST);
    }

    @Override
    protected void onOpen(Player player) {
        super.onOpen(player);

        Pagination pagination = getPagination();

        pagination.setSlots(10, 43);
        pagination.setItems(getMatchItems());

        super.setItem(50, WoolWars.get().getGUISettings().getProperty(GuiFile.ARROW_NEXT).toItemStack(), event -> pagination.setCurrentPage(getNextPage()));
        super.setItem(49, WoolWars.get().getGUISettings().getProperty(GuiFile.CLOSE_PAGE).toItemStack(), event -> event.getWhoClicked().closeInventory());
        super.setItem(48, WoolWars.get().getGUISettings().getProperty(GuiFile.ARROW_BACK).toItemStack(), event -> pagination.setCurrentPage(getPreviousPage()));

        pagination.setCurrentPage(0);
    }

    private List<ClickableItem> getMatchItems() {
        List<ClickableItem> items = new ArrayList<>();

        for (ApiMatch match : WoolWars.get().getGameManager().getMatches()) {
            HItemBuilder itemBuilder = new HItemBuilder(WoolWars.get().getGUISettings().getProperty(GuiFile.CLASSIC_MATCH_ITEM).toItemStack());
            itemBuilder.name(itemBuilder.getName().replace("{ID}", match.getMatchID()));

            List<String> newLore = new ArrayList<>();
            for (String s : itemBuilder.getLore()) {
                newLore.add(s.replace("{state}", match.getMatchState().toString())
                        .replace("{players-counter}", String.valueOf(match.getPlayerHolder().getPlayers().size()))
                        .replace("{spectators-counter}", String.valueOf(match.getPlayerHolder().getSpectators().size()))
                        .replace("{arena}", match.getArena().getName())
                        .replace("{blue_score}", String.valueOf(match.getRoundHolder().getPointsByTeamColor(TeamColor.BLUE)))
                        .replace("{red_score}", String.valueOf(match.getRoundHolder().getPointsByTeamColor(TeamColor.RED)))
                );
            }
            itemBuilder.lores(false, newLore);
            items.add(new ClickableItem(itemBuilder.build(), event -> {
            }));
        }


        return items;
    }

    private int getNextPage() {
        int totalPage = pagination.getPagesSafe().size() - 1;
        int currentIndex = pagination.getPage(pagination.getCurrentPage()).getNumber();

        if (currentIndex + 1 > totalPage) return currentIndex;
        else return pagination.getNextPage();
    }

    private int getPreviousPage() {
        int currentIndex = pagination.getPage(pagination.getCurrentPage()).getNumber();

        if (currentIndex - 1 < 0) return currentIndex;
        else return pagination.getPreviousPage();
    }


}
