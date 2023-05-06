# `lineman`
〰️ The super simple Java framework for command-line applications.

[Getting Started](https://github.com/blueysh/lineman#getting-started) | 
[Runtime Hooks](https://github.com/blueysh/lineman#attaching-runtime-hooks) | 
[Commands](https://github.com/blueysh/lineman#adding-commands) | 
[Logger](https://github.com/blueysh/lineman#using-the-logger) | 
[Install](https://github.com/blueysh/lineman#use-lineman)

## What is lineman?
Lineman is a Java framework that makes developing command-line applications easy. Within just a few lines of code, you can have a CLI app working.

## Getting Started
Getting started with lineman is as easy as this line of code:
```java
Lineman lineman = Lineman.create();
```

From here, you can start building out your CLI app.

### Attaching Runtime Hooks
Lineman provides easy support for startup and shutdown hooks.

To attach a startup hook:
```java
lineman.addStartupHook(new Thread( () -> lineman.getLogger().println("my-cool-cli-tool v1.0") ));
```
Upon running your app, you would see:
```
my-cool-cli-tool v1.0
```

To attach a shutdown hook:
```java
lineman.addShutdownHook(new Thread( () -> lineman.getLogger().info("Closing") ));
```
Upon stopping your app, you would see:
```
Closing
```

You can attach as many of these hooks as you wish to your lineman app.

### Adding Commands
Commands are a crucial aspect of CLI apps, and lineman provides an easy way to work with them.

A lineman Command is a class that implements the `Command` interface.

Here's an example command class:
```java
public class MyCoolCommand implements Command {
  @Override
  public void run(Lineman lineman, String[] commands) {
    // handle command logic
  }
}
```

You can add your command to your lineman app by calling:
```java
lineman.addCommand("mycoolcommand", new MyCoolCommand());
```

Lineman also supports Subcommands. Subcommands are just like regular Commands, but are added to a parent Command class.

We can create an example subcommand:
```java
public class MySuperSubcommand implements Command {
  @Override
  public void run(Lineman lineman, String[] args) {
    lineman.getLogger().success("Ran my super subcommand!");
  }
}
```

... and attach it to our parent command:
```java
lineman.addCommand("mycoolcommand", new MyCoolCommand())
  .addSubcommand("supersubcommand", new MySuperSubcommand());
```

However, your subcommand won't run on its own. Within your parent command, add the following:
```java
public class MyCoolCommand implements Command{
  @Override
  public void run(Lineman lineman, String[] commands) {
    runSubcommand(lineman, commands);
  }
}
```
You can run any logic either before or after running subcommands, but this is 100% optional.

Now, if you ran your app and passed the following arguments...
```
<your app> mycoolcommand supersubcommand
```
... you would see the result of logic in `MyCoolCommand` and `MySuperSubcommand`.

### Using the Logger
Lineman has a custom built-in logger which you should use when logging messages.

The logger can create these messages:
- info
- warn
- error
- severe
- success

You can use color in your log messages by using `Logger.Color`.
```java
lineman.getLogger().info(Logger.Color.CYAN + "Hello, world - but in cyan!");
```
An advantage of the lineman logger is that colors automatically reset so you don't have to do it yourself.

## Get lineman
Currently, lineman can be found on [jitpack](https://jitpack.io).

```xml
  <repositories>
    <repository>
      <id>jitpack.io</id>=
      <url>https://jitpack.io</url>
    </repository>
  </repositories>
  <dependencies>
    <dependency>
      <groupId>com.github.blueysh</groupId>
      <artifactId>lineman</artifactId>
      <version>VERSION</version>
    </dependency>
  </dependencies>
```
```gradle
  repositories {
    maven { url 'https://jitpack.io' }
  }
  dependencies {
    implementation 'com.github.blueysh:lineman:VERSION'
  }
```
