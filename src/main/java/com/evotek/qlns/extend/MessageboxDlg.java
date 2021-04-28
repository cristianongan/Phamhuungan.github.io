package com.evotek.qlns.extend;

import org.zkoss.mesg.Messages;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox.ClickEvent;

/**
 * Used with {@link Messagebox} to implement a message box.
 *
 * @author tomyeh
 */
public class MessageboxDlg extends Window {
	/**
	 * Represents a button on the message box.
	 * 
	 * @since 3.0.4
	 */
	public static class Button extends org.zkoss.zul.Button {
		private Messagebox.Button _button;

		@Override
		protected String getDefaultMold(Class klass) {
			return super.getDefaultMold(org.zkoss.zul.Button.class);
		}

		public void onClick() throws Exception {
			((MessageboxDlg) getSpaceOwner()).endModal(this._button);
		}

		/** Sets the label's information with a default label. */
		public void setButton(Messagebox.Button button) {
			setButton(button, null);
		}

		/** Sets the label's information and label. */
		public void setButton(Messagebox.Button button, String label) {
			this._button = button;
			setLabel(label != null ? label : Messages.get(this._button.label));
			setId("btn" + this._button.id);
			if (label != null && label.length() > 7) {
				setWidth("auto");
			}

			// set iconSclass
			switch (button.id) {
			case Messagebox.YES:
				setIconSclass("z-icon-check");
				setSclass("btn-info");
				break;
			case Messagebox.NO:
				setIconSclass("z-icon-times");
				setSclass("btn-default");
				break;
			case Messagebox.RETRY:
				setIconSclass("z-icon-refresh");
				setSclass("btn-warning");
				break;
			case Messagebox.ABORT:
				setIconSclass("z-icon-minus-circle");
				setSclass("btn-danger");
				break;
			case Messagebox.IGNORE:
				setIconSclass("z-icon-ban");
				setSclass("btn-danger");
				break;
			case Messagebox.CANCEL:
				setIconSclass("z-icon-times");
				setSclass("btn-default");
				break;
			default:
				setIconSclass("z-icon-check");
				setSclass("btn-success");
				break;
			}
		}

		/**
		 * @deprecated As of release 6.0.0, buttons are created in Java
		 */
		@Deprecated
		public void setIdentity(int button) {
			switch (button) {
			case Messagebox.YES:
				setButton(Messagebox.Button.YES);
				break;
			case Messagebox.NO:
				setButton(Messagebox.Button.NO);
				break;
			case Messagebox.RETRY:
				setButton(Messagebox.Button.RETRY);
				break;
			case Messagebox.ABORT:
				setButton(Messagebox.Button.ABORT);
				break;
			case Messagebox.IGNORE:
				setButton(Messagebox.Button.IGNORE);
				break;
			case Messagebox.CANCEL:
				setButton(Messagebox.Button.CANCEL);
				break;
			default:
				setButton(Messagebox.Button.OK);
				break;
			}
		}
	}

	/** What buttons are allowed. */
	private Messagebox.Button[] _buttons;
	/** The event listener. */
	private EventListener<ClickEvent> _listener;

	/** Which button is pressed. */
	private Messagebox.Button _result;

	private boolean contains(Messagebox.Button button) {
		for (int j = 0; j < this._buttons.length; ++j) {
			if (this._buttons[j] == button) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called only internally.
	 */
	public void endModal(Messagebox.Button button) throws Exception {
		this._result = button;
		if (this._listener != null) {
			final ClickEvent evt = new ClickEvent(button.event, this, button);
			this._listener.onEvent(evt);
			if (!evt.isPropagatable()) {
				return; // no more processing
			}
		}
		detach();
	}

	/**
	 * Returns the result which is the button being pressed.
	 */
	public Messagebox.Button getResult() {
		return this._result;
	}

	public void onCancel() throws Exception {
		if (this._buttons.length == 1 && this._buttons[0] == Messagebox.Button.OK) {
			endModal(Messagebox.Button.OK);
		} else if (contains(Messagebox.Button.CANCEL)) {
			endModal(Messagebox.Button.CANCEL);
		} else if (contains(Messagebox.Button.NO)) {
			endModal(Messagebox.Button.NO);
		} else if (contains(Messagebox.Button.ABORT)) {
			endModal(Messagebox.Button.ABORT);
		}
	}

	// Override//
	@Override
	public void onClose() {
		if (this._listener != null) {
			final ClickEvent evt = new ClickEvent(Events.ON_CLOSE, this, null);
			try {
				this._listener.onEvent(evt);
				if (!evt.isPropagatable()) {
					return; // no more processing
				}
			} catch (Exception ex) {
				throw UiException.Aide.wrap(ex);
			}
		}
		super.onClose();
	}

	public void onOK() throws Exception {
		if (contains(Messagebox.Button.OK)) {
			endModal(Messagebox.Button.OK);
		} else if (contains(Messagebox.Button.YES)) {
			endModal(Messagebox.Button.YES);
		} else if (contains(Messagebox.Button.RETRY)) {
			endModal(Messagebox.Button.RETRY);
		}
	}

	/** Sets what buttons are allowed. */
	public void setButtons(Messagebox.Button[] buttons, String[] btnLabels) {
		this._buttons = buttons;

		final Component parent = getFellowIfAny("buttons");
		if (parent != null && parent.getFirstChild() == null) {
			// Backward compatible to ZK 5
			// We check if any child since user's old template might create them
			final String sclass = (String) parent.getAttribute("button.sclass");
			for (int j = 0; j < this._buttons.length; ++j) {
				final Button mbtn = new Button();
				mbtn.setButton(this._buttons[j], btnLabels != null && j < btnLabels.length ? btnLabels[j] : null);
				mbtn.setSclass(mbtn.getSclass() + " " + sclass);
				mbtn.setAutodisable("self");
				parent.appendChild(mbtn);
			}
		}
	}

	/**
	 * Sets the event listener.
	 * 
	 * @param listener the event listener. If null, no invocation at all.
	 * @since 3.0.4
	 */
	public void setEventListener(EventListener<ClickEvent> listener) {
		this._listener = listener;
	}

	/**
	 * Sets the focus.
	 * 
	 * @param button the button to gain the focus. If 0, the default one (i.e., the
	 *               first one) is assumed.
	 * @since 3.0.0
	 */
	public void setFocus(Messagebox.Button button) {
		if (button != null) {
			final Button btn = (Button) getFellowIfAny("btn" + button.id);
			if (btn != null) {
				btn.focus();
			}
		}
	}
}
