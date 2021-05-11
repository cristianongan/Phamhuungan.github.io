/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util.key;

/**
 *
 * @author linhlh2
 */
public class Constants {
	public static interface Action {

		public static final String ADD = "ACTION_ADD";

		public static final String EDIT = "ACTION_EDIT";
	}

	public static interface Attr {

		public static final String DATA = "data";

		public static final String EDIT_OBJECT = "editObject";

		public static final String ID = "Id";

		public static final String INDEX = "index";

		public static final String KEY = "key";

		public static final String LIST_PARAMETER = "listParameter";

		public static final String MAP_PARAMETER = "mapParameter";

		public static final String MODEL = "model";

		public static final String OBJECT = "object";

		public static final String OBJECT_ID = "objectId";

		public static final String PARENT_WINDOW = "parentWindow";

		public static final String SECOND_OBJECT = "secondObject";

		public static final String SELF_UPDATE = "selfUpdate";

		public static final String TITLE = "title";

		public static final String VALUE = "value";
	}

	public static interface Class {

		public static final String LINK_BUTTON = "link-button";

		public static final String LINK_BUTTON_CL = "link-button-cl";

		public static final String NO_STYLE = "no-style";

		public static final String PROCESS_MENU_BAR = "processMenuBar";

		public static final String TASK_DEFAULT = "task-default";

		public static final String TASK_GREEN = "task-green";

		public static final String TASK_ORANGE = "task-orange";

		public static final String TASK_PINK = "task-pink";
	}

	public static interface ContentType {

		public static final String TEXT_CSS = "text/css";

		public static final String TEXT_HTML = "text/html";

		public static final String TEXT_JAVASCRIPT = "text/javascript";

		public static final String TEXT_PLAIN = "text/plain";

		public static final String TEXT_WML = "text/wml";

		public static final String TEXT_XML = "text/xml";
	}

	/**
	 * Email template id
	 */

	public static interface Email {

		public static final String RESET_PASSWORD = "RESET_PASSWORD";

		public static final String SSL = "ssl";

		public static final String TSL = "tsl";

		public static final String USER_CREATE = "USER_CREATE";

		public static final String USER_REGISTER = "USER_REGISTER";

		public static final String VERIFY_RESET_PASSWORD = "VERIFY_RESET_PASSWORD";

		public static final String VERIFY_RESET_PASSWORD_USER_DEACTIVE = "VERIFY_RESET_PASSWORD_USER_DEACTIVE";
	}

	public static interface Icon {

		public static final String ADD = "~./images/icons/add.png";

		public static final String ASSIGN = "~./images/icons/assign.png";

		public static final String ATTACH = "~./images/icons/attach.png";

		public static final String DELETE = "~./images/icons/delete.png";

		public static final String DOWNLOAD = "~./images/icons/download.png";

		public static final String EDIT = "~./images/icons/edit.png";

		public static final String EDIT_ADD = "~./images/icons/edit_add.png";

		public static final String KEY = "~./images/icons/key.png";

		public static final String LOCK = "~./images/icons/lock.png";

		public static final String OK = "~./images/icons/ok.png";

		public static final String PENDING = "~./images/icons/pending.png";

		public static final String PUBLIC = "~./images/icons/public.png";

		public static final String REFERENCE = "~./images/icons/reference.png";

		public static final String REMOVE = "~./images/icons/remove.png";

		public static final String RESOURCE = "~./images/icons/resource.png";

		public static final String STAR = "~./images/icons/star.png";

		public static final String UNLOCK = "~./images/icons/unlock.png";
	}

	public static interface Message {

		public static final String MSG_ERROR = "error";

		public static final String MSG_INFORMATION = "information";

		public static final String MSG_SUCCESS = "success";

		public static final String MSG_WARNING = "warning";
	}

	public static interface Page {

		public static interface Admin {

			public static final String DEFAULT = "~./pages/admin/default.zul";
		}

		public static interface Common {

			public static final String COMMON_DEPT = "~./pages/common/commonDepartment.zul";

			public static final String COMMON_GRID_USER = "~./pages/common/commonGridUser.zul";

			public static final String DEPT_TREE = "~./pages/common/departmentTree.zul";

			public static final String MESSAGE_BOX = "~./pages/common/messageBox.zul";

			public static final String PREVIEW_FILE = "~./pages/common/previewFile.zul";

			public static final String TREE_DOCUMENT_TYPE = "~./pages/common/documentTypeTree.zul";
		}

		public static interface Department {

			public static final String ADD_EDIT = "~./pages/department/edit.zul";

			public static final String VIEW = "~./pages/department/view.zul";
		}

		public static interface DocumentType {

			public static final String ADD_EDIT = "~./pages/document_type/edit.zul";

			public static final String VIEW = "~./pages/document_type/view.zul";

		}

		public static interface DocumentManagement {

			public static final String ADD_EDIT = "~./pages/document_management/edit.zul";

			public static final String DETAIL = "~./pages/document_management/detail.zul";
		}

		public static interface HumanResourceManagement {

			public static final String ADD_EDIT = "~./pages/human_resource_management/edit.zul";

			public static final String ADD_EDIT_CONTRACT_TYPE = "~./pages/human_resource_management/editContractType.zul";

			public static final String ADD_EDIT_JOB = "~./pages/human_resource_management/editJob.zul";

			public static final String ADD_EDIT_SALARY_LANDMARK = "~./pages/human_resource_management/editSalaryLandmark.zul";

			public static final String ADD_EDIT_WORK_PROCESS = "~./pages/human_resource_management/editWorkProcess.zul";

			public static final String DETAIL = "~./pages/human_resource_management/detail.zul";
		}

		public static interface Menu {

			public static final String ADD_EDIT = "~./pages/menu/edit.zul";

			public static final String EDIT_RIGHT = "~./pages/menu/edit_right.zul";

			public static final String RESOURCE_ACTION = "~./pages/menu/resource_action.zul";

			public static final String RIGHT = "~./pages/menu/right.zul";

			public static final String ASSIGN_RIGHT = "~./pages/menu/assign_right.zul";

			public static final String ADD_EDIT_GROUP = "~./pages/menu/edit_group.zul";

			public static final String ADD_EDIT_RIGHT = "~./pages/menu/edit_right.zul";
		}

		public static interface RoleManagement {

			public static final String ADD_EDIT = "~./pages/role_management/edit.zul";

			public static final String PERMISSION = "~./pages/role_management/permission.zul";
		}

		public static interface UserManagement {

			public static final String ADD_EDIT = "~./pages/user_management/edit.zul";

			public static final String ADD_ROLE = "~./pages/user_management/add_role.zul";

			public static final String ASSIGNED_ROLE = "~./pages/user_management/assigned_role.zul";

			public static final String IMPORT_USERS = "~./pages/user_management/import_users.zul";
		}

		public static interface Notification {

			public static final String VIEW = "~./pages/notification/view.zul";
		}

		public static final String ROOT_FOLDER = "~./pages/";
	}

	public static interface Sclass {

		public static final String BLUE = "blue";

		public static final String BTN_DANGER = "btn-danger";

		public static final String BTN_INFO = "btn-info";

		public static final String BTN_PRIMARY = "btn-primary";

		public static final String BTN_SUCCESS = "btn-success";

		public static final String CONTRACT = "btn btn-xs no-hover btn-pink z-icon-copy";

		public static final String DATE = "btn btn-xs no-hover btn-success z-icon-calendar";

		public static final String NO_STYLE = "no-style";

		public static final String ORANGE = "orange";

		public static final String PURPLE = "purple";

		public static final String RED = "red";
	}

	public static interface Style {

		public static final String BORDER_NONE = "border:none;";

		public static final String COLUMN_MULTILINE = "white-space: normal;word-wrap:break-word;text-wrap:none;";

		public static final String FONT_STYLE_ITALIC = "font-style: italic;";

		public static final String FONT_WEIGHT_BOLD = "font-weight: bold;";

		public static final String NO_PADDING = "padding:0;";

		public static final String TEXT_ALIGN_CENTER = "text-align:center;";

		public static final String TEXT_ALIGN_LEFT = "text-align:left;";

		public static final String TEXT_ALIGN_RIGHT = "text-align:right;";
	}

	public static interface Tooltip {

		public static final String ASSIGN_RIGHT = "assignRightTooltip";

		public static final String ASSIGN_ROLE = "assignRoleTooltip";

		public static final String CALCEL = "calcelTooltip";

		public static final String DEL = "delTooltip";

		public static final String DETAIL = "detailTooltip";

		public static final String EDIT = "editTooltip";

		public static final String LOCK = "lockTooltip";

		public static final String RIGHT = "rightTooltip";

		public static final String UNLOCK = "unlockTooltip";
	}

	/**
	 * Authentication type
	 */

	public static interface Zicon {

		public static final String EDIT = "z-icon-edit";

		public static final String KEY = "z-icon-key";

		public static final String LEVEL_UP = "z-icon-level-up";

		public static final String LOCK = "z-icon-lock";

		public static final String PAPERCLIP = "z-icon-paperclip";

		public static final String PENCIL = "z-icon-pencil";

		public static final String PLUS = "z-icon-plus";

		public static final String RANDOM = "z-icon-random";

		public static final String SAVE = "z-icon-save";

		public static final String SEARCH_PLUS = "z-icon-search-plus";

		public static final String SHARE = "z-icon-share";

		public static final String TIMES = "z-icon-times";

		public static final String TRASH_O = "z-icon-trash-o";

		public static final String UNLOCK = "z-icon-unlock";
	}

}
