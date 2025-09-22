package com.leokayasen.sentinel.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BroadcastScreen extends Screen {
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 10;

    private final Screen parent;
    private TextFieldWidget titleField;
    private TextFieldWidget subtitleField;
    private TextFieldWidget playerField;
    private ButtonWidget sendTitleButton;
    private ButtonWidget sendSubtitleButton;
    private ButtonWidget sendBothButton;
    private ButtonWidget sendToAllButton;
    private ButtonWidget sendToPlayerButton;
    private ButtonWidget clearTitlesButton;
    private ButtonWidget backButton;

    public BroadcastScreen(Screen parent) {
        super(Text.literal("Broadcast & Announcements"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 6;

        // Title input field
        this.titleField = new TextFieldWidget(this.textRenderer, centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Title"));
        this.titleField.setPlaceholder(Text.literal("Enter title text..."));
        this.titleField.setMaxLength(100);
        this.addSelectableChild(this.titleField);

        // Subtitle input field
        this.subtitleField = new TextFieldWidget(this.textRenderer, centerX - BUTTON_WIDTH / 2, startY + BUTTON_HEIGHT + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Subtitle"));
        this.subtitleField.setPlaceholder(Text.literal("Enter subtitle text..."));
        this.subtitleField.setMaxLength(100);
        this.addSelectableChild(this.subtitleField);

        // Player name field
        this.playerField = new TextFieldWidget(this.textRenderer, centerX - BUTTON_WIDTH / 2, startY + 2 * (BUTTON_HEIGHT + MARGIN), BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Player"));
        this.playerField.setPlaceholder(Text.literal("Enter player name (optional)..."));
        this.playerField.setMaxLength(16);
        this.addSelectableChild(this.playerField);

        int currentY = startY + 3 * (BUTTON_HEIGHT + MARGIN) + MARGIN;

        // Send title only button
        this.sendTitleButton = ButtonWidget.builder(Text.literal("Send Title Only"), button -> sendTitle())
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.sendTitleButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        // Send subtitle only button
        this.sendSubtitleButton = ButtonWidget.builder(Text.literal("Send Subtitle Only"), button -> sendSubtitle())
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.sendSubtitleButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        // Send both title and subtitle button
        this.sendBothButton = ButtonWidget.builder(Text.literal("Send Title & Subtitle"), button -> sendBoth())
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.sendBothButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        // Target buttons (smaller, side by side)
        int targetButtonWidth = (BUTTON_WIDTH - MARGIN) / 2;
        
        this.sendToAllButton = ButtonWidget.builder(Text.literal("Send to All Players"), button -> sendToAll())
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, targetButtonWidth, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.sendToAllButton);

        this.sendToPlayerButton = ButtonWidget.builder(Text.literal("Send to Player"), button -> sendToPlayer())
                .dimensions(centerX - BUTTON_WIDTH / 2 + targetButtonWidth + MARGIN, currentY, targetButtonWidth, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.sendToPlayerButton);
        currentY += BUTTON_HEIGHT + MARGIN;

        // Clear titles button
        this.clearTitlesButton = ButtonWidget.builder(Text.literal("Clear All Titles"), button -> clearTitles())
                .dimensions(centerX - BUTTON_WIDTH / 2, currentY, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.clearTitlesButton);
        currentY += BUTTON_HEIGHT + MARGIN * 2;

        // Back button
        this.backButton = ButtonWidget.builder(Text.literal("Back"), button -> this.close())
                .dimensions(centerX - 50, currentY, 100, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.backButton);

        this.setInitialFocus(this.titleField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
        
        // Draw subtitle
        Text subtitle = Text.literal("Send titles and subtitles to players").formatted(Formatting.GRAY);
        context.drawCenteredTextWithShadow(this.textRenderer, subtitle, this.width / 2, 55, 0xAAAAAA);

        // Draw field labels
        context.drawTextWithShadow(this.textRenderer, Text.literal("Title:"), 
                this.titleField.getX(), this.titleField.getY() - 15, 0xFFFFFF);
        
        context.drawTextWithShadow(this.textRenderer, Text.literal("Subtitle:"), 
                this.subtitleField.getX(), this.subtitleField.getY() - 15, 0xFFFFFF);
        
        context.drawTextWithShadow(this.textRenderer, Text.literal("Target Player (leave empty for all):"), 
                this.playerField.getX(), this.playerField.getY() - 15, 0xFFFFFF);

        // Draw help text
        Text helpText = Text.literal("Use & for color codes (e.g., &c for red, &b for aqua)").formatted(Formatting.DARK_GRAY);
        context.drawCenteredTextWithShadow(this.textRenderer, helpText, this.width / 2, this.height - 60, 0x666666);

        this.titleField.render(context, mouseX, mouseY, delta);
        this.subtitleField.render(context, mouseX, mouseY, delta);
        this.playerField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private void sendTitle() {
        String title = this.titleField.getText().trim();
        if (title.isEmpty()) {
            return;
        }

        String target = getTarget();
        executeCommand("title " + target + " title \"" + formatText(title) + "\"");
    }

    private void sendSubtitle() {
        String subtitle = this.subtitleField.getText().trim();
        if (subtitle.isEmpty()) {
            return;
        }

        String target = getTarget();
        executeCommand("title " + target + " subtitle \"" + formatText(subtitle) + "\"");
    }

    private void sendBoth() {
        String title = this.titleField.getText().trim();
        String subtitle = this.subtitleField.getText().trim();
        
        if (title.isEmpty() && subtitle.isEmpty()) {
            return;
        }

        String target = getTarget();
        
        // Send subtitle first, then title (this is the correct order for both to display)
        if (!subtitle.isEmpty()) {
            executeCommand("title " + target + " subtitle \"" + formatText(subtitle) + "\"");
        }
        if (!title.isEmpty()) {
            executeCommand("title " + target + " title \"" + formatText(title) + "\"");
        }
    }

    private void sendToAll() {
        sendBoth();
    }

    private void sendToPlayer() {
        String playerName = this.playerField.getText().trim();
        if (playerName.isEmpty()) {
            sendToAll();
        } else {
            sendBoth();
        }
    }

    private void clearTitles() {
        String target = getTarget();
        executeCommand("title " + target + " clear");
    }

    private String getTarget() {
        String playerName = this.playerField.getText().trim();
        return playerName.isEmpty() ? "@a" : playerName;
    }

    private String formatText(String text) {
        // Convert & color codes to § for Minecraft formatting
        return text.replace("&", "§");
    }

    private void executeCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
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