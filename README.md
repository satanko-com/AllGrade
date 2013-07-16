AllGrade Info/FAQ
========

###What is AllGrade?
AllGrade is a collection of applications that allow a teacher a to grade students in class via smartphone.
It consists of the following three projects:
+ __ExcelAllGrade__ (Excel Add-In)
+ __MobileAllGrade__ (Android app)
+ __WebAllGrade__ (Java Web Application)

----
###How do the projects work and interact with each other?
When techers enters a classroom, they scan a QR code that uniquely identifies the class with their smartphone. 
MobileAllGrade will then present data such as students, subjects and tasks associated with the class to choose from.
Students are then graded by the teacher and at the end of the lesson the generated data gets uploaded to the WebAllGrade server.

WebAllGrade acts as a central point for saving and provding data via REST. The server mainly manages gradings and assignment of students to classes.
Additionally it is capable of generating the whole database and fetching assignements of students to classes from a "source". In our case this source was
a LDAP server, but any other class which implements the [SourceDataProvider](WebAllGrade/src/main/java/com/satanko/weballgrade/controller/SourceDataProvider.java)
interface can act as a source for data.

ExcelAllGrade is a Add-In for Excel 2010 (and up) to represent the data on a desktop. 
This can be used to decide on final grades, get an overview, see the performance of students over a certain time period etc.

---
###Does AllGrade work out of the box?
Unfortunately no. Some configuration is required in order for AllGrade to work "as is".

1. You need a Google Apps or [Google Apps for Education][1] domain and configure AllGrade to work with it. (used for authentication)
2. Configure all projects to use the right ip addresses. Currently all network adresses are set to **_localhost_**.
3. Configure WebAllGrade to use the right database ip, username and password.
4. Change the name and tokens of the subjects to fit your school


List of files which **definitely** need configuration:

####ExcelAllGrade
+ [Constants.cs](ExcelAllGrade/ExcelAllGrade/web/adapter/Constants.cs)

####MobileAllGrade
+ [C.java](MobileAllGrade/src/com/satanko/allgrade/data/C.java)

####WebAllGrade
+ [hibernate.cfg.xml](WebAllGrade/src/main/resources/hibernate.cfg.xml)
+ [AllGradeDBUtil.java](WebAllGrade/src/main/java/com/satanko/weballgrade/data/dao/impl/hibernate/AllGradeDBUtil.java)
+ [LdapDataProvider.java](WebAllGrade/src/main/java/com/satanko/weballgrade/controller/impl/LdapDataProvider.java) (if you need LDAP integration)

Looking at the code it should become clear what changes are required.
If you need help, feel free wo contact us. (Email addresses are in the documentation)

---
###Who created AllGrade initially and why?
This repository contains the code written by _Julian Tropper_ and _Philipp Sommersguter_ over the course of their diploma thesis.

---
###Are there any known bugs and/or complications?
####Rare memory leak in WebAllGrade
>Quickfix: restarting tomcat

Near the end of our development, we had the suspicion that the Quartz framework (used for scheduling in WebAllGrade) has a problem with tomcat. This rarely causes memory leaks. 
Unfortunately there was no time to conduct further testing in order to get more details on this issue. 

----
[1]:http://www.google.com/enterprise/apps/education/
