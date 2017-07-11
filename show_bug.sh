#!/bin/sh
# delete all classes
rm -f *.class

javac -version 2>&1

# compile our annotation processor once
javac ParameterPrintingProcessor.java

echo 'these are the correct argument names, compiled with and without -parameters argument, compiling both classes in a single invocation:'
javac -parameters -processor ParameterPrintingProcessor Person.java PersonDao.java 2>&1 | head -n1
rm Person.class PersonDao.class
javac -processor ParameterPrintingProcessor Person.java PersonDao.java 2>&1 | head -n1

echo
echo 'this shows expected missing parameter names when compiled in 2 steps, like in the case of libraries or multiple module projects, without -parameters:'
rm Person.class PersonDao.class
javac Person.java
javac -processor ParameterPrintingProcessor PersonDao.java 2>&1 | head -n1

echo
echo 'and finally the bug, same two step compilation as above, but -parameters passed into first step, shows shifted/incorrect parameter names:'
rm Person.class PersonDao.class
javac -parameters Person.java
# below is the actual invocation of javac where the bug lives, somehow reads saved parameter names from step above incorrectly
# they are saved correctly above, because you can read them at runtime correctly
javac -processor ParameterPrintingProcessor PersonDao.java 2>&1 | head -n1

# clean up by deleting all classes
rm -f *.class
