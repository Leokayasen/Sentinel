package com.leokayasen.sentinel.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PlayerManagementScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 10;

    private final Screen parent;
    private TextFieldWidget playerNameField;
    private ButtonWidget kickButton;
    private ButtonWidget banButton;
    private ButtonWidget opButton;
    private ButtonWidget deopButton;
    private ButtonWidget tpToPlayerButton;
    private ButtonWidget tpPlayerToMeButton;
    private ButtonWidget gamemodeCreativeButton;
    private ButtonWidget gamemodeSurvivalButton;
    private ButtonWidget gamemodeSpectatorButton;
    private ButtonWidget backButton;

    public PlayerManagementScreen(Screen parent) {
        super(Text.literal("Player Management"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 6;

        // Player name input field
        this.playerNameField = new TextFieldWidget(this.textRenderer, centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Player Name"));
        this.playerNameField.setPlaceholder(Text.literal("Enter player name..."));
        this.playerNameField.setMaxLength(16);
        this.addSelectableChild(this.playerNameField);

        int currentY = startY + BUTTON_HEIGHT + MARGIN;

        // Player action buttons
        this.kickButton = ButtonWidget.builder(Text.literal("Kick Player"), button -> executePlayerCommand("kick"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.kickButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        this.banButton = ButtonWidget.builder(Text.literal("Ban Player"), button -> executePlayerCommand("ban"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.banButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        this.opButton = ButtonWidget.builder(Text.literal("Give OP"), button -> executePlayerCommand("op"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.opButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        this.deopButton = ButtonWidget.builder(Text.literal("Remove OP"), button -> executePlayerCommand("deop"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.deopButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        this.tpToPlayerButton = ButtonWidget.builder(Text.literal("Teleport to Player"), button -> executePlayerCommand("tp_to"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.tpToPlayerButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        this.tpPlayerToMeButton = ButtonWidget.builder(Text.literal("Teleport Player to Me"), button -> executePlayerCommand("tp_here"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.tpPlayerToMeButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        // Gamemode buttons (smaller, side by side)
        int gamemodeButtonWidth = (BUTTON_WIDTH - MARGIN) / 3;
        
        this.gamemodeCreativeButton = ButtonWidget.builder(Text.literal("Creative"), button -> executeGamemodeCommand("creative"))
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, gamemodeButtonWidth, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.gamemodeCreativeButton);

        this.gamemodeSurvivalButton = ButtonWidget.builder(Text.literal("Survival"), button -> executeGamemodeCommand("survival"))
                .dimensions(centerX - BUTTON_WIDTH / 2 + gamemodeButtonWidth + MARGIN / 2, currentY, gamemodeButtonWidth, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.gamemodeSurvivalButton);

        this.gamemodeSpectatorButton = ButtonWidget.builder(Text.literal("Spectator"), button -> executeGamemodeCommand("spectator"))
                .dimensions(centerX + BUTTON_WIDTH / 2 - gamemodeButtonWidth, currentY, gamemodeButtonWidth, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.gamemodeSpectatorButton);
        currentY += BUTTON_HEIGHT + MARGIN * 2;

        // Back button
        this.backButton = ButtonWidget.builder(Text.literal("Back"), button -> this.close())
                .dimensions(centerX - 50, currentY, 100, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.backButton);

        this.setInitialFocus(this.playerNameField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
        
        // Draw subtitle
        Text subtitle = Text.literal("Manage players on the server").formatted(Formatting.GRAY);
        context.drawCenteredTextWithShadow(this.textRenderer, subtitle, this.width / 2, 55, 0xAAAAAA);

        // Draw player name field label
        context.drawTextWithShadow(this.textRenderer, Text.literal("Player Name:"), 
                this.playerNameField.getX(), this.playerNameField.getY() - 15, 0xFFFFFF);

        // Draw gamemode label
        context.drawTextWithShadow(this.textRenderer, Text.literal("Set Gamemode:"), 
                this.gamemodeCreativeButton.getX(), this.gamemodeCreativeButton.getY() - 15, 0xFFFFFF);

        this.playerNameField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private void executePlayerCommand(String action) {
        String playerName = this.playerNameField.getText().trim();
        if (playerName.isEmpty()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            String command;
            switch (action) {
                case "kick":
                    command = "kick " + playerName;
                    break;
                case "ban":
                    command = "ban " + playerName;
                    break;
                case "op":
                    command = "op " + playerName;
                    break;
                case "deop":
                    command = "deop " + playerName;
                    break;
                case "tp_to":
                    command = "tp @s " + playerName;
                    break;
                case "tp_here":
                    command = "tp " + playerName + " @s";
                    break;
                default:
                    return;
            }
            client.player.networkHandler.sendChatCommand(command);
        }
    }

    private void executeGamemodeCommand(String gamemode) {
        String playerName = this.playerNameField.getText().trim();
        if (playerName.isEmpty()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            String command = "gamemode " + gamemode + " " + playerName;
            client.player.networkHandler.sendChatCommand(command);
        }
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
}