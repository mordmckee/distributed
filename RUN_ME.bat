call mvn clean compile assembly:single
call cd target
start java -jar %~dp0target\distributed-1.0.0-jar-with-dependencies.jar
pause