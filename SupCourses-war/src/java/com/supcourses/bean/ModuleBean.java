/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.bean;

import com.supcourses.entity.Course;
import com.supcourses.entity.Module;
import com.supcourses.service.ModuleService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author jngue
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class ModuleBean implements Serializable {
    
    @EJB
    private ModuleService ms;
    
    private List<Module> resultList;
    
    private DataModel moduleModel = new ListDataModel();

    public DataModel getModuleModel() {
        return moduleModel;
    }

    public void setModuleModel(DataModel moduleModel) {
        this.moduleModel = moduleModel;
    }
    
    public void getModulesByCourse (Course course) {
        List<Module> finalRes = new ArrayList<Module>();
        resultList = ms.findAll();
        for (Module module : resultList) {
            if(module.getCourseId().getCourseId() == course.getCourseId()) {
                System.out.println(course);
                finalRes.add(module);
            }
        }
        moduleModel.setWrappedData(finalRes);
        //return ms.findModulesCourse(course);
    }

    /**
     * Creates a new instance of ModuleBean
     */
    public ModuleBean() {
    }
    
}
