KatMana
=======

Installation
------------

- Install Gradle according to your platform.
- Copy jetty-env.xml.sample to jetty-env.xml. Modify the database setting according to your database setting.
- Run `gradle appRun`. It should now download all dependencies and if everything works, it will also migrate your database and everything will be fine.
- If it says something about your db driver cannot be found, put your db driver jar in jettyJar directory.

Used Library, Tools and some Implication
----------------------------------------
Please put any tools that you use that may be hard to figureout what here.

- Gradle
    - Gradle is a build tools/system.
    - We can specify our dependencies (hibernate,liquibase,anything else you want that is available through maven repository) and it will download it for you.
    - The development server command is `gradle runApp`.
    - The command is provided by Gretty, a Gradle plugin that replace jettyRun with a newer version of Jetty. 
- Hibernate
    - Hibernate is the JPA implimentation.
    - JPA is Java Persistent API.
    - JPA is like an ORM that interface JDBC for us.
    - Be careful to differentiate between implementation and specification. JPA is specification, Hibernate is an ORM that provide its implementation.
- Liquibase
    - Liquibase is a database migration library.
    - It should be database independent.
    - All migration should be put in resource/db.changelog.yml
- Gson
    - Gson is a library that allow us to generate JSON from Java Object as easy as if we are using PHP or other programming language.
- commons-beanutils
    - BeanUtils is a part of the Apache Commons java library.
    - It allow us to easily set and get beans property by name.
    - Used by EntityRestConfiguration to set entity properties.

Directory Structure
-------------------

- I've modified the directory structure to match Gradle build system.
- Why? Because gradle have more than one way to hurt programmer.
- The WebContent directory is now in `src/main/webapp`
- The src for class is in `src/main/java`
- It has an eclipse plugin, so running `gradle eclipse` and `gradle eclipseWtp` would generate a compatible eclipse project configuration.
- So eclipse user should not have much problem other than the inbuild server should not be user. (use `gradle appRun` and open in `localhost:8080/KatMana`). 
- Netbeans user should have gradle plugin.

Notices
-------

- Nothing yet....
