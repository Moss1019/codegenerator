
from django.db import models


class BaseObj(object):
    def set_field(self, key, val):
        self.__dict__[key] = val


class Agent(models.Model, BaseObj):
    agent_id = models.AutoField(primary_key=True)
    username = models.CharField(max_length=32, unique=True)

    @staticmethod
    def get_class():
        return 'Agent'


class Item(models.Model, BaseObj):
    item_id = models.UUIDField(primary_key=True)
    title = models.CharField(max_length=32, unique=True)
    agent = models.ForeignKey(Agent, on_delete=models.CASCADE)

    @staticmethod
    def get_class():
        return 'Item'


class ItemCollaborator(models.Model, BaseObj):
    item = models.ForeignKey(Item, on_delete=models.CASCADE)
    agent = models.ForeignKey(Agent, on_delete=models.CASCADE)

    class Meta:
        unique_together = (('item', 'agent'), )

    @staticmethod
    def get_class():
        return 'ItemCollaborator'

