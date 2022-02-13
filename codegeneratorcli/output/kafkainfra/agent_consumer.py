
import threading

from kafka import KafkaConsumer
from serializers import AgentSerializer


class AgentConsumer(object):
    def __init__(self, on_message_received, timeout_ms):
        self._on_message_received = on_message_received
        self._worker = None
        self._running = False
        self._serializer = AgentSerializer()
        self._consumer = KafkaConsumer('agent-topic',
                                       bootstrap_servers='localhost:29092',
                                       consumer_timeout_ms=timeout_ms,
                                       value_deserializer=self._deserialize)

    def start(self):
        self._running = True
        self._worker = threading.Thread(target=self._do_work)
        self._worker.start()

    def stop(self):
        self._running = False
        self._worker.join()
        self._consumer.close()

    def _do_work(self):
        while self._running:
            for msg in self._consumer:
                self._on_message_received(msg.value)

    def _deserialize(self, json_str):
        return self._serializer.deserialize(json_str.decode('utf-8'))
