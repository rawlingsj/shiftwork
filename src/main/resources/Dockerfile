FROM fabric8/maven-builder

ADD https://nodejs.org/download/release/latest/node-v6.0.0-linux-x64.tar.gz /tmp/

RUN tar zxvf /tmp/node-v6.0.0-linux-x64.tar.gz -C /opt/ \
    && rm -f /tmp/node-v6.0.0-linux-x64.tar.gz

ENV PATH $PATH:/opt/node-v6.0.0-linux-x64/bin



#docker build -t 172.30.150.12:80/shiftwork/jhipster-build .
