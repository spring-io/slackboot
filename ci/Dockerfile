FROM adoptopenjdk/openjdk8:latest

RUN apt-get update && apt-get install -y wget

RUN apt-get clean \
 && rm -rf /var/lib/apt/lists/*

RUN wget -O cf-linux.tgz "https://cli.run.pivotal.io/stable?release=linux64-binary&source=github"

RUN tar xvfz cf-linux.tgz

RUN chmod 755 /cf

RUN mkdir /.cf && chmod 666 /.cf


