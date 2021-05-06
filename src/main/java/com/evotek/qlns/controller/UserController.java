package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.SimpleModel;
import com.evotek.qlns.model.User;
import com.evotek.qlns.model.list.UserListModel;
import com.evotek.qlns.model.render.UserRender;
import com.evotek.qlns.service.UserService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.ExcelUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.QueryUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author LinhLH
 */
@Controller
@Scope("prototype")
public class UserController extends BasicController<Div> implements Serializable {

	private static final long serialVersionUID = 7543542864782444825L;

	private static final Logger _log = LogManager.getLogger(UserController.class);

	private static final String EXPORT_USER = "export_user";

	@Autowired
	private UserService userService;

	private A btnClearGender;
	private A btnClearStatus;

	private Button btnActivate;
	private Button btnDel;
	private Button btnEnableAdvSearch;
	private Button btnLock;
	private Button btnUnlock;

	private Combobox cbGender;
	private Combobox cbStatus;

	private Datebox dbBirthdayFrom;
	private Datebox dbBirthdayTo;

	private Div winUser;

	private Listbox searchResultGrid;

	private Map<String, Object> paramMap = new HashMap<String, Object>();

	private Popup advanceSearchPopup;

	private Textbox tbAccount;
	private Textbox tbBirthPlace;
	private Textbox tbEmail;
	private Textbox tbKeyword;
	private Textbox tbMobile;
	private Textbox tbPhone;
	private Textbox tbUserName;

	private boolean isAdmin = false;
	private boolean isAdvance = false;

	private Map<String, Object> _createParameterMap(User user) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Constants.Attr.PARENT_WINDOW, this.winUser);
		parameters.put(Constants.Attr.OBJECT, user);
		parameters.put(Constants.Attr.SECOND_OBJECT, true);

		return parameters;
	}

	public void advanceSearch() {
		String userName = GetterUtil.getString(this.tbUserName.getValue());
		String email = GetterUtil.getString(this.tbEmail.getValue());
		Long gender = ComponentUtil.getComboboxValue(this.cbGender);
		String birthplace = GetterUtil.getString(this.tbBirthPlace.getValue());
		Date birthdayFrom = this.dbBirthdayFrom.getValue();
		Date birthdayTo = this.dbBirthdayTo.getValue();
		String phone = GetterUtil.getString(this.tbPhone.getValue());
		String mobile = GetterUtil.getString(this.tbMobile.getValue());
		String account = GetterUtil.getString(this.tbAccount.getValue());
		Long status = ComponentUtil.getComboboxValue(this.cbStatus);
		// create param map

		this.paramMap.put("userName", userName);
		this.paramMap.put("email", email);
		this.paramMap.put("gender", gender);
		this.paramMap.put("birthplace", birthplace);
		this.paramMap.put("birthdayFrom", birthdayFrom);
		this.paramMap.put("birthdayTo", birthdayTo);
		this.paramMap.put("phone", phone);
		this.paramMap.put("mobile", mobile);
		this.paramMap.put("account", account);
		this.paramMap.put("status", status);

		ListModel model = new UserListModel(this.searchResultGrid.getPageSize(), userName, email, gender, birthplace,
				birthdayFrom, birthdayTo, phone, mobile, account, status, this.isAdvance, this.userService);

		this.searchResultGrid.setItemRenderer(new UserRender(this.winUser, model, this.isAdmin));
		this.searchResultGrid.setModel(model);
	}

	public void basicSearch() {
		String keyword = GetterUtil.getString(this.tbKeyword.getValue());

		this.paramMap.put("keyword", keyword);

		ListModel model = new UserListModel(this.searchResultGrid.getPageSize(), keyword, this.isAdvance,
				this.userService);

		this.searchResultGrid.setModel(model);

		this.searchResultGrid.setItemRenderer(new UserRender(this.winUser, model, this.isAdmin));

		this.searchResultGrid.setMultiple(true);
	}

	@Override
	public void doAfterCompose(Div win) throws Exception {
		super.doAfterCompose(win);
		// init data
		this.initData();
	}

	@Override
	public void doBeforeComposeChildren(Div win) throws Exception {
		super.doBeforeComposeChildren(win);

		this.winUser = win;
	}

	public Map<Integer, String[]> getConvertMap() {
		Map<Integer, String[]> convertMap = new HashMap<Integer, String[]>();

		String[] genderArray = new String[] { Labels.getLabel(LanguageKeys.MALE),
				Labels.getLabel(LanguageKeys.FEMALE) };

		String[] statusArray = new String[] { Labels.getLabel(LanguageKeys.STATUS_ACTIVE),
				Labels.getLabel(LanguageKeys.STATUS_LOCK) };

		convertMap.put(2, genderArray);
		convertMap.put(4, statusArray);

		return convertMap;
	}

	// export
	public List<Object[]> getHeaderInfors() {
		List<Object[]> headerInfors = new ArrayList<Object[]>();

		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.ORDINAL), 2000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.ACCOUNT), 8000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.EMAIL), 8000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.GENDER), 8000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.FULL_NAME), 6000 });
		headerInfors.add(new Object[] { Labels.getLabel(LanguageKeys.STATUS), 6000 });
		//

		return headerInfors;
	}

	public List<User> getListExport() {
		List<User> results = new ArrayList<User>();

		try {
			if (this.isAdvance) {
				String userName = GetterUtil.getString(this.paramMap.get("userName"));
				String email = GetterUtil.getString(this.paramMap.get("email"));
				Long gender = GetterUtil.getLong(this.paramMap.get("gender"));
				String birthplace = GetterUtil.getString(this.paramMap.get("birthplace"));
				Date birthdayFrom = (Date) this.paramMap.get("birthdayFrom");
				Date birthdayTo = (Date) this.paramMap.get("birthdayTo");
				String phone = GetterUtil.getString(this.paramMap.get("phone"));
				String mobile = GetterUtil.getString(this.paramMap.get("mobile"));
				String account = GetterUtil.getString(this.paramMap.get("account"));
				Long status = GetterUtil.getLong(this.paramMap.get("status"));

				results = this.userService.getUsers(userName, email, gender, birthplace, birthdayFrom, birthdayTo,
						phone, mobile, account, status, QueryUtil.GET_ALL, QueryUtil.GET_ALL, null, null);

			} else {
				String keyword = (String) this.paramMap.get("keyword");

				results = this.userService.getUsers(keyword, QueryUtil.GET_ALL, QueryUtil.GET_ALL, null, null);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}

		return results;
	}

	public List<String> getProperties() {
		List<String> properties = new ArrayList<String>();

		properties.add("userName");
		properties.add("email");
		properties.add("gender");
		properties.add("fullName");
		properties.add("status");

		return properties;
	}

	private List<User> getUserSelected() {
		List<User> users = new ArrayList<User>();

		for (Listitem item : this.searchResultGrid.getSelectedItems()) {
			User user = (User) item.getAttribute("data");

			if (Validator.isNotNull(user)) {
				users.add(user);
			}
		}

		return users;
	}

	public void initData() throws Exception {
		try {
			this.isAdmin = this.getUserWorkspace().isAdministrator();

			this.onCreateGender();

			this.onCreateStatus();

			this.onOkWindow();
		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onAfterRender$cbStatus() {
		this.cbStatus.setSelectedIndex(Values.FIRST_INDEX);

		refreshModel();
	}

	public void onClick$btnActivate() {
		final List<User> users = this.getUserSelected();

		if (Validator.isNull(users)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_ACTIVATE),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_ACTIVATE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									UserController.this.userService.activateUser(users);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_ACTIVATE_SUCCESS);

									refreshModel();
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_ACTIVATE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnAdd() {
		Window win = (Window) Executions.createComponents(Constants.Page.ManagerUser.ADD_EDIT, this.winUser,
				_createParameterMap(null));

		win.doModal();
	}

	public void onClick$btnAdvanceSearch() {
		this.refreshModel();
	}

	public void onClick$btnAdvSearch() {
		this.isAdvance = true;

		this.refreshModel();
	}

	public void onClick$btnBasicSearch() {
		this.isAdvance = false;

		this.refreshModel();

	}

	public void onClick$btnClearGender() {
		this.cbGender.setSelectedIndex(-1);
		this.btnClearGender.setVisible(false);
	}

	public void onClick$btnClearStatus() {
		this.cbStatus.setSelectedIndex(-1);
		this.btnClearStatus.setVisible(false);
	}

	public void onClick$btnDel() {
		final List<User> users = this.getUserSelected();

		if (Validator.isNull(users)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									List<String> userNotDel = UserController.this.userService.delete(users);

									if (Validator.isNull(userNotDel)) {
										ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);
									} else {
										Messagebox.show(Values.getInUseMsg(Labels.getLabel(LanguageKeys.ACCOUNT,
												StringUtils.join(userNotDel, StringPool.COMMA))));
									}

									refreshModel();

								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnEnableAdvSearch() {
		this.advanceSearchPopup.open(this.btnEnableAdvSearch, ZkKeys.OVERLAP_END);

		this.tbUserName.setFocus(true);

		this.isAdvance = true;
	}

	public void onClick$btnExport() throws Exception {
		try {
			List<Object[]> headerInfors = getHeaderInfors();
			List<String> properties = getProperties();

			List<User> datas = getListExport();

			if (!datas.isEmpty()) {
				String title = Labels.getLabel(LanguageKeys.LIST_USER).toUpperCase();

				ExcelUtil excelUtil = new ExcelUtil<User>(getConvertMap());

				excelUtil.toSingleSheetXlsx(EXPORT_USER, title, headerInfors, properties, datas);
			} else {
				Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_NO_RECORD_EXPORT),
						Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK, Messagebox.ERROR);
			}

		} catch (Exception ex) {
			_log.error(ex.getMessage(), ex);
		}
	}

	public void onClick$btnImport() {
		Window win = (Window) Executions.createComponents(Constants.Page.ManagerUser.IMPORT_USERS, this.winUser,
				_createParameterMap(null));

		win.doModal();
	}

	public void onClick$btnLock() {
		final List<User> users = this.getUserSelected();

		if (Validator.isNull(users)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									UserController.this.userService.lockUser(users);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

									refreshModel();
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnResetPwd() {
		final List<User> users = this.getUserSelected();

		if (Validator.isNull(users)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_RESET),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_RESET), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									UserController.this.userService.resetPassword(users);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UPDATE_SUCCESS);
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UPDATE_FAIL));
								}
							}
						}
					});
		}
	}

	public void onClick$btnUnlock() {
		final List<User> users = this.getUserSelected();

		if (Validator.isNull(users)) {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD), Labels.getLabel(LanguageKeys.ERROR),
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
					Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK), Messagebox.OK | Messagebox.CANCEL,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(Event e) throws Exception {
							if (Messagebox.ON_OK.equals(e.getName())) {
								try {
									UserController.this.userService.unlockUser(users);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

									refreshModel();
								} catch (Exception ex) {
									_log.error(ex.getMessage(), ex);

									Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
								}
							}
						}
					});
		}
	}

	public void onCreateGender() throws Exception {
		List<SimpleModel> genders = this.userService.getGenderType();
		this.cbGender.setModel(new ListModelList<SimpleModel>(genders));
	}

	public void onCreateStatus() {
		List<SimpleModel> statusList = new ArrayList<SimpleModel>();

//        statusList.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT,
//                Labels.getLabel(LanguageKeys.OPTION)));

		statusList.add(new SimpleModel(Values.STATUS_ACTIVE, Labels.getLabel(LanguageKeys.STATUS_ACTIVE)));

		statusList.add(new SimpleModel(Values.STATUS_DEACTIVE, Labels.getLabel(LanguageKeys.STATUS_LOCK)));

		statusList.add(new SimpleModel(Values.STATUS_NOT_READY, Labels.getLabel(LanguageKeys.STATUS_NOT_ACTIVE)));

		this.cbStatus.setModel(new ListModelList<SimpleModel>(statusList));
	}

	/**
	 * Hàm xóa SysInstance khi click chọn nút "Xóa". SysInstance chỉ có thể xóa sau
	 * khi Khóa (status=0)
	 *
	 * @param event
	 * @throws Exception
	 */
	public void onDeleteUser(Event event) throws Exception {
		final User user = (User) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								if (user.getStatus().equals(Values.STATUS_NOT_READY)) {
									UserController.this.userService.delete(user);

									ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_DELETE_SUCCESS);

									refreshModel();
								} else {
									Messagebox.show(Values.getInUseMsg(Labels.getLabel(LanguageKeys.USER)));
								}

							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_DELETE_FAIL));
							}
						}
					}
				});
	}

	public void onLoadData(Event event) throws Exception {
		this.refreshModel();
	}

	public void onLoadDataAndReOpen(Event event) throws Exception {
		this.refreshModel();

		onClick$btnAdd();
	}

	/**
	 * Hàm xử lý sự kiện khi click vào nút "Khóa". Hàm sẽ chuyển Users về trạng thái
	 * khóa(không hoạt động), status=0
	 *
	 * @param event
	 * @throws Exception
	 */
	public void onLockUser(Event event) throws Exception {
		final User user = (User) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								if (user.getUserId().equals(getUserId())) {
									ComponentUtil.createErrorMessageBox(LanguageKeys.MESSAGE_CANNOT_LOCK_YOURSELF);

									return;
								}

								UserController.this.userService.lockUser(user);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

								refreshModel();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	public void onOK$advanceSearchPopup() {
		this.isAdvance = true;

		this.refreshModel();
	}

	public void onOK$tbKeyword() {
		this.isAdvance = false;

		this.refreshModel();
	}

	public void onOkWindow() {
		this.winUser.addEventListener(Events.ON_OK, new EventListener<Event>() {
			@Override
			public void onEvent(Event t) throws Exception {
				refreshModel();
			}
		});
	}

	public void onSelect$cbGender() {
		this.btnClearGender.setVisible(true);
	}

	public void onSelect$cbStatus() {
		this.btnClearStatus.setVisible(true);
	}

	/**
	 * Hàm xử lý sự kiện khi click vào nút "Mở khóa". Hàm sẽ chuyển Users về trạng
	 * thái mở khóa (hoạt động), status=1
	 *
	 * @param event
	 * @throws Exception
	 */
	public void onUnlockUser(Event event) throws Exception {
		final User user = (User) event.getData();

		Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
				Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK), Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(Event e) throws Exception {
						if (Messagebox.ON_OK.equals(e.getName())) {
							try {
								UserController.this.userService.unlockUser(user);

								ComponentUtil.createSuccessMessageBox(LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

								refreshModel();
							} catch (Exception ex) {
								_log.error(ex.getMessage(), ex);

								Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
							}
						}
					}
				});
	}

	public void refreshModel() {
		this.showHideButton();

		if (this.isAdvance) {
			this.advanceSearch();
		} else {
			this.basicSearch();
		}
	}

	public void showHideButton() {
		Long status = ComponentUtil.getComboboxValue(this.cbStatus);

		if (this.btnLock != null) {
			this.btnLock.setVisible(this.isAdvance && Values.STATUS_ACTIVE.equals(status));
		}

		if (this.btnUnlock != null) {
			this.btnUnlock.setVisible(this.isAdvance && Values.STATUS_DEACTIVE.equals(status));
		}

		if (this.btnDel != null) {
			this.btnDel.setVisible(this.isAdvance && Values.STATUS_NOT_READY.equals(status));
		}

		if (this.btnActivate != null) {
			this.btnActivate.setVisible(this.isAdvance && Values.STATUS_NOT_READY.equals(status));
		}
	}
}
