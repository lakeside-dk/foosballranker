HOW TO BUILD AND RUN VIA ANT

checkout from github

download google app engine sdk

download dart sdk

add "username".properties file - fix paths in file

now you should be able to run ant war



HOW TO CREATE IDEA PROJECT

create new project in Idea
	select the google app engine facet

install the dart plugin

create library with all files from lib/

the appengine facet should have added a lib with app engine stuff
	add the following jars to that lib

appengine-remote-api.jar
impl/appengine-api.jar
shared/appengine-lcoal-runtime-shared.jar
shared/el-api.jar
shared/jsp-api.jar
shared/servlet-api.jar

change Output directory to target/foosballranker in artifact
check Build on make
set dart2js ant target to execute before compilation
ensure that the project is pointing on the correct web.xml

now you should be able to run the Debug configuration