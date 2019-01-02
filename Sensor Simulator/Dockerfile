FROM python:3.7.1-alpine3.8
RUN pip install kafka-python
COPY . /app
WORKDIR /app
CMD ["python", "SensorMain.py"]
