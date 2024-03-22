import pika
import json
import atexit
import time

credentials = pika.PlainCredentials('admin', 'admin')
parameters = pika.ConnectionParameters('img-rabbitmq.image-caption-generation.svc.cluster.local', 5672, '/', credentials)
connection = pika.BlockingConnection(parameters)
channel = connection.channel()
prop = pika.BasicProperties(
    content_type='application/json',
    content_encoding='utf-8',
    delivery_mode = 1,
)

def sendMessageWithRetry(user, file, caption):
  for i in range(100):
    for attempt in range(10):
      try:
        sendMessage(user, file, caption)
      except:
        time.sleep(3)
        print("Retrying... ", i, attempt)
      else:
        break

def sendMessage(user, file, caption):
  global connection
  global channel
  if not connection or connection.is_closed:
    print("Reopening the connection")
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()
  else:
    print("Reusing the connection")

  message = { "user": user, "file": file, "caption": caption }
  channel.basic_publish(exchange='images', routing_key='images', body=json.dumps(message), properties=prop)

def exit_handler():
    connection.close()

atexit.register(exit_handler)