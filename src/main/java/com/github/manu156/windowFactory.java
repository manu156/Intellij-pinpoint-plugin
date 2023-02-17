package com.github.manu156;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class windowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        com.cyan.pinpoint.toolWindow.CyanToolWindow cyanToolWindow = new com.cyan.pinpoint.toolWindow.CyanToolWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(cyanToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}