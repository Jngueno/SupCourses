/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supcourses.service;

import com.supcourses.entity.Certification;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author jngue
 */
@Stateless
@LocalBean
public class PrinterCertificationService {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Resource(mappedName="jms/SupCoursesFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(mappedName="jms/SupCourses")
    private Destination destination;
    
    
    public void printCertification(Certification certification) {
        
        Connection connection = null;
        
        try {
            connection = connectionFactory.createConnection();
            
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            
            MessageProducer producer = session.createProducer(destination);
            StringBuilder messageContent = new StringBuilder("Order for ");
            messageContent.append(certification.getCertificationId());
            messageContent.append(" ");
            messageContent.append(certification.getCourseId().getName());
            TextMessage message = session.createTextMessage(messageContent.toString());
            
            producer.send(message);
            
        } catch (JMSException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if(null != connection) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        
    }
}
