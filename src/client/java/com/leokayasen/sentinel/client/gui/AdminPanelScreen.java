package com.leokayasen.sentinel.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AdminPanelScreen extends Screen {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int MARGIN = 10;

    private TextFieldWidget commandField;
    private ButtonWidget executeButton;
    private ButtonWidget gameruleButton;
    private ButtonWidget playerManagementButton;
    private ButtonWidget closeButton;

    public AdminPanelScreen() {
        super(Text.literal("Sentinel Admin Panel"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4;

        // Command input field
        this.commandField = new TextFieldWidget(this.textRenderer, centerX - BUTTON_WIDTH / 2, startY, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Command"));
        this.commandField.setPlaceholder(Text.literal("Enter command..."));
        this.commandField.setMaxLength(256);
        this.addSelectableChild(this.commandField);

        // Execute command button
        this.executeButton = ButtonWidget.builder(Text.literal("Execute Command"), button -> executeCommand())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + BUTTON_HEIGHT + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.executeButton);

        // Gamerule management button
        this.gameruleButton = ButtonWidget.builder(Text.literal("Manage Gamerules"), button -> openGameruleScreen())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + 2 * (BUTTON_HEIGHT + MARGIN), BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.gameruleButton);

        // Player management button
        this.playerManagementButton = ButtonWidget.builder(Text.literal("Player Management"), button -> openPlayerManagementScreen())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + 3 * (BUTTON_HEIGHT + MARGIN), BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.playerManagementButton);

        // Close button
        this.closeButton = ButtonWidget.builder(Text.literal("Close"), button -> this.close())
                .dimensions(centerX - BUTTON_WIDTH / 2, startY + 4 * (BUTTON_HEIGHT + MARGIN), BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        this.addDrawableChild(this.closeButton);

        this.setInitialFocus(this.commandField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Draw title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFF);
        
        // Draw subtitle
        Text subtitle = Text.literal("Admin Tools for Operators").formatted(Formatting.GRAY);
        context.drawCenteredTextWithShadow(this.textRenderer, subtitle, this.width / 2, 55, 0xAAAAAA);

        // Draw command field label
        context.drawTextWithShadow(this.textRenderer, Text.literal("Command:"), 
                this.commandField.getX(), this.commandField.getY() - 15, 0xFFFFFF);

        this.commandField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private void executeCommand() {
        String command = this.commandField.getText().trim();
        if (!command.isEmpty()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                // Execute command as if the player typed it in chat
                if (!command.startsWith("/")) {
                    command = "/" + command;
                }
                client.player.networkHandler.sendChatCommand(command.substring(1));
                this.commandField.setText("");
            }
        }
    }

    private void openGameruleScreen() {
        if (this.client != null) {
            this.client.setScreen(new GameruleScreen(this));
        }
    }

    private void openPlayerManagementScreen() {
        if (this.client != null) {
            this.client.setScreen(new PlayerManagementScreen(this));
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && this.commandField.isFocused()) { // Enter key
            this.executeCommand();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}