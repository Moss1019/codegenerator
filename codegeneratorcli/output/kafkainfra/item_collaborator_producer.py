
from kafka import KafkaProducer

from serializers import ItemCollaboratorSerializer


class ItemCollaboratorProducer(object):
    def __init__(self, max_queue_size):
        self._max_queue_size = max_queue_size
        self._current_queue_size = 0
        self._producer = KafkaProducer(bootstrap_servers='localhost:29092', value_serializer=self._serialize)
        self._serializer = AgentSerializer()
        self._topic = 'item_collaborator-topic'

    def publish(self, item_collaborator):
        self._producer.send(self._topic, item_collaborator)
        self._current_queue_size += 1
        if self._current_queue_size == self._max_queue_size:
            self._producer.flush(timeout=5000)
            self._current_queue_size = 0

    def close(self):
        self._producer.close()

    def _serialize(self, item_collaborator):
        return self._serializer.serialize(item_collaborator).encode('utf-8')
