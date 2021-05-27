package codever.intellij.plugin;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class SearchOnCodeverDialog extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);

        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        final String selectedText = editor.getSelectionModel().getSelectedText();

        String queryTxt;
        if (selectedText != null) {
            queryTxt = Messages.showInputDialog("Input query to search in My Snippets", "Codever Search", Messages.getQuestionIcon(), selectedText, null);
        } else {
            queryTxt = Messages.showInputDialog(project, "Input query to search in My Snippets", "Codever Search", Messages.getQuestionIcon());
        }
        String url = "https://www.codever.land/search?sd=my-snippets&q=" + queryTxt;
        if (queryTxt != null) {
            BrowserUtil.browse(url);
        }

    }
}
