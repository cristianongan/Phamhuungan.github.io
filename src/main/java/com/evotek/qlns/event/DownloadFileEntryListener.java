package com.evotek.qlns.event;

import java.io.File;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.evotek.qlns.model.FileEntry;
import com.evotek.qlns.util.FileUtil;
import com.evotek.qlns.util.StaticUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.key.LanguageKeys;

/**
 *
 * @author linhlh2
 */
public class DownloadFileEntryListener implements EventListener<Event>{

    private FileEntry entry;

    public DownloadFileEntryListener(FileEntry entry) {
        this.entry = entry;
    }

    @Override
	public void onEvent(Event t) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(StaticUtil.SYSTEM_STORE_FILE_DIR);
        sb.append(StringPool.SLASH);
        sb.append(StaticUtil.FILE_UPLOAD_DIR);
        sb.append(StringPool.SLASH);
        sb.append(this.entry.getFolderId());
        sb.append(StringPool.SLASH);
        sb.append(this.entry.getFileId());

        File file = new File(sb.toString());

        if (file.exists()) {
            FileUtil.download(file, this.entry.getName());
        } else {
            Messagebox.show(Labels.getLabel(
                    LanguageKeys.FILE_NOT_FOUND),
                    Labels.getLabel(LanguageKeys.ERROR),
                    Messagebox.OK, Messagebox.ERROR);
        }
    }

}

