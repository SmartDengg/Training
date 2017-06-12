1. invokespecial只能调用三类方法：<init>方法；私有方法；super.method()。因为这三类方法的调用对象在编译时就可以确定
2. invokevirtual是一种动态分派的调用指令：也就是引用的类型并不能决定方法属于哪个类型

JVM调用方法有五条指令，分别是invokestatic,invokespecial,invokevirtual,invokeinterface,invokedynamic。
invokestatic用来调用静态方法；
invokespecial用来调用私有方法，父类方法(super.)，类构造器方法；
invokeinterface调用接口方法；
invokedynamic方法动态执行；
invokevirtual调用所有虚方法，即除了以上的方法外全用invokevirtual调用。