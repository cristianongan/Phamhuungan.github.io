package com.evotek.qlns.event;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.util.FileUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class PreviewFileListener implements EventListener<Event>{

    private FileEntry entry;

    public PreviewFileListener(FileEntry entry) {
        this.entry = entry;
    }

    public void onEvent(Event t) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(StaticUtil.SYSTEM_STORE_FILE_DIR);
        sb.append(StringPool.SLASH);
        sb.append(StaticUtil.FILE_UPLOAD_DIR);
        sb.append(StringPool.SLASH);
        sb.append(entry.getFolderId());
        sb.append(StringPool.SLASH);
        sb.append(entry.getFileId());

        File file = new File(sb.toString());

        if (file.exists()) {
            String ext = FileUtil.getFileExtension(entry.getName());

            if (Validator.isNotNull(ext)
                    && Validator.isNotNull(FileUtil.FILE_TYPE_MAP.get(ext))) {
                Map map = new HashMap();

                map.put(Constants.OBJECT, file);
                map.put(Constants.KEY, ext);
                map.put(Constants.VALUE, FileUtil.FILE_TYPE_MAP.get(ext));

                Window win = (Window) Executions.createComponents(PREVIEW_FILE_PAGE,
                        null, map);
                win.doModal();
            } else {
                FileUtil.download(file, entry.getName());
            }
        } else {
            Messagebox.show(Labels.getLabel(
                    LanguageKeys.FILE_NOT_FOUND),
                    Labels.getLabel(LanguageKeys.ERROR),
                    Messagebox.OK, Messagebox.ERROR);
        }
    }

    private static final String PREVIEW_FILE_PAGE
            = "/html/pages/common/previewFile.zul";
}

