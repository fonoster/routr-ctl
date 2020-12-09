FROM openjdk:13-ea-alpine3.10
LABEL maintainer="Pedro Sanders <fonosterteam@fonoster.com>"

RUN apk add --update nodejs npm bash netcat-openbsd; \
  npm -g --unsafe-perm install routr-ctl; \
  rm -rf /var/cache/apk/* /tmp/* /var/tmp/*
