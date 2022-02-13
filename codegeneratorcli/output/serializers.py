
import json

from uuid import UUID
from .models import Agent, Item, ItemCollaborator


class UUIDEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, UUID):
            return str(obj)
        return json.JSONEncoder.default(self, obj)


class UUIDDecoder(json.JSONDecoder):
    def default(self, obj):
        if isinstance(obj, UUID):
            return UUID(obj)
        return json.JSONEncoder.default(self, obj)


class Serializer(object):
    def __init__(self):
        self._fields = []
        self._children = []
        self._type = Serializer

        self._joint_fields = None

    def serialize(self, obj):
        d = obj.__dict__
        d = {self._to_camel(k): self._serialized(k, d[k]) for k in d if k in self._get_joint_fields()}
        return json.dumps(d)

    def deserialize(self, data):
        j = json.loads(data.decode(), cls=UUIDDecoder)
        obj = self._type()
        for k in j:
            obj.set_field(self._to_snake(k), j[k])
        return obj

    def _get_joint_fields(self):
        if self._joint_fields is None:
            self._joint_fields = self._fields + self._children
        return self._joint_fields

    def _serialized(self, key, value):
        if key in self._children:
            return [{k: v.__dict__[k]
                     for k in v.__dict__ if k in FIELDS[v.get_class()]}
                    for v in value]
        if key in self._fields:
            return value

    def _to_snake(self, value):
        parts = re.findall('^[a-z]*|[A-Z][^A-Z]*', value)
        return '_'.join(parts).lower()

    def _to_camel(self, value):
        s = re.sub(r'(_)+', " ", value).title().replace(" ", "")
        return ''.join(s[0].lower() + s[1:])


class AgentSerializer(Serializer):
    def __init__(self):
        Serializer.__init__(self)
        self._children = ['items']
        self._fields = FIELDS[Agent.get_class()]
        self._type = Agent


class ItemSerializer(Serializer):
    def __init__(self):
        Serializer.__init__(self)
        self._children = []
        self._fields = FIELDS[Item.get_class()]
        self._type = Item


class ItemCollaboratorSerializer(Serializer):
    def __init__(self):
        Serializer.__init__(self)
        self._children = []
        self._fields = FIELDS[ItemCollaborator.get_class()]
        self._type = ItemCollaborator


FIELDS = {
    Agent.get_class(): ['agent_id', 'username'],
    Item.get_class(): ['item_id', 'title', 'owner_agent_id'],
    ItemCollaborator.get_class(): ['owner_item_id', 'owner_collaborator_id'],
}
