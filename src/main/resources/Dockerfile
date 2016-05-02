# For building JHipster applications
FROM fabric8/maven-builder

ADD https://nodejs.org/dist/v4.4.3/node-v4.4.3-linux-x64.tar.gz /tmp/

ENV PATH $PATH:/opt/node-v4.4.3-linux-x64/bin
ENV PHANTOMJS_BIN /opt/node-v4.4.3-linux-x64/bin/phantomjs
RUN tar zxvf /tmp/node-v4.4.3-linux-x64.tar.gz -C /opt/ \
    && rm -f /tmp/node-v4.4.3-linux-x64.tar.gz \
    && npm install -g npm \
    && npm install -g bower \
    && npm install -g gulp \
    && yum install -y bzip2 \
    && yum install -y freetype \
    && yum install -y fontconfig \
    && npm install -g phantomjs \
    && npm install -g yo




#docker build -t 172.30.150.12:80/shiftwork/jhipster-build .
#docker push 172.30.150.12:80/shiftwork/jhipster-build
