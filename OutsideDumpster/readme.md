## OutsideDumpster is the bug module
The OutsideDumspter project implements Java annotations to add runtime-toggleable bugs to your code.

### Creating buggy code
Buggy code is annotated on class level, using the `@Buggy` annotation. To have the bug toggleable, you use `Bug.isBuggy(this)`.

```java
package inside.dumpster;

@Buggy(because="it always crashes")
class Adder {
  public int plus(int a, int b) {
    if(Bug.isBuggy(this)) {
      crashAndBurn();
    }
    return a + b;
  }
}
```
The Buggy annotation
* must have the `because` attribute set.
* can be enabled or disabled by default (if it always crashes, it might be wise to have it disabled by default)

### Enabling/disabling buggy code
The [`Bug.registerMXBean()`](https://github.com/jaokim/inside-java-dumpster/blob/main/OutsideDumpster/src/main/java/inside/dumpster/outside/Bug.java) registers an [MXBean](https://github.com/jaokim/inside-java-dumpster/blob/main/OutsideDumpster/src/main/java/inside/dumpster/outside/BugBehaviourMXBean.java) that is used to toggle bugs on or off.

If you connect JConsole to the running server JVM, you can in the MBeans tab, see the inside.dumpster.outside section with its operation `setBuggy()`.

![The JConsole showing MBeans, and the inside.dumspter.outside operations](https://github.com/jaokim/inside-java-dumpster/blob/main/OutsideDumpster/docs/jconsole-bug.png)

When you, with the aid of JMC or hotspot error logs have found the class where the bug likely is, you can simply disable it by entering the class name, and setting it to false.
