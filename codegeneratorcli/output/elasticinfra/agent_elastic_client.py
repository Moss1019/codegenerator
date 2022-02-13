
import json

from elasticsearch import Elasticsearch
from serialiazers import AgentSerializer


class AgentElasticClient(object):
    def __init__(self):
        self._es = Elasticsearch('http://localhost:29200')
        self._serializer = AgentSerializer()
        self._index = 'agent-index'

    def index(self, agent):
        json_str = self._serializer.serialize(agent)
        resp = self._es.index(index=self._index, id=agent.agent_id, document=json_str)
        self._es.indices.refresh()
        return resp['result'] == 'created'

    def delete(self, agent):
        resp = self._es.delete(index=self._index, id=agent.agent_id)
        return resp['_shards']['successful'] != '0'

    def update(self, agent):
        json_str = self._serializer.serialize(agent)
        resp = self._es.index(index=self._index, id=agent.agent_id, document=json_str)
        self._es.indices.refresh()
        return resp['result'] == 'updated'

    def get_by_id(self, agent_id):
        query = {'match': {'agentId': agent_id}}
        resp = self._es.search(index=self._index, query=query)
        hits = resp['hits']['hits']
        if len(hits) == 0:
            return None
        return self._serializer.deserialize(json.dumps(hits[0]['_source']))

    def search_by_username(self, username):
        query = {'match': {'username': username}}
        resp = self._es.search(index=self._index, query=query)
        hits = resp['hits']['hits']
        if len(hits) == 0:
            return None
        return self._serializer.deserialize(json.dumps(hits[0]['_source']))

    def close(self):
        self._es.close()
