package codever.intellij.plugin;

import com.intellij.ide.BrowserUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import icons.CodeverPluginIcons;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SaveToCodeverAction extends AnAction {

    /**
     * Only make this action visible when text is selected.
     * <p>
     * The update method below is only called periodically so need
     * to be careful to check for selected text
     * https://jetbrains.org/intellij/sdk/docs/basics/action_system.html#overriding-the-anactionupdate-method
     *
     * @param e
     */
    @Override
    public void update(AnActionEvent e) {
        // Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);

        // Set visibility only in case of existing project and editor and if a selection exists
        e.getPresentation().setEnabledAndVisible(project != null
                && editor != null
                && editor.getSelectionModel().hasSelection());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final String selectedCode = getSelectedCode(e);
        final String languageTag = getLanguageTag(e);
        final String filename = getFilename(e);
        final String projectName = getProjectName(e);
        final String comment = getComment(e);
        final String location = getLocation(e);

        final String title = Messages.showInputDialog("Snippet title", "Codever Save", CodeverPluginIcons.CODEVER_ICON_48, "Change me", null);
        if (title != null) {
            if (title.trim().equals("")) {
                Messages.showErrorDialog("A title for code snippet is mandatory on Codever", "Title mandatory");
                return;
            }

            String url = getUrl(languageTag, location, title, selectedCode, comment, filename, projectName);
            if (url != null) {
                BrowserUtil.browse(url);
            }
        }

    }

    private String getSelectedCode(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        return editor.getSelectionModel().getSelectedText();
    }

    private String getLanguageTag(AnActionEvent e) {
        String languageTag = "";
        PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
        if (file != null) {
            Language lang = e.getData(CommonDataKeys.PSI_FILE).getLanguage();
            languageTag = lang != null ? lang.getDisplayName().toLowerCase() : null;
        }
        return languageTag;
    }

    private String getFilename(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String fileName = vFile != null ? vFile.getName() : null;

        return fileName;
    }

    private String getProjectName(AnActionEvent e) {
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        String projectName = project != null ? project.getName() : null;

        return projectName;
    }

    private String getComment(AnActionEvent e) {
        String projectName = getProjectName(e);
        String fileName = getFilename(e);
        StringBuilder sb = new StringBuilder();
        if (projectName != null) {
            sb.append("**Project**: `" + projectName + "`");
        }
        if (fileName != null) {
            sb.append(" - **File**:  `" + fileName + "`");
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    private String getLocation(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        return vFile != null ? vFile.getUrl() : null;
    }

    private String getUrl(String languageTag, String sourceUrl, String title, String selectedCode, String comment,
                          String filename, String projectName) {
        try {
            StringBuilder sb = new StringBuilder("https://www.codever.dev/my-snippets/new?");
            sb.append("code=" + URLEncoder.encode(selectedCode, "UTF-8"));
            if (title != null) {
                sb.append("&title=" + URLEncoder.encode(title, "UTF-8"));
            }
            if (comment != null) {
                sb.append("&comment=" + URLEncoder.encode(comment, "UTF-8"));
            }
            if (sourceUrl != null) {
                sb.append("&location=" + URLEncoder.encode(sourceUrl, "UTF-8"));
            }
            if (languageTag != null) {
                sb.append("&tags=" + URLEncoder.encode(languageTag, "UTF-8"));
            }
            if (filename != null) {
                sb.append("&file=" + URLEncoder.encode(filename, "UTF-8"));
            }
            if (projectName != null) {
                sb.append("&project=" + URLEncoder.encode(projectName, "UTF-8"));
            }
            sb.append("&initiator=intellij-plugin");

            return sb.toString();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        }
    }

}
