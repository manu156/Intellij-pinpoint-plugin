package com.github.manu156.pinpointintegration.editor.render;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.util.ui.UIUtilities;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MyElementRenderer implements EditorCustomElementRenderer {

    private final @NotNull RenderData renderData;

    private final @NotNull EditorFontType fontType = EditorFontType.PLAIN;

    public MyElementRenderer(@NotNull RenderData renderData) {
        this.renderData = renderData;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        Editor editor = inlay.getEditor();
        FontMetrics fontMetrics = UIUtilities.getFontMetrics(editor.getComponent(), editor.getColorsScheme().getFont(fontType));
        if (!renderData.showText) return fontMetrics.stringWidth("     ");
        return fontMetrics.stringWidth(renderData.description) + fontMetrics.charWidth(' ') * 3 / 2;
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        int borderMagicNumberToFixBoxBlinking = 2;
        targetRegion.y = targetRegion.y + borderMagicNumberToFixBoxBlinking;
        targetRegion.x = targetRegion.x + borderMagicNumberToFixBoxBlinking;
        targetRegion.width = targetRegion.width - 2 * borderMagicNumberToFixBoxBlinking;
        targetRegion.height = targetRegion.height - 2 * borderMagicNumberToFixBoxBlinking;

        Editor editor = inlay.getEditor();
        Font font = editor.getColorsScheme().getFont(fontType);
        FontMetrics fontMetrics = UIUtilities.getFontMetrics(editor.getComponent(), font);

        int arc = fontMetrics.getHeight() * 4 / 10;
        int charWidth = fontMetrics.charWidth('a');

        targetRegion.x = targetRegion.x + renderData.numberOfWhitespaces * charWidth;

        String description = renderData.description;

        if (renderData.showEffect) {
            switch (renderData.effectType) {
                case BOX:
                    g.setColor(renderData.effectColor);
                    g.drawRoundRect(targetRegion.x, targetRegion.y, targetRegion.width, targetRegion.height, arc, arc);
                    break;
                case SHADOW:
                    g.setFont(font);
                    description = description + ".";

                    Color color = renderData.effectColor;
                    // Right shadow
                    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).darker());
                    g.drawString(description, targetRegion.x + charWidth * 2 / 3 + charWidth * 2 / 10,
                            targetRegion.y + charWidth * 2 / 10 + editor.getLineHeight() * 3 / 4 - borderMagicNumberToFixBoxBlinking);
                    // Left shadow
                    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 130));
                    g.drawString(description, targetRegion.x + charWidth * 2 / 3 - charWidth * 1 / 10,
                            targetRegion.y - charWidth * 1 / 10 + editor.getLineHeight() * 3 / 4 - borderMagicNumberToFixBoxBlinking);
                    break;
                case NONE:
                    description = description + ".";
                    break;
            }
        }
        if (renderData.showText) {
            g.setFont(font);
            g.setColor(renderData.textColor);
            g.drawString(description, targetRegion.x + charWidth * 2 / 3,
                    targetRegion.y + editor.getLineHeight() * 3 / 4 - borderMagicNumberToFixBoxBlinking);
        }
    }
}
