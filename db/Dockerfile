FROM scylladb/scylla

WORKDIR /workspace

COPY watcher.sh .

RUN  ./watcher.sh

ENV WAIT_VERSION 2.7.2

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait

RUN chmod +x /wait