JDK-8184051 : jdk8 -parameters breaks annotation processor parameter names
----------------------------------------------------------------------

This is the test case showing the broken javac behavior reported in [JDK-8184051](http://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8184051)

    $ ./show_bug.sh
    javac 1.8.0_131
    these are the correct argument names, compiled with and without -parameters argument, compiling both classes in a single invocation:
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date birthDate, java.lang.String firstName, java.lang.String lastName'
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date birthDate, java.lang.String firstName, java.lang.String lastName'

    this shows expected missing parameter names when compiled in 2 steps, like in the case of libraries or multiple module projects, without -parameters:
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long arg0, java.util.Date arg1, java.lang.String arg2, java.lang.String arg3'

    and finally the bug, same two step compilation as above, but -parameters passed into first step, shows shifted/incorrect parameter names:
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date firstName, java.lang.String lastName, java.lang.String arg3'

This was discovered by Travis Burtrum on June 19th, 2017, when writing test cases for [JdbcMapper](https://code.moparisthebest.com/moparisthebest/JdbcMapper/src/68c1c0482e56c1a2a3ea842a6a71fea3e4444738/jdbcmapper/src/main/java/com/moparisthebest/jdbc/codegen/CompileTimeRowToObjectMapper.java#L88)

it looks like the bug currently doesn't affect javac 9b177:

    javac 9
    these are the correct argument names, compiled with and without -parameters argument, compiling both classes in a single invocation:
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date birthDate, java.lang.String firstName, java.lang.String lastName'
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date birthDate, java.lang.String firstName, java.lang.String lastName'
    
    this shows expected missing parameter names when compiled in 2 steps, like in the case of libraries or multiple module projects, without -parameters:
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long arg0, java.util.Date arg1, java.lang.String arg2, java.lang.String arg3'
    
    and finally the bug, same two step compilation as above, but -parameters passed into first step, shows shifted/incorrect parameter names:
    warning: Person(long,java.util.Date,java.lang.String,java.lang.String): 'long personNo, java.util.Date birthDate, java.lang.String firstName, java.lang.String lastName'
