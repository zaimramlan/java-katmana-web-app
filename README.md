# KatMana Web App

Web app that allows event organizers to plot points-of-interest on a map that is sharable to event attendees.

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
- Hibernate-Search
    - Hibernate search is the search engine.
    - It is basically a frontend to lucene similar to elasticsearch or solr but much easier to implement with Hibernate already used.
- Hibernate-Validator
    - The thing that helps with validation.
- Liquibase
    - Liquibase is a database migration library.
    - It should be database independent.
    - All migration should be put in resource/db.changelog.yml
- SnakeYaml
    - This is suppose to be liquibase dependency, but for some reason, it have to be included manually.
- Gson
    - Gson is a library that allow us to generate JSON from Java Object as easy as if we are using PHP or other programming language.
- gson-fire
    - Its kinda like a plugin for Gson. 
    - Added the ability to serialize a method result.
    - Not used much in the newer serialization structure.
- commons-beanutils
    - BeanUtils is a part of the Apache Commons java library.
    - It allow us to easily set and get beans property by name.
    - Used by EntityRestConfiguration to set entity properties.
    - Also include commons-io, which is very helpful for working with streams and such.
- Guava
   - Guava, like apache-commons is a java utility library, this time made by Google.
   - In our case, the utility we are interested in is the CaseFormat enumerator used to convert
     java camelCased name to snake_case which is used by http parameters.

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

- if you get `java.lang.OutOfMemoryError: PermGen space`, the just restart the server. The java process may need to be killed manually in task manager.
- Or, you could upgrade to java 8.

Endpoints
---------

- All endpoints follow basic CRUD mechanism. (resource here is the type of resource such as user or points)
    - INDEX endpoint at `GET /resources`
        - Will return list of resource in JSON form.
        - By default all index endpoint will return a maximum of 100 record.
        - They should accept parameter `offset` and `limit` for the rest of the record.
        - Default implementation also accept the model attributes as parameters in which you can do filter query.
    - CREATE endpoint at `POST /resources`
        - The POST params are the resource property (as in variable not bean property) name. 
        - Also return the modified resource JSON representation.
    - SHOW endpoint at `GET /resource/<id>`. Notice that this one and the other two endpoint uses singular url.
    	- Will return the JSON representation of the resource.
    - EDIT endpoint at `POST /resource/<id>`
    	- Parameters same as the CREATE endpoint, just it has an id and it update the resource not create it.
    - DELETE endpoint at `DELETE /resource/<id>`
    	- Return the resource JSON to, but the resource should no longer be available later.
- Several exception to that usual rule are:
    - PointContext endpoint only have create and delete endpoint and the delete endpoint uses the index url (no id)
        - INDEX - `POST /point_contexts`
        	- Accept `point_id` and `context_id`
        - DELETE endpoint `DELETE /point_contexts?point_id=<pointid>&context_id=<contextid>`
    - PointRating and SubmitterRating endpoint do not have create and index endpoints. The update endpoint act as a create endpoint.
    - User endpoint has an additional endpoint `/user/me` which will return current user if logged in and 404 of not. You can also use POST/PUT on the url to update current user.
    - PointPhoto endpoint do not have index endpoint. Index it from points show.
- The point INDEX endpoint can be used to search for endpoints.
	- It accept two additional parameter `search` and `context_id`
- There is one helper endpoint at `POST /reindex_points` . This will reindex all point into the search engine.
    	
