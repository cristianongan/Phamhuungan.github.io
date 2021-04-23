/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Nav;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.A;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.evotek.qlns.extend.Messagebox;
import com.evotek.qlns.model.Department;
import com.evotek.qlns.model.Job;
import com.evotek.qlns.model.Staff;
import com.evotek.qlns.model.list.StaffListModel;
import com.evotek.qlns.model.render.StaffRender;
import com.evotek.qlns.service.DepartmentService;
import com.evotek.qlns.service.StaffService;
import com.evotek.qlns.util.ComponentUtil;
import com.evotek.qlns.util.GetterUtil;
import com.evotek.qlns.util.StringPool;
import com.evotek.qlns.util.Validator;
import com.evotek.qlns.util.key.Constants;
import com.evotek.qlns.util.key.LanguageKeys;
import com.evotek.qlns.util.key.ZkKeys;

/**
 *
 * @author PC
 */
public class StaffController extends BasicController<Hlayout>
        implements Serializable {

    private Hlayout winStaff;

    private Navbar navbar;
    private Navitem selectedNavitem;
    
    private Div sidebar;
    
    private A toggler;
    
    private Popup advanceSearchPopup;
    
    private Textbox tbStaffName;
    private Longbox lgbYearOfBirth;
    private Textbox tbEmail;
    private Textbox tbPhone;
    
    //Bandbox Department
    private Bandbox bbDepartment;
    
    private Include bodyLayout;
    private Include icDepartment;
    
    private A btnClearDept;
    private A btnClearJob;
    
    private Combobox cbJobTitle;
    
    private Button btnEnableAdvSearch;
    private Button btnPreviousPage;
    private Button btnNextPage;

    private Auxheader ahGeneral;
    private Auxheader ahContract;
    private Auxheader ahInsurance;
    private Auxheader ahQualifications;
    private Auxheader ahIdentification;
    private Auxheader ahContact;

    //thong tin chung
    private Listheader lhDepartmentName;
    private Listheader lhJobTitleName;
    private Listheader lhWorkDate;
    private Listheader lhDateOfBirth;
    private Listheader lhPermanentResidence;
    private Listheader lhCurrentResidence;
    private Listheader lhStatus;
    private Listheader lhNote;
    //hop dong
    private Listheader lhContractType;
    private Listheader lhContractFromDate;
    private Listheader lhContractToDate;
    private Listheader lhContractNumber;
    private Listheader lhTaxCode;
    //bao hiem
    private Listheader lhSalaryBasic;
    private Listheader lhInsurancePaidDate;
    private Listheader lhInsuranceBookNumber;
    private Listheader lhPaidPlace;
    //Trinh do chuyen mon
    private Listheader lhLevels;
    private Listheader lhMajors;
    private Listheader lhCollege;
    //CMND
    private Listheader lhIdentityCard;
    private Listheader lhGrantDate;
    private Listheader lhGrantPlace;
    //Lien he
    private Listheader lhTelephone;
    private Listheader lhEmail;
    //group array
    private Auxheader[] firstAhGroupPage;
    private Auxheader[] secondAhGroupPage;
    private Auxheader[] thirdAhGroupPage;

    private Listheader[] firstLhGroup;
    private Listheader[] secondLhGroup;
    private Listheader[] thirdLhGroup;

    private Textbox tbKeyword;
    
    private Listbox listboxResult;
    
    private Map<String, Object> paramMap = new HashMap<String, Object>();
    private Map<Long, List<Department>> paramDept = new HashMap<Long, List<Department>>();
    
    private boolean isAdvance;

    @Override
    public void doBeforeComposeChildren(Hlayout comp) throws Exception {
        super.doBeforeComposeChildren(comp);

        this.winStaff = comp;
    }

    @Override
    public void doAfterCompose(Hlayout comp) throws Exception {
        super.doAfterCompose(comp);

        bodyLayout = (Include) winStaff.getParent();
        
        bodyLayout.setSclass("bodylayout-min");
        
        initData();
        
        refreshModel();
    }

    public void initData() {
        firstAhGroupPage = new Auxheader[]{
            ahGeneral
        };
        secondAhGroupPage = new Auxheader[]{
            ahContract, ahInsurance
        };
        thirdAhGroupPage = new Auxheader[]{
            ahQualifications, ahIdentification, ahContact
        };

        firstLhGroup = new Listheader[]{
            lhDepartmentName, lhJobTitleName, lhWorkDate,
            lhDateOfBirth, lhPermanentResidence, lhCurrentResidence,
            lhStatus, lhNote
        };
        secondLhGroup = new Listheader[]{
            lhContractType, lhContractFromDate, lhContractToDate,
            lhContractNumber, lhTaxCode, lhSalaryBasic, lhInsurancePaidDate,
            lhInsuranceBookNumber, lhPaidPlace
        };
        thirdLhGroup = new Listheader[]{
            lhLevels, lhMajors, lhCollege, lhIdentityCard,
            lhGrantDate, lhGrantPlace, lhTelephone, lhEmail
        };

        listboxResult.setAttribute("page", 1);

        _showHideAh(secondAhGroupPage, false);
        _showHideAh(thirdAhGroupPage, false);

        _showHideLh(secondLhGroup, false);
        _showHideLh(thirdLhGroup, false);
    }

    public void onCreate$navbar() {
        List<Department> roots = departmentService.getDepartmentByParentId(null);

        paramDept.put(null, roots);
        
        for (Department root : roots) {
            if (Validator.isNull(root)) {
                continue;
            }

            createDeptNav(root, navbar);
        }
    }
    
    private void createDeptNav(Department dept, Component parentNode){
        List<Department> childs = departmentService.getDepartmentByParentId(
                dept.getDeptId());
        
        if(Validator.isNull(childs)){
            final Navitem navItem = new Navitem();
            
            navItem.setLabel(dept.getDeptName());
            
            if(Validator.isNotNull(dept.getIcon())){
                navItem.setIconSclass(dept.getIcon());
            } else {
                navItem.setIconSclass("z-icon-angle-double-right");
            }
            
            navItem.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

                public void onEvent(Event t) throws Exception {
                    navItem.setSelected(true);
                }
            });
            
            navItem.addForward(Events.ON_CLICK, winStaff, "onSelectDept", 
                    new Object[]{navItem, dept});
            
            parentNode.appendChild(navItem);
        } else {
            paramDept.put(dept.getDeptId(), childs);
            
            Nav nav = new Nav(dept.getDeptName());
            
            if(Validator.isNotNull(dept.getIcon())){
                nav.setIconSclass(dept.getIcon());
            } else {
                nav.setIconSclass("z-icon-angle-double-right");
            }
            
            for (Department child : childs) {
                if (Validator.isNull(child)) {
                    continue;
                }
                
                createDeptNav(child, nav);
            }
            
            nav.addForward(Events.ON_OPEN, winStaff, "onSelectDept", 
                    new Object[]{nav, dept});
            
            parentNode.appendChild(nav);
        }
    }
    
    public void onClick$toggler() {
        if (navbar.isCollapsed()) {
            sidebar.setSclass("sidebar");
            navbar.setCollapsed(false);
            toggler.setIconSclass("z-icon-angle-double-left");
            bodyLayout.setSclass("bodylayout-max");
        } else {
            sidebar.setSclass("sidebar sidebar-min");
            navbar.setCollapsed(true);
            toggler.setIconSclass("z-icon-angle-double-right");
            bodyLayout.setSclass("bodylayout-min");
        }
        // Force the hlayout contains sidebar to recalculate its size
        Clients.resize(sidebar.getRoot().query("#main"));
    }
    //new
    
    //combobox jobtitle
    public void onCreate$cbJobTitle() throws Exception {
        List<Job> jobs = staffService.getJobTitle();

        cbJobTitle.setModel(new ListModelList<Job>(jobs));
    }
    
    public void onSelect$cbJobTitle() {
        btnClearJob.setVisible(true);
    }
    
    public void onClick$btnClearJob() {
        cbJobTitle.setSelectedIndex(-1);
        btnClearJob.setVisible(false);
    }
    
    //Bandbox documentType
    public void onClick$btnClearDept() {
        bbDepartment.setValue(StringPool.BLANK);
        bbDepartment.setAttribute(Constants.ID, null);
        
        btnClearDept.setDisabled(true);
        btnClearDept.setVisible(false);
    }
    
    public void onOpen$bbDepartment(){
        if(bbDepartment.isOpen() 
                && Validator.isNull(icDepartment.getSrc())) {
            icDepartment.setAttribute("bandbox", bbDepartment);
            icDepartment.setAttribute("btnclear", btnClearDept);
            
            icDepartment.setSrc(Constants.TREE_DEPARTMENT_PAGE);
        }
    }

    //Bandbox documentType
    
    public void basicSearch() {
        String keyword = GetterUtil.getString(tbKeyword.getValue());

        paramMap.put("keyword", keyword);

        ListModel model = new StaffListModel(listboxResult.getPageSize(),
                keyword, isAdvance, false, null, staffService);
        
        listboxResult.setModel(model);
        
        listboxResult.setItemRenderer(
                new StaffRender(winStaff, model));

        listboxResult.setMultiple(true);
    }
    
    public void advanceSearch() {
        String staffName = GetterUtil.getString(tbStaffName.getValue());
        Long  yearOfBirth = GetterUtil.getLong(lgbYearOfBirth.getValue());
        Department dept = (Department) bbDepartment.
                    getAttribute(Constants.OBJECT);
        String email = GetterUtil.getString(tbEmail.getValue());
        Job job = cbJobTitle.getSelectedItem()!=null ? (Job) cbJobTitle.getSelectedItem().
                    getAttribute("data") : null;
        String phone = GetterUtil.getString(tbPhone.getValue());
        
        paramMap.put("staffName", staffName);
        paramMap.put("yearOfBirth", yearOfBirth);
        paramMap.put("dept", dept);
        paramMap.put("email", email);
        paramMap.put("job", job);
        paramMap.put("phone", phone);
        
        ListModel model = new StaffListModel(listboxResult.getPageSize(),
                staffName, yearOfBirth, dept, email, job, phone, isAdvance, false, 
                null, staffService);
        
        listboxResult.setModel(model);
        
        listboxResult.setItemRenderer(
                new StaffRender(winStaff, model));

        listboxResult.setMultiple(true);
    }
    
    public void refreshModel() {
        navbar.clearSelection();
        
        if (isAdvance) {
            this.advanceSearch();
        } else {
            this.basicSearch();
        }
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
        
        tbStaffName.setFocus(true);
        
        isAdvance = true;
    }
    
    public void onClick$btnAdvSearch(){
        advanceSearch();
    }
    
    public void onOK$advanceSearchPopup(){   
        advanceSearch();
    }
    
    public void onClick$btnAdd() {
        Map map = new HashMap();

        map.put(Constants.PARENT_WINDOW, winStaff);
        map.put(Constants.OBJECT, null);

        Window win = (Window) Executions.createComponents(
                ADD_EDIT_HUMAN_RESOURCE_URL, null, map);

        win.doModal();
    }
    
    public void onClick$btnPreviousPage() {
        int _page = GetterUtil.getIntegerValue(
                listboxResult.getAttribute("page"), 1);

        switch (_page) {
            case 1:
                _showHideAh(firstAhGroupPage, false);
                _showHideLh(firstLhGroup, false);

                _showHideAh(thirdAhGroupPage, true);
                _showHideLh(thirdLhGroup, true);

                listboxResult.setAttribute("page", 3);

                break;
            case 2:
                _showHideAh(secondAhGroupPage, false);
                _showHideLh(secondLhGroup, false);

                _showHideAh(firstAhGroupPage, true);
                _showHideLh(firstLhGroup, true);

                listboxResult.setAttribute("page", 1);

                break;
            default:
                _showHideAh(thirdAhGroupPage, false);
                _showHideLh(thirdLhGroup, false);

                _showHideAh(secondAhGroupPage, true);
                _showHideLh(secondLhGroup, true);

                listboxResult.setAttribute("page", 2);

                break;
        }
    }

    public void onClick$btnNextPage() {
        int _page = GetterUtil.getIntegerValue(
                listboxResult.getAttribute("page"), 1);

        switch (_page) {
            case 1:
                _showHideAh(firstAhGroupPage, false);
                _showHideLh(firstLhGroup, false);

                _showHideAh(secondAhGroupPage, true);
                _showHideLh(secondLhGroup, true);

                listboxResult.setAttribute("page", 2);

                break;
            case 2:
                _showHideAh(secondAhGroupPage, false);
                _showHideLh(secondLhGroup, false);

                _showHideAh(thirdAhGroupPage, true);
                _showHideLh(thirdLhGroup, true);

                listboxResult.setAttribute("page", 3);

                break;
            default:
                _showHideAh(thirdAhGroupPage, false);
                _showHideLh(thirdLhGroup, false);

                _showHideAh(firstAhGroupPage, true);
                _showHideLh(firstLhGroup, true);

                listboxResult.setAttribute("page", 1);

                break;
        }
    }

    public void onClick$btnDeptManager(){
        Map map = new HashMap();
        
        map.put(Constants.PARENT_WINDOW, winStaff);

        Window win = (Window) Executions.createComponents(DEPARTMENT_PAGE, 
                null, map);
        
        win.doModal();
    }
    
    public void onLockStaff(Event event) throws Exception {
        final Staff staff = (Staff) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_LOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_LOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener<Event>() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.lockStaff(staff);

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

    public void onUnlockStaff(Event event) throws Exception {
        final Staff staff = (Staff) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_UNLOCK),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_UNLOCK),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.unlockStaff(staff);

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

    public void onDeleteStaff(Event event) throws Exception {
        final Staff staff = (Staff) event.getData();

        Messagebox.show(Labels.getLabel(LanguageKeys.MESSAGE_QUESTION_DELETE),
                Labels.getLabel(LanguageKeys.MESSAGE_INFOR_DELETE),
                Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new EventListener() {

                    public void onEvent(Event e) throws Exception {
                        if (Messagebox.ON_OK.equals(e.getName())) {
                            try {
                                staffService.deleteStaff(staff);

                                 ComponentUtil.createSuccessMessageBox(
                                         LanguageKeys.MESSAGE_DELETE_SUCCESS);

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
    
    public void onSelectDept(Event event) {
        Object[] ob = (Object[]) event.getData();
        
        Component comp = (Component) ob[0];
        Department dept = (Department) ob[1];

        if (comp instanceof Nav) {
            Nav nav = (Nav) comp;
            
            if(!nav.isOpen()){
                return;
            } else if (selectedNavitem!=null){
                selectedNavitem.setSelected(false);
            }
        } else if(comp instanceof Navitem){
            selectedNavitem = (Navitem) comp;
        }
        
        List<Long> deptIds = new ArrayList<Long>();

        this.getListDeptChild(dept, deptIds);
        
        paramMap.put("deptIds", deptIds);

        ListModel model = new StaffListModel(listboxResult.getPageSize(), 
                StringPool.BLANK, true, true, deptIds, staffService);
        
        listboxResult.setModel(model);
        
        listboxResult.setItemRenderer(
                new StaffRender(winStaff, model));

        listboxResult.setMultiple(true);
    }
    
    public void onLoadData(Event event) throws Exception {
        this.refreshModel();
    }
    
    public void onLoadPage(Event event) throws Exception {
        Component comp = winStaff.getParent();
        
        if(comp instanceof Include){
            Include inc = (Include) comp;
            
            String src = inc.getSrc();
            
            inc.setSrc(null);
            inc.setSrc(src);
        }
    }
    
    private List<Long> getListDeptChild(Department deptParent, 
            List<Long> deptIds) {
        if(Validator.isNull(deptParent)){
            return deptIds;
        }

        Long deptId = deptParent.getDeptId();
        
        List<Department> childs = paramDept.get(deptId);

        if (Validator.isNull(childs)) {
            deptIds.add(deptId);
        } else {
            deptIds.add(deptId);
            
            for (Department child : childs) {
                getListDeptChild(child, deptIds);
            }
        }
        
        return deptIds;
    }
    
    private void _showHideAh(Auxheader[] ahGroup, boolean visible) {
        for (Auxheader ah : ahGroup) {
            ah.setVisible(visible);
        }

//        listboxResult.invalidate();
    }

    private void _showHideLh(Listheader[] lhGroup, boolean visible) {
        for (Listheader lh : lhGroup) {
            lh.setVisible(visible);
        }

//        listboxResult.invalidate();
    }

    //get set service
    public StaffService getStaffService() {
        if (staffService == null) {
            staffService = (StaffService) SpringUtil.getBean("staffService");
            setStaffService(staffService);
        }
        return staffService;
    }

    public void setStaffService(StaffService StaffService) {
        this.staffService = StaffService;
    }

    public DepartmentService getDepartmentService() {
        if (departmentService == null) {
            departmentService = (DepartmentService) SpringUtil.getBean("departmentService");
            
            setDepartmentService(departmentService);
        }
        
        return departmentService;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    
    private transient StaffService staffService;
    private transient DepartmentService departmentService;

    private static final String ADD_EDIT_HUMAN_RESOURCE_URL = 
            "/html/pages/manager_human_resource/edit.zul";
    private final String DEPARTMENT_PAGE                    = 
            "/html/pages/department/view.zul";
    
    private static final Logger _log                        = 
            LogManager.getLogger(StaffController.class);
}
