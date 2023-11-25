# Schedule Manager

<img src="https://i.ibb.co/D5cgHSd/schedule.png" alt="image" width="100%">

## Description
This project uses the Schedule API Component, designed for creating, managing, searching, and saving schedules. The core of this project is a JavaFX GUI application that performs various operations based on the functionalities provided by the Schedule API Component.

## Dependencies
- Schedule API (Compile Dependency)
- Schedule Implementation (Runtime Dependency)

## How to set up implementation component
Create custom implementation using Schedule API and save its instance to the Manager, so you can use implementation later in runtime.
```
public class MySchedule extends Schedule {

    static {
        Manager.setSchedule(new MySchedule());
    }

    public MySchedule() {}

    ...
}
```
Add implementation to pom.xml.
```
<dependency>
    <groupId>GroupId</groupId>
    <artifactId>ArtifacdId</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>runtime</scope>
</dependency>
```
Change implementation reference in Core.class.
```
 try {
    Class.forName("path to the implementation");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    ...
}
```
