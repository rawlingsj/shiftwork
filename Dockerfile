FROM node

ENV MAVEN_VERSION 3.3.3

RUN apt-get update \
    && apt-get install -y software-properties-common \
    && add-apt-repository ppa:openjdk-r/ppa \
    && apt-get update \
    && apt-get install -y openjdk-8-jdk \
    && mkdir -p /usr/share/maven \
    && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
       | tar -xzC /usr/share/maven --strip-components=1 \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn \
    && apt-get clean    \
    && mkdir /workdir

ENV MAVEN_HOME /usr/share/maven

WORKDIR /workdir

COPY . /workdir

RUN npm install \
    && bower install \
    && $MAVEN_HOME/bin/mvn -Pprod


