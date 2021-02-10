Code for the Inside Java blog post [Deciphering the stacktrace](https://inside.java/2021/02/11/deciphering-the-stacktrace/).

## Instructions
Download the entire repo.

* If you want to use Docker, you just execute `Dockerstart.bat|sh` which will build an image, and start a docker container. This is recommended since it'll download OpenJDK 15, and everything you need.

* If you'd rather build it without Docker, you need a JVM of some kind, an SDK for native compilation, and then perhaps modify the Makefile slightly to suit your system. 

## Build project
Make all will compile all Java classes, the native library, and the AOT compiled class.
```
make all
```

## Run examples
Following the [Inside Java article](https://inside.java/2021/02/10/deciphering-the-stacktrace/), these are the make targets that will provoke the crashes.

#### Loop a few times
This will produce an `hs_err` file showing stacktrace with native execution, interpreted Java and VM internal frames.
```
make test_loop
```

#### Loop many times
This will produce an `hs_err` file showing JIT compiled Java frames.
```
make test_loop
```

#### Using AOT
This will produce an `hs_err` file showing AOT compiled Java frames.
```
make test_aot
```



