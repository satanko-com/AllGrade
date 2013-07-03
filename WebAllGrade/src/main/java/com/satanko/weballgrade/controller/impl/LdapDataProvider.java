/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.controller.impl;

import com.satanko.weballgrade.controller.SourceDataProvider;
import com.satanko.weballgrade.data.model.Clazz;
import com.satanko.weballgrade.data.model.Student;
import com.satanko.weballgrade.data.model.Teacher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

/**
 * This class provides a way to access an LDAP-Server and search it for {@link Class}es, {@link Student}s and {@link Teacher}s.
 *
 * @author Julian Tropper (julian.tropper@gmail.com) 
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 *
 * @version 1.0
 */
public class LdapDataProvider implements SourceDataProvider {

    /**
     * The naming-context.
     */
    private final String CONTEXT = "com.sun.jndi.ldap.LdapCtxFactory";
    /**
     * The address of the LDAP-Server.
     */
    private final String HOST = "ldap://localhost";
    /**
     * The base-address in the LDAP-Server.
     */
    private final String BASE = "dc=htl-kaindorf, dc=ac, dc=at";
    private Hashtable<String, String> env = new Hashtable<>();
    private DirContext ctx;
    /**
     * Class-Names, which should be included from the searching-process.
     */
    private String[] testclasses = {"testaah", "testadh", "testbah", "testbdh", "testcdh"};

    /**
     * Sets the {@link Hashtable} and {@link DirContext} up to work with the given Connection-Strings.
     *
     * @throws NamingException if a naming exception is encountered
     */
    public LdapDataProvider() {
        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXT);
            env.put(Context.PROVIDER_URL, HOST);
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            Logger.getLogger(LdapDataProvider.class.getSimpleName())
                    .log(Level.SEVERE, "Folloging NamingException occured: \n{0}", e.getExplanation());
        }
    }

    /**
     * Searches the LDAP-Server for entries which meet the requirements to be
     * {@link Class}es.
     *
     * @return An {@link ArrayList} containing all <i>Classes</i>.
     * @throws NamingException if a naming exception is encountered
     */
    @Override
    public ArrayList<String> getAllClasses() {
        try {
            String filter = "cn=*";
            ArrayList<String> classes = new ArrayList<>();

            SearchControls sc = new SearchControls();
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<?> items = ctx.search(BASE, filter, sc);

            while (items != null && items.hasMore()) {
                SearchResult sr = (SearchResult) items.next();
                String dn = sr.getName();

                Attributes attrs = sr.getAttributes();
                NamingEnumeration<?> attributs = attrs.getAll();

                if (attributs != null && attributs.hasMore()
                        && dn.contains("ou=Groups")) {
                    String[] parts = dn.split(",");

                    if (isInteger(parts[0].substring(parts[0].length() - 2))) {
                        classes.add(parts[0].substring(3));
                    }
                }
            }
            return classes;
        } catch (NamingException e) {
            Logger.getLogger(LdapDataProvider.class.getSimpleName())
                    .log(Level.SEVERE, "Folloging NamingException occured: \n{0}", e.getExplanation());
            return null;
        }
    }

    /**
     * Searches a given {@link Class} for <i>Name-Tags</i>.
     *
     * @param paramClass The expression which is used to identify a <i>Class</i>.
     * @return An
     * <code>ArrayList</code> containting all <i>nameTag</i>s.
     * @return An {@link ArrayList} containing all <i>Students</i>.
     * @throws NamingException if a naming exception is encountered
     */
    @Override
    public ArrayList<Student> getAllStudents(String paramClass) {
        try {
            ArrayList<Student> students = new ArrayList<>();

            String[] attrIDs = {"memberUid"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attrIDs);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "cn=" + paramClass;
            NamingEnumeration<?> items = ctx.search(BASE, filter, sc);

            while (items != null && items.hasMore()) {

                SearchResult sr = (SearchResult) items.next();
                String dn = sr.getName();

                Attributes attrs = sr.getAttributes();
                NamingEnumeration<?> attributs = attrs.getAll();

                while (attributs != null && attributs.hasMore()) {
                    Attribute attr = (Attribute) attributs.next();

                    if (dn.equals(filter + ",ou=Groups")) {

                        NamingEnumeration<?> values = attr.getAll();
                        while (values != null && values.hasMoreElements()) {
                            String nameTag = values.next().toString();
                            if (Arrays.asList(testclasses).contains(nameTag)) {
                                continue;
                            }

                            String[] fullName = getFullName(nameTag);

                            Student student = new Student(nameTag, fullName[0], fullName[1]);
                            students.add(student);

                            System.out.println('\t' + fullName[0] + " " + fullName[1]);
                        }
                        return students;
                    }
                }
            }
            return students;
        } catch (NamingException e) {
            Logger.getLogger(LdapDataProvider.class.getSimpleName())
                    .log(Level.SEVERE, "Folloging NamingException occured: \n{0}", e.getExplanation());
            return null;
        }
    }

    /**
     * Searches a given {@link Class} for <i>Name-Tags</i>. <br> Only use this method if {@link #searchInClasses} does NOT return any results since it can be inaccurate!
     *
     * @param paramClass The expression which is used to identify a <i>Class</i>.
     * @return An
     * <code>ArrayList</code> containting all <i>nameTag</i>s. of a certain class.
     * @throws NamingException if a naming exception is encountered
     */
    @Override
    public ArrayList<Student> getAllStudentsV2(String paramClass) {
        try {
            ArrayList<Student> students = new ArrayList<>();

            String[] attrIDs = {"givenName", "jahrgang", "sn", "status", "uid"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attrIDs);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "jahrgang=" + paramClass;
            NamingEnumeration<?> items = ctx.search(BASE, filter, sc);

            String firstName, lastName, nameTag;

            while (items != null && items.hasMore()) {
                firstName = "-";
                lastName = "-";
                nameTag = "-";

                SearchResult sr = (SearchResult) items.next();

                Attributes attrs = sr.getAttributes();
                NamingEnumeration<?> attributs = attrs.getAll();

                while (attributs != null && attributs.hasMore()) {
                    Attribute attr = (Attribute) attributs.next();
                    String attrId = attr.getID();

                    Object value = attr.get();

                    switch (attrId) {
                        case "givenName":
                            firstName = value.toString();
                            break;
                        case "sn":
                            lastName = value.toString();
                            break;
                        case "uid":
                            nameTag = value.toString();
                            break;
                        case "status":
                            if (!value.toString().equals("SCHUELER")) {
                                continue;
                            }
                    }
                }
                Student student = new Student(nameTag, firstName, lastName, new Clazz(paramClass));
                students.add(student);

                System.out.println('\t' + firstName + " " + lastName);
            }
            return students;
        } catch (NamingException e) {
            Logger.getLogger(LdapDataProvider.class.getSimpleName())
                    .log(Level.SEVERE, "Folloging NamingException occured: \n{0}", e.getExplanation());
            return null;
        }
    }

    /**
     * Get the <i>Full Name</i> of a student when only the <i>Name-Tag</i> is given.
     *
     * @param nameTag The expression which is the tag for a <i>Student</i>. <br> A <i>nameTag</i> has a format as following:
     * <code>??????00</code>.
     * @return A String-Array which contains the <i>Full Name</i> of a <i>Student</i>. <br>
     * <code>String[0]</code> = First Name <br>
     * <code>String[1]</code> = Surname
     * @throws NamingException if a naming exception is encountered
     */
    private String[] getFullName(String nameTag) {
        try {
            String firstName = "-", lastName = "-";
            String fullName[] = new String[2];

            String filter = "uid=" + nameTag;

            String[] attrIDs = {"givenName", "sn", "status"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attrIDs);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<?> items = ctx.search(BASE, filter, sc);

            if (items != null && items.hasMore()) {
                SearchResult sr = (SearchResult) items.next();
                String dn = sr.getName();

                if (dn.equals(filter + ",ou=Users")) {
                    Attributes attrs = sr.getAttributes();
                    NamingEnumeration<?> attributs = attrs.getAll();

                    while (attributs != null && attributs.hasMore()) {
                        Attribute attr = (Attribute) attributs.next();
                        String attrId = attr.getID();

                        Object value = attr.get();

                        switch (attrId) {
                            case "givenName":
                                firstName = value.toString();
                                break;
                            case "sn":
                                lastName = value.toString();
                                break;
                            case "status":
                                if (!value.toString().equals("SCHUELER")) {
                                    continue;
                                }
                        }
                    }
                }
            }
            fullName[0] = firstName;
            fullName[1] = lastName;

            return fullName;
        } catch (NamingException e) {
            Logger.getLogger(LdapDataProvider.class.getSimpleName())
                    .log(Level.SEVERE, "Folloging NamingException occured: \n{0}", e.getExplanation());
            return null;
        }
    }

    /**
     * Searches the LDAP-Server for all registered {@link Teacher}s.
     *
     * @return An
     * <code>ArrayList</code> containting all <i>nameTag</i>s of registered teachers.
     * @throws NamingException if a naming exception is encountered
     */
    @Override
    public ArrayList<String> getAllTeachers() {
        try {
            String filter = "cn=allelehrer";
            ArrayList<String> teachers = new ArrayList<>();

            String[] attrIDs = {"memberUid"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attrIDs);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<?> items = ctx.search(BASE, filter, sc);

            if (items != null && items.hasMore()) {
                SearchResult sr = (SearchResult) items.next();

                Attributes attrs = sr.getAttributes();
                NamingEnumeration<?> attributs = attrs.getAll();

                while (attributs != null && attributs.hasMore()) {
                    Attribute attr = (Attribute) attributs.next();

                    NamingEnumeration<?> values = attr.getAll();

                    while (values != null && values.hasMore()) {
                        String token = values.next().toString();
                        System.out.println('\t' + token);
                        teachers.add(token);
                    }
                }
            }
            return teachers;
        } catch (NamingException e) {
            Logger.getLogger(LdapDataProvider.class.getSimpleName()).log(Level.SEVERE, "Folloging NamingException occured: \n{0}", e.getExplanation());
            return null;
        }
    }

    /**
     * Checks if a certain expression is a valid {@link Integer} or not.
     *
     * @param expression The expression to be checked.
     * @return
     * <code>true</code> if the expression is an {@link Integer},
     * <code>false</code> if not.
     */
    private boolean isInteger(String expression) {
        try {
            Integer.parseInt(expression);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
