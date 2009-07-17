/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * <p>
 * This is the demonstration of using this component in GUI environment.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectDataGUI implements ActionListener {

    /**
     * We assume that the bean is accessed remotely.
     */
    @EJB
    private ProjectServiceRemote remote;

    /**
     * Widgets used to enter data.
     */
    private JTextField id, name, description, userId;

    /**
     * A list to show project data.
     */
    private JList list;

    /**
     * Buttons to execute commands.
     */
    private JButton create, retrieve, retrieveUser, retrieveAll, update, delete;

    /**
     * <p>
     * Creates a <code>ProjectDataGUI</code> instance.
     * </p>
     */
    public ProjectDataGUI() {
        /*
        * Correctly setting up this InitialContext without specifying any properties
        * in code can be accomplished by putting the properties in a file
        * 'jndi.properties' in the classpath
        */
        Properties env = new Properties();
        env.setProperty(Context.SECURITY_PRINCIPAL, "username");
        env.setProperty(Context.SECURITY_CREDENTIALS, "password");
        env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");

        try {
            Context context = new InitialContext(env);
            remote = (ProjectServiceRemote) context.lookup("remote/ProjectServiceBean");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        /* Setup a frame, add the widgets, setup button listeners etc. */
    }

    /**
     * <p>
     * main method.
     * </p>
     *
     * @param args
     *            the arguments list.
     */
    public static void main(String[] args) {
        ProjectDataGUI gui = new ProjectDataGUI();
    }

    /**
     * <p>
     * Performs the action.
     * </p>
     *
     * @param e
     *            the action event
     */
    public void actionPerformed(ActionEvent e) {
        try {
            String command = e.getActionCommand();
            if (command.equals("create")) {
                ProjectData projectData = new ProjectData();
                projectData.setName(name.getText());
                projectData.setDescription(description.getText());
                projectData = remote.createProject(projectData);
                show(projectData);
            } else if (command.equals("retrieve")) {
                long projectId = Long.parseLong(this.id.getText());
                ProjectData projectData = remote.getProject(projectId);
                show(projectData);
            } else if (command.equals("retrieveUser")) {
                long userId = Long.parseLong(this.userId.getText());
                List < ProjectData > projectData = remote.getProjectsForUser(userId);
                show(projectData);
            } else if (command.equals("retrieveAll")) {
                List < ProjectData > projectData = remote.getAllProjects();
                show(projectData);
            } else if (command.equals("update")) {
                ProjectData projectData = new ProjectData();
                projectData.setProjectId(Long.parseLong(id.getText()));
                projectData.setName(name.getText());
                projectData.setDescription(description.getText());
                remote.updateProject(projectData);
                show(projectData);
            } else if (command.equals("D")) {
                long projectId = Long.parseLong(this.id.getText());
                boolean result = remote.deleteProject(projectId);
                if (result) {
                    alert("Project found and deleted.");
                } else {
                    alert("Project does not exist.");
                }
            }
        } catch (ProjectServiceFault fault) {
            alert(fault.getMessage());
        }
    }

    /**
     * <p>
     * Shows the project data.
     * </p>
     *
     * @param projectData
     *            the project data.
     */
    public static void show(ProjectData projectData) {
        /* Clear the JList and show the given item */
    }

    /**
     * <p>
     * Shows the list of project data.
     * </p>
     *
     * @param projectDatas
     *            the project data list.
     */
    public static void show(List < ProjectData > projectDatas) {
        /* Clear the JList and show the given items */
    }

    /**
     * <p>
     * Shows a message with alert information.
     * </p>
     *
     * @param message
     *            the alert message.
     */
    public void alert(String message) {
        /* Show an alert box, with the given message */
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.PLAIN_MESSAGE);
    }

}
