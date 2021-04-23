package com.evotek.qlns.application;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.evotek.qlns.service.DocumentTypeService;

/**
 * EN: Sets the real path of the started application in the
 * ApplicationWorkspace. it's been declared as a ServletListener in the web.xml
 * configuration file. <br>
 */
// @WebListener --> no need for declaration in web.xml
public class InitApplicationWorkspace implements ServletContextListener {

    // Scheduler for periodically starts a db cleaning job
    private ServletContext context;
    private DocumentTypeService documentTypeService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();
        
        //init document type map
        WebApplicationContext applicationContext =
            WebApplicationContextUtils
            .getWebApplicationContext(context);
        
        documentTypeService = (DocumentTypeService)
                    applicationContext.getBean("documentTypeService");
        
        documentTypeService.getDocTypeMap(context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
