package dev.bookmarks.intellij.plugin;

import com.intellij.ide.BrowserUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SaveToBookmarksDevAction extends AnAction {

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
        final String title = getTitle(e);
        final String comment = getComment(e);
        final String sourceUrl = getSourceUrl(e);

        String url = getUrl(languageTag, sourceUrl, title, selectedCode, comment);
        if (url != null) {
            BrowserUtil.browse(url);
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

    private String getTitle(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String fileName = vFile != null ? vFile.getName() : null;

        final Project project = e.getData(PlatformDataKeys.PROJECT);
        String projectName = project != null ? project.getName() : null;
        StringBuilder sb = new StringBuilder();
        if (projectName != null) {
            sb.append(projectName);
        }
        if (fileName != null) {
            sb.append(" - " + fileName);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    private String getComment(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String fileName = vFile != null ? vFile.getName() : null;

        final Project project = e.getData(PlatformDataKeys.PROJECT);
        String projectName = project != null ? project.getName() : null;
        StringBuilder sb = new StringBuilder();
        if (projectName != null) {
            sb.append("**Project**: `" + projectName + "`" );
        }
        if (fileName != null) {
            sb.append(" - **File**:  `" + fileName + "`" );
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    private String getSourceUrl(AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        return vFile != null ? vFile.getUrl() : null;
    }

    private String getUrl(String languageTag, String sourceUrl, String title, String selectedCode, String comment) {
        try {
            StringBuilder sb = new StringBuilder("https://www.bookmarks.dev/my-codelets/new?") ;
            sb.append("code=" + URLEncoder.encode(selectedCode, "UTF-8"));
            if (title != null) {
                sb.append("&title=" + URLEncoder.encode(title, "UTF-8"));
            }
            if (comment != null) {
                sb.append("&comment=" + URLEncoder.encode(comment, "UTF-8"));
            }
            if (sourceUrl != null) {
                sb.append("&sourceUrl=" + URLEncoder.encode(sourceUrl, "UTF-8"));
            }
            if (languageTag != null) {
                sb.append("&tags=" + URLEncoder.encode(languageTag, "UTF-8"));
            }

            return sb.toString();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        }
    }

}
