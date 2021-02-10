FROM gcc:9.3

RUN curl https://download.java.net/java/GA/jdk15.0.1/51f4f36ad4ef43e39d0dfdbaf6549e32/9/GPL/openjdk-15.0.1_linux-x64_bin.tar.gz | tar xz

ENV JAVA_HOME=/jdk-15.0.1
ENV PATH="${JAVA_HOME}/bin/:${PATH}"

WORKDIR /work
RUN /bin/bash