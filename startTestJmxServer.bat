mkdir bin
javac -sourcepath src\main\java -d bin src\main\java\testimpl\SimpleServer.java
java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1617 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -classpath ./bin testimpl.SimpleServer