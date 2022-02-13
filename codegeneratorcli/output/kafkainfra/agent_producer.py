
from kafka import KafkaProducer

from serializers import AgentSerializer


class AgentProducer(object):
    def __init__(self, max_queue_size):
        self._max_queue_size = max_queue_size
        self._current_queue_size = 0
        self._producer = KafkaProducer(bootstrap_servers='localhost:29092', value_serializer=self._serialize)
        self._serializer = AgentSerializer()
        self._topic = 'agent-topic'

    def publish(self, agent):
        self._producer.send(self._topic, agent)
        self._current_queue_size += 1
        if self._current_queue_size == self._max_queue_size:
            self._producer.flush(timeout=5000)
            self._current_queue_size = 0

    def close(self):
        self._producer.close()

    def _serialize(self, agent):
        return self._serializer.serialize(agent).encode('utf-8')
