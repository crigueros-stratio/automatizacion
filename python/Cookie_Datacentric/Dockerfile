FROM python:3.7.4

RUN apt-get update

USER root

RUN mkdir -p /opt

WORKDIR /opt

COPY . ./

RUN apt-get update 
RUN apt-get -y install sudo

RUN pip install --upgrade pip
RUN pip install pandas==1.0.5
RUN pip install -U Flask
RUN python -m pip install lxml
RUN python -m pip install requests
RUN python -m pip install beautifulsoup4

EXPOSE 5000

RUN chmod a+x ./entrypoint.sh
RUN chown $USER:$USER ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]