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
import org.zkoss.spring.SpringUtil;
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
 * @author hungnt81
 */
public class UserController extends BasicController<Div>
        implements Serializable {

    private Div winUser;
    
    private Textbox tbUserName;
    private Textbox tbEmail;
    private Textbox tbBirthPlace;
    private Datebox dbBirthdayFrom;
    private Datebox dbBirthdayTo;
    private Textbox tbPhone;
    private Textbox tbMobile;
    private Textbox tbAccount;
    private Textbox tbKeyword;
    
    private Combobox cbGender;
    private Combobox cbStatus;
    
    private Button btnLock;
    private Button btnUnlock;
    private Button btnDel;
    private Button btnActivate;
    
    private Listbox searchResultGrid;
    
    private Button btnEnableAdvSearch;
    
    private Popup advanceSearchPopup;
    
    private A btnClearGender;
    private A btnClearStatus;
    
    private Map<String, Object> paramMap = new HashMap<String, Object>();
    
    private boolean isAdmin = false;
    private boolean isAdvance = false;

    @Override
    public void doBeforeComposeChildren(Div win) throws Exception {
        super.doBeforeComposeChildren(win);

        this.winUser = win;
    }

    @Override
    public void doAfterCompose(Div win) throws Exception {
        super.doAfterCompose(win);
        //init data
        this.initData();
    }

    public void initData() throws Exception {
        try {
            isAdmin = this.getUserWorkspace().isAdministrator();

            this.onCreateGender();

            this.onCreateStatus();

            this.onOkWindow();
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void onCreateGender() throws Exception {
        List<SimpleModel> genders =
                userService.getGenderType();
        cbGender.setModel(new ListModelList<SimpleModel>(genders));
    }

    public void onSelect$cbGender() {
        btnClearGender.setVisible(true);
    }
    
    public void onClick$btnClearGender() {
        cbGender.setSelectedIndex(-1);
        btnClearGender.setVisible(false);
    }
    
    public void onCreateStatus() {
        List<SimpleModel> statusList = new ArrayList<SimpleModel>();

//        statusList.add(new SimpleModel(Values.DEFAULT_OPTION_VALUE_INT,
//                Labels.getLabel(LanguageKeys.OPTION)));

        statusList.add(new SimpleModel(Values.STATUS_ACTIVE,
                Labels.getLabel(LanguageKeys.STATUS_ACTIVE)));

        statusList.add(new SimpleModel(Values.STATUS_DEACTIVE,
                Labels.getLabel(LanguageKeys.STATUS_LOCK)));

        statusList.add(new SimpleModel(Values.STATUS_NOT_READY,
                Labels.getLabel(LanguageKeys.STATUS_NOT_ACTIVE)));

        cbStatus.setModel(new ListModelList<SimpleModel>(statusList));
    }

    public void onSelect$cbStatus() {
        btnClearStatus.setVisible(true);
    }
    
    public void onClick$btnClearStatus() {
        cbStatus.setSelectedIndex(-1);
        btnClearStatus.setVisible(false);
    }
    
    public void onAfterRender$cbStatus() {
        cbStatus.setSelectedIndex(Values.FIRST_INDEX);

        refreshModel();
    }

    public void onClick$btnAdvanceSearch() {
        this.refreshModel();
    }

    public void onClick$btnImport(){
        Window win = (Window) 
                Executions.createComponents("/html/pages/manager_user/import_users.zul", 
                        winUser, _createParameterMap(null));

        win.doModal();
    }

    public void onOkWindow() {
        winUser.addEventListener(Events.ON_OK, new EventListener<Event>() {
            public void onEvent(Event t) throws Exception {
                refreshModel();
            }
        });
    }
    
    public void onOK$tbKeyword() {
        isAdvance = false;
        
        this.refreshModel();
    }
    
    public void onClick$btnBasicSearch() {
        isAdvance = false;
        
        this.refreshModel();
        
    }
    
    public void onClick$btnEnableAdvSearch() {
        advanceSearchPopup.open(btnEnableAdvSearch, ZkKeys.OVERLAP_END);
        
        tbUserName.setFocus(true);
        
        isAdvance = true;
    }
    
    public void onClick$btnAdvSearch(){
        isAdvance = true;
        
        this.refreshModel();
    }
    
    public void onOK$advanceSearchPopup(){
        isAdvance = true;
        
        this.refreshModel();
    }
    
    public void onLoadData(Event event) throws Exception {
        this.refreshModel();
    }

    public void onLoadDataAndReOpen(Event event) throws Exception {
        this.refreshModel();

        onClick$btnAdd();
    }

    public void refreshModel() {
        this.showHideButton();

        if (isAdvance) {
            this.advanceSearch();
        } else {
            this.basicSearch();
        }
    }

    public void showHideButton() {
        Long status = ComponentUtil.getComboboxValue(cbStatus);

        if (btnLock != null) {
            btnLock.setVisible(isAdvance && Values.STATUS_ACTIVE.equals(status));
        }

        if (btnUnlock != null) {
            btnUnlock.setVisible(isAdvance && Values.STATUS_DEACTIVE.equals(status));
        }

        if (btnDel != null) {
            btnDel.setVisible(isAdvance && Values.STATUS_NOT_READY.equals(status));
        }

        if (btnActivate != null) {
            btnActivate.setVisible(isAdvance && Values.STATUS_NOT_READY.equals(status));
        }
    }

    public void basicSearch() {
        String keyword = GetterUtil.getString(tbKeyword.getValue());

        paramMap.put("keyword", keyword);

        searchResultGrid.setItemRenderer(
                new UserRender(winUser, isAdmin));

        searchResultGrid.setModel(new UserListModel(
                searchResultGrid.getPageSize(), keyword, isAdvance,
                userService));

        searchResultGrid.setMultiple(true);
    }

    public void advanceSearch() {
        String userName = GetterUtil.getString(tbUserName.getValue());
        String email = GetterUtil.getString(tbEmail.getValue());
        Long gender = ComponentUtil.getComboboxValue(cbGender);
        String birthplace = GetterUtil.getString(tbBirthPlace.getValue());
        Date birthdayFrom = dbBirthdayFrom.getValue();
        Date birthdayTo = dbBirthdayTo.getValue();
        String phone = GetterUtil.getString(tbPhone.getValue());
        String mobile = GetterUtil.getString(tbMobile.getValue());
        String account = GetterUtil.getString(tbAccount.getValue());
        Long status = ComponentUtil.getComboboxValue(cbStatus);
        //create param map

        paramMap.put("userName", userName);
        paramMap.put("email", email);
        paramMap.put("gender", gender);
        paramMap.put("birthplace", birthplace);
        paramMap.put("birthdayFrom", birthdayFrom);
        paramMap.put("birthdayTo", birthdayTo);
        paramMap.put("phone", phone);
        paramMap.put("mobile", mobile);
        paramMap.put("account", account);
        paramMap.put("status", status);

        searchResultGrid.setItemRenderer(new UserRender(winUser, isAdmin));
        searchResultGrid.setModel(new UserListModel(searchResultGrid.getPageSize(),
                userName, email, gender, birthplace, birthdayFrom, birthdayTo,
                phone, mobile, account, status, isAdvance, userService));
    }

    public List<User> getListExport() {
        List<User> results = new ArrayList<User>();

        try {
            if (isAdvance) {
                String userName = GetterUtil.getString(paramMap.get("userName"));
                String email = GetterUtil.getString(paramMap.get("email"));
                Long gender = GetterUtil.getLong(paramMap.get("gender"));
                String birthplace = GetterUtil.getString(paramMap.get("birthplace"));
                Date birthdayFrom = (Date) paramMap.get("birthdayFrom");
                Date birthdayTo = (Date) paramMap.get("birthdayTo");
                String phone = GetterUtil.getString(paramMap.get("phone"));
                String mobile = GetterUtil.getString(paramMap.get("mobile"));
                String account = GetterUtil.getString(paramMap.get("account"));
                Long status = GetterUtil.getLong(paramMap.get("status"));

                results = userService.getUsers(userName, email, gender,
                        birthplace, birthdayFrom, birthdayTo, phone, mobile,
                        account, status, QueryUtil.GET_ALL,
                        QueryUtil.GET_ALL, null, null);

            } else {
                String keyword = (String) paramMap.get("keyword");

                results = userService.getUsers(keyword, QueryUtil.GET_ALL,
                        QueryUtil.GET_ALL, null, null);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return results;
    }

    public void onClick$btnExport() throws Exception {
        try {
            List<Object[]> headerInfors = getHeaderInfors();
            List<String> properties = getProperties();

            List<User> datas = getListExport();

            if (!datas.isEmpty()) {
                String title = Labels.getLabel(LanguageKeys.LIST_USER).
                        toUpperCase();

                ExcelUtil excelUtil =
                        new ExcelUtil<User>(getConvertMap());

                excelUtil.toSingleSheetXlsx(EXPORT_USER,
                        title, headerInfors, properties, datas);
            } else {
                Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_NO_RECORD_EXPORT),
                        Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                        Messagebox.ERROR);
            }

        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    /**
     * Hàm xử lý sự kiện khi click vào nút "Khóa". Hàm sẽ chuyển Users về trạng
     * thái khóa(không hoạt động), status=0
     *
     * @param event
     * @throws Exception
     */
    public void onLockUser(Event event) throws Exception {
        final User user = (User) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {
            public void onEvent(Event e) throws Exception {
                if (Messagebox.ON_OK.equals(e.getName())) {
                    try {
                        if (user.getUserId().equals(getUserId())) {
                            ComponentUtil.createErrorMessageBox(
                                    LanguageKeys.MESSAGE_CANNOT_LOCK_YOURSELF);

                            return;
                        }

                        userService.lockUser(user);

                        ComponentUtil.createSuccessMessageBox(
                                LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

                        refreshModel();
                    } catch (Exception ex) {
                        _log.error(ex.getMessage(), ex);

                        Messagebox.show(Labels.getLabel(
                                LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
                    }
                }
            }
        });
    }

    /**
     * Hàm xử lý sự kiện khi click vào nút "Mở khóa". Hàm sẽ chuyển Users về
     * trạng thái mở khóa (hoạt động), status=1
     *
     * @param event
     * @throws Exception
     */
    public void onUnlockUser(Event event) throws Exception {
        final User user = (User) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {
            public void onEvent(Event e) throws Exception {
                if (Messagebox.ON_OK.equals(e.getName())) {
                    try {
                        userService.unlockUser(user);

                        ComponentUtil.createSuccessMessageBox(
                                LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

                        refreshModel();
                    } catch (Exception ex) {
                        _log.error(ex.getMessage(), ex);

                        Messagebox.show(Labels.getLabel(
                                LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
                    }
                }
            }
        });
    }

    /**
     * Hàm xóa SysInstance khi click chọn nút "Xóa". SysInstance chỉ có thể xóa
     * sau khi Khóa (status=0)
     *
     * @param event
     * @throws Exception
     */
    public void onDeleteUser(Event event) throws Exception {
        final User user = (User) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {
            public void onEvent(Event e) throws Exception {
                if (Messagebox.ON_OK.equals(e.getName())) {
                    try {
                        if (user.getStatus().equals(Values.STATUS_NOT_READY)) {
                            userService.delete(user);

                            ComponentUtil.createSuccessMessageBox(
                                    LanguageKeys.MESSAGE_DELETE_SUCCESS);

                            refreshModel();
                        } else {
                            Messagebox.show(Values.getInUseMsg(
                                    Labels.getLabel(LanguageKeys.USER)));
                        }

                    } catch (Exception ex) {
                        _log.error(ex.getMessage(), ex);

                        Messagebox.show(Labels.getLabel(
                                LanguageKeys.MESSAGE_DELETE_FAIL));
                    }
                }
            }
        });
    }

    public void onClick$btnResetPwd() {
        final List<User> users = this.getUserSelected();

        if (Validator.isNull(users)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_RESET),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_RESET),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                public void onEvent(Event e) throws Exception {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        try {
                            userService.resetPassword(users);

                            ComponentUtil.createSuccessMessageBox(
                                    LanguageKeys.MESSAGE_UPDATE_SUCCESS);
                        } catch (Exception ex) {
                            _log.error(ex.getMessage(), ex);

                            Messagebox.show(Labels.getLabel(
                                    LanguageKeys.MESSAGE_UPDATE_FAIL));
                        }
                    }
                }
            });
        }
    }

    public void onClick$btnLock() {
        final List<User> users = this.getUserSelected();

        if (Validator.isNull(users)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                public void onEvent(Event e) throws Exception {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        try {
                            userService.lockUser(users);

                            ComponentUtil.createSuccessMessageBox(
                                    LanguageKeys.MESSAGE_LOCK_ITEM_SUCCESS);

                            refreshModel();
                        } catch (Exception ex) {
                            _log.error(ex.getMessage(), ex);

                            Messagebox.show(Labels.getLabel(
                                    LanguageKeys.MESSAGE_LOCK_ITEM_FAIL));
                        }
                    }
                }
            });
        }
    }

    public void onClick$btnUnlock() {
        final List<User> users = this.getUserSelected();

        if (Validator.isNull(users)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                public void onEvent(Event e) throws Exception {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        try {
                            userService.unlockUser(users);

                            ComponentUtil.createSuccessMessageBox(
                                    LanguageKeys.MESSAGE_UNLOCK_ITEM_SUCCESS);

                            refreshModel();
                        } catch (Exception ex) {
                            _log.error(ex.getMessage(), ex);

                            Messagebox.show(Labels.getLabel(
                                    LanguageKeys.MESSAGE_UNLOCK_ITEM_FAIL));
                        }
                    }
                }
            });
        }
    }

    public void onClick$btnDel() {
        final List<User> users = this.getUserSelected();

        if (Validator.isNull(users)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                public void onEvent(Event e) throws Exception {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        try {
                            List<String> userNotDel =
                                    userService.delete(users);

                            if (Validator.isNull(userNotDel)) {
                                ComponentUtil.createSuccessMessageBox(
                                        LanguageKeys.MESSAGE_DELETE_SUCCESS);
                            } else {
                                Messagebox.show(Values.getInUseMsg(
                                        Labels.getLabel(
                                        LanguageKeys.ACCOUNT,
                                        StringUtils.join(userNotDel, StringPool.COMMA))));
                            }

                            refreshModel();

                        } catch (Exception ex) {
                            _log.error(ex.getMessage(), ex);

                            Messagebox.show(Labels.getLabel(
                                    LanguageKeys.MESSAGE_DELETE_FAIL));
                        }
                    }
                }
            });
        }
    }

    public void onClick$btnActivate() {
        final List<User> users = this.getUserSelected();

        if (Validator.isNull(users)) {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_SELECT_RECORD),
                    Labels.getLabel(LanguageKeys.ERROR), Messagebox.OK,
                    Messagebox.EXCLAMATION);
        } else {
            Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_ACTIVATE),
                    Labels.getLabel(LanguageKeys.MESSAGE_INFOR_ACTIVATE),
                    Messagebox.OK | Messagebox.CANCEL,
                    Messagebox.QUESTION,
                    new EventListener() {
                public void onEvent(Event e) throws Exception {
                    if (Messagebox.ON_OK.equals(e.getName())) {
                        try {
                            userService.activateUser(users);

                            ComponentUtil.createSuccessMessageBox(
                                    LanguageKeys.MESSAGE_ACTIVATE_SUCCESS);

                            refreshModel();
                        } catch (Exception ex) {
                            _log.error(ex.getMessage(), ex);

                            Messagebox.show(Labels.getLabel(
                                    LanguageKeys.MESSAGE_ACTIVATE_FAIL));
                        }
                    }
                }
            });
        }
    }

    private List<User> getUserSelected() {
        List<User> users = new ArrayList<User>();

        for (Listitem item : searchResultGrid.getSelectedItems()) {
            User user = (User) item.getAttribute("data");

            if (Validator.isNotNull(user)) {
                users.add(user);
            }
        }

        return users;
    }

    public void onClick$btnAdd() {
        Window win = (Window) Executions.createComponents(EDIT_PAGE, winUser,
                _createParameterMap(null));

        win.doModal();
    }

    private Map<String, Object> _createParameterMap(User user) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, winUser);
        parameters.put(Constants.OBJECT, user);
        parameters.put(Constants.SECOND_OBJECT, true);

        return parameters;
    }

    //export
    public List<Object[]> getHeaderInfors() {
        List<Object[]> headerInfors = new ArrayList<Object[]>();

        headerInfors.add(new Object[]{
            Labels.getLabel(LanguageKeys.ORDINAL), 2000});
        headerInfors.add(new Object[]{
            Labels.getLabel(LanguageKeys.ACCOUNT), 8000});
        headerInfors.add(new Object[]{
            Labels.getLabel(LanguageKeys.EMAIL), 8000});
        headerInfors.add(new Object[]{
            Labels.getLabel(LanguageKeys.GENDER), 8000});
        headerInfors.add(new Object[]{
            Labels.getLabel(LanguageKeys.FULL_NAME), 6000});
        headerInfors.add(new Object[]{
            Labels.getLabel(LanguageKeys.STATUS), 6000});
        //

        return headerInfors;
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

    public Map<Integer, String[]> getConvertMap() {
        Map<Integer, String[]> convertMap = new HashMap<Integer, String[]>();

        String[] genderArray = new String[]{
            Labels.getLabel(LanguageKeys.MALE),
            Labels.getLabel(LanguageKeys.FEMALE)
        };
        
        String[] statusArray = new String[]{
            Labels.getLabel(LanguageKeys.STATUS_ACTIVE),
            Labels.getLabel(LanguageKeys.STATUS_LOCK)
        };

        convertMap.put(2, genderArray);
        convertMap.put(4, statusArray);

        return convertMap;
    }

    //get set service
    public UserService getUserService() {
        if (this.userService == null) {
            this.userService = (UserService) SpringUtil.getBean("userService");
            setUserService(this.userService);
        }
        return this.userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private transient UserService userService;

    private static final String EXPORT_USER = "export_user";
    private static final String EDIT_PAGE =
            "/html/pages/manager_user/edit.zul";
    private static final Logger _log =
            LogManager.getLogger(UserController.class);
}
