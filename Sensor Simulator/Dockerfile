FROM alpine
RUN apk update && apk add python3 && apk add py-pip
RUN pip install kafka-python
COPY . /app
WORKDIR /app
CMD ["python", "SensorMain.py"]
