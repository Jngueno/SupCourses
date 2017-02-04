/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service.soap;

import com.supcourses.entity.Certification;
import com.supcourses.service.PrinterCertificationService;
import javax.ejb.EJB;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author jngue
 */
@WebService(serviceName = "soap/certification")
public class PrintCertification {

    @EJB
    private PrinterCertificationService ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Web Service > Add Operation"

    @WebMethod(operationName = "printCertification")
    @Oneway
    public void printCertification(@WebParam(name = "certification") Certification certification) {
        ejbRef.printCertification(certification);
    }
    
}
