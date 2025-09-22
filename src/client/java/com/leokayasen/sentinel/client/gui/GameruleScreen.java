package com.leokayasen.sentinel.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;

public class GameruleScreen extends Screen {
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 5;

    private final Screen parent;
    private final List<GameruleEntry> gameruleEntries = new ArrayList<>();
    private int scrollOffset = 0;
    private final int maxVisibleEntries = 12;

    public GameruleScreen(Screen parent) {
        super(Text.literal("Gamerule Management"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.gameruleEntries.clear();
        
        // Add common gamerules
        addGameruleEntry("doFireTick", "Fire Spread");
        addGameruleEntry("doMobSpawning", "Mob Spawning");
        addGameruleEntry("keepInventory", "Keep Inventory");
        addGameruleEntry("doDaylightCycle", "Daylight Cycle");
        addGameruleEntry("doWeatherCycle", "Weather Cycle");
        addGameruleEntry("mobGriefing", "Mob Griefing");
        addGameruleEntry("naturalRegeneration", "Natural Regeneration");
        addGameruleEntry("showDeathMessages", "Show Death Messages");
        addGameruleEntry("announceAdvancements", "Announce Advancements");
        addGameruleEntry("disableElytraMovementCheck", "Disable Elytra Movement Check");
        addGameruleEntry("doLimitedCrafting", "Limited Crafting");
        addGameruleEntry("doMobLoot", "Mob Loot");
        addGameruleEntry("doTileDrops", "Block Drops");
        addGameruleEntry("doEntityDrops", "Entity Drops");
        addGameruleEntry("commandBlockOutput", "Command Block Output");
        addGameruleEntry("logAdminCommands", "Log Admin Commands");
        addGameruleEntry("sendCommandFeedback", "Send Command Feedback");

        // Add back button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> this.close())
                .dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
                .build());

        updateGameruleButtons();
    }

    private void addGameruleEntry(String gameruleName, String displayName) {
        gameruleEntries.add(new GameruleEntry(gameruleName, displayName));
    }

    private void updateGameruleButtons() {
        // Remove existing gamerule buttons
        this.children().removeIf(widget -> widget instanceof ClickableWidget && 
                ((ClickableWidget) widget).getMessage().getString().contains("Toggle"));
        this.drawables.removeIf(drawable -> drawable instanceof ClickableWidget && 
                ((ClickableWidget) drawable).getMessage().getString().contains("Toggle"));

        int startY = 60;
        int visibleCount = Math.min(maxVisibleEntries, gameruleEntries.size() - scrollOffset);

        for (int i = 0; i < visibleCount; i++) {
            int index = i + scrollOffset;
            if (index >= gameruleEntries.size()) break;

            GameruleEntry entry = gameruleEntries.get(index);
            int y = startY + i * (BUTTON_HEIGHT + MARGIN);

            ButtonWidget toggleButton = ButtonWidget.builder(
                    Text.literal("Toggle " + entry.displayName),
                    button -> toggleGamerule(entry.gameruleName)
            ).dimensions(this.width / 2 - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT).build();

            this.addDrawableChild(toggleButton);
        }
    }

    private void toggleGamerule(String gameruleName) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            // Send command to toggle gamerule
            String command = "gamerule " + gameruleName;
            client.player.networkHandler.sendChatCommand(command);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        
        // Draw subtitle
        Text subtitle = Text.literal("Click to toggle gamerules").formatted(Formatting.GRAY);
        context.drawCenteredTextWithShadow(this.textRenderer, subtitle, this.width / 2, 35, 0xAAAAAA);

        // Draw scroll indicators
        if (scrollOffset > 0) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("▲ Scroll Up"), 
                    this.width / 2, 45, 0xFFFFFF);
        }
        if (scrollOffset + maxVisibleEntries < gameruleEntries.size()) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("▼ Scroll Down"), 
                    this.width / 2, this.height - 50, 0xFFFFFF);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0 && scrollOffset > 0) {
            scrollOffset--;
            updateGameruleButtons();
            return true;
        } else if (verticalAmount < 0 && scrollOffset + maxVisibleEntries < gameruleEntries.size()) {
            scrollOffset++;
            updateGameruleButtons();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static class GameruleEntry {
        final String gameruleName;
        final String displayName;

        GameruleEntry(String gameruleName, String displayName) {
            this.gameruleName = gameruleName;
            this.displayName = displayName;
        }
    }
}