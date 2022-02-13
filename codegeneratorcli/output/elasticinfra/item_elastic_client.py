
import json

from elasticsearch import Elasticsearch
from serialiazers import ItemSerializer


class ItemElasticClient(object):
    def __init__(self):
        self._es = Elasticsearch('http://localhost:29200')
        self._serializer = ItemSerializer()
        self._index = 'item-index'

    def index(self, item):
        json_str = self._serializer.serialize(item)
        resp = self._es.index(index=self._index, id=item.item_id, document=json_str)
        self._es.indices.refresh()
        return resp['result'] == 'created'

    def delete(self, agent):
        resp = self._es.delete(index=self._index, id=item.item_id)
        return resp['_shards']['successful'] != '0'

    def update(self, item):
        json_str = self._serializer.serialize(item)
        resp = self._es.index(index=self._index, id=item.item_id, document=json_str)
        self._es.indices.refresh()
        return resp['result'] == 'updated'

    def get_by_id(self, item_id):
        query = {'match': {'itemId': item_id}}
        resp = self._es.search(index=self._index, query=query)
        hits = resp['hits']['hits']
        if len(hits) == 0:
            return None
        return self._serializer.deserialize(json.dumps(hits[0]['_source']))

    def search_by_title(self, title):
        query = {'match': {'title': title}}
        resp = self._es.search(index=self._index, query=query)
        hits = resp['hits']['hits']
        if len(hits) == 0:
            return None
        return self._serializer.deserialize(json.dumps(hits[0]['_source']))

    def close(self):
        self._es.close()
