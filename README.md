KatMana
=======

Installation
------------

- Install Gradle according to your platform.
- Copy jetty-env.xml.sample to jetty-env.xml. Modify the database setting according to your database setting.
- Run `gradle runApp'. It should now download all dependencies and if everything works, it will also migrate your database and everything will be fine.
- If it says something about your db driver cannot be found, put your db driver jar in jettyJar directory.

Used Library, Tools and some Implication
----------------------------------------
Please put any tools that you use that may be hard to figureout what here.

- Gradle
  - Gradle is a build tools/system.
  - We can specify our dependencies (hibernate,liquibase,anything else you want that is available through maven repository) and it will download it for you.
  - The development server command is `gradle runApp`.
  - The command is provided by Gretty, a Gradle plugin that replace jettyRun with a newer version of Jetty. 
  - Because the layout is not the default Gradle preferred layout, some stuff may breaks.
- Hibernate
  - Hibernate is the JPA implimentation.
  - JPA is Java Persistent API.
  - JPA is like an ORM that interface JDBC for us.
  - Be careful to differentiate between implementation and specification. JPA is specification, Hibernate is an ORM that provide its implementation.
- Liquibase
  - Liquibase is a database migration library.
  - It should be database independent.
  - All migration should be put in resource/db.changelog.yml

Notices
-------

- Nothing yet....
