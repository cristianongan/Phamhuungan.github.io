/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.event;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.evotek.qlns.util.ComponentUtil;
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
public class OnUploadAttachmentListener implements EventListener<UploadEvent> {

	private Div divFileList;
	private List<Media> medium;

	public OnUploadAttachmentListener(List<Media> medium, Div divFileList) {
		this.medium = medium;
		this.divFileList = divFileList;
	}

	private Long getInstanceValidMaxSizeMB() {
		Long maxSize = StaticUtil.ATTACH_FILE_UPLOAD_ALLOW_MAX_SIZE;

		return FileUtil.getMegabyte(maxSize);
	}

	private int getSize(Media media) {
		int size = 0;

		try {
			if (media.isBinary()) {
				size = media.getByteData().length;
			} else {

				size = IOUtils.toByteArray(media.getReaderData()).length;

			}
		} catch (IOException ex) {
		} finally {
			return size;
		}
	}

	private boolean isInstanceValidFileExtension(String extension) {
		String[] exts = StaticUtil.ATTACH_FILE_UPLOAD_ALLOW_EXTENSIONS;

		return FileUtil.isValidFileExtension(extension, exts);
	}

	private boolean isInstanceValidMaxSize(int size) {
		Long maxSize = StaticUtil.ATTACH_FILE_UPLOAD_ALLOW_MAX_SIZE;

		return FileUtil.isValidMaxSize(size, maxSize);
	}

	@Override
	public void onEvent(UploadEvent event) throws Exception {
		Media[] medias = event.getMedias();

		Grid grid = ComponentUtil.createGrid(StringPool.BLANK, Constants.CLASS_NO_STYLE);

		grid.appendChild(ComponentUtil.createColumns(new String[] { "88%", "12%" }));

		grid.setModel(new ListModelList<Media>(medias));
		grid.setRowRenderer(new RowRenderer<Media>() {

			@Override
			public void render(final Row row, final Media media, int index) throws Exception {
				String name = media.getName();
				String extension = FileUtil.getFileExtension(name);
				int size = getSize(media);

				if (size == 0) {
					org.zkoss.zul.Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_FILE_IS_EMPTY),
							Labels.getLabel(LanguageKeys.ERROR), org.zkoss.zul.Messagebox.OK,
							org.zkoss.zul.Messagebox.ERROR);

					row.getParent().removeChild(row);

					return;
				}

				if (Validator.isNull(extension) || !isInstanceValidFileExtension(extension)) {
					org.zkoss.zul.Messagebox.show(
							Labels.getLabel(LanguageKeys.MESSAGE_INVALID_FILE_UPLOAD_EXTENSION,
									new String[] { StaticUtil.ATTACH_FILE_UPLOAD_ALLOW_EXTENSION }),
							Labels.getLabel(LanguageKeys.ERROR), org.zkoss.zul.Messagebox.OK,
							org.zkoss.zul.Messagebox.ERROR);

					row.getParent().removeChild(row);

					return;
				}

				if (!isInstanceValidMaxSize(size)) {
					org.zkoss.zul.Messagebox.show(
							Labels.getLabel(LanguageKeys.MESSAGE_INVALID_FILE_MAX_SIZE,
									new Long[] { getInstanceValidMaxSizeMB() }),
							Labels.getLabel(LanguageKeys.ERROR), org.zkoss.zul.Messagebox.OK,
							org.zkoss.zul.Messagebox.ERROR);

					row.getParent().removeChild(row);

					return;
				}

				row.appendChild(new Label(name));

				Button button = new Button();

				button.setTooltiptext(Labels.getLabel(LanguageKeys.DELETE));
				button.setIconSclass(Constants.Z_ICON_TRASH_O);
				button.setSclass(Constants.RED);

				button.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						OnUploadAttachmentListener.this.medium.remove(media);

						row.getParent().removeChild(row);
					}
				});

				row.appendChild(button);

				row.setStyle(Constants.STYLE_NO_PADDING);

				OnUploadAttachmentListener.this.medium.add(media);

			}
		});

		this.divFileList.appendChild(grid);
	}
}
