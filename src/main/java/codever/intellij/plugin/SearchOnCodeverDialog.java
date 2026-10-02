package codever.intellij.plugin;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import icons.CodeverPluginIcons;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchOnCodeverDialog extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabledAndVisible(project != null && editor != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);

        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        final String selectedText = editor.getSelectionModel().getSelectedText();

        String queryTxt;
        if (selectedText != null) {
            queryTxt = Messages.showInputDialog("Input query to search in My Notes", "Codever Search", CodeverPluginIcons.CODEVER_ICON_48, selectedText, null);
        } else {
            queryTxt = Messages.showInputDialog(project, "Input query to search in My Notes", "Codever Search", CodeverPluginIcons.CODEVER_ICON_48);
        }
        if (queryTxt != null) {
            try {
                String url = "https://www.codever.dev/search?sd=my-notes&q="
                        + URLEncoder.encode(queryTxt, "UTF-8");
                BrowserUtil.browse(url);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                Messages.showErrorDialog("Could not encode the search query", "Codever Search");
            }
        }

    }
}
