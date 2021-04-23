/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Window;

import com.evotek.qlns.common.UserWorkspace;
import com.evotek.qlns.event.MenuSelectedEventListener;
import com.evotek.qlns.model.Category;
import com.evotek.qlns.service.MainService;
import com.evotek.qlns.util.PermissionUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class MainController extends BasicController<Div>
        implements Serializable {

    private Div mainDiv;
    
    private Navbar mainMenu;
    private Menu mniUsername;
//    private Borderlayout mainLayout;
    private Include bodyLayout;
    private List<Category> categorys = new ArrayList<Category>();
    private TreeSet<Category> menus =
            new TreeSet<Category>();
    private Map<Long, TreeSet<Category>> menuItems =
            new HashMap<Long, TreeSet<Category>>();

    //notification
    private A aNotiTotal;
    private A aSeeAll;
    private Label lbNotiBag;
    private Label lbContractEx;
    private Label lbBirthDay;
    private Label lbCurrentYear;
    
    private UserWorkspace workspace;

    @Override
    public void doBeforeComposeChildren(Div comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.mainDiv = comp;
    }

    @Override
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp);
        //init menu trang chu
        initMenu();
        
        initNotificaion();
        
        initFooter();
    }

    public void initMenu() throws Exception {
        //hien thi ten nguoi dang nhap o goc phai man hinh _ manhnn1
        this.mniUsername.setLabel(Labels.getLabel(LanguageKeys.MESSAGE_WELLCOME_USER,
                new String[]{getUser().getUserName()}));
        this.mniUsername.setTooltiptext(Labels.getLabel(LanguageKeys.MESSAGE_AVATAR));

        this.workspace = getUserWorkspace();

        List<String> roles = this.workspace.getRoles();

        if (PermissionUtil.isAdministrator(roles)) {
            this.categorys = this.mainService.getAllCategory();
        } else {
            this.categorys = this.mainService.getCategoryByUser(getUser());
        }

        this._doGroup();

        this._buildMenu();

        this._redirectPage();
    }


    private void _doGroup() throws Exception {
        for (Category category : this.categorys) {
            Long type = category.getType();

            if (type.equals(Values.MENU_TYPE_ITEM)) {

                Long parentId = category.getParentId();

                if(Validator.isNull(parentId)){
                    this.menus.add(category);

                    continue;
                }

                TreeSet<Category> childs = this.menuItems.get(parentId);

                if (Validator.isNull(childs)) {
                    childs = new TreeSet<Category>();

                    Category menu = this.mainService.getCategoryById(parentId);

                    if(Validator.isNotNull(menu)){
                        this.menus.add(menu);
                    }
                }

                childs.add(category);

                this.menuItems.put(parentId, childs);
            }
        }
    }

    private void _buildMenu() {
        //main
//        mainMenu.appendChild(_createMenuItem(LanguageKeys.MENU_MAIN,
//                PermissionConstants.MENU_ITEM_MAIN_PAGE, _getHomePageSource()));

        for (Category category : this.menus) {
            if (category.getType().equals(Values.MENU_TYPE_CATEGORY)) {
//                Nav menu = _createMenu(category);

                TreeSet<Category> items = this.menuItems.get(category.getCategoryId());

                if (Validator.isNotNull(items)) {
//                    for (Category item : items) {
//                        Navitem menuitem = _createMenuItem(item);
//
//                        menu.appendChild(menuitem);
//                    }
                    this.mainMenu.appendChild(_createMenu(category, items));
                }

//                mainMenu.appendChild(menu);
            } else {
                this.mainMenu.appendChild(_createMenuItem(category));
            }
        }
    }

    private Navitem _createMenuItem(Category category) {
        return _createMenuItem(category.getLanguageKey(),
                category.getFolderName(), category.getViewPage());
    }

    private Navitem _createMenuItemUrl(String key, String folderName,
            String srcPage) {
        Navitem item = new Navitem();

        if (Validator.isNotNull(key)) {
            item.setLabel(Labels.getLabel(key));
        }

        item.setId(folderName);

        item.addEventListener(Events.ON_CLICK, new MenuSelectedEventListener(
                this.bodyLayout, srcPage));

        return item;
    }
    
    private Navitem _createMenuItemUrl(String key, String folderName,
            String srcPage, Map<String, Object> parameters) {
        Navitem item = new Navitem();

        if (Validator.isNotNull(key)) {
            item.setLabel(Labels.getLabel(key));
        }

        item.setId(folderName);

        item.addEventListener(Events.ON_CLICK, new MenuSelectedEventListener(
                this.bodyLayout, srcPage, parameters));

        return item;
    }

    private Navitem _createMenuItem(String key, String folderName,
            String pageView) {
        return _createMenuItemUrl(key, folderName, getPageSource(folderName, pageView));
    }
    
    private Navitem _createMenuItem(String key, String folderName,
            String pageView, Map<String, Object> parameters) {
        return _createMenuItemUrl(key, folderName, 
                getPageSource(folderName, pageView), parameters);
    }

//    private Nav _createMenu(Category category) {
//        Nav menu = new Nav();
//
//        menu.setLabel(Labels.getLabel(category.getLanguageKey()));
//        menu.setId(category.getFolderName());
//
//        return menu;
//    }
    private Navitem _createMenu(Category category, TreeSet<Category> items) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.MENU_ITEMS, items);
        
        Navitem menu = _createMenuItem(category.getLanguageKey(),
                category.getFolderName(), category.getViewPage(), parameters);
        
        return menu;
    }

    private void _redirectPage() {
//        if (workspace.isAdministrator()) {
//            bodyLayout.setSrc(HOME_PAGE);
//
//            return;
//        }

        boolean defautl = true;

        for (Category category : this.categorys) {
            if (category.getType().equals(Values.MENU_TYPE_CATEGORY)) {
                continue;
            }

            String folderName = category.getFolderName();

            List<String> rights = new ArrayList<String>();

            rights.add(folderName);
//            rights.add("home_" + folderName);

            if (this.workspace.isAllowed(rights)) {
                this.bodyLayout.setSrc(getPageSource(category.getFolderName(),
                        category.getViewPage()));

                defautl = false;

                break;
            }
        }

//        if (defautl) {
//            bodyLayout.setSrc(_getHomePageSource());
//        }
    }

//    private String _getHomePageSource() {
//        return HOME_PAGE;
//    }

    private String getPageSource(String folder, String pageView){
        StringBuilder sb = new StringBuilder(4);

        sb.append(Constants.CATEGORY_FOLDER);

        if (Validator.isNotNull(folder)) {
            sb.append(folder);
            sb.append(StringPool.FORWARD_SLASH);
        }

        sb.append(pageView);
        sb.append(StringPool.ZUL);

        return sb.toString();
    }

    /**
     * When the 'Logout' button is clicked.<br>
     *
     * @throws IOException
     */
    public void onClick$logout() throws IOException {
        getUserWorkspace().doLogout(); // logout.
    }

    /**
     * When the 'User Information' button is clicked.<br>
     *
     * @throws IOException
     */
    public void onClick$userDetail() throws IOException {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(Constants.PARENT_WINDOW, this.mainDiv);
        parameters.put(Constants.TITLE, Labels.getLabel(
                LanguageKeys.USER_INFOMATION));
        parameters.put(Constants.OBJECT, getUser());

        Window win = (Window) Executions.createComponents(USER_DETAIL_PAGE, this.mainDiv,
                parameters);

        win.doModal();
    }

    //notification
    public void initNotificaion(){
        int cteCount = this.mainService.getContractExpiredCount();// contract expired count
        int bdCount = this.mainService.getBirthDayCount();// birthday count
        
        this.lbNotiBag.setValue(String.valueOf(cteCount+bdCount));
        this.aNotiTotal.setLabel(Labels.getLabel(LanguageKeys.MESSAGE_NOTIFICATION_TOTAL, 
                new Object[]{cteCount+bdCount}));
        this.lbContractEx.setValue(StringPool.PLUS+cteCount);
        this.lbBirthDay.setValue(StringPool.PLUS+ bdCount);
        
        if(cteCount+bdCount <= 0){
            this.aSeeAll.setVisible(false);
        } else {
            this.aSeeAll.setVisible(true);
        }
    }
    
    public void initFooter(){
        this.lbCurrentYear.setValue(String.valueOf(
                Calendar.getInstance().get(Calendar.YEAR)));
    }
    
    public void onClick$aSeeAll() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put(Constants.PARENT_WINDOW, this.mainDiv);

        Window win = (Window) Executions.createComponents(
                VIEW_ALL_NOTIFICATION, null, params);
        
        win.doModal();
    }
    
    public void onTimer$timer(){
        initNotificaion();
    }
    
//    public void onClick$aGotoTop(){
//        Clients.evalJavaScript("goToTop()");
//    }
    
    public void onUpdateNotification(Event event) throws Exception{
        initNotificaion();
    }
    //get set method
    private transient MainService mainService;

    public MainService getMainService() {
        if (this.mainService == null) {
            this.mainService = (MainService) SpringUtil.getBean("mainService");

            setMainService(this.mainService);
        }

        return this.mainService;
    }

    public void setMainService(MainService mainService) {
        this.mainService = mainService;
    }
    //get set method
    private static final long serialVersionUID = 1366774306343L;

//    private static final String HOME_PAGE = "/html/pages/home/home.zul";
    private static final String USER_DETAIL_PAGE = "/html/pages/manager_user/edit.zul";
    private static final String VIEW_ALL_NOTIFICATION = "/html/pages/notification/view.zul";
    
}
